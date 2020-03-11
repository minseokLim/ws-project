package com.wsproject.authsvr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class AuthsvrApplicationTests {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	void test() throws Exception {
		System.out.println("encoded : " + passwordEncoder.encode("ZR5Znvq2KZ8Ld4hdyZgrDHOPqJdZq9ju"));
	}
}
