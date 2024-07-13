package com.tool.translator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/*
Translate Tool App
 */
public class TranslateApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TranslateApplication.class.getResource("translate-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        scene.getRoot().setStyle("-fx-font-family: Serif");
        stage.setTitle("翻译工具");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}