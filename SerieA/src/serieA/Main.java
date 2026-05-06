package serieA;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import serieA.controller.LoginController;
import serieA.model.Gestione;

public class Main extends Application {
    private Gestione gestione;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gestione = new Gestione();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/serieA/view/Login.fxml"));
        AnchorPane pane = loader.load();
        primaryStage.setScene(new Scene(pane, 450, 430));
        primaryStage.setTitle("Serie A Manager - Login");
        LoginController ctrl = loader.getController();
        ctrl.initData(gestione);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
