package controllers;

import SQL.ConexionMySQL;
import aplication.Aplicacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AgregarController {

    @Setter
    private Aplicacion aplication;

    Connection conexion = ConexionMySQL.obtener();

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtPrecio;

    @FXML
    private Spinner<Integer> spinnerStock;

    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnCancelar;

    @FXML
    private TextField txtCodigo;

    @Setter
    private PrincipalController principalController;

    public AgregarController() throws SQLException, ClassNotFoundException {
    }


    @FXML
    void agregarEvent(ActionEvent event) {
        String nombre = txtNombre.getText();
        String codigo = txtCodigo.getText();
        String descripcion= txtDescripcion.getText();
        String precio= txtPrecio.getText();
        int stock = spinnerStock.getValue();

        if (nombre != null && !nombre.isEmpty() && descripcion != null && !descripcion.isEmpty() && codigo != null && !codigo.isEmpty()
        && precio!=null &&!precio.isEmpty()) {
            try {
                String consulta = "INSERT INTO inventario.producto (codigo, nombre, descripcion, precio, stock) VALUES (?, ?, ?,?,?)";
                PreparedStatement statement = conexion.prepareStatement(consulta);
                statement.setString(1, codigo);
                statement.setString(2, nombre);
                statement.setString(3, descripcion);
                statement.setString(4, precio);
                statement.setInt(5, stock);
                int filasInsertadas = statement.executeUpdate();
                statement.close();
                if (filasInsertadas > 0) {
                    mostrarMensaje("Notificacion Producto", "Producto agregado correctamente", "Se ha agregado correctamente el producto", Alert.AlertType.INFORMATION);
                    refrescarAgregar();
                    principalController.refrescarTablaProductos();
                } else {
                    mostrarMensaje("Notificacion Producto", "Producto no agregado", "Hubo un fallo al agregar el producto", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                mostrarMensaje("Notificacion Producto", "Error", e.getMessage(), Alert.AlertType.WARNING);
            }
        } else {
            mostrarMensaje("Notificacion Producto", "Información inválida", "Por favor llene todos los datos para poder agregar el producto", Alert.AlertType.WARNING);
        }
    }

    private void refrescarAgregar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500, 0);
        spinnerStock.setValueFactory(valueFactory);
    }


    @FXML
    private void initialize() {
        btnCancelar.setOnAction(event -> handleCerrar());
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500, 0);
        spinnerStock.setValueFactory(valueFactory);
    }
    @FXML
    private void handleCerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    public void mostrarMensaje (String titulo, String header, String contenido, Alert.AlertType alertType){

        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }




}
