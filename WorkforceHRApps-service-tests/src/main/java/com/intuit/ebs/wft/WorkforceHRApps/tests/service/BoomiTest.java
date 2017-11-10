package com.intuit.ebs.wft.WorkforceHRApps.tests.service;
/**
 * @author spoddar
 */
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.BoomiTestBase;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

//import org.apache.log4j.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;

//import org.hamcrest.Matchers.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class BoomiTest extends BoomiTestBase {
	
	String payloadSchemaPath;
	String testFilePath;
	RequestSpecification reqSpec;
	
    BoomiTest(){
    }
    
    @BeforeSuite(alwaysRun = true)
	  public void setupTestData() throws FileNotFoundException, IOException {
	    //This is to get the Absolute Path for Payload Schema and Test Files
		payloadSchemaPath = Constant.PAYLOAD_SCHEMA_LOCATION;
		payloadSchemaPath = new File(payloadSchemaPath).getAbsolutePath();
	    logger.info("Payload Schema Absolute Path : -----"+payloadSchemaPath+"-----");
	    testFilePath = Constant.TEST_FILE_LOCATION;
	    testFilePath = new File(testFilePath).getAbsolutePath();
	    logger.info("Test File Absolute Path : -----"+testFilePath+"-----");
		
    }
    
    @BeforeClass(groups = { "Smoke"})
	  public void setRestAssuredConfig() {
	    logger.info("------- setRestAssuredConfig ---------");
	    logger.info("----Building Request Specification for Boomi-----");
	    Map<String, String> requestHeaders = Utility.getHeaders("contentType:application/xml,Authorization:"+getConfigManager().getConfig(Constant.AUTH_TOKEN_VPCE2E));
	    requestSpecBuilder = requestSpecBuilder.addHeaders(requestHeaders);
	    requestSpec = requestSpecBuilder.build();
	    
	    logger.info("----Building Request Specification for Workday-----");
	    Map<String, String> reqHeaders = Utility.getHeaders("contentType:application/xml,Authorization:"+getConfigManager().getConfig(Constant.AUTH_TOKEN_INTUIT2));
	    reqSpecBuilder = reqSpecBuilder.addHeaders(reqHeaders);
	    reqSpec = reqSpecBuilder.build();
    }


    @Test(groups = { "Smoke" })
	public void testUpdateCorpInfoE2E() throws IOException {
		
		logger.info("=========== Starting updateCorpInfo API call ======================");
		logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.VPCE2E_URL));
		logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.AUTH_TOKEN_VPCE2E));
		
		Response postResponse = Utility.doPostCall(requestSpec, payloadSchemaPath, "updateCorpInfo.xml", testFilePath, 
				"UpdateCorpInfo.csv", "updateCorpInfo_v1.0");
		
		//postResponse.body().toString();
		postResponse.then().assertThat().statusCode(200);

		logger.info("=========== updateCorpInfo API call completed ======================");
		logger.info("=============== Staring getEmpInfoFromWD using CorpId ===================");
		
		Response getResp = Utility.doGetCall(reqSpec, getConfigManager().getConfig(Constant.GET_WORKER_SENSITIVE_ENDPOINT));
		
		logger.info("Response from getCall: " + getResp.asString());
		XmlPath xml1 = new XmlPath(getResp.asString()).setRoot("Report_Data.Report_Entry");
        String actualCorpId = xml1.getString("CORP_ID");
        assertEquals(actualCorpId, "700033119");
		
		logger.info("===========getEmpInfoFromWD using CorpId completed ======================");
		
	}
}
