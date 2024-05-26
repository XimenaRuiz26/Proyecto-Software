package controllers;

import SQL.ConexionMySQL;
import aplication.Aplicacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ActualizarController {

    private Aplicacion aplication;
    Connection conexion = ConexionMySQL.obtener();

    @FXML
    private TextField txtNombre;

    @FXML
    private Label labelCodigo;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtPrecio;

    @FXML
    private Spinner<Integer> spinnerStock;
    private String codigo;

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnCancelar;

    @Setter
    private PrincipalController principalController;

    public ActualizarController() throws SQLException, ClassNotFoundException {
    }

    @FXML
    void actualizarEvent(ActionEvent event) {
        String nombre = txtNombre.getText();
        String descripcion= txtDescripcion.getText();
        String precio= txtPrecio.getText();
        int stock = spinnerStock.getValue();
        if (nombre != null && !nombre.isEmpty() && descripcion != null && !descripcion.isEmpty()
        &&precio!= null && !precio.isEmpty()) {
            try {
                String consulta = "UPDATE producto SET nombre = ?, descripcion = ?, precio=?, stock=? WHERE codigo = ?";
                PreparedStatement statement = conexion.prepareStatement(consulta);
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setString(3, precio);
                statement.setInt(4, stock);
                statement.setString(5, codigo);
                int filasActualizadas = statement.executeUpdate();
                if (filasActualizadas > 0) {
                    mostrarMensaje("Notificacion Producto", "Producto actualizado correctamente", "Se ha actualizado correctamente el producto", Alert.AlertType.INFORMATION);
                    refrescarActualizar();
                    principalController.refrescarTablaProductos();
                } else {
                    mostrarMensaje("Notificacion Producto", "Producto actualizado incorrectamente", "Hubo un fallo al actualizar el producto", Alert.AlertType.ERROR);
                }
            } catch (SQLException e) {
                mostrarMensaje("Notificacion Producto", "Error", e.getMessage(), Alert.AlertType.WARNING);
            }
        } else {
            mostrarMensaje("Notificacion Producto", "Información inválida", "Por favor llene todos los datos para poder actualizar el producto", Alert.AlertType.WARNING);
        }

    }

    private void refrescarActualizar() {
        labelCodigo.setText("Código");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPrecio.setText("");
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 500, 0);
        spinnerStock.setValueFactory(valueFactory);
    }

    private void buscarProducto(String codigo) {
        if (codigo != null && !codigo.isEmpty()) {
            try {
                String consulta = "SELECT * FROM producto WHERE codigo = ?";
                PreparedStatement statement = conexion.prepareStatement(consulta);
                statement.setString(1, codigo);
                try (ResultSet resultado = statement.executeQuery()) {
                    if (resultado.next()) {
                        String nombre = resultado.getString("nombre");
                        String descripcion = resultado.getString("descripcion");
                        String precio = resultado.getString("precio");
                        int stock = resultado.getInt("stock");
                        txtNombre.setText(nombre);
                        txtDescripcion.setText(descripcion);
                        txtPrecio.setText(precio);
                        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(stock, 500, stock);
                        spinnerStock.setValueFactory(valueFactory);
                    } else {
                        mostrarMensaje("Notificacion Producto", "Producto No Encontrado", "No se encontro ningun producto", Alert.AlertType.WARNING);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al insertar datos: " + e.getMessage());
            }
        } else {
            mostrarMensaje("Notificacion Producto", "Información inválida", "Por favor llene todos los datos para poder buscar el producto", Alert.AlertType.WARNING);
        }
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

    public void setAplication(Aplicacion aplicacion, String codigo) {
        this.aplication = aplicacion;
        this.codigo= codigo;
        labelCodigo.setText(codigo);
        buscarProducto(codigo);
    }

}
