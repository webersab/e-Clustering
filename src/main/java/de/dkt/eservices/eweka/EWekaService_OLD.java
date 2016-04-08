package de.dkt.eservices.eweka;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.hp.hpl.jena.rdf.model.Model;

import de.dkt.common.niftools.DFKINIF;
import de.dkt.common.niftools.NIFReader;
import de.dkt.common.niftools.NIFWriter;
import de.dkt.eservices.eweka.modules.Classification;
import de.dkt.eservices.eweka.modules.Clustering;
import eu.freme.common.conversion.rdf.RDFConstants;
import eu.freme.common.conversion.rdf.RDFConstants.RDFSerialization;
import eu.freme.common.conversion.rdf.RDFConversionService;
import eu.freme.common.conversion.rdf.RDFSerializationFormats;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.ExternalServiceFailedException;

/**
 * @author Julian Moreno Schneider julian.moreno_schneider@dfki.de
 *
 * The whole documentation about WEKA examples can be found in ...
 *
 */
@Component
public class EWekaService_OLD {
//    
//	Logger logger = Logger.getLogger(EWekaService_OLD.class);
//
//	@Autowired
//	RDFConversionService rdfConversionService;
//
//    public Model analyzeText(String inputText, String analysis, String modelPath, String modelName, String language, String informat, String outformat) 
//    		throws ExternalServiceFailedException, BadRequestException {
//        try {
//        	Model inModel = null;
//        	RDFSerialization format = (new RDFSerializationFormats()).get(informat);
//        	if(format.equals(RDFConstants.RDFSerialization.PLAINTEXT)){
//        		inModel = NIFWriter.initializeOutputModel();
//        		NIFWriter.addInitialString(inModel, inputText, DFKINIF.getDefaultPrefix());
//        	}
//        	else{
//            	inModel = rdfConversionService.unserializeRDF(inputText, format);
//        	}
//        	String textForProcessing = NIFReader.extractIsString(inModel);
//        	
//        	if(analysis.equalsIgnoreCase("topicmodelling")){
//            	String label = Clustering.clusterInstance(textForProcessing, modelPath, modelName, language);
//            	NIFWriter.addTopicModelling(inModel, textForProcessing, NIFReader.extractDocumentURI(inModel), label);
//        	}
//        	else{
//            	String label = Classification.classifyString(textForProcessing, modelPath, modelName, language);
//            	NIFWriter.addDocumentClassification(inModel, textForProcessing, NIFReader.extractDocumentURI(inModel), label);
//        	}
//        	return inModel;
//        } catch (BadRequestException e) {
//			logger.error("EXCEPTION: "+e.getMessage());
//            throw e;
//    	} catch (ExternalServiceFailedException e2) {
//			logger.error("EXCEPTION: "+e2.getMessage());
//    		throw e2;
//    	} catch (Exception e) {
//			logger.error("EXCEPTION: "+e.getMessage());
//    		throw new ExternalServiceFailedException(e.getMessage());
//    	}
//    }
//
//    public String generateClusters(String trainDataFile, int classIndex, String clustererType, String modelPath, String modelName, String language)
//            throws ExternalServiceFailedException, BadRequestException {
//        try {
//        	Clustering.trainModel(trainDataFile, classIndex, clustererType, modelPath, modelName, language);
//        	HttpHeaders responseHeaders = new HttpHeaders();
//        	responseHeaders.add("Content-Type", "text/plain");
//        	String body = "Topic modelling Model ["+language+"-"+modelName+".EXT"+"] successfully trained!!!";
//        	return body;
//        } catch (BadRequestException e) {
//			logger.error("EXCEPTION: "+e.getMessage());
//            throw e;
//    	} catch (ExternalServiceFailedException e2) {
//			logger.error("EXCEPTION: "+e2.getMessage());
//    		throw e2;
//    	}
//    }
//
//    public String trainClassification(String trainDataFile, int classIndex, String classifierType, String modelPath, String modelName, String language)
//            throws ExternalServiceFailedException, BadRequestException {
//        try {
//        	Classification.trainClassifier(trainDataFile, classIndex, classifierType, modelPath, modelName, language);
//        	HttpHeaders responseHeaders = new HttpHeaders();
//        	responseHeaders.add("Content-Type", "text/plain");
//        	String body = "Text Classification Model ["+language+"-"+modelName+".EXT"+"] successfully trained!!!";
//        	return body;
//        } catch (BadRequestException e) {
//			logger.error("EXCEPTION: "+e.getMessage());
//            throw e;
//    	} catch (ExternalServiceFailedException e2) {
//			logger.error("EXCEPTION: "+e2.getMessage());
//    		throw e2;
//    	}
//    }

}
