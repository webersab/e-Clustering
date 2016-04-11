package de.dkt.eservices.emallet.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.apache.log4j.Logger;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEnt;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Labeling;
import de.dkt.common.filemanagement.FileFactory;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 */
public class DocumentClassification {
	
	static Logger logger = Logger.getLogger(DocumentClassification.class);

	private static String modelsDirectory = "trainedModels" + File.separator + "documentClassification" + File.separator;

	public static String classifyString(String inputText, String modelPath, String modelName, String language) throws ExternalServiceFailedException {
		if(modelPath!=null){
			if(modelPath.endsWith(File.separator)){
				modelsDirectory = modelPath;
			}
			else{
				modelsDirectory = modelPath+File.separator;
			}
		}
		
		Classifier classifier;
		try{
			File modelFile = FileFactory.generateFileInstance(modelsDirectory + language + "-" + modelName + ".EXT");
//			File modelFile = new File(modelsDirectory + language + "-" + modelName + ".EXT");
	        ObjectInputStream ois = new ObjectInputStream (new FileInputStream (modelFile));
	        classifier = (Classifier) ois.readObject();
	        ois.close();

	        ImportData importData = new ImportData("vector",language);
	        InstanceList testing = new InstanceList(importData.getPipe());
	        testing.addThruPipe(new Instance(inputText, "", "test instance", ""));
	        
	        String output="";

	        Classification classification = classifier.classify(testing.get(0));
	        Labeling labeling = classification.getLabeling();
        	output = labeling.getLabelAtRank(0).toString();
	        return output;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}
	
	public static String classifyFile(String inputDataFile, String modelPath, String modelName, String language) throws ExternalServiceFailedException {
		
		if(modelPath!=null){
			if(modelPath.endsWith(File.separator)){
				modelsDirectory = modelPath;
			}
			else{
				modelsDirectory = modelPath+File.separator;
			}
		}
		
		Classifier classifier;
		try{
			File modelFile = FileFactory.generateFileInstance(modelsDirectory + language + "-" + modelName + ".EXT");
	        ObjectInputStream ois = new ObjectInputStream (new FileInputStream (modelFile));
	        classifier = (Classifier) ois.readObject();
	        ois.close();
	        
	        CsvIterator reader = new CsvIterator(new FileReader(inputDataFile),"(\\w+)\\s+(\\w+)\\s+(.*)", 3, 2, 1);  // (data, label, name) field indices               

	        // Create an iterator that will pass each instance through the same pipe that was used to create the training data for the classifier.
	        Iterator<Instance> instances = classifier.getInstancePipe().newIteratorFrom(reader);
	        // Classifier.classify() returns a Classification object  that includes the instance, the classifier, and the classification results (the labeling). Here we only care about the Labeling.                                                                       
	        String output = "";
	        while (instances.hasNext()) {
	        	Labeling labeling = classifier.classify(instances.next()).getLabeling();

	        	// print the labels with their weights in descending order (ie best first)
	        	for (int rank = 0; rank < labeling.numLocations(); rank++){
	        		System.out.print(labeling.getLabelAtRank(rank) + ":" +
	        				labeling.getValueAtRank(rank) + " ");
	        	}
	        	System.out.println();
	        	output = output + ";" + labeling.getLabelAtRank(0);
	        }
	        return output;
		}
		catch(Exception e){
			logger.error(e.getMessage());
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}
	
	/**
	 * 
	 * 
	 * @param inputTrainData Stream of training data
	 * @param modelName Name to be assigned to the model
	 * @return true if the model has been successfully trained
	 */
	public static String trainClassifier (String inputTrainFile, String modelPath, String modelName, String language) throws BadRequestException, ExternalServiceFailedException {

		if(modelPath!=null){
			if(modelPath.endsWith(File.separator)){
				modelsDirectory = modelPath;
			}
			else{
				modelsDirectory = modelPath+File.separator;
			}
		}
		try{
			
			ImportData id = new ImportData(language);
			File trainingFile = FileFactory.generateFileInstance(inputTrainFile);
			InstanceList instances = id.readFile(trainingFile);

			//Train the model with the training instances
			ClassifierTrainer trainer = new MaxEntTrainer();
			Classifier classifier;
			try{
				classifier = trainer.train(instances);
			}
			catch(Exception e){
				throw new ExternalServiceFailedException("Fail at training the model: it is possible that you need to use a bigger amount of Data.");
			}
			
			//Save the generated classifier
			String modelOutputFile = modelsDirectory + language + "-" + modelName + ".EXT";
//			File outputFile = new File(modelOutputFile);
			File outputFile = FileFactory.generateOrCreateFileInstance(modelOutputFile);
			System.out.println(outputFile.getAbsolutePath());
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream (outputFile));
			oos.writeObject (classifier);
			oos.close();
			
			return modelOutputFile;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

	public static void main(String[] args) throws Exception{
		//String result = DocumentClassification.classifyString("This is the text that I need to classify", "en-sent.bin", "en");
		//System.out.println(result);
		
//		dc.trainClassifier("/Users/jumo04/Documents/DFKI/DataCollections/DKT/3pc/3PC_CATEGORIES.txt", "recursos", "3pc", "de");
//		dc.classifyString("recursos/data.txt", "recursos","test1", "de");

		/**
		 * Generate models for 3pc data
		 */
//		DocumentClassification.trainClassifier("/Users/jumo04/Documents/DFKI/DataCollections/DKT/3pc/3PC_CATEGORIES.txt", "recursos", "3pc_stop", "de");
		/**
		 * Generate models for condat data using categories and types as labels for classification
		 */
		DocumentClassification.trainClassifier("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Condat/Condat_CATEGORIES.txt", "recursos", "condat_categories_stop", "de");
		DocumentClassification.trainClassifier("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Condat/Condat_TYPES.txt", "recursos", "condat_types_stop", "de");
//		/**
//		 * Generate models for Kreuzwereker data using topics, categories and subcategories as labels for classification
//		 */
//		DocumentClassification.trainClassifier("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Kreuzwerker/KW_TOPICS2.txt", "recursos", "kreuzwerker_topics_stop", "de");
//		DocumentClassification.trainClassifier("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Kreuzwerker/KW_CATEGORIES.txt", "recursos", "kreuzwerker_categories_stop", "de");
//		DocumentClassification.trainClassifier("/Users/jumo04/Documents/DFKI/DataCollections/DKT/Kreuzwerker/KW_SUBCATEGORIES2.txt", "recursos", "kreuzwerker_subcategories_stop", "de");

	
	}
}
