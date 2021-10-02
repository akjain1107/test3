package com.assessment.mercedesreceiver.services;

import com.assessment.mercedesreceiver.model.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class AddXmlNode {
    public static void main(String[] args) throws Exception {
        File xmlFile = new File("C:\\Test\\user.xml");
        xmlFile.createNewFile();

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = null;
        Element root = null;

        if(xmlFile.length()==0){
            System.out.println("File is empty");
            document = documentBuilder.newDocument();
            root = document.createElement("users");
            document.appendChild(root);
        }
        else{
            System.out.println("File is not empty");
            document = documentBuilder.parse(xmlFile);
            root = document.getDocumentElement();
        }

        Collection<User> users = new ArrayList<User>();
        users.add(new User("1","Amit","1234",5678,20));

        for (User user : users) {
            // server elements
            Element newUser = document.createElement("user");

            Element name = document.createElement("name");
            name.appendChild(document.createTextNode(user.getName()));
            newUser.appendChild(name);

            Element age = document.createElement("age");
            age.appendChild(document.createTextNode(Integer.toString(user.getAge())));
            newUser.appendChild(age);

            root.appendChild(newUser);
        }

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(xmlFile);
        transformer.transform(source, result);
    }

}
