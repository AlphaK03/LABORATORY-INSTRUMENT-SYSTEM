package instruments.logic;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import instruments.Application;
import instruments.data.Data;

import java.io.FileOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Service {
    private static Service theInstance;

    public static Service instance(){
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }
    private Data data;

    private Service(){
        data = new Data();
    }

    public void create(TipoInstrumento e) throws Exception {
        TipoInstrumento result = data.getTipos().stream()
                .filter(i -> i.getCodigo().equals(e.getCodigo()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getTipos().add(e);
        } else {
            throw new Exception("Tipo ya existe");
        }

    }


    public TipoInstrumento read(TipoInstrumento e) throws Exception{
        TipoInstrumento result = data.getTipos().stream()
                .filter(i->i.getCodigo().equals(e.getCodigo())).findFirst().orElse(null);
        if (result!=null) return result;
        else throw new Exception("Tipo no existe");
    }

    public void update(TipoInstrumento e) throws Exception{
        TipoInstrumento result;
        try{
            result = this.read(e);
            data.getTipos().remove(result);
            data.getTipos().add(e);
        }catch (Exception ex) {
            throw new Exception("Tipo no existe");
        }
    }

    public void delete(TipoInstrumento e) throws Exception{
        data.getTipos().remove(e);
     }

    public List<TipoInstrumento> search(TipoInstrumento e){
        return data.getTipos().stream()
                .filter(i->i.getNombre().contains(e.getNombre()))
                .sorted(Comparator.comparing(TipoInstrumento::getNombre))
                .collect(Collectors.toList());
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


 }
