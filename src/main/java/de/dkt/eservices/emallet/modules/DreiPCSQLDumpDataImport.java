package de.dkt.eservices.emallet.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DreiPCSQLDumpDataImport {

	public List<String> parseTextsFromFile(String filePath, String outputFilePath, String outputFileName) throws Exception {
		List<String> list = new LinkedList<String>();
		BufferedWriter bwText = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath+outputFileName+"_TEXTS.txt"), "utf-8"));

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		FileInputStream fis = new FileInputStream(filePath);
		Document document = builder.parse(fis);
		NodeList nList = document.getElementsByTagName("row");
		
		String text = "";
		String category = "";
		int counter=1;
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				NodeList fields = eElement.getElementsByTagName("field");
				for (int i = 0; i < fields.getLength(); i++) {
					Element e = (Element) fields.item(i);
					String name = e.getAttribute("name");
					if(name.equalsIgnoreCase("text")){
						text = e.getTextContent().replaceAll("\\n", " ");
//						System.out.println("TEXT: "+text);
					}
				}
			}
			bwText.write(counter+" X "+text+"\n");
			counter++;
		}
		bwText.close();
		return list;
	}

	public List<String> parseCatsFromFile(String filePath, String outputFilePath, String outputFileName) throws Exception {
		List<String> list = new LinkedList<String>();
		BufferedWriter bwCats = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath+outputFileName+"_CATEGORIES.txt"), "utf-8"));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		FileInputStream fis = new FileInputStream(filePath);
		Document document = builder.parse(fis);
		NodeList nList = document.getElementsByTagName("row");
		
		String text = "";
		String category = "";
		int counter=1;
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				NodeList fields = eElement.getElementsByTagName("field");
				for (int i = 0; i < fields.getLength(); i++) {
					Element e = (Element) fields.item(i);
					String name = e.getAttribute("name");
					if(name.equalsIgnoreCase("text")){
						text = e.getTextContent().replaceAll("\\n", " ");
//						System.out.println("TEXT: "+text);
					}
					else if(name.equalsIgnoreCase("ereignis")){
						category = e.getTextContent().replace(' ', '_');;
					}
				}
			}
			bwCats.write(counter+" "+category+" "+text+"\n");
			counter++;
		}
		bwCats.close();
		return list;
	}

	public static void main(String[] args) throws Exception{
		DreiPCSQLDumpDataImport k = new DreiPCSQLDumpDataImport();
		k.parseCatsFromFile("/Users/jumo04/Documents/DFKI/DataCollections/DKT/3pc/mendelsohn_ereignis_key_text_ereignis.cleaned.xml",
				"/Users/jumo04/Documents/DFKI/DataCollections/DKT/3pc/",
				"3PC");
		k.parseTextsFromFile("/Users/jumo04/Documents/DFKI/DataCollections/DKT/3pc/mendelsohn_complete_key_text.cleaned.xml",
				"/Users/jumo04/Documents/DFKI/DataCollections/DKT/3pc/",
				"3PC");
	}
}
