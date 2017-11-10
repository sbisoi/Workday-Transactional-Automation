package com.intuit.ebs.wft.WorkforceHRApps.tests.service;
/**
 *@author spoddar
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.WorkdayTestBase;

public class WDLaunchIntegrationTest extends WorkdayTestBase{
	
	private String payloadSchemaPath;
	private String workdayEnv1URI;
	private String workdayEnv2URI;
	private String myRequest;
	private String environment1;
	private String environment2;
	private String integration;
	
	final static Logger logger = Logger.getLogger(WDLaunchIntegrationTest.class);
	
	WDLaunchIntegrationTest(){
		logger.info("===WDLaunchIntegrationTest===");
	}
	
	@BeforeClass(alwaysRun = true)
	  public void setUp() throws FileNotFoundException, IOException, URISyntaxException {
				
		//This is to get the Absolute Path for Payload Schema
		payloadSchemaPath = Constant.PAYLOAD_SCHEMA_LOCATION;
		payloadSchemaPath = new File(payloadSchemaPath).getAbsolutePath();
		
		/** @author asiji
		 * adding environment specific path
		 */
		
		environment1 = getConfigManager().getConfig(Constant.ENVIRONMENT_1);
		environment2 = getConfigManager().getConfig(Constant.ENVIRONMENT_2);
		
	    logger.info("Payload Schema Absolute Path : -----"+payloadSchemaPath+"-----");
	    
	    //This is to set the Workday URI for Intuit2 and Preview
	    String baseURL = getConfigManager().getConfig(Constant.HTTP_PROPERTY_BASEURL);
	    logger.info("Base URL : " + baseURL);
        String env1BasePath = getConfigManager().getConfig(Constant.WORKDAY_PROPERTY_ENV_1_BASEPATH);	    
        logger.info("Base Path for Env1 : " + env1BasePath);
	    workdayEnv1URI = baseURL + env1BasePath;
	    logger.info("Workday Env1 URI : " + workdayEnv1URI);
	    
        String env2BasePath = getConfigManager().getConfig(Constant.WORKDAY_PROPERTY_ENV_2_BASEPATH);	    
        logger.info("Base Path for Env2 : " + env2BasePath);
	    workdayEnv2URI = baseURL + env2BasePath;
	    logger.info("Workday Env2 URI : " + workdayEnv2URI);

	  }
	
	/**
	 * @author spoddar
	 * @throws IOException 
	 * This is to test the Workday Globoforce_Outbound Launch Integration for 2 environments (which is a Soap request)
	 */
	  @Test(groups = { "Smoke" }, priority = 0)
	  public void testGloboforce_OutboundLaunch() throws IOException {
		integration=getConfigManager().getConfig(Constant.GLOBOFORCE);
		logger.debug("-----testGloboforceOutbound on Intuit2----");
	    logger.debug(integration);
	    logger.debug(environment1);
		myRequest = Utility.GenerateStringFromResource(payloadSchemaPath+environment1+integration);		
        logger.info("**********************");
		logger.info(myRequest);
		logger.info("**********************");
		
		response = Utility.getResponseForSoapRequestWithXMLPayload(workdayEnv1URI, myRequest);
		
		logger.info("Status Code for 1st env. : " + new Integer(response.getStatusCode()).toString());
		logger.info(response.body().asString());
		
		logger.debug(response.prettyPrint());
	    response.then().statusCode(200);
	    
	    logger.debug("-----testGloboforceOutbound on Preview----");
	    logger.debug(integration);
	    logger.debug(environment2);
        myRequest = Utility.GenerateStringFromResource(payloadSchemaPath+environment2+integration);		
        logger.info("**********************");
		logger.info(myRequest);
		logger.info("**********************");
			
		response = Utility.getResponseForSoapRequestWithXMLPayload(workdayEnv2URI, myRequest);
			
		logger.info("Status Code for 2nd env. : " + new Integer(response.getStatusCode()).toString());
		logger.info(response.body().asString());
			
		logger.debug(response.prettyPrint());
		response.then().statusCode(200);
	  }
	  
	  /**
		 * @author asiji
		 * @throws IOException 
		 * This is to test the Workday IntuitCCMSIOutbound EIB- Launch EIB for Intuit2 and Preview (which is a Soap request)
		 */
		  @Test(groups = { "Smoke" }, priority = 0)
		  public void testIntuitCCMSIOutboundLaunch() throws IOException {
			integration=getConfigManager().getConfig(Constant.CCMSI);
			logger.debug("-----testIntuitCCMSIOutboundLaunch on Intuit2----");
		    logger.debug(integration);
		    logger.debug(environment1);
			myRequest = Utility.GenerateStringFromResource(payloadSchemaPath+environment1+integration);		
	        logger.info("**********************");
			logger.info(myRequest);
			logger.info("**********************");
			
			response = Utility.getResponseForSoapRequestWithXMLPayload(workdayEnv1URI, myRequest);
			
			logger.info(new Integer(response.getStatusCode()).toString());
			logger.info(response.body().asString());
			
			logger.debug(response.prettyPrint());
		    response.then().statusCode(200);
		    
		    logger.debug("-----testIntuitCCMSIOutboundLaunch on Preview----");
		    logger.debug(integration);
		    logger.debug(environment1);
	        myRequest = Utility.GenerateStringFromResource(payloadSchemaPath+environment2+integration);		
	        logger.info("**********************");
			logger.info(myRequest);
			logger.info("**********************");
				
			response = Utility.getResponseForSoapRequestWithXMLPayload(workdayEnv2URI, myRequest);
				
			logger.info(new Integer(response.getStatusCode()).toString());
			logger.info(response.body().asString());
				
			logger.debug(response.prettyPrint());
			response.then().statusCode(200);
		  }
	
	

}
