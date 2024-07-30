package com.lxq.testOnline;


import org.springframework.boot.SpringBootConfiguration;
import org.springframework.util.StringUtils;
import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;

@SpringBootConfiguration
public class TestOnlineApplication {

	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("");
			System.out.println("=======================================================================");
			System.out.println("USAGE: java -jar -DDEBUG=false xxx.jar 'testng-xml-path' ['case-name']");
			System.out.println("=======================================================================");
			System.out.println("");
			return;
		}
		String xml = args[0];
		if (StringUtils.isEmpty(xml) || !xml.contains(".xml")) {
			System.out.println("");
			System.out.println("=========================");
			System.out.println("ERROR: testng xml is null, please append the testng xml path to jar cmd");
			System.out.println("=========================");
			System.out.println("");
			return;
		}

		if (args.length > 1) {
			String caseName = args[1];
			if (! StringUtils.isEmpty(caseName)) {
				System.setProperty("test_names_to_include", caseName);
				System.out.println("test_names_to_include: " + caseName);
			}
		}

		TestNG testNG = new TestNG();
		List<String> suites = new ArrayList<>();
		System.out.println("testng xml: " + xml);
		suites.add(xml);
		testNG.setTestSuites(suites);
		testNG.run();
	}

}
