package de.dkt.eservices.eclustering;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.context.ApplicationContext;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.types.Labeling;
import de.dkt.common.filemanagement.FileFactory;
import de.dkt.eservices.emallet.modules.DocumentClassification;
import de.dkt.eservices.emallet.modules.ImportData;
import eu.freme.bservices.testhelper.TestHelper;
import eu.freme.bservices.testhelper.ValidationHelper;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;
import eu.freme.common.exception.ExternalServiceFailedException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EClusteringTest {

	TestHelper testHelper;
	ValidationHelper validationHelper;

	@Before
	public void setup() {
		ApplicationContext context = IntegrationTestSetup
				.getContext(TestConstants.pathToPackage);
		testHelper = context.getBean(TestHelper.class);
		validationHelper = context.getBean(ValidationHelper.class);
	}

	private HttpRequestWithBody baseMALLETRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-mallet/testURL";
		return Unirest.post(url);
	}

	private HttpRequestWithBody documentClassificationRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-documentclassification";
		return Unirest.post(url);
	}
	
//	private HttpRequestWithBody topicModellingRequest() {
//		String url = testHelper.getAPIBaseUrl() + "/e-topicmodelling";
//		return Unirest.post(url);
//	}
	
	private HttpRequestWithBody baseWEKARequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-weka/testURL";
		return Unirest.post(url);
	}
	
	private HttpRequestWithBody clusteringRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-clustering/generateClusters";
		return Unirest.post(url);
	}
	
	private HttpRequestWithBody trainModelRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-mallet/trainModel";
		return Unirest.post(url);
	}

	@Test
	public void test0_EMalletBasic() throws UnirestException, IOException,
	Exception {
		HttpResponse<String> response = baseMALLETRequest()
				.queryString("informat", "text")
				.queryString("input", "hello world")
				.queryString("outformat", "turtle").asString();
		assertTrue(response.getStatus() == 200);
		assertTrue(response.getBody().length() > 0);
	}

	@Test
	public void test6_EMalletAnalyzeTextDocumentClassification() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = documentClassificationRequest()
				.queryString("informat", "text")
				.queryString("input", "Einige interessanten Texte die etwas witchtiges drinnen haben über Medizin")
				.queryString("outformat", "turtle")
				.queryString("modelName", "testModel")
				.queryString("modelPath", "/Users/jumo04/Documents/DFKI/DKT/dkt-test/malletTest/trainedModels/documentClassification/")
				.queryString("language", "de")
				.asString();
		Assert.assertEquals(response.getStatus(), 200);
		assertTrue(response.getBody().length() > 0);
		Assert.assertEquals(TestConstants.expectedResponseClassification, response.getBody());
	}

	@Test
	public void test5_EMalletAnalyzeNIFTextDocumentClassification() throws UnirestException, IOException,Exception {
		HttpResponse<String> response = documentClassificationRequest()
				.queryString("informat", "turtle")
				.queryString("input", TestConstants.inputText)
				.queryString("outformat", "turtle")
				.queryString("modelName", "testModelCLASSIFICATION")
//				.queryString("modelPath", "recursos/")
				.queryString("language", "de")
				.asString();
		Assert.assertEquals(200,response.getStatus());
		assertTrue(response.getBody().length() > 0);
		Assert.assertEquals(TestConstants.expectedResponseClassification2, response.getBody());
	}
//	
//	@Test
//	public void test7() throws UnirestException, IOException,Exception {
//		String inputText = 
//			"@prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n" +
//			"@prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .\n" +
//			"@prefix itsrdf: <http://www.w3.org/2005/11/its/rdf#> .\n" +
//			"@prefix nif:   <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .\n" +
//			"@prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .\n" +
//			"\n" +
//			"<http://dkt.dfki.de/examples/#char=0,806>\n" +
//			"        a                    nif:RFC5147String , nif:String , nif:Context ;\n" +
//			"        nif:beginIndex       \"0\"^^xsd:nonNegativeInteger ;\n" +
//			"        nif:centralGeoPoint  \"42.17561833333333_-4.075817777777777\"^^xsd:string ;\n" +
//			"        nif:endIndex         \"806\"^^xsd:nonNegativeInteger ;\n" +
//			"        nif:geoStandardDevs  \"1.4446184970002673_2.202362615152696\"^^xsd:string ;\n" +
//			"        nif:isString         \"1936\\n\\nCoup leader Sanjurjo was killed in a plane crash on 20 July, leaving an effective command split between Mola in the North and Franco in the South. On 21 July, the fifth day of the rebellion, the Nationalists captured the main Spanish naval base at Ferrol in northwestern Spain. A rebel force under Colonel Beorlegui Canet, sent by General Emilio Mola, undertook the Campaign of Guipuzcoa from July to September. The capture of Guipuzcoa isolated the Republican provinces in the north. On 5 September, after heavy fighting the force took Irun, closing the French border to the Republicans. On 13 September, the Basques surrendered Madrid to the Nationalists, who then advanced toward their capital, Bilbao. The Republican militias on the border of Viscaya halted these forces at the end of September.\\n\"^^xsd:string ;\n" +
//			"        nif:meanDateRange    \"19360531090000_19361022090000\"^^xsd:string .\n" +
//			"\n" +
//			"";
//		DocumentClassification.classifyString(inputText, null, "testModel", "de");
//	}
//
////	@Test
////	public void test4_EMalletAnalyzeTextTopicModelling() throws UnirestException, IOException,Exception {
////		HttpResponse<String> response = topicModellingRequest()
////				.queryString("informat", "text")
////				.queryString("input", "Einige interessanten Texte die etwas witchtiges drinnen haben über Medizin")
////				.queryString("outformat", "turtle")
////				.queryString("modelName", "testModel")
//////				.queryString("modelPath", "recursos/")
////				.queryString("language", "de")
////				.asString();
////		Assert.assertEquals(response.getStatus(), 200);
////		assertTrue(response.getBody().length() > 0);
////		Assert.assertEquals(TestConstants.expectedResponseTopic, response.getBody());
////	}
////
////	@Test
////	public void test3_EMalletAnalyzeNIFTextTopicModelling() throws UnirestException, IOException,Exception {
////		HttpResponse<String> response = topicModellingRequest()
////				.queryString("informat", "turtle")
////				.queryString("input", TestConstants.inputText)
////				.queryString("outformat", "turtle")
////				.queryString("modelName", "testModel")
//////				.queryString("modelPath", "recursos/")
////				.queryString("language", "de")
////				.asString();
////		Assert.assertEquals(response.getStatus(), 200);
////		assertTrue(response.getBody().length() > 0);
////		Assert.assertEquals(TestConstants.expectedResponseTopic2, response.getBody());
////	}
////
//	@Test
//	public void test2_EMalletTrainClassificationModel() throws UnirestException, IOException,Exception {
//		File f = FileFactory.generateFileInstance("rdftest" + File.separator + "temptrainingdata.txt");
//		HttpResponse<String> response = trainModelRequest()
//				.queryString("modelName", "testModelCLASSIFICATION")
//				.queryString("analysis", "classification")
//				.queryString("language", "de")
//				.field("trainingFile", f)
//				.asString();
//		//			System.out.println(response.getStatus());
//		//			System.out.println(response.getStatusText());
//		//			System.out.println(response.getBody());
//		Assert.assertEquals(response.getStatus(), 200);
//		assertTrue(response.getBody().length() > 0);
//		Assert.assertEquals("Text Classification Model [de-testModelCLASSIFICATION.EXT] successfully trained!!!", response.getBody());
//	}
//
////	@Test
////	public void test1_EMalletTrainTopicModellingModel() throws UnirestException, IOException,Exception {
////		File f = FileFactory.generateFileInstance("rdftest" + File.separator + "temptrainingdata.txt");
////		HttpResponse<String> response = trainModelRequest()
////				.queryString("modelName", "testModelTOPIC")
//////				.queryString("modelPath", "recursos/")
////				.queryString("analysis", "topicmodelling")
////				.queryString("language", "de")
////				//					.field("file", f)
////				.field("trainingFile", f)
////				.asString();
////		//			System.out.println(response.getStatus());
////		//			System.out.println(response.getStatusText());
////		//			System.out.println(response.getBody());
////		Assert.assertEquals(response.getStatus(), 200);
////		assertTrue(response.getBody().length() > 0);
////		Assert.assertEquals("Topic modelling Model [de-testModelTOPIC.EXT] successfully trained!!!", response.getBody());
////	}
	
	@Test
	public void testEWekaBasic() throws UnirestException, IOException,
			Exception {

		HttpResponse<String> response = baseWEKARequest()
				.queryString("informat", "text")
				.queryString("input", "hello world")
				.queryString("outformat", "turtle").asString();

		System.out.println("BODY: "+response.getBody());
		System.out.println("STATUS:" + response.getStatus());

		assertTrue(response.getStatus() == 200);
		assertTrue(response.getBody().length() > 0);
		
	}
//	
//	@Test
//	public void simpleEMTest() throws UnirestException, IOException,
//			Exception {
//
//		HttpResponse<String> response2 = clusteringRequest()
//				.queryString("language", "en")
//				.queryString("algorithm", "EM")
//				.body(TestConstants.sampleARFF)
//				.asString();
//				
//		System.out.println("DEBUGGING output here:" + response2.getBody());
//		//System.out.println("BODY: "+response.getBody());
//		//System.out.println("STATUS:" + response.getStatus());
//
//		//assertTrue(response.getStatus() == 200);
//		//assertTrue(response.getBody().length() > 0);
//		
//	}	

//	@Test
//	public void simpleEMTest() throws UnirestException, IOException,
//			Exception {
//
//		ImportData id = new ImportData("de");
//		File trainingFile = FileFactory.generateFileInstance("rdftest" + File.separator + "temptrainingdata.txt");
//		InstanceList instances = id.readFile(trainingFile);
//
//		//Train the model with the training instances
//		ClassifierTrainer trainer = new MaxEntTrainer();
//		Classifier classifier;
//		try{
//			classifier = trainer.train(instances);
//		}
//		catch(Exception e){
//			throw new ExternalServiceFailedException("Fail at training the model: it is possible that you need to use a bigger amount of Data.");
//		}
//		
//        ImportData importData = new ImportData("vector","de");
//        InstanceList testing = new InstanceList(importData.getPipe());
////        InstanceList testing = new InstanceList(classifier.getInstancePipe());
//        testing.addThruPipe(new Instance("Einige interessanten Texte die etwas witchtiges drinnen haben über Medizin", "", "test instance", ""));
//        
////        Classification classification = classifier.classify(testing.get(0));
//        Classification classification = classifier.classify(instances.get(0));
//		Labeling labeling = classification.getLabeling();
//		String output = labeling.getLabelAtRank(0).toString();
//		System.out.println(output);
//	}	
}
