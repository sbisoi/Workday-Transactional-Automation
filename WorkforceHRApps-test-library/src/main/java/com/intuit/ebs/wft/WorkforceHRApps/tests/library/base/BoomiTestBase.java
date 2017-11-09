package com.intuit.ebs.wft.WorkforceHRApps.tests.library.base;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.RequestBuilder;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.Request;
/**
 * @author spoddar
 */
import org.testng.annotations.BeforeClass;

import com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility;
import com.intuit.tools.commontestbase.service.BaseConstant;
import com.intuit.tools.commontestbase.service.ServiceTestBase;
import com.intuit.tools.configmanagement.ConfigManager;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public class BoomiTestBase extends ServiceTestBase{
	
	public RequestSpecBuilder reqSpecBuilder;
	public RequestSpecBuilder builder;
	public RequestSpecification reqSpec;

	
	public BoomiTestBase() {
        super(Constant.PROPERTIES_FILE);
    }
	
	@BeforeClass ()
    public void setUpTestConfig() {
    	
    	//BaseConstant.{PropertyName} gets the data from /default/workday.properties
        logger.info("targetenv = " + getConfigManager().getConfig(BaseConstant.ENVIRONMENT_PROPERTY));
        logger.info("VPCE2E.URL = " + getConfigManager().getConfig(Constant.VPCE2E_URL));
        logger.info("INTUIT2.URL = " + getConfigManager().getConfig(Constant.INTUIT2_URL));
        logger.info("configDir = " + configDir);
        logger.info("Local.perf.URL = " + getConfigManager().getConfig(Constant.LOCALPERF_URL));
        logger.info("VPCQA.URL = " + getConfigManager().getConfig(Constant.QA_URL));
        // Tests setup
        // requestSpecBuilder can be configured more by each test class, this is the more generic
        // setup required by all testsuites and can be overwritten as well.
       // requestSpecBuilder = requestSpecBuilder.setBaseUri(getConfigManager().getConfig(URL));
        
        reqSpecBuilder = new RequestSpecBuilder();
        reqSpecBuilder = reqSpecBuilder.setBaseUri(getConfigManager().getConfig(Constant.INTUIT2_URL));
        
    }

    @BeforeClass(groups = { "Smoke"},enabled=false)
 	  public  RequestSpecification PostSetRestAssuredConfig(String Postauth,String Posturl) {
 	    logger.info("------- setRestAssuredConfig---------");
 	    logger.info("----Building Request Specification for Post Call -----");
 	    Map<String, String> PostReqHeaders = Utility.getHeaders("contentType:application/json,Authorization:"+getConfigManager().getConfig(Postauth));
 	    requestSpecBuilder=new RequestSpecBuilder();
 	    requestSpecBuilder = requestSpecBuilder.setBaseUri(getConfigManager().getConfig(Posturl));
 	    requestSpecBuilder = requestSpecBuilder.addHeaders(PostReqHeaders);
 	   return requestSpec = requestSpecBuilder.build();
 	   }
    @BeforeClass(groups = { "Smoke"},enabled=false)
	  public  RequestSpecification B2BB2CSetRestAssuredConfig(String Posturl) {
	  logger.info("------- setRestAssuredConfig---------");
	  logger.info("----Building Request Specification for B2B and B2C Post Call -----");
	  Map<String, String> PostReqHeaders = Utility.addHead("Content-Type","application/x-www-form-urlencoded");
      requestSpecBuilder=new RequestSpecBuilder();
      requestSpecBuilder = requestSpecBuilder.setBaseUri(Posturl);
	  requestSpecBuilder = requestSpecBuilder.addHeaders(PostReqHeaders);
	   return requestSpec = requestSpecBuilder.build();
	   }
 	    @BeforeClass(groups = { "Smoke"},enabled=false)
  	  public  RequestSpecification GetSetRestAssuredConfig(String Getauth,String Geturl){   
 	    logger.info("----Building Request Specification for Workday -----");
 	    reqSpecBuilder = new RequestSpecBuilder();
        Map<String, String> reqHeaders = Utility.getHeaders("contentType:application/xml,Authorization:"+getConfigManager().getConfig(Getauth));
 	    reqSpecBuilder = reqSpecBuilder.setBaseUri(getConfigManager().getConfig(Geturl));
 	    reqSpecBuilder = reqSpecBuilder.addHeaders(reqHeaders);
 	    return reqSpec = reqSpecBuilder.build();
 	    }
 	    
 	   @BeforeClass(groups = { "Smoke"},enabled=false)
   	  public  RequestSpecification LMSGetSetRestAssuredConfig(String Getauth,String url){   
  	    logger.info("----Building Request Specification for Workday -----");
  	    reqSpecBuilder = new RequestSpecBuilder();
  	    logger.info("=================auth===========");
        Map<String, String> reqHeaders = Utility.getHeaders("contentType:application/xml,Authorization:"+Getauth);
  	    reqSpecBuilder = reqSpecBuilder.setBaseUri(url);
  	    reqSpecBuilder = reqSpecBuilder.addHeaders(reqHeaders);
  	    return reqSpec = reqSpecBuilder.build();
  	    }
 	   
 	  @BeforeClass(groups = { "Smoke"},enabled=false)
   	  public  RequestSpecification WESGetSetRestAssuredConfig(String Getauth,String Geturl){   
  	    logger.info("----Building Request Specification for Workday -----");
  	    reqSpecBuilder = new RequestSpecBuilder();
        Map<String, String> reqHeaders = Utility.getHeader("Authorization:"+Getauth);
  	    reqSpecBuilder = reqSpecBuilder.setBaseUri(Geturl);
  	    reqSpecBuilder = reqSpecBuilder.addHeaders(reqHeaders);
  	    return reqSpec = reqSpecBuilder.build();
  	    }
 	  
 	  @BeforeClass(groups = { "Smoke"},enabled=false)
   	  public  RequestSpecification B2BCDataGetSetRestAssuredConfig(String Getauth,String Geturl){   
  	    logger.info("----Building Request Specification for Workday -----");
  	    reqSpecBuilder = new RequestSpecBuilder();
        Map<String, String> reqHeaders = Utility.getHeader("Authorization:"+Getauth);
  	    reqSpecBuilder = reqSpecBuilder.setBaseUri(getConfigManager().getConfig(Geturl));
  	    reqSpecBuilder = reqSpecBuilder.addHeaders(reqHeaders);
  	    return reqSpec = reqSpecBuilder.build();
  	    }
 	  
 	 @BeforeClass(groups = { "Smoke"},enabled=false)
  	  public  RequestSpecification OIMGetSetRestAssuredConfig(String Getauth,String url){   
 	    logger.info("----Building Request Specification for Workday -----");
 	    reqSpecBuilder = new RequestSpecBuilder();
 	    logger.info("=================auth===========");
       Map<String, String> reqHeaders = Utility.getHeaders("contentType:application/xml,Authorization:"+getConfigManager().getConfig(Getauth));
 	    reqSpecBuilder = reqSpecBuilder.setBaseUri(url);
 	    reqSpecBuilder = reqSpecBuilder.addHeaders(reqHeaders);
 	    return reqSpec = reqSpecBuilder.build();
 	    }

}
