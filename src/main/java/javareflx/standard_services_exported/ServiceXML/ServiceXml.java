package javareflx.standard_services_exported.ServiceXML;

import javareflx.bri.services.Service;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.URL;
import java.util.Properties;


public class ServiceXml extends Service {
    private enum State{
        FTP,
        PORT,
        FILE,
        EMAIL
    }

    private File file;
    private static long fileCount = 0;

    private State state = State.FILE;

    private String server;
    private int port;
    private String path;

    @Override
    protected void onStart() {
        sendMessage("Ce service permet de donner les dépendances d'un fichier projet Maven à partir de son fichier xml.\\n" +
                "Entrez l'adresse de votre serveur ftp");
        state = State.FTP;
        receive();
    }

    @Override
    protected void onClientMessage(String message) {
        switch (state){
            case FTP:
                this.server = message;
                sendMessage("Veuillez reseigner le port");
                state = State.PORT;
                receive();
                break;
            case PORT:
                try {
                    this.port = Integer.parseInt(message);
                    sendMessage("Entrez le chemin du fichier");
                    state = State.FILE;
                    receive();
                }catch (NumberFormatException e){
                    sendMessage("Port incorrect, veuillez entrer le port de nouveau");
                    receive();
                }
                break;
            case FILE:
                this.path = message;
                if(!this.downloadXml()){
                    sendMessage("Fichier incorrect, entrez l'adresse de votre serveur ftp.");
                    state = State.FTP;
                    receive();
                }
                sendMessage("Entrez votre adresse e-mail");
                state = State.EMAIL;
                receive();
                break;
            case EMAIL:
                boolean mail = sendMail(message, getDependencies());
                if (mail){
                    sendMessage("Mail envoyé !");
                }else {
                    sendMessage("Le mail ne peut pas être envoyé");
                }
                break;
        }
    }

    /**
     * Lit les dépendances d'un fichier XML Maven
     * @return Les Dépendances sous forme d'une String
     */
    private String getDependencies(){
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(file.getAbsolutePath()).append("\n");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeListProject = doc.getElementsByTagName("project");
            Node project = nodeListProject.item(0);
            if (project.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) project;

                sb.append("name : ");
                sb.append(element.getElementsByTagName("groupId").item(0).getTextContent()).append(".");
                sb.append(element.getElementsByTagName("artifactId").item(0).getTextContent()).append("\n");
            }

            sb.append("Dépendances : \n");
            NodeList nodeListDependency = doc.getElementsByTagName("dependency");
            for (int i = 0; i < nodeListDependency.getLength(); i++){
                Node repository = nodeListDependency.item(i);
                if (repository.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) repository;

                    sb.append(i).append(" : ");
                    sb.append(element.getElementsByTagName("groupId").item(0).getTextContent()).append(".");
                    sb.append(element.getElementsByTagName("artifactId").item(0).getTextContent()).append("  | Version : ");
                    sb.append(element.getElementsByTagName("version").item(0).getTextContent()).append("\n");
                }
            }
            return sb.toString();
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    private boolean sendMail(String mail, String mailText){
        String[] user = getInformationConnection();

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator(){
                    protected PasswordAuthentication getPasswordAuthentication(){
                        return new PasswordAuthentication(user[0], user[1]);
                    }
                });

        try{
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user[0]));
            message.setRecipient(Message.RecipientType.TO , new InternetAddress(mail));
            message.setSubject("Dependances de votre projet");
            message.setText(mailText);

            Transport.send(message);
            System.out.println("Mail envoyé !");
            return true;
        }catch (MessagingException e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean downloadXml(){
        FTPClient ftpClient = new FTPClient();
        try{
            ftpClient.connect(server,port);
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            file = new File("/serviceXml/data/");
            file.mkdirs();
            file = new File(file,fileCount + ".xml");
            fileCount++;

            file.createNewFile();

            OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));
            boolean success = ftpClient.retrieveFile(path, outputStream);

            System.out.println(ftpClient.getStatus(path));
            outputStream.close();
            if (success){
                return true;
            }else {
                System.out.println("c'est pas un succes !");
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static String[] getInformationConnection(){
        File f = new File("C:\\Users\\thiba\\Desktop\\Thibault\\Iut Info\\2A\\Appref\\Projet\\JavaReflx\\src\\main\\java\\javareflx\\standard_services_exported\\ServiceXML\\user.xml");

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(f);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("mail");
            Node user = nodeList.item(0);

            if (user.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) user;

                return new String[]{
                        element.getElementsByTagName("username").item(0).getTextContent(),
                        element.getElementsByTagName("password").item(0).getTextContent()
                };
            }
            return new String[]{"Error"};
        }catch (Exception e){
            e.printStackTrace();
            return new String[]{"Error"};
        }
    }
}