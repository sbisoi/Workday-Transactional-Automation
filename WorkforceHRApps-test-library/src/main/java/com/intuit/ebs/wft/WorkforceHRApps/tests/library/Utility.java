package com.intuit.ebs.wft.WorkforceHRApps.tests.library;
/**
 * @author spoddar
 */
import static com.jayway.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.testng.Assert;

import com.csvreader.CsvReader;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.intuit.tools.commontestbase.service.ServiceTestBase;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.XmlConfig;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;

public class Utility {
	
	static Response  resp;
	static XML xml;
	static CsvReader data;
	static File file;
	static ArrayList<String> headersList;
	static ArrayList<String> valuesList;
	static ArrayList<ArrayList<String>> testData;
	
	final static Logger logger = Logger.getLogger(Utility.class);
	
	/**
	 * @param relativePath
	 * @param fileName
	 * @return
	 * This method is to get the absolute File Path based on the relative path and file name
	 */
	public static String getAbsoluteFilePath(String relativePath, String fileName)
	{
		String dataFile = relativePath + fileName;
		File file = new File(dataFile);
	    dataFile = file.getAbsolutePath();
	    return dataFile;
	}
	
	/**
	 * @param path
	 * @return
	 * @throws IOException
	 * This method is to generate string from an XML resource
	 */
	public static String GenerateStringFromResource(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)));
	}
	
	/**
	 * @param firstXML
	 * @param secondXML
	 * @throws Exception
	 * This method is to assert whether two XMLs are equal
	 */
	public static void assertXMLEquals(String firstXML, String secondXML) throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        XMLUnit.setIgnoreAttributeOrder(true);
        XMLUnit.setIgnoreComments(true);
        
        DetailedDiff diff = new DetailedDiff(XMLUnit.compareXML(firstXML, secondXML));

        List<?> allDifferences = diff.getAllDifferences();
        logger.info("Differences found: "+ diff.toString());
        logger.info("Differences found: "+ allDifferences);
        Assert.assertEquals(0, allDifferences.size(), "There are differences between the 2 XML files");
    }
	
	/**
	 * @param contentType
	 * @param URL
	 * @param payLoad
	 * @return
	 * This method is to get the response from a Soap request with XML payload
	 */
	public static Response getResponseForSoapRequestWithXMLPayload(String URL, String payLoad )
	{
		Response response = given().request().contentType("application/soap+xml; charset=UTF-8;").body(payLoad)
		.when().post(URL).then().extract().response();
		return response;
	}
	
	/**
	 * @param headers
	 * @return
	 * This method is to create the request headers with name:value pair
	 */
	public static Map<String,String> getHeaders(String headers)
	{
		 String headerArray[] = headers.split(",");
		 Map<String, String> mapHeaders = new HashMap<String, String>();
		 for(String header:headerArray)
		 {
			 mapHeaders.put(header.split(":")[0],header.split(":")[1]);
		 }
		 logger.info("Map Headers : " + mapHeaders);
		 return mapHeaders;
	}
	
	/**
	 * @param reqParams
	 * @return
	 * This method is to create the request parameters with name:value pair
	 */
	public static Map<String,String> getRequestParameters(String reqParams)
	{
		 String headerArray[] = reqParams.split(",");
		 Map<String, String> mapReqParams = new HashMap<String, String>();
		 for(String header:headerArray)
		 {
			 mapReqParams.put(header.split("=")[0],header.split("=")[1]);
		 }
		 logger.info("Request Parameters : " + mapReqParams);
		 return mapReqParams;
	}

	/**
	 * @param payloadFilePath
	 * @param csvFilePath
	 * @return
	 * @throws IOException
	 * This method is to generate an XML string by parsing the XML Schema and replacing with values
	 */
	public static String parseXmlPayload(String payloadFilePath, String csvFilePath) throws IOException
	{
		xml = new XMLDocument(new File(payloadFilePath));
		String xmlString= null;
		testData = new ArrayList<ArrayList<String>>(readCSV(csvFilePath));
		
		headersList = testData.get(0);
		valuesList = testData.get(1);
		logger.info("Headers :" + headersList +"\nValues: " + valuesList);	

		xmlString = xml.toString();
		
		int i = 0;
		int j = 0;

		while(i<headersList.size())
		{
			while(j<valuesList.size())
			{
				xmlString = xmlString.replace(headersList.get(i), valuesList.get(j));
				if(i == j)
				{
					break;
				}
			}
			i++;
			j++;
		}
		logger.info("XML as String using JCabi library : "+ "\n" + "=============================" ); 
		logger.info(xmlString);
		return xmlString;
	}

	/**
	 * @param csvFilePath
	 * @return
	 * @throws IOException
	 * This method will read the test data CSV file and return the column headers and values as Array of ArrayList
	 */
	public static List<ArrayList<String>> readCSV(String csvFilePath) throws IOException
	{
		data = new CsvReader(csvFilePath);
		ArrayList <String> fieldValues = new ArrayList <String>();
		ArrayList <String> headersValues = new ArrayList<String> ();
		List <ArrayList<String>> csvData = new ArrayList <ArrayList<String>>();

		data.readHeaders();
		for(String headers : data.getHeaders())	
		{
			headersValues.add(headers);
		}
		csvData.add(headersValues);
		//System.out.println("Header values: "+ csvData);

		while(data.readRecord())
		{
			for(int i=0; i<data.getColumnCount(); i++)
			{
				fieldValues.add(data.get(i));
			}
			csvData.add(fieldValues);
		}
		//System.out.println("Final CSV is :" + csvData);
		return csvData;
	}

	public static void validateResponse(String response, String testFilePath, String csvFilePath) throws IOException
	{
		data = new CsvReader(testFilePath+"/"+csvFilePath);
		data.readHeaders();
		
		ArrayList<String> validateFields = new ArrayList<String>();
		validateFields.add("CORPID");
		validateFields.add("APPLICANTID");
		
		for(String fields : validateFields)
		{
			logger.info("reading value for " + fields + ": " + data.get("fiedls"));
			assertTrue(response.contains(data.get("fiedls")), "Response doesn't have the value");
		}
	}

	/**
	 * @param requestSpec
	 * @param payloadSchemaPath
	 * @param payloadName
	 * @param testFilePath
	 * @param testDataFileName
	 * @param endpointUrl
	 * @return
	 * @throws IOException
	 * Common method for POST call
	 */
	public static Response doPostCall(RequestSpecification requestSpec, String payloadSchemaPath, String payloadName, String testFilePath, String testDataFileName, 
			String endpointUrl) throws IOException
	{
		String payload = parseXmlPayload(payloadSchemaPath+"/"+payloadName
				, testFilePath+"/"+testDataFileName);
		
		resp = (Response) given().spec(requestSpec).body(payload).when().post(endpointUrl);
		return resp;
	}

	/**
	 * @param reqSpec
	 * @param payloadSchemaPath
	 * @param xmlPayloadName
	 * @param testFilePath
	 * @param testDataFileName
	 * @param endpointUrl
	 * @return
	 * @throws IOException
	 * Common method for GET call Without Request Body
	 */
	public static Response doGetCall(RequestSpecification reqSpec, String endpointUrl) throws IOException
	{
		HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wd", "urn:com.workday.report/IntuitIntegrationAdmin/IntuitGetWorkerSensitive_v1.1");
        
        XmlConfig xmlConf = new XmlConfig();
		Map<String, String> reqParameters = getRequestParameters("format=simplexml,Corp_ID=700033119");
		
		Response response = given().spec(reqSpec).params(reqParameters).config(RestAssured.config().xmlConfig(xmlConf.declareNamespaces(namespaces))).
				when().get(endpointUrl);
		return response;
	}
	
	/**
	 * @param integration
	 * @param env
	 * @return
	 * Commom method to return the file name from Integration and environment
	 */
	public static String getFileName(String integration, String env)
	{
		integration = integration.substring(1);
		integration = FilenameUtils.removeExtension(integration);
		logger.info("Integration : " + integration);
		String environment = env.substring(1);
		logger.info("Env : " + environment);
	    String wd_report_env = integration + "_" + environment;
	    logger.info("Report Name : " + wd_report_env);
	    return wd_report_env;
	}

}
