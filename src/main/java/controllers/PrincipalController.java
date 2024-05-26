package controllers;

import Model.Producto;
import SQL.ConexionMySQL;
import Utilidades.PDFExporter;
import aplication.Aplicacion;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PrincipalController {

    @Setter
    private Aplicacion aplication;

    Connection conexion = ConexionMySQL.obtener();

    @FXML
    private TableView<Producto> tableProductos;

    @FXML
    private TableColumn<Producto, String> columnCodigo;

    @FXML
    private TableColumn<Producto, String> columnNombre;

    @FXML
    private TableColumn<Producto, String> columnDescrip;

    @FXML
    private TableColumn<Producto, String> columnPrecio;

    @FXML
    private TableColumn<Producto, Integer> columStock;

    @FXML
    private TextField txtBuscarProducto;

    @FXML
    private Button btnAgregarProducto;

    @FXML
    private Button btnEliminarProducto;

    @FXML
    private Button btnExportarLista;

    @FXML
    private ImageView salir;

    @FXML
    private Button btnEditarProducto;

    Producto productoSeleccionado;

    public PrincipalController() throws SQLException, ClassNotFoundException {
    }

    @FXML
    void agregarEvent(ActionEvent event) {
        aplication.mostrarVentanaAgregar(this);
    }

    @FXML
    void buscarProductoEvent(KeyEvent event) {
        String detalle = txtBuscarProducto.getText().trim();
        if (detalle.isEmpty()) {
            tableProductos.setItems(listaProductos); // Restablecer la lista original si el campo de búsqueda está vacío
        } else {
            String consulta = "SELECT * FROM producto WHERE codigo LIKE ? OR nombre LIKE ?";
            ObservableList<Producto> productosFiltrados = FXCollections.observableArrayList();
            try {
                Connection connection = conexion;
                PreparedStatement statement = connection.prepareStatement(consulta);
                statement.setString(1, "%" + detalle + "%");
                statement.setString(2, "%" + detalle + "%");
                ResultSet resultado = statement.executeQuery();
                while (resultado.next()) {
                    String codigo = resultado.getString("codigo");
                    String nombre = resultado.getString("nombre");
                    String descripcion = resultado.getString("descripcion");
                    String precio = resultado.getString("precio");
                    int stock = resultado.getInt("stock");
                    Producto producto = new Producto(codigo, nombre, descripcion, precio, stock);
                    productosFiltrados.add(producto);
                }
                tableProductos.setItems(productosFiltrados);
            } catch (SQLException e) {
                System.out.println("Error al filtrar los datos: " + e.getMessage());
            }
        }
    }


    @FXML
    void editarEvent(ActionEvent event) {
        if(productoSeleccionado == null){
            mostrarMensaje("Notificación Actualizar", "Seleccion producto", "Debe seleccionar un producto", Alert.AlertType.ERROR);
        }else{
            aplication.mostrarVentanaActualizar(productoSeleccionado.getCodigo(), this);
        }
    }

    @FXML
    void eliminarEvent(ActionEvent event) {
        if (productoSeleccionado != null) {
            String codigo = productoSeleccionado.getCodigo();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText("¿Está seguro que desea eliminar este producto?");
            alert.setContentText("Producto: " + productoSeleccionado.getNombre());
            ButtonType buttonTypeSi = new ButtonType("Sí");
            ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeSi, buttonTypeNo);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == buttonTypeSi) {
                eliminarProducto(codigo);
                refrescarTablaProductos();
            } else {
                mostrarMensaje("Notificación", "Eliminación cancelada", "El producto no ha sido eliminado", Alert.AlertType.INFORMATION);
            }
        } else {
            mostrarMensaje("Notificación Producto", "Información inválida", "Por favor seleccione un producto para poder eliminarlo del inventario", Alert.AlertType.WARNING);
        }
    }

    private void eliminarProducto(String codigo) {
        String consulta = "DELETE FROM producto WHERE codigo = ?";
        try {
            PreparedStatement statement = conexion.prepareStatement(consulta);
            statement.setString(1, codigo);
            int filasEliminadas = statement.executeUpdate();
            statement.close();
            if (filasEliminadas > 0) {
                mostrarMensaje("Notificación Producto", "Producto eliminado", "El producto ha sido eliminado correctamente", Alert.AlertType.INFORMATION);
            } else {
                mostrarMensaje("Notificación Producto", "Producto no eliminado", "Hubo un fallo al eliminar el producto", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            mostrarMensaje("Notificación Producto", "Error", e.getMessage(), Alert.AlertType.WARNING);
        }
    }


    @FXML
    void exportarEvent(ActionEvent event) {
        PDFExporter.exportToPDF(tableProductos, "tableProductosDATA.pdf", "Listado de Productos");
    }

    @FXML
    void salirEvent(MouseEvent event) {

    }

    public void refrescarTablaProductos() {
        listaProductos.clear();
        llenarTablaProductos();
    }

    ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    private void llenarTablaProductos() {
        String consulta = "SELECT * FROM producto";
        try {
            Connection connection = conexion;
            PreparedStatement statement = connection.prepareStatement(consulta);
            ResultSet resultado = statement.executeQuery();
            while (resultado.next()) {
                String codigo = resultado.getString("codigo");
                String nombre = resultado.getString("nombre");
                String descripcion = resultado.getString("descripcion");
                String precio = resultado.getString("precio");
                int stock= resultado.getInt("stock");
                Producto producto = new Producto(codigo, nombre, descripcion, precio, stock);
                listaProductos.add(producto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        this.columnCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        this.columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        this.columnDescrip.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        this.columnPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        this.columStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tableProductos.setItems(listaProductos);
        llenarTablaProductos();

        tableProductos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

            productoSeleccionado = newSelection;
        });
    }

    public void mostrarMensaje (String titulo, String header, String contenido, Alert.AlertType alertType){
        Alert alert = new Alert(alertType);
        alert.setTitle(titulo);
        alert.setHeaderText(header);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

}
