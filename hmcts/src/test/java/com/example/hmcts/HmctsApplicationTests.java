package com.example.hmcts;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@SelectPackages({
		"com.example.hmcts.controller",
		"com.example.hmcts.service",
		"com.example.hmcts.repository",
		"com.example.hmcts.integration"
})
@SpringBootTest
class HmctsApplicationTests {

	@Test
	void contextLoads() {
	}
}