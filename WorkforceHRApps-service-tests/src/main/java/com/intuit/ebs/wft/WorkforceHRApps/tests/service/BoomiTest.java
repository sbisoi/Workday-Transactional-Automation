package com.intuit.ebs.wft.WorkforceHRApps.tests.service;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.google.common.collect.ImmutableBiMap.Builder;
/**
 * @author spoddar
 */
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.BoomiTestBase;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import bsh.util.Util;
import ch.qos.logback.classic.Logger;
import groovy.json.JsonParser;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
//import org.apache.log4j.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.jayway.restassured.RestAssured.given;

//import org.hamcrest.Matchers.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.multi.MultiTableHeaderUI;
import javax.xml.parsers.DocumentBuilderFactory;


public class BoomiTest extends BoomiTestBase {
	
	public static String payloadSchemaPath;
	public static String testFilePath;
	public static Response  resp;
	public static String LMS_PATH;
	public static String Taleo_path;
	public static String B2B_PATH;
	public static String B2C_PATH;
	 //RequestSpecification reqSpec;
	
    BoomiTest(){
    }
    
    @BeforeSuite(alwaysRun = false)
	  public void setupTestData() throws FileNotFoundException, IOException, Exception {
	    //This is to get the Absolute Path for Payload Schema and Test Files
		payloadSchemaPath = Constant.PAYLOAD_SCHEMA_LOCATION;
		payloadSchemaPath = new File(payloadSchemaPath).getAbsolutePath();
	    logger.info("Payload Schema Absolute Path : -----"+payloadSchemaPath+"-----");
	    testFilePath = Constant.TEST_FILE_LOCATION;
	    testFilePath = new File(testFilePath).getAbsolutePath();
	    logger.info("Test File Absolute Path : -----"+testFilePath+"-----");
	    LMS_PATH=Constant.LMS_File_Path;
	    LMS_PATH=new File(LMS_PATH).getAbsolutePath();
	    logger.info("Lms json file= "+LMS_PATH+"--------");
	    Taleo_path=Constant.taleo_File_Path;
	    Taleo_path=new File(Taleo_path).getAbsolutePath();
	    logger.info("Taleo File Path= "+Taleo_path);
	    B2B_PATH=Constant.B2B_File_Path;
	    B2B_PATH=new File(B2B_PATH).getAbsolutePath();
	    logger.info("B2B PATH----------"+B2B_PATH);
	    
	    B2C_PATH=Constant.B2C_File_Path;
	    B2C_PATH=new File(B2C_PATH).getAbsolutePath();
	    logger.info("B2C PATH----------"+B2C_PATH);
	    
	    
		
    }
    
	@BeforeTest (alwaysRun=false,groups = {"smoke"},enabled=false) 
    public void OIMGetCall(String appid) throws Exception{
		logger.info("======================OIM Validation Started=======================");
		String url=getConfigManager().getConfig(Constant.OIM_URL)+appid;
		System.out.println("URL= "+url);
	    OIMGetSetRestAssuredConfig(Constant.OIM_AUTH, url);
	    
	    Response oimrep=Utility.LMSdoGetCall(reqSpec, url);
	    
	    logger.info("Status of OIM= "+oimrep.statusCode());
	    logger.info("Response of OIM get call "+oimrep.asString());
	    
	    String fname=Utility.GetXMLData(oimrep.asString(), "oimUser", "preferredFirstName");
	    logger.info("First name= "+fname);
	    
	    String lname=Utility.GetXMLData(oimrep.asString(), "oimUser", "legalLastName");
	    logger.info("Last name= "+lname);
	    
	    String AppID=Utility.GetXMLData(oimrep.asString(), "oimUser", "applicantId");
	    logger.info("Last name= "+AppID);
	    
	    String EmpType=Utility.GetXMLData(oimrep.asString(), "oimUser", "employeeSubType");
	    logger.info("Last name= "+EmpType);
	    
	    String Corpid=Utility.GetXMLData(oimrep.asString(), "oimUser", "corporateId");
	    logger.info("Last name= "+Corpid);
	    
	}
    @BeforeTest (alwaysRun=false,groups = {"smoke"},enabled=false)
    public   void LMSValidation(String EmpIdentifier,String JasonTag,String value) throws Exception{
		   logger.info("=================started LMS Validation==========================");
	       String lms_url=getConfigManager().getConfig(Constant.LMS_POST_URL);
	       logger.info("----lms URL------"+lms_url+"-----------");
	       PostSetRestAssuredConfig(Constant.LMS_POST_AUTH, Constant.LMS_POST_URL);
	       String payload = Utility.readText(LMS_PATH+"/"+"lms.txt");
	       System.out.println(payload);
	       System.out.println("lms url- "+getConfigManager().getConfig(Constant.LMS_POST_URL));
	       
	       logger.info("============Generate Token from post call===========");
	       Response resp=Utility.PostCall(requestSpec, LMS_PATH, "lms.txt", lms_url);
	       resp.then().assertThat().statusCode(200);
	       System.out.println("post call =" +resp.asString());
	       logger.info("Token is generated  ===");
	        Utility.readJsonObject(resp.asString(), "access_token");
	        System.out.println("LMS Token value- "+Utility.readJsonObject(resp.asString(), "access_token")); 
	        String baseuri=getConfigManager().getConfig(Constant.LMS_GETWORKER_DETAIL)+"("+"'"+EmpIdentifier+"'"+")";
	        logger.info("baseuri=  "+baseuri); 
	        String token="Bearer "+Utility.readJsonObject(resp.asString(), "access_token");
	        logger.info("token=  "+token); 
	        logger.info("============Provide credential to to set the configuration==========");
	        LMSGetSetRestAssuredConfig(token, baseuri);
	        logger.info("===========LMS get call started to validate the data available in LMS==========");

	        Response replms=Utility.LMSdoGetCall(reqSpec, baseuri);

	        logger.info("status get call= "+replms.statusCode());
	        logger.info("Response body"+replms.asString());
	        replms.then().assertThat().statusCode(200);
	        String actual=Utility.readJsonObject(replms.asString(), JasonTag);
	        assertEquals(actual, value);
	        logger.info("===================LMS Validation complete===================");
			
	}
    
    @BeforeTest (alwaysRun=false,groups = {"smoke"},enabled=false)
    public void WESValidation(String EmpIdentifier) throws Exception{
		
	String auth="Intuit_IAM_Authentication intuit_appid=ebs.workforce.wes.automation,intuit_app_secret=preprdtetQd9VNGD0M6aLP3QNfssZ1mLmlqRxHXG";
	String uri=Constant.WES_BASE_URL+EmpIdentifier+"/basic";
	logger.info("URI= "+uri);
	logger.info("auth==== "+auth);
	WESGetSetRestAssuredConfig(Constant.WES_AUTH, Constant.WES_BASE_URL+EmpIdentifier+"/basic");
	logger.info("===========================start Get Call for WES==================================");
	Response repwes=Utility.LMSdoGetCall(reqSpec, uri);

	logger.info("WES Status= "+ repwes.statusCode());
	logger.info("Wes Response= "+repwes.asString());
	String actuals= Utility.readJsonObject(repwes.asString(), "APPLICANT_ID");
	assertEquals(actuals, EmpIdentifier);
	repwes.then().assertThat().statusCode(200);
	}
    @BeforeTest (alwaysRun=false,groups = {"smoke"},enabled=false)   
    public void Taleovalidation(String name) throws IOException{
    	   
        String taleo_URL=getConfigManager().getConfig(Constant.TALEO_URL);
        PostSetRestAssuredConfig(Constant.TALEO_AUTH, Constant.TALEO_URL);
        String payload=Utility.readText(Taleo_path+"/"+"Upserttaleo.txt");
        Response TaleoRep=Utility.PostCall(requestSpec, Taleo_path, "Upserttaleo.txt", taleo_URL);
         logger.info("Taleo Verification Status= "+ TaleoRep.statusCode());
         logger.info("Response body= "+TaleoRep.asString());
         
         TaleoRep.then().assertThat().statusCode(200);
         String repsonseText=TaleoRep.asString();
         Boolean found=repsonseText.contains(name);
         assertTrue(found);
        logger.info("Name found in Response body="+ found);
    }
    @BeforeTest (alwaysRun=false,groups = {"smoke"},enabled=false)     
    public void WDPValidation(String query) throws ClassNotFoundException, SQLException{
    	Class.forName("oracle.jdbc.driver.OracleDriver"); 
    	String dbUrl =getConfigManager().getConfig(Constant.db_Url);
    	logger.info("DB Url= "+dbUrl);
    	String dbid=getConfigManager().getConfig(Constant.db_id);
    	String password=getConfigManager().getConfig(Constant.db_password);
    	logger.info("==============Create the Connection================================");
    	Connection conn=DriverManager.getConnection(dbUrl, dbid, password);
    	logger.info("==============Connection create==================");
    	Statement stmt=conn.createStatement();
    	logger.info("===========create query as below===============");
    	ResultSet result=stmt.executeQuery(query);
    //	ResultSet result=stmt.executeQuery("SELECT EMPLOYEE_ID,APPLICANT_ID,EMAIL_PRIMARY_WORK FROM  WKD_BASE_WORKER_DATA WHERE EMPLOYEE_ID='192766' AND APPLICANT_ID='A4100357' AND TRANSACTION_TYPE='Rescind' and ROWNUM=1");
    	logger.info("============Display the result==========================");
    	while(result.next())
    	logger.info("Verify emp details in WDP: "+"employeid= "+result.getInt(1)+", "+"Applicantid= "+result.getString(2)+", "+"Email Id= "+result.getString(3));

    	conn.close();
    }
    @BeforeTest (alwaysRun=false,groups = {"smoke"},enabled=false) 
public void b2btokenAndData(String paramName,String query) throws Exception{
    	logger.info("===================B2B post call started=======================");
    	String b2bPosturl=getConfigManager().getConfig(Constant.b2b_url);
    	B2BB2CSetRestAssuredConfig(b2bPosturl);
    	logger.info("==============Started Post call==========================");
    	Response rep=Utility.PostCall(requestSpec, B2B_PATH, "b2b.txt", b2bPosturl);
    	logger.info("Response status="+rep.statusCode());
    	logger.info("Response body"+ rep.asString());
    	String b2b_token=Utility.readJsonObject(rep.asString(), "access_token");
    	String bearer_b2b="Bearer "+b2b_token;
    	logger.info("B2B Access token= "+b2b_token);
    	logger.info("===========Start get call to fetch ID using Federation/Corp id===========");

    	String b2bidurl=getConfigManager().getConfig(Constant.B2B_GET_ID_URL);
    	System.out.println("B2B ID URL= "+b2bidurl);
    	LMSGetSetRestAssuredConfig(bearer_b2b,b2bidurl);
    	logger.info("Get call for B2B Started===========================");
    	Response reps=Utility.b2bdoGetCall(reqSpec, b2bidurl,paramName,query);
    	//query=SELECT id from User WHERE FederationIdentifier='50001990704'
    	logger.info("Status= "+reps.statusCode());
    	logger.info("Status Body= "+reps.getBody().asString());
    	//String B2BId=reps.getBody().jsonPath().getString("records.Id").toString().substring(1, 19);//getString("records.Id").toString();
    	String B2BId=reps.getBody().jsonPath().getString("records.Id").toString().replaceAll("\\[", "").replaceAll("\\]", "");
    	logger.info("---B2b ID="+B2BId);
    	logger.info("================start get call to Validate User Data===========================");

    	String b2b_path=getConfigManager().getConfig(Constant.B2B_DATA_URL)+B2BId;
    	System.out.println("B2B path="+b2b_path);
    	LMSGetSetRestAssuredConfig(bearer_b2b, b2b_path);

    	Response b2bgetdata=Utility.LMSdoGetCall(reqSpec, b2b_path);
    	logger.info("Status "+b2bgetdata.statusCode());
    	logger.info("Get response= "+b2bgetdata.asString());
    	String fname=Utility.readJsonObject(b2bgetdata.asString(), "FirstName");
    	logger.info("Fname= "+fname);

}
    
    @BeforeTest (alwaysRun=false,groups = {"smoke"},enabled=false) 
    public void b2ctokenAndData(String paramName,String query,String corpid) throws Exception{
    	logger.info("===================B2B post call started=======================");
    	String b2cPosturl=getConfigManager().getConfig(Constant.b2c_url);
    	B2BB2CSetRestAssuredConfig(b2cPosturl);
    	logger.info("==============Started Post call==========================");
    	Response rep1=Utility.PostCall(requestSpec, B2B_PATH, "b2b.txt", b2cPosturl);
    	logger.info("Response status="+rep1.statusCode());
    	logger.info("Response body"+ rep1.asString());
    	String b2c_token=Utility.readJsonObject(rep1.asString(), "access_token");
    	logger.info("B2C Access token= "+b2c_token);
    	String bearer_b2c="Bearer "+b2c_token;
    	logger.info("B2C Access token= "+bearer_b2c);
    	logger.info("===========Start B2C get call to fetch ID using Federation/Corp id===========");

        String b2cidurl=getConfigManager().getConfig(Constant.B2C_GET_ID_URL);
        logger.info("B2B ID URL= "+b2cidurl);
        
        logger.info("===========Set Restassured Configuration============");
        LMSGetSetRestAssuredConfig(bearer_b2c,b2cidurl);
        
        logger.info("Get call for B2C Started===========================");
       //form this and call this method String val="SELECT id from User WHERE FederationIdentifier='"+corpid+"'";
        //query=SELECT id from User WHERE FederationIdentifier='50001990704'

        Response reps=Utility.b2bdoGetCall(reqSpec, b2cidurl, paramName, query);
        logger.info("Status= "+reps.statusCode());
        logger.info("Status Body= "+reps.getBody().asString());
    //String B2BId=reps.getBody().jsonPath().getString("records.Id").toString().substring(1, 19);//getString("records.Id").toString();
        String B2CId=reps.getBody().jsonPath().getString("records.Id").toString().replaceAll("\\[", "").replaceAll("\\]", "");
        logger.info("---B2b ID="+B2CId);
        logger.info("================start get call to Validate User Data===========================");

        String b2c_path=getConfigManager().getConfig(Constant.B2C_DATA_URL)+B2CId;
        logger.info("B2C path="+b2c_path);
        LMSGetSetRestAssuredConfig(bearer_b2c, b2c_path);

        logger.info("==============B2C Get Data call started=================");
        Response b2cgetdata=Utility.LMSdoGetCall(reqSpec, b2c_path);
        logger.info("Status "+b2cgetdata.statusCode());
        logger.info("Get response= "+b2cgetdata.asString());
        String fname=Utility.readJsonObject(b2cgetdata.asString(), "FirstName");
        logger.info("Fname= "+fname);
        logger.info("===========B2C Get Data call completed===============");
    }
    
    @Test (priority=0,groups= {"smoke"}, enabled=true)
     public void CreateEMP() throws Exception{
    	logger.info("==================Info Create EMP========================");
    	logger.info("=====Call Restassured config method=========");
    	PostSetRestAssuredConfig(Constant.QA_AUTH, Constant.QA_URL);
    	logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.QA_URL));
    	logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.QA_AUTH));

    	Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "createEmployee.xml", testFilePath, "createEmployee.csv", "createEmployee");
    	logger.info("statuc code : "+ resPost.statusCode());
    	resPost.then().assertThat().statusCode(200);
    	logger.info("statuc code : "+ resPost.statusCode());
    	logger.info("Response= "+resPost.asString());
    	logger.info("==================completed Create Emp========================"); 
    	
    	 Thread.sleep(15000);
    }
    
    @Test (priority=1,groups = {"smoke"}, enabled =true)
    
    public void createempValidation(){
    	
    }

  @Test (priority=1,groups = {"smoke"}, enabled =false)
    
    public  void upsertOIMTest() throws Exception{
    logger.info("==================Info OIM Upsert========================");
    logger.info("=====Call Restassured confsig method=========");
	PostSetRestAssuredConfig(Constant.QA_AUTH, Constant.QA_URL);
    logger.info("VPCE2E URL :  = " +getConfigManager().getConfig(Constant.QA_URL));
    logger.info("                                                                    ");
    logger.info("VPCE2E Auth Token : = "+ getConfigManager().getConfig(Constant.QA_AUTH));
    logger.info("                                                                    ");
    logger.info("====================Starting upsertOIM Call==========================");
    
    String ApplicantId= Utility.convertStringToXML(payloadSchemaPath, "upsertOIM.xml", "element","Applicant_ID");
    logger.info("ApplicantId=    "+ApplicantId);
    
    String Corp_ID= Utility.convertStringToXML(payloadSchemaPath, "upsertOIM.xml", "element","Corp_ID");
    logger.info("Corp_ID=    "+Corp_ID);
    
    Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "upsertOIM.xml", testFilePath, "upsertOIM.csv", "upsertOIM");
    logger.info("Post Response:= "+resPost.asString());
    
    resPost.then().assertThat().statusCode(200);
    logger.info("statuc code : "+ resPost.statusCode());
    
    logger.info("==================Completed OIM Upsert========================");
    logger.info("===============started workday Get Call============================");
    GetSetRestAssuredConfig(Constant.AUTH_TOKEN_INTUIT2, Constant.INTUIT2_URL);
    
    String param="format=simplexml,"+"CORP_ID"+"="+Corp_ID;
	System.out.println("param=" +param);
	
    Response getResp=Utility.GetCallFromXML(reqSpec, getConfigManager().getConfig(Constant.GET_WORKER_SENSITIVE_ENDPOINT),param);	
	logger.info("Response from getCall: " + getResp.asString());
	
	XmlPath xml1 = new XmlPath(getResp.asString()).setRoot("Report_Data.Report_Entry");
    String actualCorpId = xml1.getString("CORP_ID");
    logger.info("Body of get Response=="+ (getResp.getStatusCode()));
     assertEquals(actualCorpId, Corp_ID);
     logger.info("===============Completed workday Get Call============================");
       //logger.info("Body of get Response"+ getResp.body());
       
     logger.info("=================Started Oboarding Verification in WES ===================== ");
       
     logger.info("Provide applicantid= "+ ApplicantId);
     WESValidation(ApplicantId);
     logger.info("==========================End Get Call for WES=============================");
     
     logger.info("=================OIM Get call using applicant id=======================");
     
   //  OIMGetCall(ApplicantId);
     
     logger.info("==================OIM get call completed==================================");
		
    }
    
    
@Test (priority=2,groups = {"smoke"}, enabled =false)
    
    public  void OboardingTest() throws Exception{
    logger.info("==================Info OboardingTest========================");
    logger.info("=====Call Restassured config method=========");
	PostSetRestAssuredConfig(Constant.AUTH_TOKEN_VPCE2E, Constant.VPCE2E_URL);
    logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.VPCE2E_URL));
	logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.AUTH_TOKEN_VPCE2E));
    logger.info("                                                                    ");
    logger.info("====================Starting OboardingTest PostCall==========================");
    String corpid=Utility.convertStringToXML(payloadSchemaPath, "upsertOBoarding.xml", "element", "Corp_ID");
     System.out.println("Corp id= "+corpid);
    Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "upsertOBoarding.xml", testFilePath, "upsertOBoarding.csv", "upsertOBoarding");
    logger.info("statuc code : "+ resPost.statusCode());
    resPost.then().assertThat().statusCode(200);
    logger.info("statuc code : "+ resPost.statusCode());
    logger.info("==================completed Oboarding  Postcall========================");
    
    
    logger.info("===============started workday Get Call============================");
    
    logger.info("======================Get Worker detail using Corp ID===========");
	/*	
    PostSetRestAssuredConfig(Constant.GET_WORKER_QA_AUTH, Constant.GET_WORKER_QA_URL);
		
		logger.info("GET worker_URL : " + getConfigManager().getConfig(Constant.GET_WORKER_QA_URL));
		logger.info("Auth_Token_get worker : " + getConfigManager().getConfig(Constant.GET_WORKER_QA_AUTH));
		Response Getworker=Utility.doPostCall(requestSpec, payloadSchemaPath, "getWorkersWes.xml", testFilePath, "getWorkersWes.csv", "getWorkers_v1");
		logger.info("Response status"+Getworker.statusCode());
		logger.info("Response Body= "+Getworker.asString());
		logger.info("Post woker detail with Corp id completed========================");
		
		String EMP_ID=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMPLOYEE_ID");
		  logger.info("emp id= "+EMP_ID);
		  
		 String applicant_id= Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMPLOYEE_ID");
		 logger.info("applicant_id= "+applicant_id);
		 
		 String CORP_ID=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:CORP_ID");
		  logger.info("CORP_ID= "+CORP_ID);
		  
		  assertEquals(corpid, CORP_ID); 
		  logger.info("=========================Succesfull================== ");  */
/*    GetSetRestAssuredConfig(Constant.AUTH_TOKEN_INTUIT2, Constant.INTUIT2_URL);
    Response getResp=Utility.GetCall(reqSpec, getConfigManager().getConfig(Constant.GET_WORKER_SENSITIVE_ENDPOINT), "CORP_ID", 7, 0);	
		logger.info("Response from getCall: " + getResp.asString());
		XmlPath xml1 = new XmlPath(getResp.asString()).setRoot("Report_Data.Report_Entry");
      String actualCorpId = xml1.getString("CORP_ID");
      logger.info("Body of get Response=="+ (getResp.getStatusCode()));
       assertEquals(actualCorpId, "70000035032");
       logger.info("===============Completed workday Get Call============================"); */
    
    logger.info("=================Started Oboarding Verification in WES ===================== ");
    String auth="Intuit_IAM_Authentication intuit_appid=ebs.workforce.wes.automation,intuit_app_secret=preprdtetQd9VNGD0M6aLP3QNfssZ1mLmlqRxHXG";
    String uri=Constant.WES_BASE_URL+"A4006513"+"/basic";
    logger.info("URI= "+uri);
    logger.info("auth==== "+auth);
    WESGetSetRestAssuredConfig(auth, Constant.WES_BASE_URL+"A4006513"+"/basic");
    logger.info("===========================start Get Call for WES==================================");
    Response repwes=Utility.LMSGetCall(reqSpec, uri, "WES", 7, 2);
    
    logger.info("WES Status= "+ repwes.statusCode());
    logger.info("Wes Response= "+repwes.asString());
    String actual= Utility.readJsonObject(repwes.asString(), "APPLICANT_ID");
    assertEquals(actual, "A4006513");
    repwes.then().assertThat().statusCode(200);
    
    logger.info("==========================End Get Call for WES=============================");
    
    }
    

@Test(priority=3,groups = { "Smoke" },enabled=false)
	public void UpdateCorpInfoE2E() throws Exception {
		logger.info("=====Call Restassured config method=========");
		PostSetRestAssuredConfig(Constant.AUTH_TOKEN_VPCE2E, Constant.VPCE2E_URL);
		logger.info("path value = " +Constant.CorpID_Path);
		logger.info("=========== updateCorpInfo API call ======================");
		logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.VPCE2E_URL));
		logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.AUTH_TOKEN_VPCE2E));
		logger.info("=========== Starting updateCorpInfo API call ======================");
		
	    String ApplicantId= Utility.convertStringToXML(payloadSchemaPath, "UpdateCorpInfo.xml", "UpdateCorpInfo", "APPLICANT_ID");
	    logger.info("ApplicantId=    "+ApplicantId);
	    
	    String Corp_ID= Utility.convertStringToXML(payloadSchemaPath, "UpdateCorpInfo.xml", "UpdateCorpInfo", "CORP_ID");
	    logger.info("Corp_ID=    "+Corp_ID);
		Response postResponse = Utility.doPostCall(requestSpec, payloadSchemaPath, "UpdateCorpInfo.xml", testFilePath, 
				"UpdateCorpInfo.csv", "updateCorpInfo_v1.0"); 
		postResponse.then().assertThat().statusCode(200);
	
        logger.info("status code : =" + postResponse.statusCode());
		logger.info("=========== updateCorpInfo API call completed ======================");
		logger.info("=============== Staring getEmpInfoFromWD using CorpId ===================");
		GetSetRestAssuredConfig(Constant.AUTH_TOKEN_INTUIT2, Constant.INTUIT2_URL);
		
		String actualID=Utility.convertStringToXML(payloadSchemaPath, "UpdateCorpInfo.xml", "UpdateCorpInfo", "CORP_ID");
		System.out.println(actualID);
    	String param="format=simplexml,"+"CORP_ID"+"="+actualID;
    	System.out.println("param=" +param);
		Response getResp=Utility.GetCallFromXML(reqSpec, getConfigManager().getConfig(Constant.GET_WORKER_SENSITIVE_ENDPOINT), param);
		
		logger.info("Status= "+getResp.statusCode());
		logger.info("Response "+getResp.asString());
		String Fname= Utility.GetXMLData(getResp.asString(), "wd:Report_Entry", "wd:LEGAL_NAME_FIRST");
	   	logger.info("fName=   "+Fname);

		logger.info("Response from getCall: " + getResp.asString());
		XmlPath xml1 = new XmlPath(getResp.asString()).setRoot("Report_Data.Report_Entry");
        String actualCorpId = xml1.getString("CORP_ID");
        assertEquals(actualCorpId,actualID);
		
		logger.info("===========completed WORKDAY Validation using CorpId  ======================");
		
		logger.info("=================started LMS Validation==========================");
		
		logger.info("Pass Corpid= "+Corp_ID+"Pass Json tagname= "+"studentID");
		LMSValidation(Corp_ID, "firstName",Fname); 
		logger.info("===================LMS Verification Completed================================");
		 
}
 
    
    @Test(priority=4,groups = { "Smoke" },enabled=false)
    
	public void UpdateContactinfo() throws Exception {
    	logger.info("=====Call Restassured config method=========");
		PostSetRestAssuredConfig(Constant.AUTH_TOKEN_VPCE2E, Constant.VPCE2E_URL);
		
		logger.info("=========== Starting updateContactinfo API call ======================");
		logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.VPCE2E_URL));
		logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.AUTH_TOKEN_VPCE2E));
	
		String ApplicantId= Utility.convertStringToXML(payloadSchemaPath, "updateContactInfo.xml", "wd:Report_Entry", "wd:APPLICANT_ID");
		logger.info("ApplicantId=    "+ApplicantId);
		    
		String Corp_ID= Utility.convertStringToXML(payloadSchemaPath, "updateContactInfo.xml", "wd:Report_Entry", "wd:CORP_ID");
		logger.info("Corp_ID=    "+Corp_ID);
		    
		Response postResponse = Utility.doPostCall(requestSpec, payloadSchemaPath, "updateContactInfo.xml", testFilePath, 
				"updateContactInfo.csv", "updateContactInfo");
		logger.info("Post call:   "+postResponse.asString());
		postResponse.then().assertThat().statusCode(200);

		logger.info("=========== updateContactInfo API call completed ======================");
		logger.info("=============== Staring getEmpInfoFromWD using CorpId ===================");
		GetSetRestAssuredConfig(Constant.AUTH_TOKEN_INTUIT2, Constant.INTUIT2_URL);
		String param="format=simplexml,"+"CORP_ID"+"="+Corp_ID;
   	    System.out.println("param=" +param);
		Response getResp=Utility.GetCallFromXML(reqSpec, getConfigManager().getConfig(Constant.GET_WORKER_SENSITIVE_ENDPOINT),param);
		logger.info("Response from getCall: " + getResp.asString());
		
		XmlPath xml1 = new XmlPath(getResp.asString()).setRoot("Report_Data.Report_Entry");
        String actualCorpId = xml1.getString("CORP_ID");
        assertEquals(actualCorpId,Corp_ID);
		
		logger.info("===========  completed updateContactInfo using CorpId  ======================");
		
	}
    
    @Test(priority=5 , groups = { "Smoke" },enabled=false)
   	public void  updateApplicant() throws Exception {
    	logger.info("=====Call Restassured config method=========");
		PostSetRestAssuredConfig(Constant.AUTH_TOKEN_VPCE2E, Constant.VPCE2E_URL);
   		logger.info("=========== INFO updateApplicant API call ======================");
   		logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.VPCE2E_URL));
   		logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.AUTH_TOKEN_VPCE2E));
   		logger.info("                                                                     ");
   		
   		String corpid=Utility.convertStringToXML(payloadSchemaPath, "updateApplicant.xml", "wd:Report_Entry", "wd:CORP_ID");
   		logger.info("Corpid= "+corpid);
   		logger.info("=========== Starting updateApplicant API call ======================");
 
   		Response postResponse = Utility.doPostCall(requestSpec, payloadSchemaPath, "updateApplicant.xml", testFilePath, 
   				"updateApplicant.csv", "updateApplicant");
   		postResponse.then().assertThat().statusCode(200);
   		
   		logger.info("Response from POST Call:= " + postResponse.asString());
   		logger.info("=========== updateApplicant API call completed ======================");
   		
   		logger.info("======================Get Worker detail using Corp ID===========");
   		PostSetRestAssuredConfig(Constant.GET_WORKER_QA_AUTH, Constant.GET_WORKER_QA_URL);
   		
   		logger.info("GET worker_URL : " + getConfigManager().getConfig(Constant.GET_WORKER_QA_URL));
   		logger.info("Auth_Token_get worker : " + getConfigManager().getConfig(Constant.GET_WORKER_QA_AUTH));
   		Response Getworker=Utility.doPostCall(requestSpec, payloadSchemaPath, "getWorkersCorpApplicant.xml", testFilePath, "getWorkersCorpApplicant.csv", "getWorkers_v1");
   		logger.info("Response status"+Getworker.statusCode());
   		logger.info("Response Body= "+Getworker.asString());
   		logger.info("Post woker detail with Corp id completed========================");
   		
   		String EMP_ID=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMPLOYEE_ID");
   		  logger.info("emp id= "+EMP_ID);
   		  
   		 String applicant_id= Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMPLOYEE_ID");
   		 logger.info("applicant_id= "+applicant_id);
   		 
   		 String CORP_ID=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:CORP_ID");
   		  logger.info("CORP_ID= "+CORP_ID);
   		  
   		  assertEquals(corpid, CORP_ID);
   		  logger.info("=========================Succesfull================== ");
 
   	}
    
  //working
    @Test (priority=6,groups = {"smoke"}, enabled =true)

    public  void upsertTaleo() throws Exception{
   logger.info("==================Info OIM Upsert========================");
    logger.info("=====Call Restassured config method=========");
    PostSetRestAssuredConfig(Constant.AUTH_TOKEN_VPCE2E, Constant.VPCE2E_URL);
    logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.VPCE2E_URL));
    logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.AUTH_TOKEN_VPCE2E));
    String fname=Utility.convertStringToXML(payloadSchemaPath, "upsertTaleo.xml", "element", "Preferred_Name_First");
    logger.info("First name= "+ fname);
    Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "upsertTaleo.xml", testFilePath, "upsertTaleo.csv", "upsertTaleo");
    logger.info("statuc code : "+ resPost.statusCode());
    resPost.then().assertThat().statusCode(200);
    logger.info("statuc code : "+ resPost.statusCode()); 
    
    logger.info("====================Verification on Taleo Started==================");
    logger.info("Provide the Verification Input like Name= " + fname);
     Taleovalidation(fname);
  
    logger.info("=======================Taleo Verification Completed======================");
     
  }
    
    @Test(priority=7 , groups = { "Smoke" },enabled=true)
   	public void  OIMAdhocUpdate() throws Exception {
    	
   		logger.info("=========== Starting OIMAdhoc API call ======================");
   		logger.info("=====Call Restassured config method=========");
   		PostSetRestAssuredConfig(Constant.QA_AUTH, Constant.QA_URL);
   		logger.info("VPCQA_URL : " + getConfigManager().getConfig(Constant.QA_URL));
   		logger.info("Auth_Token_VPCQA : " + getConfigManager().getConfig(Constant.QA_AUTH));
   		
   		String corpid=Utility.convertStringToXML(payloadSchemaPath, "updateAdhocOIM.xml", "idm:Report_Entry", "idm:CORP_ID");
   		logger.info("Corpid= "+corpid);
   		
   		Response postResponse = Utility.doPostCall(requestSpec, payloadSchemaPath, "updateAdhocOIM.xml", testFilePath, 
   				"updateAdhocOIM.csv", "updateAdhocOIM");
   		logger.info("Post Response= "+postResponse.asString());
   		//postResponse.body().toString();
   		postResponse.then().assertThat().statusCode(200);
   		logger.info("======================Get Worker detail using Corp ID===========");
   		PostSetRestAssuredConfig(Constant.GET_WORKER_QA_AUTH, Constant.GET_WORKER_QA_URL);
   		
   		logger.info("GET worker_URL : " + getConfigManager().getConfig(Constant.GET_WORKER_QA_URL));
   		logger.info("Auth_Token_get worker : " + getConfigManager().getConfig(Constant.GET_WORKER_QA_AUTH));
   		Response Getworker=Utility.doPostCall(requestSpec, payloadSchemaPath, "getWorkersCorpAdhocOIM.xml", testFilePath, "getWorkersCorpAdhocOIM.csv", "getWorkers_v1");
   		logger.info("Response status"+Getworker.statusCode());
   		logger.info("Response Body= "+Getworker.asString());
   		logger.info("Post woker detail with Corp id completed========================");
   		
   		String EMP_ID=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMPLOYEE_ID");
   		  logger.info("emp id= "+EMP_ID);
   		  
   		 String applicant_id= Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMPLOYEE_ID");
   		 logger.info("applicant_id= "+applicant_id);
   		 
   		 String CORP_ID=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:CORP_ID");
   		  logger.info("CORP_ID= "+CORP_ID);
   		  
   		  assertEquals(corpid, CORP_ID);
   		  logger.info("=========================Succesfull================== ");
   		
   	}



@Test (priority=8,groups = {"smoke"}, enabled =true)

public  void upsertCWFs() throws Exception{
logger.info("==================Info Contract CWF=======================");
logger.info("=====Call Restassured config method=========");
PostSetRestAssuredConfig(Constant.QA_AUTH, Constant.QA_URL);
logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.QA_URL));
logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.QA_AUTH));

    Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "upsertCWFs.xml", testFilePath, "upsertCWFs.csv", "upsertCWFs");
    logger.info("statuc code : "+ resPost.statusCode());
    resPost.then().assertThat().statusCode(200);
    logger.info("statuc code : "+ resPost.statusCode());
    logger.info("==================completed Contract CWF========================");

    logger.info("=========== Upsert CWF API call completed ======================");
	logger.info("=============== Staring Get EMP data using CorpId ===================");
	
	 String Corpid= Utility.convertStringToXML(payloadSchemaPath, "upsertCWFs.xml", "bm:EngageContingentWorker","bm:Rehire_Corp_ID");
     System.out.println("Corpid= "+Corpid);
     
     String firstName=Utility.convertStringToXML(payloadSchemaPath,"upsertCWFs.xml", "bm:EngageContingentWorker", "bm:Legal_First_Name");
     System.out.println("Fname= "+firstName);
     logger.info("==Get The Corpid from Above method==============");
     
     logger.info("===========Start Get call for ");
    String param="format=simplexml,"+"CORP_ID"+"="+Corpid;
    System.out.println("param=" +param);
   GetSetRestAssuredConfig(Constant.AUTH_TOKEN_INTUIT2, Constant.INTUIT2_URL);
   Response getResp=Utility.GetCallFromXML(reqSpec, getConfigManager().getConfig(Constant.GET_WORKER_SENSITIVE_ENDPOINT), param);	
   logger.info("Response from getCall: " + getResp.asString());
   XmlPath xml1 = new XmlPath(getResp.asString()).setRoot("Report_Data.Report_Entry");
   logger.info("Validate the corpid is same as it in request==================");
   String actualCorpId = xml1.getString("CORP_ID");
   logger.info("Body of get Response=="+ (getResp.getStatusCode()));
   assertEquals(actualCorpId, Corpid);
   //logger.info("Body of get Response"+ getResp.body());
	
	logger.info("===========Completed get Upsert CWF using CorpId  ======================");
	logger.info("===========================================================================");
	logger.info("==========Lms Validation Function call===================");
	LMSValidation(Corpid, "firstName",firstName);
	logger.info("===================LMS Verification Completed================================");


}

@Test (priority=9,groups = {"smoke"}, enabled =true)

public  void updateCWFs() throws IOException{
logger.info("==================Info Update CFDS EMP========================");
logger.info("=====Call Restassured config method=========");
PostSetRestAssuredConfig(Constant.QA_AUTH, Constant.QA_URL);
logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.QA_URL));
logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.QA_AUTH));

Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "updateCWFs.xml", testFilePath, "updateCWFs.csv", "updateCWFs");
logger.info("statuc code : "+ resPost.statusCode());
resPost.then().assertThat().statusCode(200);
logger.info("statuc code : "+ resPost.statusCode());
logger.info("==================completed Update CWF========================");

}

@Test (priority=10,groups = {"smoke"}, enabled =true)

public  void deleteCWFs() throws Exception{
logger.info("==================Info Update CFDS EMP========================");
logger.info("=====Call Restassured config method=========");
PostSetRestAssuredConfig(Constant.QA_AUTH, Constant.QA_URL);
logger.info("VPCE2E_URL : " + getConfigManager().getConfig(Constant.QA_URL));
logger.info("Auth_Token_VPCE2E : " + getConfigManager().getConfig(Constant.QA_AUTH));

String Corpid= Utility.convertStringToXML(payloadSchemaPath, "deleteCWFsp.xml", "bm:termContingentWorker","bm:CorpID");
logger.info("Corpid=    "+Corpid);

String ApplicantID= Utility.convertStringToXML(payloadSchemaPath, "deleteCWFsp.xml", "bm:termContingentWorker","bm:ApplicantID");
logger.info("APPLICANTID=    "+ApplicantID);
String firstname= Utility.convertStringToXML(payloadSchemaPath, "deleteCWFsp.xml", "bm:termContingentWorker","bm:FirstName");
logger.info("firstname=    "+firstname);

Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "deleteCWFsp.xml", testFilePath, "deleteCWFsp.csv", "deleteCWFs");
logger.info("statuc code : "+ resPost.statusCode());
resPost.then().assertThat().statusCode(200);
logger.info("statuc code : "+ resPost.statusCode());
logger.info("==================completed Update CWF========================");

logger.info("=================started LMS Validation==========================");
logger.info("Provide Corid= "+Corpid +" Tagname from Json file= "+"studentID");

LMSValidation(Corpid, "firstName", firstname);

logger.info("=================End of LMS Validation========================");

logger.info("=================Started Oboarding Verification in WES ===================== ");
logger.info("Provide WES Applicant id= "+ApplicantID);
    WESValidation(ApplicantID); 
logger.info("==========================End Get Call for WES=============================");


}



@Test (priority=12,groups = {"smoke"}, enabled =true)

public  void WDPBaseWorker() throws IOException, Exception{
logger.info("==================Info WDP Base Worker========================");
logger.info("=====Call Restassured config method=========");
PostSetRestAssuredConfig(Constant.AUTH_TOKEN_PERF, Constant.LOCALPERF_URL);
logger.info("Local_perf_URL : " + getConfigManager().getConfig(Constant.LOCALPERF_URL));
logger.info("Auth_Token_perf : " + getConfigManager().getConfig(Constant.AUTH_TOKEN_PERF));

String applicantid=Utility.convertStringToXML(payloadSchemaPath, "upsertWDP.xml", "element", "Applicant_ID");
logger.info("ApplicantId= "+applicantid);

Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "upsertWDP.xml", testFilePath, "upsertWDP.csv", "upsertWDP");
logger.info("statuc code : "+ resPost.statusCode());
resPost.then().assertThat().statusCode(200);
String actual=Utility.convertStringToXML(payloadSchemaPath, "upsertWDP.xml", "element", "Applicant_ID");
logger.info("===========Convert xml and read Tag value=================="+actual);


logger.info("==================completed WDP Base Worker========================"); 


logger.info("==============Validate WDP===============================");
logger.info("Provide the SQL Query in below method========================");
WDPValidation("SELECT EMPLOYEE_ID,APPLICANT_ID,EMAIL_PRIMARY_WORK FROM  WKD_BASE_WORKER_DATA WHERE EMPLOYEE_ID='192766' AND APPLICANT_ID='A4100357' AND TRANSACTION_TYPE='Rescind' and ROWNUM=1");
logger.info("===================== WDP Passed========================");

}

@Test (priority=13,groups = {"smoke"}, enabled =false)

public  void SFWFChangeWorker() throws IOException{
logger.info("==================Info WDP Base Worker========================");
logger.info("=====Call Restassured config method=========");
PostSetRestAssuredConfig(Constant.AUTH_TOKEN_ATOM_CLOUD, Constant.ATOM_CLOUD_URL);
logger.info("Local_perf_URL : " + getConfigManager().getConfig(Constant.ATOM_CLOUD_URL));
logger.info("Auth_Token_perf : " + getConfigManager().getConfig(Constant.AUTH_TOKEN_ATOM_CLOUD));

Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "upsertChangeWorker.xml", testFilePath, "upsertChangeWorker.csv", "upsertChangeWorker");
logger.info("statuc code : "+ resPost.statusCode());
resPost.then().assertThat().statusCode(200);
logger.info("statuc code : "+ resPost.statusCode());
logger.info("==================completed WDP Base Worker========================");

}

@Test (priority=14,groups = {"smoke"}, enabled =false)

public  void SFWFupdate() throws Exception{

logger.info("==================Info SFWFupdate Base Worker Post========================");
logger.info("=====Call Restassured config method=========");
PostSetRestAssuredConfig(Constant.AUTH_TOKEN_ATOM_CLOUD, Constant.ATOM_CLOUD_URL);
logger.info("Local_perf_URL : " + getConfigManager().getConfig(Constant.ATOM_CLOUD_URL));
logger.info("Auth_Token_perf : " + getConfigManager().getConfig(Constant.AUTH_TOKEN_ATOM_CLOUD));

Response resPost=Utility.doPostCall(requestSpec, payloadSchemaPath, "updateWorkerSF.xml", testFilePath, "updateWorkerSF.csv", "updateWorkerSF");
logger.info("statuc code : "+ resPost.statusCode());
resPost.then().assertThat().statusCode(200);
logger.info("statuc code : "+ resPost.statusCode());
logger.info("==================completed SFWFupdate Base Worker Post Call========================"); 
	
	



}

@Test (priority=15,groups = {"smoke"}, enabled =false)

public  void HireCreateEmpVerification() throws Exception{


	logger.info("======================Get Worker detail using NID ===========");
	PostSetRestAssuredConfig(Constant.Get_worker_auth, Constant.Get_worker_url);
	
	logger.info("GET worker_URL : " + getConfigManager().getConfig(Constant.Get_worker_url));
	logger.info("Auth_Token_get worker : " + getConfigManager().getConfig(Constant.Get_worker_auth));
	Response Getworker=Utility.doPostCall(requestSpec, payloadSchemaPath, "getWorkers_v1.xml", testFilePath, "getWorkers_v1.csv", "getWorkers_v1");
	
	String EMP_ID=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMPLOYEE_ID");
	  logger.info("emp id= "+EMP_ID);
	  
	 String applicant_id= Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMPLOYEE_ID");
	 logger.info("applicant_id= "+applicant_id);
	 
	 String CORP_ID=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:CORP_ID");
	  logger.info("CORP_ID= "+CORP_ID);
	 
	String LEGAL_NAME=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:LEGAL_NAME");
	  logger.info("LEGAL_NAME= "+LEGAL_NAME);
		  
		 String NID= Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:NID_UNFORMATTED");
		 logger.info("N_id= "+NID);
	 
	 
	logger.info("Status code= "+Getworker.statusCode());
	logger.info("Request Log= "+Getworker.asString());
	
	/*
	logger.info("====================Verification on Taleo Started==================");

	String taleo_URL=getConfigManager().getConfig(Constant.TALEO_URL);
	PostSetRestAssuredConfig(Constant.TALEO_AUTH, Constant.TALEO_URL);
	String payload=Utility.readText(Taleo_path+"/"+"Createtaleo.txt");
	Response TaleoRep=Utility.PostCall(requestSpec, Taleo_path, "Createtaleo.txt", taleo_URL);
	 logger.info("Taleo Verification Status= "+ TaleoRep.statusCode());
	 logger.info("Response body= "+TaleoRep.asString());
	 
	 TaleoRep.then().assertThat().statusCode(200);
	 String name="Shama";
	 String repsonseText=TaleoRep.asString();
	 Boolean found=repsonseText.contains(name);
	 assertTrue(found);
	  logger.info("Name found in Response body="+ found);
	  logger.info("=======================Taleo Verification Completed======================");

	
	logger.info("=================started LMS Validation==========================");
	String lms_url=getConfigManager().getConfig(Constant.LMS_POST_URL);
	logger.info("----lms URL------"+lms_url+"-----------");
	PostSetRestAssuredConfig(Constant.LMS_POST_AUTH, Constant.LMS_POST_URL);
	String payload1 = Utility.readText(LMS_PATH+"/"+"lms.txt");
	System.out.println(payload1);
	System.out.println("lms url- "+getConfigManager().getConfig(Constant.LMS_POST_URL));
	Response resp=Utility.PostCall(requestSpec, LMS_PATH, "lms.txt", lms_url);
	resp.then().assertThat().statusCode(200);
	System.out.println("post call =" +resp.asString());
	Utility.readJsonObject(resp.asString(), "access_token");
	System.out.println("LMS Token value- "+Utility.readJsonObject(resp.asString(), "access_token")); 
	String baseuri=getConfigManager().getConfig(Constant.LMS_GETWORKER_DETAIL)+"("+"'"+"70002110897"+"'"+")";
	System.out.println("url= "+baseuri);
	String token="Bearer "+Utility.readJsonObject(resp.asString(), "access_token");
	System.out.println(token);
	LMSGetSetRestAssuredConfig(token, baseuri);
	Response replms=Utility.LMSGetCall(reqSpec, baseuri, "CORP_ID",9, 1 );

	logger.info("status get call= "+replms.statusCode());
	logger.info("Response body"+replms.asString());

	replms.then().assertThat().statusCode(200);
	String actual=Utility.readJsonObject(replms.asString(), "studentID");
	assertEquals(actual, "70002110897");
	logger.info("===================LMS Verification Completed================================");

	logger.info("=================Started Oboarding Verification in WES ===================== ");
	String auth="Intuit_IAM_Authentication intuit_appid=ebs.workforce.wes.automation,intuit_app_secret=preprdtetQd9VNGD0M6aLP3QNfssZ1mLmlqRxHXG";
	String uri=Constant.WES_BASE_URL+"A4112166"+"/basic";
	logger.info("URI= "+uri);
	logger.info("auth==== "+auth);
	WESGetSetRestAssuredConfig(Constant.WES_AUTH, Constant.WES_BASE_URL+"A4112166"+"/basic");
	logger.info("===========================start Get Call for WES==================================");
	Response repwes=Utility.LMSGetCall(reqSpec, uri, "WES", 9, 2);

	logger.info("WES Status= "+ repwes.statusCode());
	logger.info("Wes Response= "+repwes.asString());
	String actuals= Utility.readJsonObject(repwes.asString(), "APPLICANT_ID");
	assertEquals(actuals, "A4112166");
	repwes.then().assertThat().statusCode(200);

	logger.info("==========================End Get Call for WES=============================");  */
}

}
