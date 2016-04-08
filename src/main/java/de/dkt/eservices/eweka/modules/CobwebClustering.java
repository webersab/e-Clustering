package de.dkt.eservices.eweka.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

import de.dkt.common.filemanagement.FileFactory;
import de.dkt.eservices.eweka.EWekaService;
import eu.freme.common.exception.ExternalServiceFailedException;
import weka.classifiers.Classifier;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.clusterers.Cobweb;
import weka.clusterers.DensityBasedClusterer;
import weka.clusterers.EM;
import weka.clusterers.OPTICS;
import weka.clusterers.RandomizableClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToNominal;

/**
 * @author Julian Moreno Schneider - julian.moreno_schneider@dfki.de
 *
 */
public class CobwebClustering {

	static Logger logger = Logger.getLogger(EWekaService.class);

	private static String modelsDirectory = "trainedModels" + File.separator + "clustering" + File.separator;
	
	private static Clusterer clusterer;
	
	private CobwebClustering() {
	}

	public static void main(String[] args) {
		try{
			String file = "/Users/jumo04/Downloads/flatVector-3.arff";
//			String file = "/Users/jumo04/Downloads/frequencyVector.arff";
			
//			Clustering.trainModel(file, 0, "cobweb", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "em", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "simplekmeans", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "clope", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "dbscan", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "farthestfirst", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "filteredclusterer", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "hierarchicalclusterer", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "makedensitybasedclusterer", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "optics", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "sIB", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
//			Clustering.trainModel(file, 0, "xmeans", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
			CobwebClustering.trainModelAndClusterInstances(file, 0, "em", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/weka/ACLRuns/", "test1", "de");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public static boolean trainModelAndClusterInstances(String trainDataFile, int classIndex, String clustererType, String modelPath, String modelName, String language) throws ExternalServiceFailedException {
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

			Instances instancesForId = new Instances(isTrainingSet);

			String[] options = new String[2];
			options[0] = "-R";                                    // "range"
			options[1] = "1";                                     // first attribute
			Remove remove = new Remove();                         // new instance of filter
			remove.setOptions(options); 
			 
//			remove.setAttributeIndices("0-0");
			remove.setInputFormat(isTrainingSet);
			isTrainingSet = Filter.useFilter(isTrainingSet, remove);

			Instances labeled = new Instances(isTrainingSet);

			Cobweb cw = new Cobweb();
			String[] options2 = new String[2];
			options2[0] = "-I";                 // max. iterations
			options2[1] = "100";
//			options2[2] = "-N";                 // max. iterations
//			options2[3] = "5";
//			cw.setOptions(options2);     // set the options

			double upperthreshold = 0.99;
			double threshold = 0.6;
			
			cw.buildClusterer(isTrainingSet);

			System.out.println(cw.getAcuity());

			System.out.println(cw.graph());

//			Instances centroids = cw.getClusterCentroids();
//			
//			for (int i = 0; i < centroids.numInstances(); i++) {
//				System.out.println("_---------------------------------");
//				Instance inst = centroids.instance(i);
//				for (int j = 0; j < inst.numAttributes(); j++) {
////					if(inst.value(j)<upperthreshold && inst.value(j)>threshold){
//					if(inst.value(j)>threshold){
//						Attribute at = inst.attribute(j);
//						System.out.print(at.name() + "(" + inst.value(j) + ")" + "\t");
////						System.out.println(inst.value(j));
////						System.out.println(" \t\t"+inst.toString());
//					}
//				}
//			}
//			
//			clusterer = cw;
//			System.out.println(clusterer.numberOfClusters());
//			
//			// label instances
//			for (int i = 0; i < isTrainingSet.numInstances(); i++) {
//				double clsLabel = clusterer.clusterInstance(isTrainingSet.instance(i));
//				System.out.print("INSTANCE: " + instancesForId.instance(i).stringValue(0));
//				System.out.println(" " + clsLabel);
////				double clsLabel2 [] = clusterer.distributionForInstance(isTrainingSet.instance(i));
////				System.out.print("\t");
////				for (double d : clsLabel2) {
////					System.out.print(d + " ");
////				}
////				System.out.println();
//			}
			System.out.println(cw.toString());

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.error("Error training classification module in WEKA service.", e);
			throw new ExternalServiceFailedException(e.getMessage());
		}
	}

}
