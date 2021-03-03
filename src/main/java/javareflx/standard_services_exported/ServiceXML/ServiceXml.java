package javareflx.standard_services_exported.ServiceXML;

import javareflx.bri.services.Service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class ServiceXml extends Service {

    @Override
    protected void onStart() {
        sendMessage("Ce service permet de donner les dépendances d'un fichier projet Maven à partir de son fichier xml. Donnez son chemin :");
        receive();
    }

    @Override
    protected void onClientMessage(String message) {
        File f = new File("C:\\Users\\thiba\\Desktop\\Thibault\\Iut Info\\2A\\Appref\\Projet\\JavaReflx\\src\\main\\java\\javareflx\\standard_services_exported\\ServiceXML\\test.xml");
        System.out.println(getDependencies(f));
    }


    private String getDependencies(File fichier){
        try {
            StringBuilder sb = new StringBuilder();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(fichier);
            doc.getDocumentElement().normalize();

            NodeList nodeListProject = doc.getElementsByTagName("project");
            Node project = nodeListProject.item(0);
            if (project.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) project;

                sb.append("name : ");
                sb.append(element.getElementsByTagName("groupId").item(0).getTextContent()).append(".");
                sb.append(element.getElementsByTagName("artifactId").item(0).getTextContent()).append("\\n");

            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }
}
