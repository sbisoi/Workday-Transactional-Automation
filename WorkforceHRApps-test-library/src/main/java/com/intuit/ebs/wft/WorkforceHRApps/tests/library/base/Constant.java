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
	public static final String AUTH_TOKEN_VPCE2E = "AUTH.TOKEN.VPCE2E";
	public static final String VPCPERF_URL = "VPCPERF.URL";
	public static final String AUTH_TOKEN_PERF = "AUTH.TOKEN.PERF";
	public static final String INTUIT2_URL = "INTUIT2.URL";
	public static final String AUTH_TOKEN_INTUIT2 = "AUTH.TOKEN.INTUIT2";
	public static final String GET_WORKER_SENSITIVE_ENDPOINT = "GET.WORKER.SENSITIVE.ENDPOINT";
	public static final String WD_GETWORKER_PARAM_CORPID = "WD.GETWORKER.PARAM.CORPID";

}
