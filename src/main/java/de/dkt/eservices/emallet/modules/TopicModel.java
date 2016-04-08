package de.dkt.eservices.emallet.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.topics.TopicInferencer;
import cc.mallet.types.Alphabet;
import cc.mallet.types.IDSorter;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import de.dkt.common.filemanagement.FileFactory;
import eu.freme.common.exception.ExternalServiceFailedException;


/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 */
public class TopicModel {

	static Logger logger = Logger.getLogger(TopicModel.class);

	private static String modelsDirectory = "trainedModels" + File.separator + "topicModelling" + File.separator;
	
	public static void trainModel (String inputData, String modelPath, String modelName, String language) throws ExternalServiceFailedException {
		if(modelPath!=null){
			if(modelPath.endsWith(File.separator)){
				modelsDirectory = modelPath;
			}
			else{
				modelsDirectory = modelPath+File.separator;
			}
		}
		try{
	        ImportData importer = new ImportData("sequence", language);
	        File trainingFile = FileFactory.generateFileInstance(inputData);
	        InstanceList instances = importer.readFile(trainingFile);
	        
	        // Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
	        //  Note that the first parameter is passed as the sum over topics, while the second is the parameter for a single dimension of the Dirichlet prior.
	        int numTopics = 100;
	        ParallelTopicModel model = new ParallelTopicModel(numTopics, 1.0, 0.01);
	        model.addInstances(instances);

	        // Use two parallel samplers, which each look at one half the corpus and combine statistics after every iteration.
	        model.setNumThreads(2);

	        // Run the model for 50 iterations and stop (this is for testing only, for real applications, use 1000 to 2000 iterations)
	        model.setNumIterations(500);
	        model.estimate();

//	        File outputModelFile = new File(modelsDirectory+language+"-"+modelName+".EXT");
	        //model.write(outputModelFile);
	        File outputModelFile = FileFactory.generateOrCreateFileInstance(modelsDirectory+language+"-"+modelName+"_TOPIC.EXT");
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream (outputModelFile));
			oos.writeObject (model);
			oos.close();

	        Alphabet dataAlphabet = instances.getDataAlphabet();
//	        File outputAlphabetFile = new File(modelsDirectory+language+"-"+modelName+"-Alphabet.EXT");
	        File outputAlphabetFile = FileFactory.generateOrCreateFileInstance(modelsDirectory+language+"-"+modelName+"_TOPIC-Alphabet.EXT");
			ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream (outputAlphabetFile));
			oos2.writeObject (dataAlphabet);
			oos2.close();

		}
		catch(IOException e){
			logger.error("Error at training model ["+modelsDirectory+language+"-"+modelName+".EXT"+"] for language ["+language+"]");
			throw new ExternalServiceFailedException("Error at training model ["+modelsDirectory+language+"-"+modelName+".EXT"+"] for language ["+language+"]");
		}
		
	}
	
	public static String detectTopic (String text, String modelPath, String modelName, String language) throws ExternalServiceFailedException {
		if(modelPath!=null){
			if(modelPath.endsWith(File.separator)){
				modelsDirectory = modelPath;
			}
			else{
				modelsDirectory = modelPath+File.separator;
			}
		}
		try{
	        ParallelTopicModel model;
	        Alphabet alphabet;

//	        File modelFile = new File(modelsDirectory + language + "-" + modelName + ".EXT");
	        File modelFile = FileFactory.generateFileInstance(modelsDirectory + language + "-" + modelName + "_TOPIC.EXT");
	        ObjectInputStream ois = new ObjectInputStream (new FileInputStream (modelFile));
	        model = (ParallelTopicModel) ois.readObject();
	        ois.close();
	        
//	        File alphabetFile = new File(modelsDirectory + language + "-" + modelName + "-Alphabet.EXT");
	        File alphabetFile = FileFactory.generateFileInstance(modelsDirectory + language + "-" + modelName + "_TOPIC-Alphabet.EXT");
	        ObjectInputStream ois2 = new ObjectInputStream (new FileInputStream (alphabetFile));
	        alphabet = (Alphabet) ois2.readObject();
	        ois2.close();
	        
	        // Create a new instance named "test instance" with empty target and source fields.
	        ImportData importData = new ImportData("sequence",language);
	        InstanceList testing = new InstanceList(importData.getPipe());
	        testing.addThruPipe(new Instance(text, null, "test instance", null));

	        TopicInferencer inferencer = model.getInferencer();
	        double[] testProbabilities = inferencer.getSampledDistribution(testing.get(0), 100, 1, 5);
	        
	        Formatter out = new Formatter(new StringBuilder(), Locale.US);
	        
	     // Get an array of sorted sets of word ID/count pairs
	        ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
	        //Alphabet dataAlphabet = testing.getDataAlphabet();
	        
	        // Show top 5 words in topics with proportions for the first document
	        Map<Double, String> map = new TreeMap<Double, String>();
	        
	        for (int topic = 0; topic < testProbabilities.length; topic++) {
	            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
	            out = new Formatter(new StringBuilder(), Locale.US);
//	            out.format("%d\t%.3f\t", topic, testProbabilities[topic]);
	            int rank = 0;
	            while (iterator.hasNext() && rank < 5) {
	                IDSorter idCountPair = iterator.next();
//	                out.format("%s (%.0f) ", alphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
	                out.format("%s (%.0f) ", alphabet.lookupObject(idCountPair.getID()), idCountPair.getWeight());
	                rank++;
	            }
	            map.put(testProbabilities[topic], out.toString());
//	            System.out.println(out);
	        }
	        out.close();
	        
	        //TODO Fill the output label with the most common words or maybe other informations
	        Set<Double> set = map.keySet();
	        double finalD = 0;
	        for (Double double1 : set) {
//				System.out.println(double1+"  "+map.get(double1));
	        	finalD = double1;
			}
	        String outputLabel=map.get(finalD);
	        
	        return outputLabel;
		}
		catch(Exception e){
			logger.error("Error at reading stopwordss file for language ["+language+"]");
			throw new ExternalServiceFailedException("Error at reading stopwordss file for language ["+language+"]");
		}
	}

	public static void main(String[] args) throws Exception {

//		TopicModel.trainModel("recursos/data.txt", "recursos/", "test2", "en");
//		String output = TopicModel.detectTopic("Wir haben infektion in einige sachen und virus auch in ersten platz.", "recursos/", "test2", "en");
//		System.out.println("OUTPUT LABEL: "+output);
		//TopicModel tm = new TopicModel();
		//tm.detectTopic("My name is John and I am living in the paradise.", "en");
		//tm.detectTopicInDirectory("recursos/", "en");

		/**
		 * Generate models for 3pc data
		 */
		TopicModel.trainModel("/Users/jumo04/Documents/DFKI/DataCollections/DKT/3pc/3PC_TEXTS.txt", "recursos", "3pc", "de");
		/**
		 * Generate models for condat data using categories and types as labels for classification
		 */
		TopicModel.trainModel("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Condat/Condat_CATEGORIES.txt", "recursos", "condat", "de");
//		TopicModel.trainModel("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Condat/Condat_TYPES.txt", "recursos", "condat_types", "de");
		/**
		 * Generate models for Kreuzwereker data using topics, categories and subcategories as labels for classification
		 */
//		TopicModel.trainModel("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Kreuzwerker/KW_TOPICS.txt", "recursos", "kreuzwerker_topicsTOPIC", "de");
		TopicModel.trainModel("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Kreuzwerker/KW_CATEGORIES.txt", "recursos", "kreuzwerker", "de");
//		TopicModel.trainModel("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Kreuzwerker/KW_SUBCATEGORIES.txt", "recursos", "kreuzwerker_subcategories", "de");

    }
}
