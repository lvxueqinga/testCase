package com.lxq.testOnline.testng.V1;

import com.google.common.base.Preconditions;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class test1 {

    @Test
    public void test1(){
        System.out.println("test1 run");
        Preconditions.checkArgument(1==1,"1!=1");
    }

    @Test
    public void test2(){
        System.out.println("test2 run");
        Preconditions.checkArgument(1==2,"1==2");
    }
    @Test
    public void test3(){
        System.out.println("test3 run");
        Reporter.log("这是我自己的日志");

        throw new RuntimeException("这是我自己的运行时异常");

    }

}
