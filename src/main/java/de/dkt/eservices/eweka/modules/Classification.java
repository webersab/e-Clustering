package de.dkt.eservices.eweka.modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import de.dkt.common.filemanagement.FileFactory;
import de.dkt.eservices.eweka.EWekaService;
import eu.freme.common.exception.ExternalServiceFailedException;
import weka.classifiers.Classifier;
import weka.clusterers.Clusterer;
import weka.core.Instances;

/**
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 */
public class Classification {

	static Logger logger = Logger.getLogger(EWekaService.class);

	private static String modelsDirectory = "trainedModels" + File.separator + "classification" + File.separator;

	private Classification() {
	}

	public static boolean trainClassifier(String trainDataFile, int classIndex, String classifierType, String modelPath, String modelName, String language) throws ExternalServiceFailedException {
		if(modelPath!=null){
			if(modelPath.endsWith(File.separator)){
				modelsDirectory = modelPath;
			}
			else{
				modelsDirectory = modelPath+File.separator;
			}
		}
		try{
			File trainingData = FileFactory.generateFileInstance(trainDataFile);
			Instances isTrainingSet = DataLoader.loadDataFromFile(trainingData.getAbsolutePath());
			// Set class index
			if (isTrainingSet.classIndex() == -1){
				isTrainingSet.setClassIndex(classIndex);
			}
	
			// Create a naïve bayes classifier
			Classifier cModel = null; 
			
			cModel = ClassifierFactory.getClassifier(classifierType); 
			cModel.buildClassifier(isTrainingSet);

			//Save the generated classifier
			String modelOutputFile = modelsDirectory + language + "-" + modelName + ".EXT";
			File outputFile = FileFactory.generateOrCreateFileInstance(modelOutputFile);
			System.out.println(outputFile.getAbsolutePath());
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream (outputFile));
			oos.writeObject (cModel);
			oos.close();			
			return true;
		}
		catch(Exception e){
			logger.error("Error training classification module in WEKA service.", e);
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

	public static String classifyString(String textForProcessing, String modelPath, String modelName, String language) {
		if(modelPath!=null){
			if(modelPath.endsWith(File.separator)){
				modelsDirectory = modelPath;
			}
			else{
				modelsDirectory = modelPath+File.separator;
			}
		}
		Classifier classifier = null;
		try{
			File modelFile = FileFactory.generateFileInstance(modelsDirectory + language + "-" + modelName + "_weka_clustering.model");
	        ObjectInputStream ois = new ObjectInputStream (new FileInputStream (modelFile));
	        classifier = (Classifier) ois.readObject();
	        ois.close();
//	        
//			// TODO convert input text into suitable data for the model.
//			Instances unlabeled = new Instances(new BufferedReader(new FileReader(file)));
//			// set class attribute
//			// ???? unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
//
//			// create copy
//			Instances labeled = new Instances(unlabeled);
//
//			// label instances
//			for (int i = 0; i < unlabeled.numInstances(); i++) {
//				double clsLabel = classifier.classifyInstance(unlabeled.instance(i));
//				labeled.instance(i).setClassValue(clsLabel);
//			}
//			
//			// TODO Generate output of labeled data.
//			for (int i = 0; i < labeled.numInstances(); i++) {
//				labeled.instance(i);
//			}
			String output = "";
			return output;
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

}
