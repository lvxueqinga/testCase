package com.lxq.testOnline.testng.commonCase;



import com.lxq.testOnline.testng.commonDataStructure.LogMine;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class TestCaseCommon {

    public LogMine logger = new LogMine(LoggerFactory.getLogger(this.getClass()));
    public String response = "";
    public String authorization = "";
    public Map<String, String> HEADERS = new ConcurrentHashMap<>();

    private final String AUTHORIZATION = "Authorization";

    private boolean DEBUG = Boolean.parseBoolean(System.getProperty("DEBUG", "true"));



    public TestCaseCommon() {

    }

    @AfterSuite
    public void afterSuite() {



    }

    @BeforeSuite
    public void beforeSuite() {

        initialDB();
    }

    public void initialDB() {


    }



}
