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
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.XMLUnit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.omg.CORBA.Request;
import org.testng.Assert;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.csvreader.CsvReader;
import com.gargoylesoftware.htmlunit.javascript.host.NodeList;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.BoomiTestBase;
import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.intuit.tools.commontestbase.service.ServiceTestBase;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.config.XmlConfig;
import com.jayway.restassured.path.xml.XmlPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.thoughtworks.selenium.webdriven.commands.GetValue;

public class Utility extends BoomiTestBase{
	
	static Response  resp;
	static XML xml;
	static CsvReader data;
	static File file;
	static ArrayList<String> headersList;
	static ArrayList<String> valuesList;
	static ArrayList<ArrayList<String>> testData;
	static XSSFWorkbook excelsheet;
	static XSSFSheet sheet;
	static XSSFRow row;
	static XSSFCell col;
	static List<String> list;
	
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
	 * 
	 *
	 * @param path
	 * @return
	 * @throws IOException
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
	 * @author sbisoi
	 * @param headerName
	 * @param headerValue
	 * @return
	 */
	
	public static Map<String,String> addHead(String headerName,String headerValue)
	{
	
		 Map<String, String> mapHeaders = new HashMap<String, String>();
		 
			 mapHeaders.put(headerName,headerValue);

		 logger.info("Map Headers : " + mapHeaders);
		 return mapHeaders;
	}
	/**
	 * @author sbisoi
	 * @param headers
	 * @return
	 * used for passing the headers with comma separated multiple authentication
	 */
	
	public static Map<String,String> getHeader(String headers)
	{
		 String headerArray[] = headers.split("/");
		 Map<String, String> mapHeaders = new HashMap<String, String>();
		 for(String header:headerArray)
		 {
			 mapHeaders.put(header.split(":")[0],header.split(":")[1]);
		 }
		 logger.info("Map Headers : " + mapHeaders);
		 return mapHeaders;
	}
	/**
	 * @author sbisoi
	 * @param header
	 * @return
	 */
	
	public static List<String> OneHeader(String header)
	{
		 String headerArray[] = header.split("/");
		 List<String> mapHeader =new  ArrayList<>();
		 for(String head:headerArray)
		 {
			 mapHeader.add(head.split("=")[0]);
		 }
		 logger.info("Request Parameters : " + mapHeader);
		 return mapHeader;
	}
	
	/**
	 * @param reqParams
	 * @return
	 * This method is to create the request parameters with name:value pair
	 */
	public static Map<String,String> TwogetRequestParameters(String reqParams)
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
	 * @author sbisoi
	 * @param reqParams
	 * @return
	 */
	
	public static List<String> OnegetRequestParameter(String reqParams)
	{
		 String headerArray[] = reqParams.split(",");
		 List<String> mapReqParams =new  ArrayList<>();
		 for(String header:headerArray)
		 {
			 mapReqParams.add(header.split("=")[0]);
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
			logger.info("reading value for " + fields + ": " + data.get("fields"));
			assertTrue(response.contains(data.get("fiedls")), "Response doesn't have the value");
		}
	}
	/**
	 * @author sbisoi
	 * @param path
	 * @return
	 * @throws IOException
	 */
	
	public static String readText(String path) throws IOException{
		
		FileInputStream input=new FileInputStream(path);
		String text=org.apache.commons.io.IOUtils.toString(input);
		return text;
		
	}
	/**
	 * @author sbisoi
	 * @param payload
	 * @param payloadname
	 * @param tagname
	 * @param name
	 * @return
	 * @throws IOException
	 * @throws Exception
	 * Get attribute from locally stored XML file
	 */
	public static String convertStringToXML(String payload,String payloadname,String elementname,String tagname) throws IOException, Exception{
		String path=payload+"\\"+payloadname;
		String xml=readText(path);
		org.w3c.dom.Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(new StringReader(xml)));
		logger.info("Use The closing Tagname to search child tag=============");
		org.w3c.dom.NodeList nodes=doc.getElementsByTagName(elementname);
		Element element=(Element) nodes.item(0);
		logger.info("Use tagname to find the value===========");
		String value=element.getElementsByTagName(tagname).item(0).getTextContent();
		return value;
		
	}
	/**
	 * @author sbisoi
	 * @param payload
	 * @param elementname
	 * @param tagname
	 * @return
	 * @throws IOException
	 * @throws Exception
	 * Get data from running XML file
	 */
	
	public static String GetXMLData(String payload,String elementname,String tagname) throws IOException, Exception{
	//	String path=payload+"\\"+payloadname;
		//String xml=readText(path);
		org.w3c.dom.Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder()
				.parse(new InputSource(new StringReader(payload)));
	//	logger.info("Use The closing Tagname to search child tag=============");
		org.w3c.dom.NodeList nodes=doc.getElementsByTagName(elementname);
		Element element=(Element) nodes.item(0);
	//	logger.info("Use tagname to find the value===========");
		String value=element.getElementsByTagName(tagname).item(0).getTextContent();
		return value;
		
	}
	/**
	 * @author sbisoi
	 * @param tag
	 * @param element
	 * @return
	 */
	


	
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
	 * @author sbisoi
	 * @param requestSpec
	 * @param payloadSchemaPath
	 * @param filename
	 * @param endpointUrl
	 * @return
	 * @throws IOException
	 * Post call without XML Payload - any other format is accepted -text,json
	 */
	
	public static Response PostCall(RequestSpecification requestSpec, String payloadSchemaPath, String filename, String endpointUrl) throws IOException
	{
		String payload = readText(payloadSchemaPath+"/"+filename);
		
		Response rep=(Response) given().spec(requestSpec).body(payload).when().post(endpointUrl).getBody();
        return rep;
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
		Map<String, String> reqParameters = TwogetRequestParameters("format=simplexml,EMPLOYEE_ID=192664");
		
		Response response = given().spec(reqSpec).params(reqParameters).config(RestAssured.config().xmlConfig(xmlConf.declareNamespaces(namespaces))).
				when().get(endpointUrl);
		return response;
	}
	

//"format=simplexml,	
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
	/**
	 * @author sbisoi
	 * @param path
	 * @param sheetname
	 * @throws IOException
	 * Method to Set the Excell file path from Constant Library and set the Sheet Name
	 */
	
	public static  void readExcelsheet(String path,String sheetname) throws IOException{
	    FileInputStream fis=new FileInputStream(path);
		excelsheet=new XSSFWorkbook(fis);
		sheet=excelsheet.getSheet(sheetname);
		}
	/**
	 * @author sbisoi
	 * @param rw
	 * @param colm
	 * @return
	 * @throws IOException
	 * Method to pass the row and column (Column is constant here as used only 0th) value to read data from cell 
	 */
	
	public static String readStringCellValue(int rw, int cl) throws IOException{
		row=sheet.getRow(rw);
		col=row.getCell(cl);
		String getCellval=col.getStringCellValue();
		return getCellval;
	}
	
	/**
	 * @author sbisoi
	 * @param rw
	 * @param cl
	 * @return
	 * @throws IOException
	 */
	public static String readNumCellValue(int rw, int cl) throws IOException{
		row=sheet.getRow(rw);
		col=row.getCell(cl);
		String getCellval=new DataFormatter().formatCellValue(col);
		return getCellval;
	}
	/**
	 * @author sbisoi
	 * @param reqSpec
	 * @param endpointUrl
	 * @param n
	 * @return
	 * @throws IOException
	 *  Get Call method for passing different param value from excel sheet to each testcases.
	 */
	public static Response LMSGetCall(RequestSpecification reqSpec, String endpointUrl,String sheet,int r,int c) throws IOException
	{
		HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wd", "urn:com.workday.report/IntuitIntegrationAdmin/IntuitGetWorkerSensitive_v1.1");
        
        HashMap<String, String> namespace = new HashMap<String, String>();
        namespaces.put("@odata", "$metadata#Users/$entity");
         XmlConfig xmlConf = new XmlConfig();
         readExcelsheet(Constant.CorpID_Path, sheet);
		Response response = given().spec(reqSpec).config(RestAssured.config().xmlConfig(xmlConf.declareNamespaces(namespace))).
				when().get(endpointUrl);
   
return response;
	}
	/**
	 * @author sbisoi
	 * @param reqSpec
	 * @param endpointUrl
	 * @return
	 * @throws IOException
	 * LMS get call without request body and without parameter
	 */
	
	public static Response LMSdoGetCall(RequestSpecification reqSpec, String endpointUrl) throws IOException
	{
		HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wd", "urn:com.workday.report/IntuitIntegrationAdmin/IntuitGetWorkerSensitive_v1.1");
        
        HashMap<String, String> namespace = new HashMap<String, String>();
        namespaces.put("@odata", "$metadata#Users/$entity");
         XmlConfig xmlConf = new XmlConfig();
      //   readExcelsheet(Constant.CorpID_Path, sheet);
		Response response = given().spec(reqSpec).config(RestAssured.config().xmlConfig(xmlConf.declareNamespaces(namespace))).
				when().get(endpointUrl);
   
return response;
	}
	/**
	 * @author sbisoi
	 * @param reqSpec
	 * @param endpointUrl
	 * @param q
	 * @param query
	 * @return
	 * @throws IOException
	 * Use queryparameter such as SQL query in URL and fetch data
	 */
	
	public static Response b2bdoGetCall(RequestSpecification reqSpec, String endpointUrl,String q,String query) throws IOException
	{
		HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wd", "urn:com.workday.report/IntuitIntegrationAdmin/IntuitGetWorkerSensitive_v1.1");
        
        HashMap<String, String> namespace = new HashMap<String, String>();
        namespaces.put("totalSize", "1");
         XmlConfig xmlConf = new XmlConfig();
      //   readExcelsheet(Constant.CorpID_Path, sheet);
		Response response = given().spec(reqSpec).queryParameter(q,query).config(RestAssured.config().xmlConfig(xmlConf.declareNamespaces(namespace))).
				when().get(endpointUrl);
   
return response;
	}
	/**
	 * @author sbisoi
	 * @param reqSpec
	 * @param endpointUrl
	 * @param sheet
	 * @param r
	 * @param c
	 * @return
	 * @throws IOException
	 */
	
//	String path=payloadSchemaPath+"\\"+"upsertWDP.xml";
//	String actual=Utility.convertStringToXML(payloadSchemaPath, "upsertWDP.xml", "element", "Applicant_ID");
	public static Response GetCall(RequestSpecification reqSpec, String endpointUrl,String sheet,int r,int c) throws IOException
	{
		HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wd", "urn:com.workday.report/IntuitIntegrationAdmin/IntuitGetWorkerSensitive_v1.1");
        XmlConfig xmlConf = new XmlConfig();

		readExcelsheet(Constant.CorpID_Path, sheet);
		Response response = given().spec(reqSpec).params(TwogetRequestParameters(readStringCellValue(r, c))).config(RestAssured.config().xmlConfig(xmlConf.declareNamespaces(namespaces))).
				when().get(endpointUrl);
		return response;
	}
	
	public static Response GetCallFromXML(RequestSpecification reqSpec, String endpointUrl,String param) throws Exception{
	
		HashMap<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wd", "urn:com.workday.report/IntuitIntegrationAdmin/IntuitGetWorkerSensitive_v1.1");
        XmlConfig xmlConf = new XmlConfig();
        logger.info("getcall===========");
       // String actual=Utility.convertStringToXML(payloadSchemaPath, payloadname, element, tagname);
    	//String param="format=simplexml,"+tagname+"="+actual;
    	//System.out.println("param=" +param);
		Response response = given().spec(reqSpec).params(TwogetRequestParameters(param)).config(RestAssured.config().xmlConfig(xmlConf.declareNamespaces(namespaces))).
				when().get(endpointUrl);
		return response;
	}

	/**
	 * @author sbisoi
	 * @param jsonformat
	 * @param key
	 * @return
	 * @throws Exception
	 */
	
	public static String readJsonObject(String jsonformat,String key) throws Exception{
		
		JSONParser Parser=new JSONParser();
		Object obj=Parser.parse(jsonformat);
		JSONObject jobj=(JSONObject) obj;
		String token=(String) jobj.get(key);
		return token;
		
	}
	
	
}
