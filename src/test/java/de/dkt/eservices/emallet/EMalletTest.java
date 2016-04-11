package de.dkt.eservices.emallet;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

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

import de.dkt.common.filemanagement.FileFactory;
import eu.freme.bservices.testhelper.TestHelper;
import eu.freme.bservices.testhelper.ValidationHelper;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class EMalletTest {

	TestHelper testHelper;
	ValidationHelper validationHelper;

	@Before
	public void setup() {
		ApplicationContext context = IntegrationTestSetup
				.getContext(TestConstants.pathToPackage);
		testHelper = context.getBean(TestHelper.class);
		validationHelper = context.getBean(ValidationHelper.class);
	}

	private HttpRequestWithBody baseRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-mallet/testURL";
		return Unirest.post(url);
	}

	private HttpRequestWithBody documentClassificationRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-documentclassification";
		return Unirest.post(url);
	}
	
	private HttpRequestWithBody topicModellingRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-topicmodelling";
		return Unirest.post(url);
	}

	private HttpRequestWithBody trainModelRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-mallet/trainModel";
		return Unirest.post(url);
	}

	@Test
	public void test0_EMalletBasic() throws UnirestException, IOException,
	Exception {
		HttpResponse<String> response = baseRequest()
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
//				.queryString("modelPath", "recursos/")
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
				.queryString("modelName", "testModel")
//				.queryString("modelPath", "recursos/")
				.queryString("language", "de")
				.asString();
		Assert.assertEquals(200,response.getStatus());
		assertTrue(response.getBody().length() > 0);
		Assert.assertEquals(TestConstants.expectedResponseClassification2, response.getBody());
	}

//	@Test
//	public void test4_EMalletAnalyzeTextTopicModelling() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = topicModellingRequest()
//				.queryString("informat", "text")
//				.queryString("input", "Einige interessanten Texte die etwas witchtiges drinnen haben über Medizin")
//				.queryString("outformat", "turtle")
//				.queryString("modelName", "testModel")
////				.queryString("modelPath", "recursos/")
//				.queryString("language", "de")
//				.asString();
//		Assert.assertEquals(response.getStatus(), 200);
//		assertTrue(response.getBody().length() > 0);
//		Assert.assertEquals(TestConstants.expectedResponseTopic, response.getBody());
//	}
//
//	@Test
//	public void test3_EMalletAnalyzeNIFTextTopicModelling() throws UnirestException, IOException,Exception {
//		HttpResponse<String> response = topicModellingRequest()
//				.queryString("informat", "turtle")
//				.queryString("input", TestConstants.inputText)
//				.queryString("outformat", "turtle")
//				.queryString("modelName", "testModel")
////				.queryString("modelPath", "recursos/")
//				.queryString("language", "de")
//				.asString();
//		Assert.assertEquals(response.getStatus(), 200);
//		assertTrue(response.getBody().length() > 0);
//		Assert.assertEquals(TestConstants.expectedResponseTopic2, response.getBody());
//	}
//
//	@Test
//	public void test2_EMalletTrainClassificationModel() throws UnirestException, IOException,Exception {
//		File f = FileFactory.generateFileInstance("rdftest" + File.separator + "temptrainingdata.txt");
//		HttpResponse<String> response = trainModelRequest()
//				.queryString("modelName", "testModelCLASSIFICATION")
////				.queryString("modelPath", "recursos/")
//				.queryString("analysis", "classification")
//				.queryString("language", "de")
//				//					.field("file", f)
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
//	@Test
//	public void test1_EMalletTrainTopicModellingModel() throws UnirestException, IOException,Exception {
//		File f = FileFactory.generateFileInstance("rdftest" + File.separator + "temptrainingdata.txt");
//		HttpResponse<String> response = trainModelRequest()
//				.queryString("modelName", "testModelTOPIC")
////				.queryString("modelPath", "recursos/")
//				.queryString("analysis", "topicmodelling")
//				.queryString("language", "de")
//				//					.field("file", f)
//				.field("trainingFile", f)
//				.asString();
//		//			System.out.println(response.getStatus());
//		//			System.out.println(response.getStatusText());
//		//			System.out.println(response.getBody());
//		Assert.assertEquals(response.getStatus(), 200);
//		assertTrue(response.getBody().length() > 0);
//		Assert.assertEquals("Topic modelling Model [de-testModelTOPIC.EXT] successfully trained!!!", response.getBody());
//	}
}
