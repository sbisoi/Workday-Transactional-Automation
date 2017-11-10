package com.intuit.ebs.wft.WorkforceHRApps.tests.service;
/**
 * @author spoddar
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.intuit.ebs.wft.WorkforceHRApps.tests.library.Utility;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.WorkdayTestBase;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class WDIntegrationCompareTest extends WorkdayTestBase{
	
	private String testenv;
	private String SFTPHOST;
	private String SFTPUSER;
	private String SFTPPASS;
	private String environment1;
	private String environment2;
	private String integration;
	final static Logger logger = Logger.getLogger(WDIntegrationCompareTest.class);
	
	WDIntegrationCompareTest(){
		logger.info("===WDIntegrationCompareTest===");
	}
	
	@BeforeClass(alwaysRun = true)
	  public void setUp() throws Exception {
	    logger.info("------- setupClass ---------");
	    
	    environment1 = getConfigManager().getConfig(Constant.ENVIRONMENT_1);
		environment2 = getConfigManager().getConfig(Constant.ENVIRONMENT_2);
	    
	    	logger.debug("-----Create the Download Directory in the workspace if it does not exist----");
	    	//Create the Download Directory in the workspace if it does not exist
	    	File dir = new File(System.getProperty("user.dir") + "/test-output/" +"/APITestFileDownloads");
	    	if (!dir.exists()) {
	    		if (dir.mkdirs()) {
	    			logger.info("Download Directory is created in the Workspace");
	    		} else {
	    			logger.info("Failed to create Download Directory");
	    		}
	    	}
	    	//Delete the earlier files in the Download Directory if the Workspace directory already exist
	    	logger.info("----Delete the earlier files in the Download Directory if the Workspace directory already exist----");
	    	if (dir.exists())
	    	{
		    	if(dir.listFiles().length>0)
		    	{
		    		for(File file: dir.listFiles()) 
		    		if (!file.isDirectory()) 
			        {
			    		logger.info("Deleting..." + file.getName());
			    		file.delete();
			        }
		    	}
	    	}
	    	//This is to get the env. where the tests will be run
			testenv = getConfigManager().getConfig(Constant.TEST_ENV);
			logger.info("Test is run : " + testenv);
			if (testenv.equalsIgnoreCase("local"))
	  	    {
	  		    SFTPHOST = "pdevwesws300.corp.intuit.net";
	  		    SFTPUSER = "xxxx-admin";
	  			SFTPPASS = "xxxxxxxx";
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
	  			
	  			// This is to copy all the files from the SFTP directory
	  			logger.info("Copy all the files from the SFTP directory");
	  			@SuppressWarnings("unchecked")
	  			Vector<ChannelSftp.LsEntry> list = channelSftp.ls("."); 
	  			for (ChannelSftp.LsEntry oListItem : list) {
	  				// Output each item from directory listing for logs
	                  logger.info(oListItem.toString()); 

	                  if (!oListItem.getAttrs().isDir()) {
		                  	 files.add(oListItem.getFilename().toString());
		                     grabCount++; 
		              }
	                  
	                  // If it is a file (not a directory)
	                  if (!oListItem.getAttrs().isDir()) {
	                     logger.info("get " + oListItem.getFilename());
	                     channelSftp.get(oListItem.getFilename(), System.getProperty("user.dir") + "/test-output/" +"/APITestFileDownloads/"+oListItem.getFilename());
	                     
	                  }
	  			}
	  			if (grabCount == 0) { 
	  				logger.info("Found no new files to download");
	              } else {
	              	logger.info(grabCount + " Files to be downloaded from SFTP location");
	              } 
	  			
	  			int noOfFilesDownloaded = dir.listFiles().length;
	  			logger.info(noOfFilesDownloaded + " Files actually downloaded");
	  			logger.info("Files downloaded from SFTP location : " + files);
	  			Assert.assertEquals(noOfFilesDownloaded, grabCount, "No. of files present and no. of files downloaded does not match");
		  		
		  		logger.info("Clean up the SFTP Directory");
		  		for (ChannelSftp.LsEntry oListItem : list) {
		  			// If it is a file (not a directory)
	                  if (!oListItem.getAttrs().isDir()) {
	                     logger.info("Deleting... " + oListItem.getFilename());
	                     channelSftp.rm(oListItem.getFilename());
	                  }
	  			}
	  			
	  		} catch (Exception ex) {
	  			ex.printStackTrace();
	  		}
	  		
	  }

	/**
	 * @author spoddar
	 * @throws IOException 
	 * This is to compare the results of Workday Globoforce_Outbound Launch Integration for 2 environments
	 */
	@Test
     public void testGloboforce_OutboundCompare() throws IOException
	{
		integration=getConfigManager().getConfig(Constant.GLOBOFORCE);
		String wd_report_env1 = Utility.getFileName(integration, environment1);
	    String wd_report_env2 = Utility.getFileName(integration, environment2);
        logger.info("Report 1 : "+ wd_report_env1);
        logger.info("Report 2 : "+ wd_report_env2);
		testDiffText(wd_report_env1, wd_report_env2);
	}
	
	@Test
    public void testIntuitCCMSIOutboundEIBCompare() throws IOException
	{
		integration=getConfigManager().getConfig(Constant.CCMSI);
		String wd_report_env1 = Utility.getFileName(integration, environment1);
	    String wd_report_env2 = Utility.getFileName(integration, environment2);
       logger.info("Report 1 : "+ wd_report_env1);
       logger.info("Report 2 : "+ wd_report_env2);
		testDiffText(wd_report_env1, wd_report_env2);
	}
	
	/**
	 * @author spoddar
	 * @param file1
	 * @param file2
	 * @throws IOException
	 * This method is for text comparison of 2 files
	 */
	public void testDiffText(String file1, String file2) throws IOException
	{   
        /*Date date = new Date();
        String today= new SimpleDateFormat("MM-dd-yyyy").format(date);
        logger.info(today);
        System.out.println("User Dir : " + user_dir);*/
        //String file1 = "GloboforceOutbound_intuit2_"+today;
        //String file2 = "GloboforceOutbound_preview_"+today;
		String user_dir = System.getProperty("user.dir");
		BufferedReader reader1 = new BufferedReader(new FileReader(user_dir +  "/test-output/" + "/APITestFileDownloads/" +file1));
		BufferedReader reader2 = new BufferedReader(new FileReader(user_dir +  "/test-output/" + "/APITestFileDownloads/" +file2));
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
