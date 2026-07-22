package com.fae.calibracao.net;

import com.fae.calibracao.protocol.Command;
import com.fae.calibracao.protocol.JsonCodec;
import com.fae.calibracao.protocol.ProtocolException;
import com.fae.calibracao.protocol.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Conexao TCP com o simulador do medidor de referencia.
 *
 * O protocolo e sincrono e sem identificador de correlacao: a resposta e simplesmente
 * a proxima linha do stream. Se dois comandos fossem escritos ao mesmo tempo, as
 * respostas poderiam ser lidas trocadas. Por isso o par escrita+leitura de {@link #send}
 * e atomico sob um ReentrantLock — apenas UM comando trafega pela conexao por vez.
 */
public class TcpClient implements AutoCloseable {

    private final String host;
    private final int port;
    private final int connectTimeoutMs;
    private final int readTimeoutMs;
    private final JsonCodec codec = new JsonCodec();

    /** Serializa o ciclo escrita-resposta. Torna o cliente seguro para uso multi-thread. */
    private final ReentrantLock socketLock = new ReentrantLock();

    /**
     * Volatile porque e lido fora do lock: por isConnected() e por
     * {@link #derrubarConexao()}, que precisa enxergar o socket publicado por outra thread.
     */
    private volatile Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public TcpClient(String host, int port, int connectTimeoutMs, int readTimeoutMs) {
        this.host = host;
        this.port = port;
        this.connectTimeoutMs = connectTimeoutMs;
        this.readTimeoutMs = readTimeoutMs;
    }

    public void connect() throws CommunicationException {
        socketLock.lock();
        try {
            if (isConnected()) {
                return;
            }
            Socket novo = new Socket();
            try {
                novo.connect(new InetSocketAddress(host, port), connectTimeoutMs);
                // Limita quanto tempo readLine() pode bloquear: sem isso, um simulador
                // travado deixaria o cliente esperando para sempre.
                novo.setSoTimeout(readTimeoutMs);
                novo.setTcpNoDelay(true);   // mensagens curtas: nao vale acumular no buffer
            } catch (IOException e) {
                fecharSilenciosamente(novo);
                throw traduzirFalhaDeConexao(e);
            }
            this.socket = novo;
            this.in = new BufferedReader(new InputStreamReader(novo.getInputStream(), StandardCharsets.UTF_8));
            this.out = new BufferedWriter(new OutputStreamWriter(novo.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new CommunicationException("falha ao abrir os streams de " + endereco() + ": " + e.getMessage(), e);
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Envia um comando e devolve a resposta correspondente.
     *
     * Respostas com status ERROR sao devolvidas normalmente: quem decide o que fazer
     * com um erro de negocio e a camada de servico, nao o transporte.
     */
    public Response send(Command command) throws CommunicationException, ProtocolException {
        socketLock.lock();
        try {
            if (!isConnected()) {
                throw new CommunicationException("nao ha conexao ativa com " + endereco());
            }
            String linha = codec.encode(command);
            try {
                out.write(linha);
                out.write('\n');        // delimitador do protocolo
                out.flush();
            } catch (IOException e) {
                throw new CommunicationException(
                        "falha ao enviar " + command.command() + " para " + endereco() + ": " + e.getMessage(), e);
            }

            String resposta;
            try {
                resposta = in.readLine();
            } catch (SocketTimeoutException e) {
                throw new CommunicationException("timeout de " + readTimeoutMs + " ms aguardando resposta de "
                        + command.command() + " (simulador em " + endereco() + " nao respondeu)", e);
            } catch (IOException e) {
                throw new CommunicationException(
                        "falha ao ler a resposta de " + command.command() + ": " + e.getMessage(), e);
            }

            // readLine() == null significa fim de stream: o simulador fechou a conexao.
            if (resposta == null) {
                throw new CommunicationException("simulador em " + endereco()
                        + " encerrou a conexao antes de responder " + command.command());
            }

            Response decodificada = codec.decode(resposta);
            validarCorrelacao(command, decodificada);
            return decodificada;
        } finally {
            socketLock.unlock();
        }
    }

    /**
     * Confere se a resposta e do comando que foi enviado. Sem isso, uma dessincronizacao
     * do stream passaria despercebida e o cliente leria um volume de outra mensagem.
     * Respostas de erro sao isentas: o simulador responde "UNKNOWN" quando nao consegue
     * nem interpretar o comando.
     */
    private static void validarCorrelacao(Command enviado, Response recebida) throws ProtocolException {
        if (recebida.isError()) {
            return;
        }
        if (recebida.command() == null || !recebida.command().equalsIgnoreCase(enviado.command())) {
            throw new ProtocolException("resposta fora de ordem: enviado " + enviado.command()
                    + ", recebido " + recebida.command());
        }
    }

    public boolean isConnected() {
        Socket s = this.socket;
        return s != null && s.isConnected() && !s.isClosed();
    }

    public String endereco() {
        return host + ":" + port;
    }

    /**
     * Fecha o socket imediatamente, SEM adquirir o lock.
     *
     * Serve para provocar uma falha de rede real a partir de outra thread — usado pelo
     * recurso de simulacao de queda da GUI, que precisa exercitar o tratamento de excecao
     * de verdade em vez de exibir um erro ficticio.
     *
     * A ausencia do lock e deliberada e essencial. Quando a thread do ensaio esta
     * bloqueada em readLine(), ela esta segurando o socketLock; se este metodo tentasse
     * adquiri-lo, ficaria preso ate a leitura terminar — exatamente o que se quer
     * interromper — e travaria a thread que o chamou (na GUI, a Application Thread).
     * Fechar o socket de outra thread e justamente a forma canonica de desbloquear uma
     * leitura pendente: o readLine() em curso falha com SocketException e as chamadas
     * seguintes encontram o socket fechado.
     *
     * Nao mexe em 'in'/'out' nem anula 'socket': quem faz a limpeza ordenada continua
     * sendo {@link #close()}, chamado pelo try-with-resources do servico.
     */
    public void derrubarConexao() {
        fecharSilenciosamente(this.socket);
    }

    @Override
    public void close() {
        socketLock.lock();
        try {
            fecharSilenciosamente(socket);
            socket = null;
            in = null;
            out = null;
        } finally {
            socketLock.unlock();
        }
    }

    /** Converte a falha de I/O em uma mensagem que diz o que fazer, nao so o que quebrou. */
    private CommunicationException traduzirFalhaDeConexao(IOException e) {
        if (e instanceof UnknownHostException) {
            return new CommunicationException("host desconhecido: " + host, e);
        }
        if (e instanceof SocketTimeoutException) {
            return new CommunicationException(
                    "timeout de " + connectTimeoutMs + " ms ao conectar em " + endereco(), e);
        }
        if (e instanceof ConnectException) {
            return new CommunicationException("conexao recusada por " + endereco()
                    + " - o simulador esta rodando nessa porta?", e);
        }
        return new CommunicationException("falha ao conectar em " + endereco() + ": " + e.getMessage(), e);
    }

    private static void fecharSilenciosamente(Socket s) {
        if (s != null) {
            try {
                s.close();
            } catch (IOException ignorado) {
                // fechamento best-effort: nada util a fazer se o close falha
            }
        }
    }
}
