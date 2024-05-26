package Model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Producto {
    private String codigo;
    private String nombre;
    private String descripcion;
    private String precio;
    private int stock;

    public Producto(String codigo, String nombre, String descripcion, String precio, int stock){
        this.codigo= codigo;
        this.nombre= nombre;
        this.descripcion= descripcion;
        this.precio= precio;
        this.stock= stock;
    }
}
