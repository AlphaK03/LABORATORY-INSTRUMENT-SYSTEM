package instruments.logic;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XMLManager {

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

                // Calibraciones
                for (Calibracion calibracion : instrumento.getCalibracionList()) {
                    Element calibracionElement = doc.createElement("calibracion");
                    instrumentoElement.appendChild(calibracionElement);

                    // Número
                    Element numeroElement = doc.createElement("numero");
                    numeroElement.appendChild(doc.createTextNode(String.valueOf(calibracion.getNumero())));
                    calibracionElement.appendChild(numeroElement);

                    // Fecha
                    Element fechaElement = doc.createElement("fecha");
                    fechaElement.appendChild(doc.createTextNode(calibracion.getFecha()));
                    calibracionElement.appendChild(fechaElement);

                    // CantidadMediciones
                    Element cantidadMedicionesElement = doc.createElement("cantidadMediciones");
                    cantidadMedicionesElement.appendChild(doc.createTextNode(String.valueOf(calibracion.getCantidadMediciones())));
                    calibracionElement.appendChild(cantidadMedicionesElement);

                    // Mediciones
                    for (Medicion medicion : calibracion.getMediciones()) {
                        Element medicionElement = doc.createElement("medicion");
                        calibracionElement.appendChild(medicionElement);

                        // ValorReferencia
                        Element valorReferenciaElement = doc.createElement("valorReferencia");
                        valorReferenciaElement.appendChild(doc.createTextNode(String.valueOf(medicion.getValorReferencia())));
                        medicionElement.appendChild(valorReferenciaElement);

                        // ValorLectura
                        Element valorLecturaElement = doc.createElement("valorLectura");
                        valorLecturaElement.appendChild(doc.createTextNode(String.valueOf(medicion.getValorLectura())));
                        medicionElement.appendChild(valorLecturaElement);
                    }
                }
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
                        TipoInstrumento tipoInstrumento = obtenerTipoInstrumentoPorCodigo(tipoInstrumentoCodigo, "files/XMLData/TiposInstrumentos.xml");


                        Instrumento instrumento = new Instrumento(serie, descripcion, tolerancia, maximo, minimo, tipoInstrumento);
                        if (tipoInstrumento != null) {
                            // Crear una lista vacía para las calibraciones
                            List<Calibracion> calibraciones = new ArrayList<>();

                            // Obtener las calibraciones dentro del instrumento
                            NodeList calibracionNodes = instrumentoElement.getElementsByTagName("calibracion");

                            for (int j = 0; j < calibracionNodes.getLength(); j++) {
                                Node calibracionNode = calibracionNodes.item(j);

                                if (calibracionNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element calibracionElement = (Element) calibracionNode;

                                    int numero = Integer.parseInt(calibracionElement.getElementsByTagName("numero").item(0).getTextContent());
                                    String fecha = calibracionElement.getElementsByTagName("fecha").item(0).getTextContent();
                                    int cantidadMediciones = Integer.parseInt(calibracionElement.getElementsByTagName("cantidadMediciones").item(0).getTextContent());

                                    // Obtener las mediciones dentro de la calibración
                                    NodeList medicionNodes = calibracionElement.getElementsByTagName("medicion");
                                    List<Medicion> mediciones = new ArrayList<>();

                                    for (int k = 0; k < medicionNodes.getLength(); k++) {
                                        Node medicionNode = medicionNodes.item(k);

                                        if (medicionNode.getNodeType() == Node.ELEMENT_NODE) {
                                            Element medicionElement = (Element) medicionNode;
                                            double valorReferencia = Double.parseDouble(medicionElement.getElementsByTagName("valorReferencia").item(0).getTextContent());
                                            double valorLectura = Double.parseDouble(medicionElement.getElementsByTagName("valorLectura").item(0).
                                                    getTextContent());

                                            // Crear y agregar la medicion a la lista de mediciones
                                            Medicion medicion = new Medicion(valorReferencia, valorLectura);
                                            mediciones.add(medicion);
                                        }
                                    }

                                    // Crear y agregar la calibracion al instrumento
                                    Calibracion calibracion = new Calibracion(numero, instrumento, fecha, cantidadMediciones, mediciones);
                                    calibraciones.add(calibracion);
                                }
                            }

                            // Crear el instrumento con sus calibraciones
                            instrumento.setCalibracionList(calibraciones);

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



    public static void guardarCalibraciones(List<Calibracion> calibraciones, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("calibraciones");
            doc.appendChild(rootElement);

            for (Calibracion calibracion : calibraciones) {
                Element calibracionElement = doc.createElement("calibracion");
                rootElement.appendChild(calibracionElement);

                // Número
                Element numeroElement = doc.createElement("numero");
                numeroElement.appendChild(doc.createTextNode(String.valueOf(calibracion.getNumero())));
                calibracionElement.appendChild(numeroElement);

                // Fecha
                Element fechaElement = doc.createElement("fecha");
                fechaElement.appendChild(doc.createTextNode(calibracion.getFecha()));
                calibracionElement.appendChild(fechaElement);

                // CantidadMediciones
                Element cantidadMedicionesElement = doc.createElement("cantidadMediciones");
                cantidadMedicionesElement.appendChild(doc.createTextNode(String.valueOf(calibracion.getCantidadMediciones())));
                calibracionElement.appendChild(cantidadMedicionesElement);

                // InstrumentoCalibrado (assuming you save it as the Serie of the Instrumento)
                Element instrumentoCalibradoElement = doc.createElement("instrumentoCalibrado");
                instrumentoCalibradoElement.appendChild(doc.createTextNode(calibracion.getInstrumentoCalibrado().getSerie()));
                calibracionElement.appendChild(instrumentoCalibradoElement);

                // Mediciones
                for (Medicion medicion : calibracion.getMediciones()) {
                    Element medicionElement = doc.createElement("medicion");
                    calibracionElement.appendChild(medicionElement);

                    // ValorReferencia
                    Element valorReferenciaElement = doc.createElement("valorReferencia");
                    valorReferenciaElement.appendChild(doc.createTextNode(String.valueOf(medicion.getValorReferencia())));
                    medicionElement.appendChild(valorReferenciaElement);

                    // ValorLectura
                    Element valorLecturaElement = doc.createElement("valorLectura");
                    valorLecturaElement.appendChild(doc.createTextNode(String.valueOf(medicion.getValorLectura())));
                    medicionElement.appendChild(valorLecturaElement);
                }
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


    public static List<Calibracion> cargarCalibraciones(String filePath, List<Instrumento> instrumentos) {
        List<Calibracion> calibraciones = new ArrayList<>();

        try {
            File file = new File(filePath);
            if (file.exists()) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);

                NodeList calibracionNodes = doc.getElementsByTagName("calibracion");

                for (int i = 0; i < calibracionNodes.getLength(); i++) {
                    Node calibracionNode = calibracionNodes.item(i);

                    if (calibracionNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element calibracionElement = (Element) calibracionNode;

                        int numero = Integer.parseInt(calibracionElement.getElementsByTagName("numero").item(0).getTextContent());
                        String fecha = calibracionElement.getElementsByTagName("fecha").item(0).getTextContent();
                        int cantidadMediciones = Integer.parseInt(calibracionElement.getElementsByTagName("cantidadMediciones").item(0).getTextContent());
                        String serieInstrumento = calibracionElement.getElementsByTagName("instrumentoCalibrado").item(0).getTextContent();

                        // Find the Instrumento object corresponding to the serie
                        Instrumento instrumentoCalibrado = obtenerInstrumentoPorSerie(serieInstrumento, instrumentos);

                        if (instrumentoCalibrado != null) {
                            Calibracion calibracion = new Calibracion();
                            calibracion.setNumero(numero);
                            calibracion.setFecha(fecha);
                            calibracion.setCantidadMediciones(cantidadMediciones);
                            calibracion.setInstrumentoCalibrado(instrumentoCalibrado);

                            // Inicializar la lista de mediciones
                            calibracion.setMediciones(new ArrayList<>());

                            NodeList medicionNodes = calibracionElement.getElementsByTagName("medicion");

                            for (int j = 0; j < medicionNodes.getLength(); j++) {
                                Node medicionNode = medicionNodes.item(j);

                                if (medicionNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element medicionElement = (Element) medicionNode;

                                    double valorReferencia = Double.parseDouble(medicionElement.getElementsByTagName("valorReferencia").item(0).getTextContent());
                                    double valorLectura = Double.parseDouble(medicionElement.getElementsByTagName("valorLectura").item(0).getTextContent());

                                    Medicion medicion = new Medicion(valorReferencia, valorLectura);
                                    calibracion.getMediciones().add(medicion);
                                }
                            }

                            calibraciones.add(calibracion);

                        }

                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace();
        }
        return calibraciones;
    }



    // Function to find an Instrumento by its serie
    private static Instrumento obtenerInstrumentoPorSerie(String serie, List<Instrumento> instrumentos) {
        return instrumentos.stream()
                .filter(i -> i.getSerie().equals(serie))
                .findFirst()
                .orElse(null);
    }
}


