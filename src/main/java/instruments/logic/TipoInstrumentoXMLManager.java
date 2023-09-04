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
}
