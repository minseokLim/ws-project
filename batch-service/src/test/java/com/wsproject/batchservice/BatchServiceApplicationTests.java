package com.wsproject.batchservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.wsproject.batchservice.domain.enums.WsType;

@SpringBootTest
class BatchServiceApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(WsType.SELF_DEV ==WsType.valueOf("SELF_DEV"));
	}

}
