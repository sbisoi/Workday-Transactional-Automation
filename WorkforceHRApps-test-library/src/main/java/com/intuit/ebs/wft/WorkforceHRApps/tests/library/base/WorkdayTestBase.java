package com.intuit.ebs.wft.WorkforceHRApps.tests.library.base;
/**
 * @author spoddar
 */
import org.testng.annotations.BeforeClass;

import com.intuit.ebs.wft.WorkforceHRApps.tests.library.base.Constant;
import com.intuit.tools.commontestbase.service.BaseConstant;
import com.intuit.tools.commontestbase.service.ServiceTestBase;

public class WorkdayTestBase extends ServiceTestBase{
	
	public WorkdayTestBase() {
        super(Constant.PROPERTIES_FILE);
    }


    @BeforeClass
    public void setUpTestConfig() {
    	
    	//BaseConstant.{PropertyName} gets the data from /default/workday.properties
        logger.info("targetenv = " + getConfigManager().getConfig(BaseConstant.ENVIRONMENT_PROPERTY));
        logger.info("http.baseurl = " + getConfigManager().getConfig(BaseConstant.HTTP_PROPERTY_BASEURL));
        logger.info("configDir = " + configDir);

        // Tests setup
        // Removing RestAssured.baseURI, RestAssured.basePath, RestAssured.port as these
        // don't work in 2.4.0. Use requestSpec instead.
        // requestSpecBuilder can be configured more by each test class, this is the more generic
        // setup required by all testsuites and can be overwritten as well.
        /*requestSpecBuilder = requestSpecBuilder.
                setBaseUri(getConfigManager().getConfig(BaseConstant.HTTP_PROPERTY_BASEURL));*/
    }

}
