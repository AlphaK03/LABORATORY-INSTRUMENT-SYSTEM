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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Service {
    private static Service theInstance;
    private Instrumento lastSelectedInstrumento;

    private List<Medicion> medicionesList;

    public static Service instance() {
        if (theInstance == null) theInstance = new Service();
        return theInstance;
    }

    private Data data;

    public Data getData() {
        return data;
    }

    private Service() {
        medicionesList = new ArrayList<>();
        try {
            data = XmlPersister.instance().load();
        }catch (Exception ex){
            data = new Data();
        }
    }

    public List<Medicion> getMedicionesList() {
        return medicionesList;
    }

    public void setMedicionesList(List<Medicion> medicionesList) {
        this.medicionesList = medicionesList;
    }

    // Funciones para TipoInstrumento
    public void createTipoInstrumento(TipoInstrumento e) throws Exception {
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

    public TipoInstrumento readTipoInstrumento(TipoInstrumento e) throws Exception {
        TipoInstrumento result = data.getTipos().stream()
                .filter(i -> i.getCodigo().equals(e.getCodigo())).findFirst().orElse(null);
        if (result != null) return result;
        else throw new Exception("Tipo no existe");
    }

    public void updateTipoInstrumento(TipoInstrumento e) throws Exception {
        TipoInstrumento result;
        try {
            result = this.readTipoInstrumento(e);
            data.getTipos().remove(result);
            data.getTipos().add(e);
        } catch (Exception ex) {
            throw new Exception("Tipo no existe");
        }
    }

    public void deleteTipoInstrumento(TipoInstrumento e) throws Exception {
        data.getTipos().remove(e);
    }

    public List<TipoInstrumento> searchTipoInstrumento(TipoInstrumento e) {
        return data.getTipos().stream()
                .filter(i -> i.getNombre().contains(e.getNombre()))
                .sorted(Comparator.comparing(TipoInstrumento::getNombre))
                .collect(Collectors.toList());
    }

    // Funciones para Instrumento
    public void createInstrumento(Instrumento e) throws Exception {
        Instrumento result = data.getInstrumentos().stream()
                .filter(i -> i.getSerie().equals(e.getSerie()))
                .findFirst()
                .orElse(null);
        if (result == null) {
            data.getInstrumentos().add(e);
        } else {
            throw new Exception("Instrumento ya existe");
        }
    }

    public Instrumento readInstrumento(Instrumento e) throws Exception {
        Instrumento result = data.getInstrumentos().stream()
                .filter(i -> i.getSerie().equals(e.getSerie())).findFirst().orElse(null);
        if (result != null) return result;
        else throw new Exception("Instrumento no existe");
    }

    public void updateInstrumento(Instrumento e) throws Exception {
        Instrumento result;
        try {
            result = this.readInstrumento(e);
            data.getInstrumentos().remove(result);
            data.getInstrumentos().add(e);
            onSelectInstrumento(e);

        } catch (Exception ex) {
            throw new Exception("Instrumento no existe");
        }
    }

    public void deleteInstrumento(Instrumento e) throws Exception {
        data.getInstrumentos().remove(e);
    }

    public List<Instrumento> searchInstrumento(Instrumento e) {
        return data.getInstrumentos().stream()
                .filter(i -> i.getDescripcion().contains(e.getDescripcion()))
                .sorted(Comparator.comparing(Instrumento::getDescripcion))
                .collect(Collectors.toList());
    }


    public void createCalibracion(Calibracion e, Instrumento instrumento) throws Exception {
        boolean exists = instrumento.getCalibracionList().stream()
                .anyMatch(i -> i.getNumero() == e.getNumero());

        if (!exists) {
            instrumento.getCalibracionList().add(e);
            onSelectInstrumento(instrumento);
        } else {
            throw new Exception("Calibracion ya existe");
        }
    }

    // Funciones para Calibracion

    public Calibracion readCalibracion(Calibracion e, Instrumento instrumento) throws Exception {
        Calibracion result = instrumento.getCalibracionList().stream()
                .filter(i -> i.getNumero() == e.getNumero())
                .findFirst()
                .orElse(null);
        if (result != null) return result;
        else throw new Exception("Calibracion no existe");
    }

    public void updateCalibracion(Calibracion e, Instrumento instrumento) throws Exception {
        Calibracion result;
        try {
            result = this.readCalibracion(e, instrumento);
            instrumento.getCalibracionList().remove(result);
            instrumento.getCalibracionList().add(e);
        } catch (Exception ex) {
            throw new Exception("Calibracion no existe");
        }
    }

    public void deleteCalibracion(Calibracion e, Instrumento instrumento) throws Exception {
        instrumento.eliminarCalibracion(e);
    }

    public List<Calibracion> searchCalibracion(Calibracion filter) {
        if (lastSelectedInstrumento == null){lastSelectedInstrumento = new Instrumento();}
        return lastSelectedInstrumento.getCalibracionList().stream()
                .filter(i -> String.valueOf(i.getNumero()).contains(String.valueOf(filter.getNumero())))
                .sorted(Comparator.comparing(Calibracion::getNumero))
                .collect(Collectors.toList());
    }





    public void generatePDFReport(List<?> objects) throws Exception {
        String outputFilePath = reportFileName(objects);


        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new FileOutputStream(outputFilePath)));
        Document document = new Document(pdfDocument);

        Image img = new Image(ImageDataFactory.create(Objects.requireNonNull(Application.class.getResource("presentation/icons/pdf (1).png"))));
        document.add(img);

        String titleText = "Reporte";
        if (!objects.isEmpty()) {
            Object firstObject = objects.get(0);
            titleText += " de " + firstObject.getClass().getSimpleName() + "s";
        }

        Paragraph title = new Paragraph(titleText)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(24);
        document.add(title);

        for (Object obj : objects) {
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String propertyName = field.getName();
                Object propertyValue = field.get(obj);

                if (propertyValue != null) {
                    document.add(new Paragraph(propertyName + ": " + propertyValue.toString()));
                }
            }
            document.add(new Paragraph("----------------------------------"));
        }

        document.close();
    }

    public TipoInstrumento obtenerTipoInstrumentoPorCodigo(String codigo, String filePath) {
        List<TipoInstrumento> tipos = data.getTipos();
        return tipos.stream()
                .filter(t -> t.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

    private String reportFileName(List<?> objects) {
        if (objects.isEmpty()) {
            return "files/PdfReports/default_report.pdf";
        }

        Object firstObject = objects.get(0);
        if (firstObject instanceof TipoInstrumento) {
            return "files/PdfReports/tipo_instrumento_report.pdf";
        } else if (firstObject instanceof Instrumento) {
            return "files/PdfReports/instrumento_report.pdf";
        } else if (firstObject instanceof Calibracion) {
            return "files/PdfReports/calibracion_report.pdf";
        } else {
            return "files/PdfReports/default_report.pdf";
        }
    }


    public void onSelectInstrumento(Instrumento instrumento) {
        lastSelectedInstrumento = instrumento;
    }

    public Instrumento getLastSelectedInstrumentoSelectInstrumento() {
        return  lastSelectedInstrumento;
    }

    public void saveData() throws Exception {
        XmlPersister.instance().store(this.data);
    }
}

