package com.fae.calibracao.ui.gui;

import javafx.application.Application;

/**
 * Ponto de entrada da interface grafica.
 *
 * Esta classe NAO estende Application de proposito. Quando a classe principal do jar
 * estende Application e o JavaFX esta no classpath (e nao no module-path), o launcher da
 * JVM recusa a inicializacao com "JavaFX runtime components are missing". Delegando o
 * launch a partir de uma classe comum, o jar unico gerado pelo shade-plugin abre a GUI
 * normalmente — e o 'mvn javafx:run' tambem continua funcionando.
 */
public class MainGui {

    public static void main(String[] args) {
        Application.launch(CalibracaoApp.class, args);
    }
}
