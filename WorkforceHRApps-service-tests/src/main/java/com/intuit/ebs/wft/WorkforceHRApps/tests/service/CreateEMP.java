package com.intuit.ebs.wft.WorkforceHRApps.tests.service;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.BoomiTestBase;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;

import ch.qos.logback.classic.Logger;

public class CreateEMP extends BoomiTestBase{
	
	
	String payloadSchemaPath;
	String testFilePath;
	static Response  resp;
	String LMS_PATH;
	String Taleo_path;
	String B2B_PATH;
	String B2C_PATH;
	
	
	  
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
        logger.info("===========B2C Get Data call completed===============");}
	
	@Test (priority=11,groups = {"smoke"}, enabled =true)

	public  void CreateEmp() throws Exception{
		

/*
		 */
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
	
	logger.info("======================Get Worker detail using NID ===========");
	PostSetRestAssuredConfig(Constant.Get_worker_auth, Constant.Get_worker_url);
	
	logger.info("GET worker_URL : " + getConfigManager().getConfig(Constant.Get_worker_url));
	logger.info("Auth_Token_get worker : " + getConfigManager().getConfig(Constant.Get_worker_auth));
	Response Getworker=Utility.doPostCall(requestSpec, payloadSchemaPath, "getWorkers_v2.xml", testFilePath, "getWorkers_v2.csv", "getWorkers_v1");
	
	String EMP_ID=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMPLOYEE_ID");
	  logger.info("emp id= "+EMP_ID);
	  
	 String applicant_id= Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:APPLICANT_ID");
	 logger.info("applicant_id= "+applicant_id);
	 
	 String EMP_type=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:EMP_TYPE");
	  logger.info("EMP type= "+EMP_type);
	 
	String LEGAL_NAME=Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:LEGAL_NAME");
	  logger.info("LEGAL_NAME= "+LEGAL_NAME);
		  
		 String HIRE_DT= Utility.GetXMLData(Getworker.asString(), "intu:Worker", "intu:HIRE_DT");
		 logger.info("Hire Date= "+HIRE_DT);
	 
	 
	logger.info("Status code= "+Getworker.statusCode());
	logger.info("Request Log= "+Getworker.asString());    
 /*   
	logger.info("========Update Corpinfo====Update the Corp ID for the employee===============");
	PostSetRestAssuredConfig(Constant.QA_AUTH, Constant.QA_URL);
	logger.info("path value = " +Constant.CorpID_Path);
	logger.info("=========== updateCorpInfo API call ======================");
	logger.info("VPCQA_URL : " + getConfigManager().getConfig(Constant.QA_URL));
	logger.info("Auth_Token_VPCQA : " + getConfigManager().getConfig(Constant.QA_AUTH));
	logger.info("=========== Starting updateCorpInfo API call ======================");
	
    String ApplicantId= Utility.convertStringToXML(payloadSchemaPath, "updateCorpEMP.xml", "UpdateCorpInfo", "APPLICANT_ID");
    logger.info("ApplicantId=    "+ApplicantId);
    
    String Corp_ID= Utility.convertStringToXML(payloadSchemaPath, "updateCorpEMP.xml", "UpdateCorpInfo", "CORP_ID");
    logger.info("Corp_ID=    "+Corp_ID);
	Response postResponse = Utility.doPostCall(requestSpec, payloadSchemaPath, "updateCorpEMP.xml", testFilePath, 
			"updateCorpEMP.csv", "updateCorpInfo_v1.0"); 
	postResponse.then().assertThat().statusCode(200);

    logger.info("status code : =" + postResponse.statusCode());
	logger.info("=========== updateCorpInfo API call completed ======================");
	logger.info("=============== Staring getEmpInfoFromWD using CorpId ===================");
	GetSetRestAssuredConfig(Constant.AUTH_TOKEN_INTUIT2, Constant.INTUIT2_URL);
	
	String actualID=Utility.convertStringToXML(payloadSchemaPath, "updateCorpEMP.xml", "UpdateCorpInfo", "CORP_ID");
	System.out.println(actualID);
	String param="format=simplexml,"+"CORP_ID"+"="+actualID;
	System.out.println("param=" +param);
	
	Response getResp=Utility.GetCallFromXML(reqSpec, getConfigManager().getConfig(Constant.GET_WORKER_SENSITIVE_ENDPOINT), param);
	
	getResp.then().assertThat().statusCode(200);
	logger.info("Status= "+getResp.statusCode());
	logger.info("Response = "+getResp.asString());
	String Fname= Utility.GetXMLData(getResp.asString(), "wd:Report_Entry", "wd:LEGAL_NAME_FIRST");
   	logger.info("fName=   "+Fname);

	logger.info("Response from getCall: " + getResp.asString());
	XmlPath xml1 = new XmlPath(getResp.asString()).setRoot("Report_Data.Report_Entry");
    String actualCorpId = xml1.getString("CORP_ID");
    assertEquals(actualCorpId,actualID);
    String emp_status=xml1.getString("EMP_STATUS_CODE");
    logger.info("======Emp status is "+emp_status);
	
	logger.info("===========completed WORKDAY Validation using CorpId  ======================");
	
	logger.info("=================started LMS Validation==========================");
	
	logger.info("Pass Corpid= "+Corp_ID+" and Pass Json tagname");
	LMSValidation(Corp_ID, "firstName",Fname); 
	logger.info("===================LMS Verification Completed================================");
	 */
		
		
	   
	}
	
	@BeforeTest (alwaysRun=false,groups = {"smoke"},enabled=false) 
    public void OIM(String appid) throws Exception{
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
	}


