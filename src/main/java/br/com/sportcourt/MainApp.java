package br.com.sportcourt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/br/com/sportcourt/view/LoginView.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);

            String[] cssPaths = {"/br/com/sportcourt/view/styles.css", "/styles.css", "/app.css"};

            for (String cssPath : cssPaths) {
                try {
                    java.net.URL cssUrl = getClass().getResource(cssPath);
                    if (cssUrl != null) {
                        scene.getStylesheets().add(cssUrl.toExternalForm());
                        break;
                    }
                } catch (Exception e) {
                }
            }

            primaryStage.setTitle("SportCourt - Sistema de Gestão");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(800);
            primaryStage.setMinHeight(600);

            primaryStage.show();



        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO ao iniciar aplicação:");
            e.printStackTrace();

            javafx.scene.control.Alert alert =
                    new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alert.setTitle("Erro de Inicialização");
            alert.setHeaderText("Não foi possível iniciar o sistema");
            alert.setContentText("Detalhes: " + e.getMessage());
            alert.showAndWait();

            throw e;
        }
    }

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
