package de.dkt.eservices.eweka.modules;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Debug {

	
	
	public static void main(String[] args) throws Exception{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/jumo04/git/e-Weka/src/main/resources/outputALONE.txt"), "utf-8"));
		
		List<List<String>> clusters = new LinkedList<List<String>>();
		List<String> lines = null;
		
		String line = br.readLine();
		while(line!=null){
			if(!line.equalsIgnoreCase("")){
				if(line.startsWith("--")){
					if(lines!=null){
						clusters.add(lines);
					}
					lines = new LinkedList<>();
				}
				else{
					lines.add(line);
				}
			}
			line = br.readLine();
		}
		clusters.add(lines);
		
		System.out.println(clusters.size());
		int counter = 1;
//		for (List<String> ls : clusters) {
//			System.out.println("-------CLUSTER "+counter);
//			Debug.printPersons(ls,0.01);
//			counter++;
//		}
//		counter = 1;
		for (List<String> ls : clusters) {
			System.out.println("-------CLUSTER "+counter);
			Debug.printDates(ls,0.001);
			counter++;
		}
//		counter = 1;
//		for (List<String> ls : clusters) {
//			System.out.println("-------CLUSTER "+counter);
//			Debug.printLocations(ls,0.1);
//			counter++;
//		}
		br.close();
		
	}

	public static void printLocations(List<String> lines,double threshold){
		for (String s : lines) {
//			System.out.println(s);
			double d = Double.parseDouble(s.substring(s.indexOf('[')+1, s.indexOf(']')));
			if(d>threshold){
				if(s.contains("geonames")){
					System.out.println(s);
				}
			}
		}
	}
	public static void printDates(List<String> lines,double threshold){
		for (String s : lines) {
//			System.out.println(s);
			double d = Double.parseDouble(s.substring(s.indexOf('[')+1, s.indexOf(']')));
			if(d>threshold){
				if(s.contains("date")){
					System.out.println(s);
				}
			}
		}
	}
	public static void printPersons(List<String> lines,double threshold){
		for (String s : lines) {
//			System.out.println(s);
			double d = Double.parseDouble(s.substring(s.indexOf('[')+1, s.indexOf(']')));
			if(d>threshold){
				if(s.contains("d-nb")){
					System.out.println(s);
				}
			}
		}
	}
}
