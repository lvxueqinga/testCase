package com.lxq.testOnline.testng.commonDataStructure;

import org.slf4j.Logger;

public class LogMine {

    private final Logger logger;

    public LogMine(Logger logger) {

        this.logger = logger;
    }

    public void printImportant(String info) {
        logger.info("");
        logger.info("");
        logger.info(info);
        logger.info("");
        logger.info("");
    }

    public void printImportantError(String info) {
        logger.info("");
        logger.info("");
        logger.error(info);
        logger.info("");
        logger.info("");
    }

    public void info(String info) {
        logger.info(info);
    }

    public void error(String info) {
        logger.error(info);
    }

    public void debug(String info) {
        logger.debug(info);
    }

    public void warn(String info) {
        logger.warn(info);
    }

    public void logCase(String info) {
        logger.info("==================================");
        logger.info("[CASE]" + info);
        logger.info("==================================");
    }

    public void logCaseStart(String caseName) {
        logger.info("==================================");
        logger.info("[CASE-START] " + caseName);
        logger.info("==================================");
    }

    public void logCaseEnd(boolean isSuccess, String caseName) {
        String result = "FAIL";
        if (isSuccess) {
            result = "PASS";
        }
        logger.info("[CASE-END]" + "[" + result + "] " + caseName);
    }

    public void logStep(String info) {
        logger.info("");
        logger.info(">>>>>>step: " + info);
    }

    public void info(String var1, Object... var2) {
        logger.info(var1, var2);
    }
}
