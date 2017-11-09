package com.intuit.ebs.wft.WorkforceHRApps.tests.library.base;
/**
 * @author spoddar
 * Consolidates all the constants into 1 file for code reuse.
 */
public class Constant {

	/**
	 * Properties
	 */
	public static final String PROPERTIES_FILE = "/workday.properties";
	public static final String HTTP_PROPERTY_BASEURL = "http.baseurl";
	
	public static final String TEST_ENV = "testenv";
	
	/** @author asiji
     * adding environment specific path
	 */
	public static final String ENVIRONMENT_1 = "workday.environment1.payloadpath";
	public static final String ENVIRONMENT_2 = "workday.environment2.payloadpath";    
	public static final String WORKDAY_PROPERTY_ENV_1_BASEPATH = "workday.environment1.basepath";
	public static final String WORKDAY_PROPERTY_ENV_2_BASEPATH = "workday.environment2.basepath";
	 
	
	/** @author asiji
	 * adding integrations payloads - eibs and core connectors
	*/
	
	public static final String BEST_DOC = "workday.eib.BestDoc";
	public static final String CALLIDUS    = "workday.eib.Callidus";
	public static final String CAN_TRS_ENWISEN = "workday.eib.CANTRSEnwisen";
	public static final String CCMSI = "workday.eib.CCMSI";
	public static final String PRO_BUSINESS_TERM_RPT = "workday.eib.ProBusinessTermRpt";
	public static final String SAILPOINT = "workday.eib.Sailpoint";
	public static final String SUN_LIFE_RRSP_EMPLOYEE_DATA = "workday.eib.SunLifeRRSPEmployeeData";
	public static final String TOTAL_REWARDS_STATEMENT = "workday.eib.TotalRewardsStatement";
	public static final String US_CURRENT_PERIOD_VACATION_ACCRUALS = "workday.eib.USCurrentPeriodVacationAccruals";
	public static final String WDP_SUPERVISORY_ORG_MASTER_EXTRACT = "workday.eib.WDPSupervisoryOrgMasterExtract";
	public static final String ASPECT_PTO_BALANCES = "workday.eib.AspectPTOBalances";
	public static final String ADP_US_NON_EXEMPT_SICK_BALANCE = "workday.eib.ADPUSNonExemptSickBalance";
	public static final String ADP_US_EXEMPT_SICK_BALANCE = "workday.eib.ADPUSExemptSickBalance";
	public static final String ADP_CAN_ADDL = "workday.eib.ADPCANAddl";
	public static final String NH_SURVEY_WFR_QUALTRICS = "workday.eib.NHSurveyWFRQualtrics";
	public static final String NH_SURVEY_RT_QUALTRICS = "workday.eib.NHSurveyRTQualtrics";
	
	public static final String ADP_GLOBE_ONLINE_AUS = "workday.coreconnector.ADPGlobeOnlineAUS";
	public static final String ADP_GLOBE_ONLINE_DEU = "workday.coreconnector.ADPGlobeOnlineDEU";
	public static final String ADP_GLOBE_ONLINE_ISR = "workday.coreconnector.ADPGlobeOnlineISR";
	public static final String ADP_GLOBE_ONLINE_SGP = "workday.coreconnector.ADPGlobeOnlineSGP";
	public static final String ADP_GLOBE_ONLINE_IND = "workday.coreconnector.ADPGlobeOnlineIND";
	public static final String ADP_PAY_AT_WORK = "workday.coreconnector.ADPPayAtWork";
	public static final String ADP_PAY_AT_WORK_OTP = "workday.coreconnector.ADPPayAtWorkOTP";
	public static final String ADP_PRO_BUSINESS = "workday.coreconnector.ADPProBusiness";
	public static final String ADP_PRO_BUSINESS_OTP = "workday.coreconnector.ADPProBusinessOTP";
	public static final String GLOBOFORCE = "workday.coreconnector.Globoforce";
	public static final String PAYROLL_PREVIEW = "workday.coreconnector.PayrollPreview";
	public static final String ADP_PAY_AT_WORK_SALARY_OT_PAYROLL_EXPORT = "workday.coreconnector.ADPPayAtWorkSalaryOTPayrollExport";

	// File will be loaded by WorkforceHRApps-test-runner thats why ../ to get
	// correct relative path
	public static final String DATA_FILE_LOCATION_E2E = "../WorkforceHRApps-service-tests/src/main/resources/e2e/";
	public static final String PAYLOAD_SCHEMA_LOCATION = "../WorkforceHRApps-service-tests/src/main/resources/testdata/payloadSchema";
	public static final String TEST_FILE_LOCATION = "../WorkforceHRApps-service-tests/src/main/resources/testdata/testFile";
	
	public static final String VPCE2E_URL = "VPCE2E.URL";
	public static final String QA_URL = "VPCQA.URL";
	public static final String AUTH_TOKEN_VPCE2E = "AUTH.TOKEN.VPCE2E";
	public static final String QA_AUTH = "AUTH.TOKEN.VPCQA";
	public static final String LOCALPERF_URL = "LOCAL.PERF.URL";
	public static final String AUTH_TOKEN_PERF = "AUTH.TOKEN.PERF";
	public static final String ATOM_CLOUD_URL= "Test.Atom.Cloud.URL";
	public static final String AUTH_TOKEN_ATOM_CLOUD = "AUTH.Token.Atom.Cloud";
	
	public static final String INTUIT2_URL = "INTUIT2.URL";
	public static final String AUTH_TOKEN_INTUIT2 = "AUTH.TOKEN.INTUIT2";
	public static final String GET_WORKER_SENSITIVE_ENDPOINT = "GET.WORKER.SENSITIVE.ENDPOINT";
	public static final String WD_GETWORKER_PARAM_CORPID = "WD.GETWORKER.PARAM.CORPID";
    public static final String CorpID_Path=System.getProperty("user.dir")+"/src/main/resources/testdata/corpFile/CORP_ID.xlsx";
    public static final String LMS_POST_URL="LMS.POST.URL";
    public static final String LMS_POST_AUTH="LMS.AUTH";
    public static final String LMS_File_Path=System.getProperty("user.dir")+"/src/main/resources/testdata/lms";
    public static final String LMS_GETWORKER_DETAIL="LMS.GET.URL";
    public static final String WES_AUTH="Intuit_IAM_Authentication intuit_appid=ebs.workforce.wes.automation,intuit_app_secret=preprdtetQd9VNGD0M6aLP3QNfssZ1mLmlqRxHXG";
    public static final String WES_BASE_URL="https://workforce-e2e.platform.intuit.com/ws/api/v1/workers/";
    public static final String TALEO_URL="TALEO.URL";
    public static final String TALEO_AUTH="TALEO.AUTH";
    public static final String taleo_File_Path=System.getProperty("user.dir")+"/src/main/resources/testdata/taleo";
    public static final String db_Url="dbUrl";
    public static final String db_id="dbid";
    public static final String db_password="dbpassword";
    public static final String B2B_File_Path=System.getProperty("user.dir")+"/src/main/resources/testdata/b2b";
    public static final String B2C_File_Path=System.getProperty("user.dir")+"/src/main/resources/testdata/b2c";
    public static final String b2b_url="b2bendpointurl";
    public static final String b2c_url="b2cEndpointurl";
    public static final String B2B_GET_ID_URL="B2B.GET.ID.URL";
    public static final String B2B_DATA_URL="B2B.GET.DATA.URL";
    public static final String B2C_DATA_URL="B2C.GET.DATA.URL";
    public static final String B2C_GET_ID_URL="B2C.GET.ID.URL";
    public static final String Get_worker_url="Get_worker_url";
    public static final String Get_worker_auth="Get_worker_auth";
    public static final String GET_WORKER_QA_URL="GET_WORKER_QA_URL";
    public static final String GET_WORKER_QA_AUTH="GET_WORKER_QA_AUTH";
    public static final String OIM_URL="OIM_URL";
    public static final String OIM_AUTH="OIM_AUTH";
}
