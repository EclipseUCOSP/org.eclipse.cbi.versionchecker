package org.eclipse.cbi.versionchecker.ui.actions;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.eclipse.core.runtime.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

/**
 * VCXmlParser is used for loading data from artifacts.xml. It will check the
 * Eclipse platform install path and try loading the data from XML file.
 */
public class VCXmlParser {

	public Object[] parse() {
		Object[] toRet = null;
		try {
			java.net.URL tmpUrl = Platform.getInstallLocation().getURL();

			File fXmlFile = new File(tmpUrl.getPath() + "artifacts.xml");

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			Node tmp = doc.getElementsByTagName("artifacts").item(0);
			NodeList artList = tmp.getChildNodes();

			int artSize = 0;
			for (int temp = 0; temp < artList.getLength(); temp++) {
				Node nNode = artList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					artSize++;
				}
			}

			toRet = new Object[artSize];
			int curIndex = 0;
			for (int temp = 0; temp < artList.getLength(); temp++) {
				Node nNode = artList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) nNode;
					toRet[curIndex] = new VCArtifact(e.getAttribute("id"),
							e.getAttribute("version"));
					curIndex++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return toRet;
	}

}
