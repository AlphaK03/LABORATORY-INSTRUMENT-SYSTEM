package instruments.presentation.tipos;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import java.io.FileOutputStream;

import com.itextpdf.layout.properties.TextAlignment;
import instruments.Application;
import instruments.logic.Service;
import instruments.logic.TipoInstrumento;
import java.util.List;
import java.util.Objects;

public class Controller{
    View view;
    Model model;

    public Controller(View view, Model model) {
        model.init(Service.instance().search(new TipoInstrumento()));
        this.view = view;
        this.model = model;
        view.setController(this);
        view.setModel(model);
    }

    public void generatePDFReport(List<TipoInstrumento> tipos) throws Exception {
        String outputFilePath = "report.pdf"; // Nombre del archivo de salida

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new FileOutputStream(outputFilePath)));
        Document document = new Document(pdfDocument);

        Image img = new Image(ImageDataFactory.create(Objects.requireNonNull(Application.class.getResource("presentation/icons/pdf (1).png"))));
        document.add(img);

        Paragraph title = new Paragraph("Reporte de Tipos de Instrumento")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(24); // Tamaño de letra más grande para el título
        document.add(title);

        for (TipoInstrumento tipo : tipos) {
            document.add(new Paragraph("Código: " + tipo.getCodigo()));
            document.add(new Paragraph("Nombre: " + tipo.getNombre()));
            document.add(new Paragraph("Unidad: " + tipo.getUnidad()));
            document.add(new Paragraph("----------------------------------"));
        }

        document.close();
    }

    public void search(TipoInstrumento filter) throws  Exception{
        List<TipoInstrumento> rows = Service.instance().search(filter);
        if (rows.isEmpty()){
            throw new Exception("NINGUN REGISTRO COINCIDE");
        }
        model.setList(rows);
        model.setCurrent(new TipoInstrumento());
        model.commit();
    }

    public void edit(int row) {
        TipoInstrumento selectedInstrumento = model.getList().get(row);
        model.setCurrent(selectedInstrumento); // Establece el instrumento seleccionado como "current"
        view.enableEditing(); // Activa la edición en la vista
    }


    public void delete(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().delete(tipoInstrumento);
        model.delete(tipoInstrumento);
    }

    public void create(TipoInstrumento tipoInstrumento) {
        try {
            Service.instance().create(tipoInstrumento);
            model.getList().add(tipoInstrumento); // Agregar el nuevo registro a la lista del modelo
            model.commit(); // Notificar a la vista que la lista ha cambiado
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void update(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().update(tipoInstrumento); // Actualiza el registro en la capa de lógica
        model.update(tipoInstrumento); // Actualiza el registro en el modelo
    }




}
