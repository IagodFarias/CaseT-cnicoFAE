package com.fae.calibracao.ui.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Ferramenta de teste: aciona os modos de falha do simulador e descobre portas mortas.
 *
 * <h2>Por que uma conexao de controle separada</h2>
 * Durante o ensaio, a conexao com o simulador esta protegida por um ReentrantLock e ocupada
 * pela thread de comunicacao. Usa-la aqui bloquearia quem chama — na GUI, a JavaFX
 * Application Thread — ate a leitura em curso terminar, congelando a janela. Por isso a
 * injecao viaja por uma conexao propria, efemera, que nao disputa nada com o ensaio.
 *
 * Nao usa TcpClient nem os tipos de protocol/ de proposito: o comando INJETAR e de
 * controle do simulador, fora do protocolo do ensaio, e nao deve contaminar as classes que
 * modelam o protocolo real.
 *
 * Nenhum metodo desta classe deve ser chamado na Application Thread — todos fazem I/O.
 */
public class InjetorDeFalhas {

    private static final int TIMEOUT_CONTROLE_MS = 3_000;

    /**
     * Pede ao simulador que suspenda as respostas pelo periodo indicado.
     *
     * A conexao do ensaio permanece aberta e simplesmente para de receber respostas, que e
     * o que caracteriza um timeout de verdade (diferente de uma queda, em que o socket cai).
     */
    public void suspenderRespostas(String host, int porta, int segundos) throws IOException {
        enviarComando(host, porta,
                "{\"command\":\"INJETAR\",\"segundos\":" + segundos + "}");
    }

    /** Normaliza o simulador antes do previsto. Usado ao encerrar a aplicacao. */
    public void cancelarInjecao(String host, int porta) throws IOException {
        enviarComando(host, porta, "{\"command\":\"CANCELAR_INJECAO\"}");
    }

    private void enviarComando(String host, int porta, String json) throws IOException {
        try (Socket controle = new Socket()) {
            controle.connect(new InetSocketAddress(host, porta), TIMEOUT_CONTROLE_MS);
            controle.setSoTimeout(TIMEOUT_CONTROLE_MS);
            Writer out = new OutputStreamWriter(controle.getOutputStream(), StandardCharsets.UTF_8);
            out.write(json);
            out.write('\n');
            out.flush();
            // Le a confirmacao para garantir que o simulador processou antes de fecharmos.
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(controle.getInputStream(), StandardCharsets.UTF_8));
            in.readLine();
        }
    }

    /**
     * Devolve uma porta local onde com certeza nao ha ninguem escutando.
     *
     * Abre uma porta efemera so para o sistema operacional atribuir um numero livre e a
     * fecha em seguida. Apontar o ensaio para ela produz um ConnectException legitimo, sem
     * precisar adivinhar um numero "provavelmente livre".
     */
    public int portaSemServidor() throws IOException {
        try (ServerSocket efemera = new ServerSocket(0)) {
            return efemera.getLocalPort();
        }
    }
}
