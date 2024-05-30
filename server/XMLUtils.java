package server;

import shared.Participant;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class XMLUtils {

    public static void exportToXML(List<Participant> participants, String filePath) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Participants");
            doc.appendChild(rootElement);

            for (Participant participant : participants) {
                Element participantElement = doc.createElement("Participant");

                Element name = doc.createElement("Name");
                name.appendChild(doc.createTextNode(participant.getName()));
                participantElement.appendChild(name);

                Element family = doc.createElement("Family");
                family.appendChild(doc.createTextNode(participant.getFamily()));
                participantElement.appendChild(family);

                Element organization = doc.createElement("Organization");
                organization.appendChild(doc.createTextNode(participant.getOrganization()));
                participantElement.appendChild(organization);

                Element report = doc.createElement("Report");
                report.appendChild(doc.createTextNode(participant.getReport()));
                participantElement.appendChild(report);

                Element email = doc.createElement("Email");
                email.appendChild(doc.createTextNode(participant.getEmail()));
                participantElement.appendChild(email);

                rootElement.appendChild(participantElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "Windows-1251");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));

            transformer.transform(source, result);

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }

    public static List<Participant> importFromXML(String filePath) {
        List<Participant> participants = new ArrayList<>();

        try {
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Participant");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String name = eElement.getElementsByTagName("Name").item(0).getTextContent();
                    String family = eElement.getElementsByTagName("Family").item(0).getTextContent();
                    String organization = eElement.getElementsByTagName("Organization").item(0).getTextContent();
                    String report = eElement.getElementsByTagName("Report").item(0).getTextContent();
                    String email = eElement.getElementsByTagName("Email").item(0).getTextContent();

                    Participant participant = new Participant(name, family, organization, report, email);
                    participants.add(participant);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return participants;
    }

    public static String convertToString(List<Participant> participants) throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("Participants");
        doc.appendChild(rootElement);

        for (Participant participant : participants) {
            Element participantElement = doc.createElement("Participant");

            Element name = doc.createElement("Name");
            name.appendChild(doc.createTextNode(participant.getName()));
            participantElement.appendChild(name);

            Element family = doc.createElement("Family");
            family.appendChild(doc.createTextNode(participant.getFamily()));
            participantElement.appendChild(family);

            Element organization = doc.createElement("Organization");
            organization.appendChild(doc.createTextNode(participant.getOrganization()));
            participantElement.appendChild(organization);

            Element report = doc.createElement("Report");
            report.appendChild(doc.createTextNode(participant.getReport()));
            participantElement.appendChild(report);

            Element email = doc.createElement("Email");
            email.appendChild(doc.createTextNode(participant.getEmail()));
            participantElement.appendChild(email);

            rootElement.appendChild(participantElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "Windows-1251");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        DOMSource source = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);

        transformer.transform(source, result);
        return writer.toString();
    }
}