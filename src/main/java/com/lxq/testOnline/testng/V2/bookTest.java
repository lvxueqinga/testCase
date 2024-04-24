package com.lxq.testOnline.testng.V2;

import com.alibaba.fastjson.JSONObject;
import com.lxq.testOnline.testng.commonCase.TestCaseCommon;

import org.testng.annotations.Test;



public class bookTest extends TestCaseCommon {
    ScenarioUtil book = ScenarioUtil.getInstance();

    @Test
    public void chkGetBookList(){
        JSONObject response = book.getbook(1,20);
        System.out.println(response.toString());
    }

    @Test
    public void chkEditBook() {

        JSONObject response = null;
        try {
            response = book.editBook("autoFM","auto","10");
            System.out.println("---------"+response.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
