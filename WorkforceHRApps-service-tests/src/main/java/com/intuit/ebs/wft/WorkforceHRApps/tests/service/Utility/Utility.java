package com.intuit.ebs.wft.WorkforceHRApps.tests.service.Utility;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.BoomiTestBase;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.intuit.ebs.wft.WorkforceHRApps.tests.service.BoomiTest;
import com.jayway.restassured.response.Response;

public class Utility extends BoomiTestBase{
    public   void LMSValidation(String EmpIdentifier,String JasonTag,String value) throws Exception{
		   logger.info("=================started LMS Validation==========================");
	       String lms_url=getConfigManager().getConfig(Constant.LMS_POST_URL);
	       logger.info("----lms URL------"+lms_url+"-----------");
	       PostSetRestAssuredConfig(Constant.LMS_POST_AUTH, Constant.LMS_POST_URL);
	       String payload =com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readText(BoomiTest.LMS_PATH+"/"+"lms.txt");
	       System.out.println(payload);
	       System.out.println("lms url- "+getConfigManager().getConfig(Constant.LMS_POST_URL));
	       
	       logger.info("============Generate Token from post call===========");
	       Response resp=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.PostCall(requestSpec, BoomiTest.LMS_PATH, "lms.txt", lms_url);
	       resp.then().assertThat().statusCode(200);
	       System.out.println("post call =" +resp.asString());
	       logger.info("Token is generated  ===");
	       com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readJsonObject(resp.asString(), "access_token");
	        System.out.println("LMS Token value- "+com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readJsonObject(resp.asString(), "access_token")); 
	        String baseuri=getConfigManager().getConfig(Constant.LMS_GETWORKER_DETAIL)+"("+"'"+EmpIdentifier+"'"+")";
	        logger.info("baseuri=  "+baseuri); 
	        String token="Bearer "+com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readJsonObject(resp.asString(), "access_token");
	        logger.info("token=  "+token); 
	        logger.info("============Provide credential to to set the configuration==========");
	        LMSGetSetRestAssuredConfig(token, baseuri);
	        logger.info("===========LMS get call started to validate the data available in LMS==========");

	        Response replms=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.LMSdoGetCall(reqSpec, baseuri);

	        logger.info("status get call= "+replms.statusCode());
	        logger.info("Response body"+replms.asString());
	        replms.then().assertThat().statusCode(200);
	        String actual=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readJsonObject(replms.asString(), JasonTag);
	        assertEquals(actual, value);
	        logger.info("===================LMS Validation complete===================");
			
	}

public void WESValidation(String EmpIdentifier) throws Exception{
		
	String auth="Intuit_IAM_Authentication intuit_appid=ebs.workforce.wes.automation,intuit_app_secret=preprdtetQd9VNGD0M6aLP3QNfssZ1mLmlqRxHXG";
	String uri=Constant.WES_BASE_URL+EmpIdentifier+"/basic";
	logger.info("URI= "+uri);
	logger.info("auth==== "+auth);
	WESGetSetRestAssuredConfig(Constant.WES_AUTH, Constant.WES_BASE_URL+EmpIdentifier+"/basic");
	logger.info("===========================start Get Call for WES==================================");
	Response repwes=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.LMSdoGetCall(reqSpec, uri);

	logger.info("WES Status= "+ repwes.statusCode());
	logger.info("Wes Response= "+repwes.asString());
	String actuals= com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readJsonObject(repwes.asString(), "APPLICANT_ID");
	assertEquals(actuals, EmpIdentifier);
	repwes.then().assertThat().statusCode(200);
	}

public void Taleovalidation(String name) throws IOException{
	   
  String taleo_URL=getConfigManager().getConfig(Constant.TALEO_URL);
  PostSetRestAssuredConfig(Constant.TALEO_AUTH, Constant.TALEO_URL);
  String payload=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readText(BoomiTest.Taleo_path+"/"+"Upserttaleo.txt");
  Response TaleoRep=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.PostCall(requestSpec, BoomiTest.Taleo_path, "Upserttaleo.txt", taleo_URL);
   logger.info("Taleo Verification Status= "+ TaleoRep.statusCode());
   logger.info("Response body= "+TaleoRep.asString());
   
   TaleoRep.then().assertThat().statusCode(200);
   String repsonseText=TaleoRep.asString();
   Boolean found=repsonseText.contains(name);
   assertTrue(found);
  logger.info("Name found in Response body="+ found);
}
  
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

public void b2btokenAndData(String paramName,String query) throws Exception{
	logger.info("===================B2B post call started=======================");
	String b2bPosturl=getConfigManager().getConfig(Constant.b2b_url);
	B2BB2CSetRestAssuredConfig(b2bPosturl);
	logger.info("==============Started Post call==========================");
	Response rep=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.PostCall(requestSpec, BoomiTest.B2B_PATH, "b2b.txt", b2bPosturl);
	logger.info("Response status="+rep.statusCode());
	logger.info("Response body"+ rep.asString());
	String b2b_token=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readJsonObject(rep.asString(), "access_token");
	String bearer_b2b="Bearer "+b2b_token;
	logger.info("B2B Access token= "+b2b_token);
	logger.info("===========Start get call to fetch ID using Federation/Corp id===========");

	String b2bidurl=getConfigManager().getConfig(Constant.B2B_GET_ID_URL);
	System.out.println("B2B ID URL= "+b2bidurl);
	LMSGetSetRestAssuredConfig(bearer_b2b,b2bidurl);
	logger.info("Get call for B2B Started===========================");
	Response reps=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.b2bdoGetCall(reqSpec, b2bidurl,paramName,query);
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

	Response b2bgetdata=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.LMSdoGetCall(reqSpec, b2b_path);
	logger.info("Status "+b2bgetdata.statusCode());
	logger.info("Get response= "+b2bgetdata.asString());
	String fname=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readJsonObject(b2bgetdata.asString(), "FirstName");
	logger.info("Fname= "+fname);

}

public void b2ctokenAndData(String paramName,String query,String corpid) throws Exception{
	logger.info("===================B2B post call started=======================");
	String b2cPosturl=getConfigManager().getConfig(Constant.b2c_url);
	B2BB2CSetRestAssuredConfig(b2cPosturl);
	logger.info("==============Started Post call==========================");
	Response rep1=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.PostCall(requestSpec, BoomiTest.B2B_PATH, "b2b.txt", b2cPosturl);
	logger.info("Response status="+rep1.statusCode());
	logger.info("Response body"+ rep1.asString());
	String b2c_token=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readJsonObject(rep1.asString(), "access_token");
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

  Response reps=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.b2bdoGetCall(reqSpec, b2cidurl, paramName, query);
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
  Response b2cgetdata=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.LMSdoGetCall(reqSpec, b2c_path);
  logger.info("Status "+b2cgetdata.statusCode());
  logger.info("Get response= "+b2cgetdata.asString());
  String fname=com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility.readJsonObject(b2cgetdata.asString(), "FirstName");
  logger.info("Fname= "+fname);
  logger.info("===========B2C Get Data call completed===============");

}

}
