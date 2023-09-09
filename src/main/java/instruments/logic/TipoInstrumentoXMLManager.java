package instruments.logic;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import instruments.data.Data;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TipoInstrumentoXMLManager {

    public static void guardarTiposInstrumento(List<TipoInstrumento> tipos, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("tipos_instrumento");
            doc.appendChild(rootElement);

            for (TipoInstrumento tipo : tipos) {
                Element tipoElement = doc.createElement("tipo");
                rootElement.appendChild(tipoElement);

                // Código
                Element codigoElement = doc.createElement("codigo");
                codigoElement.appendChild(doc.createTextNode(tipo.getCodigo()));
                tipoElement.appendChild(codigoElement);

                // Nombre
                Element nombreElement = doc.createElement("nombre");
                nombreElement.appendChild(doc.createTextNode(tipo.getNombre()));
                tipoElement.appendChild(nombreElement);

                // Unidad
                Element unidadElement = doc.createElement("unidad");
                unidadElement.appendChild(doc.createTextNode(tipo.getUnidad()));
                tipoElement.appendChild(unidadElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }
    }

    public static List<TipoInstrumento> cargarTiposInstrumento(String filePath) {
        List<TipoInstrumento> tipos = new ArrayList<>();

        try {
            File file = new File(filePath);
            if (file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);

                NodeList tipoNodes = doc.getElementsByTagName("tipo");

                for (int i = 0; i < tipoNodes.getLength(); i++) {
                    Node tipoNode = tipoNodes.item(i);

                    if (tipoNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element tipoElement = (Element) tipoNode;
                        String codigo = tipoElement.getElementsByTagName("codigo").item(0).getTextContent();
                        String nombre = tipoElement.getElementsByTagName("nombre").item(0).getTextContent();
                        String unidad = tipoElement.getElementsByTagName("unidad").item(0).getTextContent();
                        tipos.add(new TipoInstrumento(codigo, nombre, unidad));
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }

        return tipos;
    }
    public static void guardarInstrumentos(List<Instrumento> instrumentos, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("instrumentos");
            doc.appendChild(rootElement);

            for (Instrumento instrumento : instrumentos) {
                Element instrumentoElement = doc.createElement("instrumento");
                rootElement.appendChild(instrumentoElement);

                // Serie
                Element serieElement = doc.createElement("serie");
                serieElement.appendChild(doc.createTextNode(instrumento.getSerie()));
                instrumentoElement.appendChild(serieElement);

                // Descripcion
                Element descripcionElement = doc.createElement("descripcion");
                descripcionElement.appendChild(doc.createTextNode(instrumento.getDescripcion()));
                instrumentoElement.appendChild(descripcionElement);

                // Tolerancia
                Element toleranciaElement = doc.createElement("tolerancia");
                toleranciaElement.appendChild(doc.createTextNode(String.valueOf(instrumento.getTolerancia())));
                instrumentoElement.appendChild(toleranciaElement);

                // Maximo
                Element maximoElement = doc.createElement("maximo");
                maximoElement.appendChild(doc.createTextNode(String.valueOf(instrumento.getMaximo())));
                instrumentoElement.appendChild(maximoElement);

                // Minimo
                Element minimoElement = doc.createElement("minimo");
                minimoElement.appendChild(doc.createTextNode(String.valueOf(instrumento.getMinimo())));
                instrumentoElement.appendChild(minimoElement);

                // TipoInstrumento (asumiendo que el tipo se guarda como código)
                Element tipoInstrumentoElement = doc.createElement("tipoInstrumento");
                tipoInstrumentoElement.appendChild(doc.createTextNode(instrumento.getTipoInstrumento().getCodigo()));
                instrumentoElement.appendChild(tipoInstrumentoElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }
    }

    public static List<Instrumento> cargarInstrumentos(String filePath) {
        List<Instrumento> instrumentos = new ArrayList<>();

        try {
            File file = new File(filePath);
            if (file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);

                NodeList instrumentoNodes = doc.getElementsByTagName("instrumento");

                for (int i = 0; i < instrumentoNodes.getLength(); i++) {
                    Node instrumentoNode = instrumentoNodes.item(i);

                    if (instrumentoNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element instrumentoElement = (Element) instrumentoNode;

                        // Obtén los valores dentro del bucle
                        String serie = instrumentoElement.getElementsByTagName("serie").item(0).getTextContent();
                        String descripcion = instrumentoElement.getElementsByTagName("descripcion").item(0).getTextContent();
                        double tolerancia = Double.parseDouble(instrumentoElement.getElementsByTagName("tolerancia").item(0).getTextContent());
                        double maximo = Double.parseDouble(instrumentoElement.getElementsByTagName("maximo").item(0).getTextContent());
                        double minimo = Double.parseDouble(instrumentoElement.getElementsByTagName("minimo").item(0).getTextContent());
                        String tipoInstrumentoCodigo = instrumentoElement.getElementsByTagName("tipoInstrumento").item(0).getTextContent();

                        // En este punto, debes buscar el TipoInstrumento correspondiente en tu lista de TipoInstrumento cargada previamente
                        TipoInstrumento tipoInstrumento = obtenerTipoInstrumentoPorCodigo(tipoInstrumentoCodigo, "files/TiposInstrumentos.xml");

                        if (tipoInstrumento != null) {
                            Instrumento instrumento = new Instrumento(serie, descripcion, tolerancia, maximo, minimo, tipoInstrumento);
                            instrumentos.add(instrumento);
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }

        return instrumentos;
    }



    // Función para obtener un TipoInstrumento por su código
    private static TipoInstrumento obtenerTipoInstrumentoPorCodigo(String codigo, String filePath) {
        List<TipoInstrumento> tipos = cargarTiposInstrumento(filePath);
        return tipos.stream()
                .filter(t -> t.getCodigo().equals(codigo))
                .findFirst()
                .orElse(null);
    }

}
