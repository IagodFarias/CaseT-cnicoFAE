package com.fae.calibracao.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Aplicacao JavaFX do software de calibracao.
 *
 * A GUI e uma camada adicional de visualizacao: o console continua funcionando em
 * paralelo, alimentado pelos mesmos eventos (ver ObserverComposto).
 */
public class CalibracaoApp extends Application {

    private PainelEnsaio painel;

    @Override
    public void start(Stage palco) {
        painel = new PainelEnsaio();
        palco.setTitle("Calibracao de Medidores de Agua - Ensaio Metrologico");
        palco.setScene(new Scene(painel, 1360, 860));
        palco.setMinWidth(1100);
        palco.setMinHeight(700);
        palco.show();
    }

    /**
     * Encerramento limpo: interrompe um ensaio em curso e fecha o EntityManagerFactory.
     * Sem isso, fechar a janela no meio de um ensaio deixaria a thread de pulsos e a
     * conexao com o banco vivas ate a JVM morrer.
     */
    @Override
    public void stop() {
        if (painel != null) {
            painel.encerrar();
        }
    }
}
