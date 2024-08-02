package ru.netology;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String FILE_NAME_SRC = "src/main/resources/data.xml";
    private static final String FILE_NAME_DEST = "src/main/resources/data2.json";

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> list = parseXML(FILE_NAME_SRC);
        String json = listToJson(list);
        writeString(json, FILE_NAME_DEST);
    }

    private static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));
        Node root = doc.getDocumentElement();
        List<Employee> employees = new ArrayList<>();
        read(root, employees);
        return employees;
    }


    private static void read(Node node, List<Employee> employees) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element element = (Element) node_;

                if (element.getTagName().equals("employee")) {
                    String idStr = element.getElementsByTagName("id").item(0).getTextContent();
                    String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
                    String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
                    String country = element.getElementsByTagName("country").item(0).getTextContent();
                    String ageStr = element.getElementsByTagName("age").item(0).getTextContent();

                    if (!idStr.isEmpty() && !ageStr.isEmpty()) {
                        long id = Long.parseLong(idStr);
                        int age = Integer.parseInt(ageStr);
                        Employee employee = new Employee(id, firstName, lastName, country, age);
                        employees.add(employee);
                    }
                }
                read(node_, employees);
            }
        }
    }


    static String listToJson(List<Employee> list) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    static void writeString(String json, String fileName) {

        try (FileWriter file = new FileWriter(fileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}