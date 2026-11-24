package com.xchange.admin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/xchange/admin/startup/StartUp.fxml")
        );
        Scene scene = new Scene(loader.load(), 520, 440);
        scene.getStylesheets().add(
                getClass().getResource("/com/xchange/admin/app.css").toExternalForm()
        );

        stage.setTitle("Connect to XChange");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
