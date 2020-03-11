package com.wsproject.wsservice;

import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class WsServiceApplicationTests {
	
//	@Autowired
//	private WsController wsController;
//	
//	@Autowired
//	private WsPersonalController wsPersonalController;
//	
//	@Autowired
//	private ObjectMapper objectMapper;
//	
//	private MockMvc mockMvc;
//	
//	@BeforeEach
//	public void setup() {
//		mockMvc = MockMvcBuilders.standaloneSetup(wsController, wsPersonalController).build();
//	}
//	
//	@Test
//	void contextLoads() throws Exception {
//		Ws ws = Ws.builder().content("Let it be").author("Beatles").type(WsType.ETC).build();
//		String jsonStr = mockMvc.perform(post("/v1.0/wses").contentType(MediaType.APPLICATION_JSON)
//				.content(objectMapper.writeValueAsString(ws))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
//		Long id = objectMapper.readValue(jsonStr, Ws.class).getId();
//		
//		jsonStr = mockMvc.perform(get("/v1.0/wses/" + id).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
//		Ws saved = objectMapper.readValue(jsonStr, Ws.class);
//		LocalDateTime createdDate = saved.getCreatedDate();
//		LocalDateTime modifiedDate = saved.getModifiedDate();
//		
//		assertTrue("Beatles".equals(saved.getAuthor()));
//		
//		Thread.sleep(1000);
//		Ws newWs = Ws.builder().content("Hey Jude").author("Beatles").type(WsType.ETC).build();
//		saved.update(newWs);
//		
//		mockMvc.perform(put("/v1.0/wses/" + id).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(saved))).andExpect(status().isOk());
//		
//		jsonStr = mockMvc.perform(get("/v1.0/wses/" + id).accept(MediaType.APPLICATION_JSON)).andReturn().getResponse().getContentAsString();
//		Ws updated = objectMapper.readValue(jsonStr, Ws.class);
//		
//		assertTrue("Hey Jude".equals(updated.getContent()));
//		assertTrue(createdDate.equals(updated.getCreatedDate()));
//		assertFalse(modifiedDate.equals(updated.getModifiedDate()));
//	}
}
