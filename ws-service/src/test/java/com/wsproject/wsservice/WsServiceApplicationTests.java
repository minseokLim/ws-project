package com.wsproject.wsservice;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wsproject.wsservice.controller.WsController;
import com.wsproject.wsservice.controller.WsPersonalController;
import com.wsproject.wsservice.domain.Ws;
import com.wsproject.wsservice.domain.enums.WsType;


@SpringBootTest
class WsServiceApplicationTests {
	
	@Autowired
	private WsController wsController;
	
	@Autowired
	private WsPersonalController wsPersonalController;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private MockMvc mockMvc;
	
	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(wsController, wsPersonalController).build();
	}
	
	@Test
	void contextLoads() throws Exception {
		Ws ws = Ws.builder().content("Let it be").author("Beatles").type(WsType.ETC).build();
		String jsonStr = mockMvc.perform(post("/v1.0/api/wses").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(ws))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
		Long id = objectMapper.readValue(jsonStr, Ws.class).getId();
		
		jsonStr = mockMvc.perform(get("/v1.0/api/wses/" + id).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
		Ws saved = objectMapper.readValue(jsonStr, Ws.class);
		LocalDateTime createdDate = saved.getCreatedDate();
		LocalDateTime modifiedDate = saved.getModifiedDate();
		
		assertTrue("Beatles".equals(saved.getAuthor()));
		
		Ws newWs = Ws.builder().content("Hey Jude").author("Beatles").type(WsType.ETC).build();
		saved.update(newWs);
		
		mockMvc.perform(put("/v1.0/api/wses/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(saved))).andExpect(status().isOk());
		
		jsonStr = mockMvc.perform(get("/v1.0/api/wses/" + id).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
		Ws updated = objectMapper.readValue(jsonStr, Ws.class);
		
		assertTrue("Hey Jude".equals(updated.getContent()));
		assertTrue(createdDate.equals(updated.getCreatedDate()));
		assertFalse(modifiedDate.equals(updated.getModifiedDate()));
	}
}
