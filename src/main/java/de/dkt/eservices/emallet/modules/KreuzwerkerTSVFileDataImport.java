package de.dkt.eservices.emallet.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

public class KreuzwerkerTSVFileDataImport {

	
	
	public List<String> parseFile(String filePath, String outputFilePath, String outputFileName) throws Exception {
		List<String> list = new LinkedList<String>();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-8"));

		BufferedWriter bwTop = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath+outputFileName+"_TOPICS.txt"), "utf-8"));
		BufferedWriter bwCat = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath+outputFileName+"_CATEGORIES.txt"), "utf-8"));
		BufferedWriter bwSubcat = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath+outputFileName+"_SUBCATEGORIES.txt"), "utf-8"));
		List<String> lTopics= new LinkedList<String>();
		List<String> categories = new LinkedList<String>();
		List<String> subcategories = new LinkedList<String>();
		
		String line = br.readLine();
		line = br.readLine();
		int counter = 1;
		while(line!=null){

//			System.out.println(line);
			String parts[] = line.split("\t");
			if(parts.length>15){
//				for (int i = 0; i < parts.length; i++) {
//					System.out.println(i+"  "+parts[i]);
//				}
				String source = parts[0];
				String sourceURL = parts[1];
				String titleSource = parts[2];
				String tTitlePre = parts[3];
				String title = parts[4];
				String authors = parts[5];
				String articleType = parts[6];
				String articleIntro = parts[7];
				String articleBody = parts[8];
				String thirdPartySources = parts[9];
				String dateTimePublished = parts[10];
				String dateTimePublishedSourceString = parts[11];
				String topics = parts[12].replace(' ', '_');
				String mainCategory = parts[13].replace(' ', '_');
				String subCategory = parts[14].replace(' ', '_');
//					String leadImage = parts[15];
//					String leadImageTitle = parts[16];
//					String leadImageCaption = parts[17];
//					if(parts.length>18){
//					String leadImageSource = parts[18];
//					String copyrightNotice = parts[19];
//					if(parts.length>20){
//						String commentCount = parts[20];
//					}
//				}
				//TODO Generate NIF from input line.
//				if(!lTopics.contains(topics)){
//					lTopics.add(topics);
//				}
//				if(!categories.contains(mainCategory)){
//					categories.add(mainCategory);
//				}
//				if(!subcategories.contains(subCategory)){
//					subcategories.add(subCategory);
//				}
				if(!topics.equalsIgnoreCase("")){
					bwTop.write(counter+" "+topics+" "+articleBody+"\n");
				}
				if(!mainCategory.equalsIgnoreCase("")){
					bwCat.write(counter+" "+mainCategory+" "+articleBody+"\n");
				}
				if(!subCategory.equalsIgnoreCase("")){
					bwSubcat.write(counter+" "+subCategory+" "+articleBody+"\n");
				}
				counter++;
			}
			
			line = br.readLine();
		}
		br.close();
		bwTop.close();
		bwCat.close();
		bwSubcat.close();
		
//		System.out.println("=======================");
//		System.out.println("TOPICS");
//		for (String string : lTopics) {
//			System.out.println("\t"+string);
//		}
//		System.out.println("=======================");
//		System.out.println("CATEGORIES");
//		for (String string : categories) {
//			System.out.println("\t"+string);
//		}
//		System.out.println("=======================");
//		System.out.println("SUBCATEGORIES");
//		for (String string : subcategories) {
//			System.out.println("\t"+string);
//		}
		return list;
	}
	
	public static void main(String[] args) throws Exception{
		KreuzwerkerTSVFileDataImport k = new KreuzwerkerTSVFileDataImport();
		k.parseFile("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Kreuzwerker/Data-Set-Spiegel-Online-Sueddeutsche-Berliner-Zeitung-konsolidiert-20160126.tsv",
				"/Users/jumo04/Documents/DFKI/DataCollections/DKT/Kreuzwerker/",
				"KW");
	}
}
