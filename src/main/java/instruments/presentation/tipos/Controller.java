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
import instruments.logic.TipoInstrumentoXMLManager;

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
        String outputFilePath = "files/report.pdf";

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new FileOutputStream(outputFilePath)));
        Document document = new Document(pdfDocument);

        Image img = new Image(ImageDataFactory.create(Objects.requireNonNull(Application.class.getResource("presentation/icons/pdf (1).png"))));
        document.add(img);

        Paragraph title = new Paragraph("Reporte de Tipos de Instrumento")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(24);
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
        model.setCurrent(selectedInstrumento);
        view.enableEditing();
    }


    public void delete(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().delete(tipoInstrumento);
        model.delete(tipoInstrumento);
    }

    public void create(TipoInstrumento tipoInstrumento) {
        try {
            Service.instance().create(tipoInstrumento);
            model.getList().add(tipoInstrumento);
            model.commit();
            guardarDatos("files/TiposInstrumentos.xml");
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }

    public void update(TipoInstrumento tipoInstrumento) throws Exception {
        Service.instance().update(tipoInstrumento);
        model.update(tipoInstrumento);
        guardarDatos("files/TiposInstrumentos.xml");
    }


    public void guardarDatos(String filePath) {
        TipoInstrumentoXMLManager.guardarTiposInstrumento(model.getList(), filePath);
    }


}
