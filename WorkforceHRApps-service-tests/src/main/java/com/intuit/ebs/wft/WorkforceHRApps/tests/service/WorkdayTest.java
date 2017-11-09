package com.intuit.ebs.wft.WorkforceHRApps.tests.service;
/**
 * @author spoddar
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.WorkdayTestBase;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


public class WorkdayTest extends WorkdayTestBase {
	String dataFile;
	String payloadSchemaPath;
	String workdayIntuit2URI;
	String workdayPreviewURI;
	String testenv;
	String SFTPHOST;
	String SFTPUSER;
	String SFTPPASS;
	String myRequest;
	
	WorkdayTest()
	{
	}
	
	@BeforeSuite(alwaysRun = true)
	  public void setupTestData() throws FileNotFoundException, IOException, URISyntaxException {
		
		//This is to get the env. where the tests will be run
		testenv = getConfigManager().getConfig(Constant.TEST_ENV);
				
		//This is to get the Absolute Path for Payload Schema
		payloadSchemaPath = Constant.PAYLOAD_SCHEMA_LOCATION;
		payloadSchemaPath = new File(payloadSchemaPath).getAbsolutePath();
	    logger.info("Payload Schema Absolute Path : -----"+payloadSchemaPath+"-----");
	    
	    //This is to set the Workday URI for Intuit2 and Preview
	    String baseURL = getConfigManager().getConfig(Constant.HTTP_PROPERTY_BASEURL);
	    logger.info("Base URL : " + baseURL);
	    String intuit2BasePath = getConfigManager().getConfig(Constant.WORKDAY_PROPERTY_ENV_1_BASEPATH);
	    logger.info("Base Path for Intuit2 : " + intuit2BasePath);
	    workdayIntuit2URI = baseURL + intuit2BasePath;
	    logger.info("Workday Intuit2 URI : " + workdayIntuit2URI);
	    
	    String previewBasePath = getConfigManager().getConfig(Constant.WORKDAY_PROPERTY_ENV_2_BASEPATH);
	    logger.info("Base Path for Preview : " + previewBasePath);
	    workdayPreviewURI = baseURL + previewBasePath;
	    logger.info("Workday Preview URI : " + workdayPreviewURI);

	  }
	
	//@BeforeClass(groups = { "Smoke"})
	  public void setupClass() throws Exception {
	    logger.info("------- setupClass ---------");
	    
	    	logger.debug("-----WorkdayIntegrationDocumentDownload----");
	    	//Create the Download Directory if not present
	    	File dir = new File(System.getProperty("user.dir") + "/test-output/" +"/APITestFileDownloads");
	    	if (!dir.exists()) {
	    		if (dir.mkdirs()) {
                System.out.println("Multiple directories are created!");
	    		} else {
                System.out.println("Failed to create multiple directories!");
	    		}
	    	}
	    	for(File file: dir.listFiles()) 
	    		if (!file.isDirectory()) 
		        {
		    		System.out.println(file.getName());
		    		file.delete();
		        }
	
	  	    if (testenv.equalsIgnoreCase("local"))
	  	    {
	  		    SFTPHOST = "pdevwesws300.corp.intuit.net";
	  		    SFTPUSER = "vvenkatesh-admin";
	  			SFTPPASS = "wesQAT613";
	  	    }else if(testenv.equalsIgnoreCase("CICD"))
	  	    {
	  	    	SFTPHOST = "deploy@pdevwesws300.corp.intuit.net";
	  		    SFTPUSER = "";
	  			SFTPPASS = "";
	  	    }
	  		int SFTPPORT = 22;
	  		int grabCount = 0;
	  		
	  		String SFTPWORKINGDIR = "/app/Workday_Upgradation/";

	  		Session session = null;
	  		Channel channel = null;
	  		ChannelSftp channelSftp = null;
	  		
	  		ArrayList<String> files = new ArrayList<String>();

	  		try {
	  			JSch jsch = new JSch();
	  			session = jsch.getSession(SFTPUSER, SFTPHOST, SFTPPORT);
	  			session.setPassword(SFTPPASS);
	  			java.util.Properties config = new java.util.Properties();
	  			config.put("StrictHostKeyChecking", "no");
	  			session.setConfig(config);
	  			session.connect();
	  			channel = session.openChannel("sftp");
	  			channel.connect();
	  			channelSftp = (ChannelSftp) channel;
	  			channelSftp.cd(SFTPWORKINGDIR);
	  			
	  			// This is to copy all the files from a directory
	  			@SuppressWarnings("unchecked")
	  			Vector<ChannelSftp.LsEntry> list = channelSftp.ls("."); 
	  			for (ChannelSftp.LsEntry oListItem : list) {
	  				// output each item from directory listing for logs
	                  System.out.println(oListItem.toString()); 

	                  // If it is a file (not a directory)
	                  if (!oListItem.getAttrs().isDir()) {
	                      // Grab the remote file ([remote filename], [local path/filename to write file to])

	                  	System.out.println("get " + oListItem.getFilename());
	                  	files.add(oListItem.getFilename().toString());
	                      //channelSftp.get(oListItem.getFilename(), System.getProperty("user.home") + "/APITestDownloads/"+oListItem.getFilename());  // while testing, disable this or all of your test files will be grabbed
	                      channelSftp.get(oListItem.getFilename(), System.getProperty("user.dir") + "/test-output/" +"/APITestFileDownloads/"+oListItem.getFilename());
	                      grabCount++; 
	                  }
	  			}
	  			if (grabCount == 0) { 
	  				System.out.println("Found no new files to grab.");
	              } else {
	              	System.out.println("Retrieved " + grabCount + " new files.");
	              } 
	  			
	  			//This is to copy a single file
	  			/*byte[] buffer = new byte[1024];
	  			BufferedInputStream bis = new BufferedInputStream(channelSftp.get("mail-editor.png"));
	  			File newFile = new File("/Users/spoddar/Documents/mail-editor.png");
	  			OutputStream os = new FileOutputStream(newFile);
	  			BufferedOutputStream bos = new BufferedOutputStream(os);
	  			int readCount;
	  			while ((readCount = bis.read(buffer)) > 0) {
	  				System.out.println("Writing: ");
	  				bos.write(buffer, 0, readCount);
	  			}
	  			bis.close();
	  			bos.close();*/
	  			
	  		} catch (Exception ex) {
	  			ex.printStackTrace();
	  		}
	  		
	  		System.out.println("Files : " + files);
	  }

	/**
	 * @throws IOException 
	 * This is to test the Workday Launch Integration for Intuit2 (which is a Soap request)
	 */
	  @Test(groups = { "Smoke" }, priority = 0)
	  public void testWorkdayLaunchIntegration() throws IOException {
	    logger.debug("-----testWorkdayLaunchIntegration on Intuit2----");
	    
	    myRequest = Utility.GenerateStringFromResource(payloadSchemaPath+"/launchWithoutParametersIntuit2.xml");
		logger.info("**********************");
		logger.info(myRequest);
		logger.info("**********************");
		
		response = Utility.getResponseForSoapRequestWithXMLPayload(workdayIntuit2URI, myRequest);
		
		logger.info(new Integer(response.getStatusCode()).toString());
		logger.info(response.body().asString());
		
		logger.debug(response.prettyPrint());
	    response.then().statusCode(200);
	    
	    logger.debug("-----testWorkdayLaunchIntegration on Preview----");
	    
		myRequest = Utility.GenerateStringFromResource(payloadSchemaPath+"/launchWithoutParametersPreview.xml");
		logger.info("**********************");
		logger.info(myRequest);
		logger.info("**********************");
			
		response = Utility.getResponseForSoapRequestWithXMLPayload(workdayPreviewURI, myRequest);
			
		logger.info(new Integer(response.getStatusCode()).toString());
		logger.info(response.body().asString());
			
		logger.debug(response.prettyPrint());
		response.then().statusCode(200);
	  }
	  
	  	
	/**
	 * @throws IOException 
	 * @throws Exception
	 * This is to compare 2 XML files
	 */
	/*@Test
	public void diffXMLs() throws Exception
	{
		String user_dir = System.getProperty("user.dir");
		String XML1 = Utility.GenerateStringFromResource(payloadSchemaPath+"/soapResponse1.xml");
		String XML2 = Utility.GenerateStringFromResource(payloadSchemaPath+"/soapResponse2.xml");
		
		Utility.assertXMLEquals(XML1,XML2);
	}*/
		

	@Test
	public void testIntegrationCompare() throws IOException
	{
		String file1="";
		String file2="";
		testDiffText(file1, file2);
	}
	
	/**
	 * @param file1
	 * @param file2
	 * @throws IOException
	 * This method for text comparison of 2 files
	 */
	public void testDiffText(String file1, String file2) throws IOException
	{   
        Date date = new Date();
        String today= new SimpleDateFormat("MM-dd-yyyy").format(date);
        logger.info(today);
        String user_dir = System.getProperty("user.dir");
        System.out.println("User Dir : " + user_dir);
        //String file1 = "GloboforceOutbound_intuit2_"+today;
        //String file2 = "GloboforceOutbound_preview_"+today;
		BufferedReader reader1 = new BufferedReader(new FileReader(user_dir +  "/test-output/" + "/APITestFileDownloads" +file1));
		BufferedReader reader2 = new BufferedReader(new FileReader(user_dir +  "/test-output/" + "/APITestFileDownloads" +file2));
		String line1 = reader1.readLine();
		String line2 = reader2.readLine();
		boolean areEqual = true;
		int lineNum = 1;
		while (line1 != null || line2 != null)
		  {
		    if(line1 == null || line2 == null)
		        {
		           areEqual = false;
		           break;
		        }
		        else if(! line1.equalsIgnoreCase(line2))
		        {
		        	areEqual = false;
		        	break;
		        }
		        line1 = reader1.readLine();
		        line2 = reader2.readLine();
		        lineNum++;
		  }
		         
		  if(areEqual)
		  {
		      logger.info("Two files have same content.");
		  }
		  else
		  {
		      System.out.println();
			 System.out.println("Two files have different content. They differ at line "+lineNum);
		     System.out.println("File1 has "+line1+" and " + "\n" + "File2 has "+line2+" at line "+lineNum);
		  }
		         
		  reader1.close();
		  reader2.close();
		  Assert.assertTrue(areEqual,"Two files have different content");
	}
}
