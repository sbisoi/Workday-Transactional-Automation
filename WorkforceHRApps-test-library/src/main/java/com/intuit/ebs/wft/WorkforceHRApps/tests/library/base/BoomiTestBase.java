package com.intuit.ebs.wft.WorkforceHRApps.tests.library.base;
/**
 * @author spoddar
 */
import org.testng.annotations.BeforeClass;

import com.intuit.tools.commontestbase.service.BaseConstant;
import com.intuit.tools.commontestbase.service.ServiceTestBase;
import com.jayway.restassured.builder.RequestSpecBuilder;

public class BoomiTestBase extends ServiceTestBase{
	
	public RequestSpecBuilder reqSpecBuilder;
	
	public BoomiTestBase() {
        super(Constant.PROPERTIES_FILE);
    }
	
	@BeforeClass
    public void setUpTestConfig() {
    	
    	//BaseConstant.{PropertyName} gets the data from /default/workday.properties
        logger.info("targetenv = " + getConfigManager().getConfig(BaseConstant.ENVIRONMENT_PROPERTY));
        logger.info("VPCE2E.URL = " + getConfigManager().getConfig(Constant.VPCE2E_URL));
        logger.info("INTUIT2.URL = " + getConfigManager().getConfig(Constant.INTUIT2_URL));
        logger.info("configDir = " + configDir);

        // Tests setup
        // requestSpecBuilder can be configured more by each test class, this is the more generic
        // setup required by all testsuites and can be overwritten as well.
        requestSpecBuilder = requestSpecBuilder.
                setBaseUri(getConfigManager().getConfig(Constant.VPCE2E_URL));
        
        reqSpecBuilder = new RequestSpecBuilder();
        reqSpecBuilder = reqSpecBuilder.setBaseUri(getConfigManager().getConfig(Constant.INTUIT2_URL));
        
    }

}
