package versionchecker.actions;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class VCXmlParser {

	public void parse() {
		try {

			File fXmlFile = new File("C:\\eclipse32\\artifacts.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();

			System.out.println("Root element :"
					+ doc.getDocumentElement().getNodeName());
			Node tmp = doc.getElementsByTagName("artifacts").item(0);
			NodeList artList = tmp.getChildNodes();
			
			
			System.out.println("-----------------------");

			for (int temp = 0; temp < artList.getLength(); temp++) {

				Node nNode = artList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element e = (Element) nNode;

					System.out.println("ID : "
							+ e.getAttribute("id"));
					System.out.println("Version : "
							+ e.getAttribute("version"));

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
