package gmit.ie.sw;
import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.xml.parsers.*;
import java.io.*;

public class Config {
	public Config(String fileName) {
		super();
		this.fileName = fileName;
	}

	String fileName;
	String serverHost;
	public String getServerHost() {
		return serverHost;
	}

	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	public String getDownloadDir() {
		return downloadDir;
	}

	public void setDownloadDir(String downloadDir) {
		this.downloadDir = downloadDir;
	}

	String serverPort;
	String downloadDir;
	
	public void parse(){
	try {	
        File inputFile = new File(fileName);
        DocumentBuilderFactory dbFactory 
           = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" 
           + doc.getDocumentElement().getNodeName());
        NodeList serverhost = doc.getElementsByTagName("server-host");
        NodeList serverport = doc.getElementsByTagName("server-port");
        NodeList downloaddir = doc.getElementsByTagName("download-dir");
        System.out.println("----------------------------");
        System.out.println("Server host = "+ serverhost.item(0).getTextContent());
        System.out.println("Server port = "+ serverport.item(0).getTextContent());
        System.out.println("download dir = "+ downloaddir.item(0).getTextContent());
        serverPort = serverport.item(0).getTextContent();
        serverHost = serverhost.item(0).getTextContent();
        downloadDir = downloaddir.item(0).getTextContent();
     } catch (Exception e) {
        e.printStackTrace();
     }
  }
}