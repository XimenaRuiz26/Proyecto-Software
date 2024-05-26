package aplication;

import controllers.ActualizarController;
import controllers.AgregarController;
import controllers.PrincipalController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Aplicacion extends Application {

    private Stage primaryStage;

    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Gestión inventario");
        mostrarVentanaPrincipal();
    }

    public void mostrarVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("../Views/Principal.fxml"));

            AnchorPane rootLayout = (AnchorPane)loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            //primaryStage.setResizable(false);
            primaryStage.show();
            primaryStage.centerOnScreen();
            PrincipalController inicioController= loader.getController();
            inicioController.setAplication(this);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void mostrarVentanaActualizar(String codigo,PrincipalController principalController ) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("../Views/Actualiza.fxml"));
            AnchorPane rootLayout = (AnchorPane)loader.load();
            Stage actualizarStage = new Stage();
            actualizarStage.setTitle("Actualizar producto");
            actualizarStage.initModality(Modality.WINDOW_MODAL);
            actualizarStage.initOwner(primaryStage);

            Scene scene = new Scene(rootLayout);
            actualizarStage.setScene(scene);
            actualizarStage.show();
            actualizarStage.centerOnScreen();

            ActualizarController inicioController = loader.getController();
            inicioController.setAplication(this, codigo);
            inicioController.setPrincipalController(principalController);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void mostrarVentanaAgregar(PrincipalController principalController) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Aplicacion.class.getResource("../Views/Agregar.fxml"));
            AnchorPane rootLayout = (AnchorPane)loader.load();
            Stage agregarStage = new Stage();
            agregarStage.setTitle("Agregar producto");
            agregarStage.initModality(Modality.WINDOW_MODAL);
            agregarStage.initOwner(primaryStage);

            Scene scene = new Scene(rootLayout);
            agregarStage.setScene(scene);
            agregarStage.show();
            agregarStage.centerOnScreen();

            AgregarController agregarController = loader.getController();
            agregarController.setAplication(this);
            agregarController.setPrincipalController(principalController);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }

}
