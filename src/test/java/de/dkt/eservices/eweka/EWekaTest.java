package de.dkt.eservices.eweka;


import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;

import eu.freme.bservices.testhelper.TestHelper;
import eu.freme.bservices.testhelper.ValidationHelper;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;

/**
 * @author 
 */

public class EWekaTest {

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
		String url = testHelper.getAPIBaseUrl() + "/e-weka/testURL";
		return Unirest.post(url);
	}
	
	private HttpRequestWithBody clusteringRequest() {
		String url = testHelper.getAPIBaseUrl() + "/e-clustering/generateClusters";
		return Unirest.post(url);
	}
	
	
	@Test
	public void testEWekaBasic() throws UnirestException, IOException,
			Exception {

		HttpResponse<String> response = baseRequest()
				.queryString("informat", "text")
				.queryString("input", "hello world")
				.queryString("outformat", "turtle").asString();

		System.out.println("BODY: "+response.getBody());
		System.out.println("STATUS:" + response.getStatus());

		assertTrue(response.getStatus() == 200);
		assertTrue(response.getBody().length() > 0);
		
	}
	
	@Test
	public void simpleEMTest() throws UnirestException, IOException,
			Exception {

		HttpResponse<String> response2 = clusteringRequest()
				.queryString("language", "en")
				.queryString("algorithm", "EM")
				.body(TestConstants.sampleARFF)
				.asString();
				
		System.out.println("DEBUGGING output here:" + response2.getBody());
		//System.out.println("BODY: "+response.getBody());
		//System.out.println("STATUS:" + response.getStatus());

		//assertTrue(response.getStatus() == 200);
		//assertTrue(response.getBody().length() > 0);
		
	}
	
}
