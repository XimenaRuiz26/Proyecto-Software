package Utilidades;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PDFExporter {
    public static <T> void exportToPDF(TableView<T> tableView, String filePath, String titulo) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            Paragraph title = new Paragraph(titulo)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20);
            document.add(title);
            document.add(new Paragraph("\n"));
            Table table = new Table(tableView.getColumns().size());
            for (TableColumn<T, ?> column : tableView.getColumns()) {
                table.addHeaderCell(new Paragraph(column.getText()));
            }
            for (int rowIndex = 0; rowIndex < tableView.getItems().size(); rowIndex++) {
                T item = tableView.getItems().get(rowIndex);
                for (TableColumn<T, ?> column : tableView.getColumns()) {
                    Object cellData = column.getCellData(item);
                    table.addCell(new Paragraph(cellData == null ? "" : cellData.toString()));
                }
            }
            document.add(table);
            document.close();
            File file = new File(filePath);
            if (file.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(file);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
