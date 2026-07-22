package com.fae.calibracao.ui.gui;

/**
 * Cenarios do painel de injecao de falhas.
 *
 * Cada um provoca uma falha REAL no caminho de rede — nenhum exibe mensagem fabricada.
 * O objetivo e que o tratamento de excecao ja existente seja quem reage, do jeito que
 * reagiria a uma falha espontanea em campo.
 */
public enum CenarioFalha {

    QUEDA_CONEXAO(
            "Queda de conexao",
            true,
            "Fecha o socket do cliente durante o ensaio. A leitura seguinte falha com "
                    + "excecao de I/O real."),

    TIMEOUT_SIMULADOR(
            "Timeout do simulador",
            true,
            "Manda o simulador parar de responder sem fechar a conexao. O setSoTimeout do "
                    + "cliente dispara SocketTimeoutException real."),

    RECUSA_CONEXAO(
            "Recusa de conexao inicial",
            false,
            "Faz o proximo Iniciar mirar uma porta sem simulador. O connect falha com "
                    + "ConnectException real.");

    private final String rotulo;
    /** true = precisa de ensaio em andamento; false = so faz sentido com o ensaio parado. */
    private final boolean exigeEnsaioAtivo;
    private final String explicacao;

    CenarioFalha(String rotulo, boolean exigeEnsaioAtivo, String explicacao) {
        this.rotulo = rotulo;
        this.exigeEnsaioAtivo = exigeEnsaioAtivo;
        this.explicacao = explicacao;
    }

    public boolean exigeEnsaioAtivo() {
        return exigeEnsaioAtivo;
    }

    public boolean aplicavelCom(boolean ensaioEmAndamento) {
        return exigeEnsaioAtivo == ensaioEmAndamento;
    }

    public String explicacao() {
        return explicacao;
    }

    @Override
    public String toString() {
        return rotulo;   // e o que aparece no ComboBox
    }
}
