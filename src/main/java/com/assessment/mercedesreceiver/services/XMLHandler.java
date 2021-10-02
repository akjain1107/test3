package com.assessment.mercedesreceiver.services;


import com.assessment.mercedesreceiver.model.User;
import com.assessment.mercedesreceiver.model.UserProtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class XMLHandler {
    @Value("${xmlpath}")
    private String xmlFilePath;
    @Autowired
    private RestTemplate restTemplate;

    public String store(User userDetails, String fileType) {
        prepareProtoRequest(userDetails);
        if("XML".equalsIgnoreCase(fileType)){
            try {
                File xmlFile = XMLFileCreator.getInstance(xmlFilePath);
                Document document = setUpDocumentBuider(xmlFile);
                Element root=null;
                if(xmlFile.length()==0){
                    System.out.println("Root added to empty file!!");
                    root = document.createElement("users");
                    document.appendChild(root);
                }
                else{
                    System.out.println("Root added into non empty file!!");
                    root = document.getDocumentElement();
                }
                root.appendChild(createXMLTags(document,userDetails));
                DOMSource source = new DOMSource(document);
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                StreamResult result = new StreamResult(xmlFile);
                transformer.transform(source, result);
                return userDetails.getUuid();
            } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
                e.printStackTrace();
            }
        }else if("CSV".equalsIgnoreCase(fileType)){

            }
        return "Something went wrong!!";
        }

    private UserProtos.Player prepareProtoRequest(User userDetails) {
        return UserProtos.Player.newBuilder()
                .setId(userDetails.getUuid())
                .setDob(userDetails.getDob())
                .setSalary(String.valueOf(userDetails.getSalary()))
                .setAgr(userDetails.getAge())
                .build();
    }

    private Element createXMLTags(Document document, User userDetails) {

        Element user = document.createElement("user");
        Element id = document.createElement("id");
        id.appendChild(document.createTextNode(userDetails.getUuid()));
        user.appendChild(id);

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(userDetails.getName()));
        user.appendChild(name);

        Element dob = document.createElement("dob");
        dob.appendChild(document.createTextNode(userDetails.getDob()));
        user.appendChild(dob);

        Element salary = document.createElement("salary");
        salary.appendChild(document.createTextNode(String.valueOf(userDetails.getSalary())));
        user.appendChild(salary);

        Element age = document.createElement("age");
        age.appendChild(document.createTextNode(String.valueOf(userDetails.getAge())));
        user.appendChild(age);

        return user;
    }

    private Document setUpDocumentBuider(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = getDocumentBuilder();
        Document document = null;
        if(xmlFile.length()==0){
            System.out.println("File is empty");
            document = documentBuilder.newDocument();
        }
        else{
            System.out.println("File is not empty");
            document = documentBuilder.parse(xmlFile);
        }
        return document;
    }

    private DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    public List<User> read() {
        try {
            File xmlFile = XMLFileCreator.getInstance(xmlFilePath);
            if(xmlFile.length()==0){
                System.out.println("Root added to empty file!!");
                return new ArrayList<>();
            }
            DocumentBuilder builder = getDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.normalizeDocument();
            System.out.println("Node name: " +document.getDocumentElement().getNodeName());
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getElementsByTagName("user");
            return printNodeList(nodeList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    private List<User> printNodeList(NodeList childNodes) {
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node elemNode = childNodes.item(i);
            if (elemNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element eElement = (Element) elemNode;
                userList.add(new User(eElement.getElementsByTagName("id").item(0).getTextContent(),
                        eElement.getElementsByTagName("name").item(0).getTextContent(),
                        eElement.getElementsByTagName("dob").item(0).getTextContent(),
                        Double.parseDouble(eElement.getElementsByTagName("salary").item(0).getTextContent()),
                        Integer.parseInt(eElement.getElementsByTagName("age").item(0).getTextContent())));
                System.out.println("Id: "+ eElement.getElementsByTagName("id").item(0).getTextContent());
                System.out.println("Name: "+ eElement.getElementsByTagName("name").item(0).getTextContent());
                System.out.println("DOB: "+ eElement.getElementsByTagName("dob").item(0).getTextContent());
                System.out.println("Salary: "+ eElement.getElementsByTagName("salary").item(0).getTextContent());
                System.out.println("Age: "+ eElement.getElementsByTagName("age").item(0).getTextContent());
            }
        }
        return userList;
    }

}
