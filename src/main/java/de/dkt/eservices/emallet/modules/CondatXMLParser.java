package de.dkt.eservices.emallet.modules;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class CondatXMLParser {

	
	public List<String> parseDirectory(String inputDirectory, String outputFilePath, String outputFileName) throws Exception {
		List<String> list = new LinkedList<String>();
		
		int counter = 1;
		
		BufferedWriter bwTyp = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath+outputFileName+"_TYPES.txt"), "utf-8"));
		BufferedWriter bwCat = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath+outputFileName+"_CATEGORIES.txt"), "utf-8"));
		
		File dir = new File(inputDirectory);
		File files[] = dir.listFiles();
		for (File f: files) {
			String[] parts = parseFile(f.getAbsolutePath());
			
			bwTyp.write(counter+" "+parts[2]+" "+parts[0]+" "+parts[1]+"\n");
			bwCat.write(counter+" "+parts[3]+" "+parts[0]+" "+parts[1]+"\n");
			counter++;
		}
		bwTyp.close();
		bwCat.close();
		
		return list;
	}
	
	
	public String[] parseFile(String inputFile) throws Exception {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		FileInputStream fis = new FileInputStream(inputFile);
		Document document = builder.parse(fis);
		NodeList nList = document.getElementsByTagName("OM_FIELD");
		
		String type = "";
		String title = "";
		String text = "";
		String category = "";
		String agency = "";
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				String name = eElement.getAttribute("Name");
				if(name.equalsIgnoreCase("Title")){
					title = eElement.getTextContent().replaceAll("\\\\n", " ");
				}
				else if(name.equalsIgnoreCase("ObjectType")){
					type = eElement.getTextContent().replace(' ', '_');;
				}
				else if(name.equalsIgnoreCase("Text")){
					text = eElement.getTextContent().replaceAll("\\n", " ");
//					System.out.println("TEXT: "+text);
				}
				else if(name.equalsIgnoreCase("Category")){
					category = eElement.getTextContent().replace(' ', '_');;
				}
				else if(name.equalsIgnoreCase("Agentur")){
					agency = eElement.getTextContent();
				}
			}
		}
		return new String[]{title,text,type,category,agency};
	}
	
	
	
	public static void main(String[] args) throws Exception {
		String inputFile = "/Users/jumo04/Documents/DFKI/DataCollections/DKT/Condat/OpenMedia_de_20150729/read_WR_1_3037889.xml";
		String inputDirectory = "/Users/jumo04/Documents/DFKI/DataCollections/DKT/Condat/OpenMedia_de_20150729/";
		CondatXMLParser c = new CondatXMLParser();
		c.parseDirectory(inputDirectory, 
				"/Users/jumo04/Documents/DFKI/DataCollections/DKT/Condat/", 
				"Condat");
	}
}
