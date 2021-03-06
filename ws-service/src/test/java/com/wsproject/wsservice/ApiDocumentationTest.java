package com.wsproject.wsservice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.GsonBuilder;
import com.wsproject.wsservice.domain.WsAdmin;
import com.wsproject.wsservice.domain.WsPrivate;
import com.wsproject.wsservice.domain.enums.WsType;
import com.wsproject.wsservice.dto.WsAdminRequestDto;
import com.wsproject.wsservice.dto.WsPrivateRequestDto;
import com.wsproject.wsservice.repository.WsAdminRepository;
import com.wsproject.wsservice.repository.WsPrivateRepository;

/**
 * API 문서화를 위한 Test Class
 * @author mslim
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiDocumentationTest {
	
	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private WsAdminRepository wsAdminRepository;
	
	@Autowired
	private WsPrivateRepository wsPrivateRepository;
	
	@Value("${spring.application.name}")
	private String applicationName;
	
	private MockMvc mockMvc;
	
	private RestDocumentationResultHandler document;
	
	private GsonBuilder gsonBuilder = new GsonBuilder();
	
	private static final String AUTH_HEADER 
		= "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
		+ ".eyJleHAiOjE1ODg3MjE4MjYsInVzZXJfbmFtZSI6IjEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl0sImp0aSI6ImNhZTI1NDI3LTRhZGEtNGIyMS04YTQyLTQ3ZWNkNjk4OTJjOSIsImNsaWVudF9pZCI6IndzLXByb2plY3QiLCJzY29wZSI6WyJtb2JpbGUiXX0"
		+ ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
	
	@Before // 매 테스트가 실행될 때마다 실행된다는 것에 주의해야한다.
	public void setup() throws Exception {
		this.document = document("{class-name}/{method-name}", preprocessResponse(prettyPrint()));
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
							.apply(documentationConfiguration(this.restDocumentation)
								.uris().withScheme("https").withHost("api.mslim8803.shop/api/" + applicationName).withPort(443))
							.alwaysDo(document)
							.build();
		
		setInitValues();
	}
	
	private void setInitValues() throws Exception {
		
		for(int i = 0; i < 3; i++) {
			String content = "실패하는 것은 곧 성공으로 한 발짝 더 나아가는 것이다.";
			String author = "메리 케이 애쉬";
			WsType type = WsType.SELF_DEV;
			
			setInitValue(content, author, type);
			
			content = "기운과 끈기는 모든 것을 이겨낸다.";
			author = "벤자민 프랭클린";
			type = WsType.SELF_DEV;
			
			setInitValue(content, author, type);
			
			content = "좋은 일을 많이 해내려고 기다리는 사람은 하나의 좋은 일도 해낼 수가 없다.";
			author = "사무엘 존슨";
			type = WsType.ETC;
			
			setInitValue(content, author, type);
		}
	}

	private void setInitValue(String content, String author, WsType type) throws Exception {
		WsAdmin wsAdmin = WsAdmin.builder().content(content).author(author).type(type).build();
		wsAdminRepository.save(wsAdmin);
		
		WsPrivate wsPrivate = WsPrivate.builder().content(content).author(author).type(type).ownerIdx(1L).build();
		wsPrivateRepository.save(wsPrivate);
	}
	
	/**
	 * Field Description 상수화
	 * @author mslim
	 *
	 */
	private static class Description {
		
		private static final String PAGE = "페이지 번호 (첫번째 페이지: 0)";
		private static final String SIZE = "페이지 사이즈";
		private static final String SORT = "정렬 방법 (예: sort=createdDate,asc -> createdDate 오름차순)";
		
		private static final String FIRST_HREF = "첫번째 페이지 조회 링크";
		private static final String LAST_HREF = "마지막 페이지 조회 링크";
		private static final String NEXT_HREF = "다음 페이지 조회 링크";
		private static final String PREV_HREF = "이전 페이지 조회 링크";
		private static final String SELF_HREF = "현재 페이지 조회 링크";
		
		private static final String PAGE_NUMBER = "현재 페이지 번호";
		private static final String PAGE_SIZE = "현재 페이지 사이즈";
		private static final String PAGE_TOTAL_ELEMENTS = "총 아이템 갯수";
		private static final String PAGE_TOTAL_PAGES = "총 페이지 수";
		
		private static final String ID = "Key value";
		private static final String CONTENT = "명언 내용";
		private static final String AUTHOR = "말한 사람";
		private static final String TYPE = "명언 종류 (SELF_DEV: 자기 개발, YOLO: YOLO, ETC: 기타)";
		private static final String CREATED_DATE = "생성 날짜";
		private static final String MODIFIED_DATE = "수정 날짜";
		private static final String SELF_DETAIL_HREF = "명언 상세 조회 링크";
		
		private static final String USER_IDX = "사용자별로 부여되는 고유한 Key Value";
		private static final String WS_ID = "명언 Key Value";
		private static final String PRIVATE_FLAG = "사용자가 등록한 명언인지의 여부 (true: 사용자 등록, false: 관리자 등록)";
		private static final String LIKED = "사용자가 해당 명언에 대해 좋아요를 눌렀는지의 여부";
		private static final String SEARCH = "검색조건. 'key=value' 형태. 파라미터가 2개 이상일 경우 AND 조건 (예: author=벤자민,type=SELF_DEV)";
	}
	
	@Test
	public void selectTodaysWs() throws Exception {
		mockMvc.perform(
					get("/v1.0/users/{userIdx}/todaysWs", 1)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					pathParameters(
						parameterWithName("userIdx").description(Description.USER_IDX)
					),
					responseFields(
						fieldWithPath("id").description(Description.ID),
						fieldWithPath("userIdx").description(Description.USER_IDX),
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE),
						fieldWithPath("wsId").description(Description.WS_ID),
						fieldWithPath("privateFlag").description(Description.PRIVATE_FLAG),
						fieldWithPath("liked").description(Description.LIKED),
						fieldWithPath("createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
						fieldWithPath("modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
						fieldWithPath("_links.self.href").description(Description.SELF_DETAIL_HREF)
					)
				))
				.andExpect(jsonPath("id", is(notNullValue())))
				.andExpect(jsonPath("userIdx", is(notNullValue())))
				.andExpect(jsonPath("content", is(notNullValue())))
				.andExpect(jsonPath("author", is(notNullValue())))
				.andExpect(jsonPath("type", is(notNullValue())))
				.andExpect(jsonPath("wsId", is(notNullValue())))
				.andExpect(jsonPath("privateFlag", is(notNullValue())))
				.andExpect(jsonPath("liked", is(notNullValue())))
				.andExpect(jsonPath("createdDate", is(notNullValue())))
				.andExpect(jsonPath("modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())));
	}
	
	@Test
	public void refreshTodaysWs() throws Exception {
		mockMvc.perform(
					post("/v1.0/users/{userIdx}/todaysWs", 1)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					pathParameters(
						parameterWithName("userIdx").description(Description.USER_IDX)
					),
					responseFields(
						fieldWithPath("id").description(Description.ID),
						fieldWithPath("userIdx").description(Description.USER_IDX),
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE),
						fieldWithPath("wsId").description(Description.WS_ID),
						fieldWithPath("privateFlag").description(Description.PRIVATE_FLAG),
						fieldWithPath("liked").description(Description.LIKED),
						fieldWithPath("createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
						fieldWithPath("modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
						fieldWithPath("_links.self.href").description(Description.SELF_DETAIL_HREF)
					)
				))
				.andExpect(jsonPath("id", is(notNullValue())))
				.andExpect(jsonPath("userIdx", is(notNullValue())))
				.andExpect(jsonPath("content", is(notNullValue())))
				.andExpect(jsonPath("author", is(notNullValue())))
				.andExpect(jsonPath("type", is(notNullValue())))
				.andExpect(jsonPath("wsId", is(notNullValue())))
				.andExpect(jsonPath("privateFlag", is(notNullValue())))
				.andExpect(jsonPath("liked", is(notNullValue())))
				.andExpect(jsonPath("createdDate", is(notNullValue())))
				.andExpect(jsonPath("modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())));
	}
	
	@Test
	public void insertLike() throws Exception {
		mockMvc.perform(
					post("/v1.0/users/{userIdx}/wses/{wsId}/like", 2, 1)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.param("privateFlag", "true"))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					pathParameters(
						parameterWithName("userIdx").description(Description.USER_IDX),
						parameterWithName("wsId").description(Description.WS_ID)
					),
					requestParameters(
						parameterWithName("privateFlag").description(Description.PRIVATE_FLAG)
					)
				));
	}
	
	@Test
	public void deleteLike() throws Exception {
		mockMvc.perform(
					post("/v1.0/users/{userIdx}/wses/{wsId}/like", 2, 5)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.param("privateFlag", "false"));
		
		mockMvc.perform(
					delete("/v1.0/users/{userIdx}/wses/{wsId}/like", 2, 5)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.param("privateFlag", "false"))
				.andDo(print())
				.andExpect(status().isNoContent())
				.andDo(document.document(
					pathParameters(
						parameterWithName("userIdx").description(Description.USER_IDX),
						parameterWithName("wsId").description(Description.WS_ID)
					),
					requestParameters(
						parameterWithName("privateFlag").description(Description.PRIVATE_FLAG)
					)
				));
	}

	@Test
	public void selectWsAdminList() throws Exception {
		mockMvc.perform(
					get("/v1.0/admin/wses")
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.queryParam("page", "1")
					.queryParam("size", "5")
					.queryParam("sort", "id,asc")
					.queryParam("search", "type=SELF_DEV")
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					requestParameters(
						parameterWithName("page").description(Description.PAGE),
						parameterWithName("size").description(Description.SIZE),
						parameterWithName("sort").description(Description.SORT),
						parameterWithName("search").description(Description.SEARCH)
					), 
					responseFields(
						fieldWithPath("_embedded.wsAdminResponseDtoList[].id").description(Description.ID),
						fieldWithPath("_embedded.wsAdminResponseDtoList[].content").description(Description.CONTENT),
						fieldWithPath("_embedded.wsAdminResponseDtoList[].author").description(Description.AUTHOR),
						fieldWithPath("_embedded.wsAdminResponseDtoList[].type").description(Description.TYPE),
						fieldWithPath("_embedded.wsAdminResponseDtoList[].createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
						fieldWithPath("_embedded.wsAdminResponseDtoList[].modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
						fieldWithPath("_embedded.wsAdminResponseDtoList[]._links.self.href").description(Description.SELF_DETAIL_HREF),
						fieldWithPath("_links.first.href").description(Description.FIRST_HREF),
						fieldWithPath("_links.last.href").description(Description.LAST_HREF),
						fieldWithPath("_links.prev.href").description(Description.PREV_HREF),
						fieldWithPath("_links.next.href").description(Description.NEXT_HREF),
						fieldWithPath("_links.self.href").description(Description.SELF_HREF),
//						fieldWithPath("_links.self.templated").ignored(),
						fieldWithPath("page.number").description(Description.PAGE_NUMBER),
						fieldWithPath("page.size").description(Description.PAGE_SIZE),
						fieldWithPath("page.totalElements").description(Description.PAGE_TOTAL_ELEMENTS),
						fieldWithPath("page.totalPages").description(Description.PAGE_TOTAL_PAGES)
					)
				))
				.andExpect(jsonPath("_embedded.wsAdminResponseDtoList[0].id", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsAdminResponseDtoList[0].content", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsAdminResponseDtoList[0].author", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsAdminResponseDtoList[0].type", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsAdminResponseDtoList[0].createdDate", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsAdminResponseDtoList[0].modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsAdminResponseDtoList[0]._links.self.href", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())))
				.andExpect(jsonPath("page", is(notNullValue())));
	}
	
	@Test
	public void selectWsAdmin() throws Exception {
		mockMvc.perform(
					get("/v1.0/admin/wses/{id}", 1)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
							parameterWithName("id").description(Description.ID)
						),
						responseFields(
							fieldWithPath("id").description(Description.ID),
							fieldWithPath("content").description(Description.CONTENT),
							fieldWithPath("author").description(Description.AUTHOR),
							fieldWithPath("type").description(Description.TYPE),
							fieldWithPath("createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
							fieldWithPath("modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
							fieldWithPath("_links.self.href").description(Description.SELF_DETAIL_HREF)
						)
				))
				.andExpect(jsonPath("id", is(notNullValue())))
				.andExpect(jsonPath("content", is(notNullValue())))
				.andExpect(jsonPath("author", is(notNullValue())))
				.andExpect(jsonPath("type", is(notNullValue())))
				.andExpect(jsonPath("createdDate", is(notNullValue())))
				.andExpect(jsonPath("modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())));
	}
	
	@Test
	public void insertWsAdmin() throws Exception {
		
		WsAdminRequestDto ws = WsAdminRequestDto.builder().content("발견은 준비된 사람이 맞닥뜨린 우연이다.").author("알버트 센트 디외르디").type(WsType.SELF_DEV).build();
		
		String jsonString = gsonBuilder.setPrettyPrinting().create().toJson(ws);
		
		mockMvc.perform(
					post("/v1.0/admin/wses")
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonString)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
					requestFields(
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE)
					),
					responseFields(
						fieldWithPath("id").description(Description.ID),
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE),
						fieldWithPath("createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
						fieldWithPath("modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
						fieldWithPath("_links.self.href").description(Description.SELF_DETAIL_HREF)
					)
				))
				.andExpect(jsonPath("id", is(notNullValue())))
				.andExpect(jsonPath("content", is(notNullValue())))
				.andExpect(jsonPath("author", is(notNullValue())))
				.andExpect(jsonPath("type", is(notNullValue())))
				.andExpect(jsonPath("createdDate", is(notNullValue())))
				.andExpect(jsonPath("modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())));
	}
	
	@Test
	public void updateWsAdmin() throws Exception {
		WsAdminRequestDto ws = WsAdminRequestDto.builder().content("수정된 명언 내용이다.").author("수정된 말한 사람").type(WsType.YOLO).build();
		
		String jsonString = gsonBuilder.setPrettyPrinting().create().toJson(ws);
		
		mockMvc.perform(
					put("/v1.0/admin/wses/{id}", 1)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonString)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					pathParameters(
						parameterWithName("id").description(Description.ID)
					),
					requestFields(
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE)
					),
					responseFields(
						fieldWithPath("id").description(Description.ID),
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE),
						fieldWithPath("createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
						fieldWithPath("modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
						fieldWithPath("_links.self.href").description(Description.SELF_DETAIL_HREF)
					)
				))
				.andExpect(jsonPath("id", is(notNullValue())))
				.andExpect(jsonPath("content", is(notNullValue())))
				.andExpect(jsonPath("author", is(notNullValue())))
				.andExpect(jsonPath("type", is(notNullValue())))
				.andExpect(jsonPath("createdDate", is(notNullValue())))
				.andExpect(jsonPath("modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())));
	}
	
	@Test
	public void deleteWsAdmin() throws Exception {
		mockMvc.perform(
					delete("/v1.0/admin/wses/{id}", 2)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent())
				.andDo(document.document(
					pathParameters(
						parameterWithName("id").description(Description.ID)
					)
		));
	}
	
	@Test
	public void selectWsPrivateList() throws Exception {
		mockMvc.perform(
					get("/v1.0/users/{userIdx}/wses", 1)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.queryParam("page", "1")
					.queryParam("size", "5")
					.queryParam("sort", "id,desc")
					.queryParam("search", "type=SELF_DEV")
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					pathParameters(
						parameterWithName("userIdx").description(Description.USER_IDX)
					),
					requestParameters(
						parameterWithName("page").description(Description.PAGE),
						parameterWithName("size").description(Description.SIZE),
						parameterWithName("sort").description(Description.SORT),
						parameterWithName("search").description(Description.SEARCH)
					), 
					responseFields(
						fieldWithPath("_embedded.wsPrivateResponseDtoList[].id").description(Description.ID),
						fieldWithPath("_embedded.wsPrivateResponseDtoList[].content").description(Description.CONTENT),
						fieldWithPath("_embedded.wsPrivateResponseDtoList[].author").description(Description.AUTHOR),
						fieldWithPath("_embedded.wsPrivateResponseDtoList[].type").description(Description.TYPE),
						fieldWithPath("_embedded.wsPrivateResponseDtoList[].ownerIdx").description(Description.USER_IDX),
						fieldWithPath("_embedded.wsPrivateResponseDtoList[].liked").description(Description.LIKED),
						fieldWithPath("_embedded.wsPrivateResponseDtoList[].createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
						fieldWithPath("_embedded.wsPrivateResponseDtoList[].modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
						fieldWithPath("_embedded.wsPrivateResponseDtoList[]._links.self.href").description(Description.SELF_DETAIL_HREF),
						fieldWithPath("_links.first.href").description(Description.FIRST_HREF),
						fieldWithPath("_links.last.href").description(Description.LAST_HREF),
						fieldWithPath("_links.prev.href").description(Description.PREV_HREF),
						fieldWithPath("_links.next.href").description(Description.NEXT_HREF),
						fieldWithPath("_links.self.href").description(Description.SELF_HREF),
//						fieldWithPath("_links.self.templated").ignored(),
						fieldWithPath("page.number").description(Description.PAGE_NUMBER),
						fieldWithPath("page.size").description(Description.PAGE_SIZE),
						fieldWithPath("page.totalElements").description(Description.PAGE_TOTAL_ELEMENTS),
						fieldWithPath("page.totalPages").description(Description.PAGE_TOTAL_PAGES)
					)
				))
				.andExpect(jsonPath("_embedded.wsPrivateResponseDtoList[0].id", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsPrivateResponseDtoList[0].content", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsPrivateResponseDtoList[0].author", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsPrivateResponseDtoList[0].type", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsPrivateResponseDtoList[0].ownerIdx", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsPrivateResponseDtoList[0].liked", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsPrivateResponseDtoList[0].createdDate", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsPrivateResponseDtoList[0].modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_embedded.wsPrivateResponseDtoList[0]._links.self.href", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())))
				.andExpect(jsonPath("page", is(notNullValue())));
	}
	
	@Test
	public void selectWsPrivate() throws Exception {
		mockMvc.perform(
					get("/v1.0/users/{userIdx}/wses/{id}", 1, 1)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
						pathParameters(
							parameterWithName("userIdx").description(Description.USER_IDX),
							parameterWithName("id").description(Description.ID)
						),
						responseFields(
							fieldWithPath("id").description(Description.ID),
							fieldWithPath("content").description(Description.CONTENT),
							fieldWithPath("author").description(Description.AUTHOR),
							fieldWithPath("type").description(Description.TYPE),
							fieldWithPath("ownerIdx").description(Description.USER_IDX),
							fieldWithPath("liked").description(Description.LIKED),
							fieldWithPath("createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
							fieldWithPath("modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
							fieldWithPath("_links.self.href").description(Description.SELF_DETAIL_HREF)
						)
				))
				.andExpect(jsonPath("id", is(notNullValue())))
				.andExpect(jsonPath("content", is(notNullValue())))
				.andExpect(jsonPath("author", is(notNullValue())))
				.andExpect(jsonPath("type", is(notNullValue())))
				.andExpect(jsonPath("ownerIdx", is(notNullValue())))
				.andExpect(jsonPath("liked", is(notNullValue())))
				.andExpect(jsonPath("createdDate", is(notNullValue())))
				.andExpect(jsonPath("modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())));
	}
	
	@Test
	public void insertWsPrivate() throws Exception {
		
		WsPrivateRequestDto ws = WsPrivateRequestDto.builder().content("발견은 준비된 사람이 맞닥뜨린 우연이다.").author("알버트 센트 디외르디").type(WsType.SELF_DEV).build();
		
		String jsonString = gsonBuilder.setPrettyPrinting().create().toJson(ws);
		
		mockMvc.perform(
					post("/v1.0/users/{userIdx}/wses", 1)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonString)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andDo(document.document(
					pathParameters(
						parameterWithName("userIdx").description(Description.USER_IDX)
					),
					requestFields(
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE)
					),
					responseFields(
						fieldWithPath("id").description(Description.ID),
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE),
						fieldWithPath("ownerIdx").description(Description.USER_IDX),
						fieldWithPath("liked").description(Description.LIKED),
						fieldWithPath("createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
						fieldWithPath("modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
						fieldWithPath("_links.self.href").description(Description.SELF_DETAIL_HREF)
					)
				))
				.andExpect(jsonPath("id", is(notNullValue())))
				.andExpect(jsonPath("content", is(notNullValue())))
				.andExpect(jsonPath("author", is(notNullValue())))
				.andExpect(jsonPath("type", is(notNullValue())))
				.andExpect(jsonPath("ownerIdx", is(notNullValue())))
				.andExpect(jsonPath("liked", is(notNullValue())))
				.andExpect(jsonPath("createdDate", is(notNullValue())))
				.andExpect(jsonPath("modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())));
	}
	
	@Test
	public void updateWsPrivate() throws Exception {
		WsPrivateRequestDto ws = WsPrivateRequestDto.builder().content("수정된 명언 내용이다.").author("수정된 말한 사람").type(WsType.YOLO).build();
		
		String jsonString = gsonBuilder.setPrettyPrinting().create().toJson(ws);
		
		mockMvc.perform(
					put("/v1.0/users/{userIdx}/wses/{id}", 1, 1)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonString)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					pathParameters(
						parameterWithName("userIdx").description(Description.USER_IDX),
						parameterWithName("id").description(Description.ID)
					),
					requestFields(
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE)
					),
					responseFields(
						fieldWithPath("id").description(Description.ID),
						fieldWithPath("content").description(Description.CONTENT),
						fieldWithPath("author").description(Description.AUTHOR),
						fieldWithPath("type").description(Description.TYPE),
						fieldWithPath("ownerIdx").description(Description.USER_IDX),
						fieldWithPath("liked").description(Description.LIKED),
						fieldWithPath("createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
						fieldWithPath("modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE),
						fieldWithPath("_links.self.href").description(Description.SELF_DETAIL_HREF)
					)
				))
				.andExpect(jsonPath("id", is(notNullValue())))
				.andExpect(jsonPath("content", is(notNullValue())))
				.andExpect(jsonPath("author", is(notNullValue())))
				.andExpect(jsonPath("type", is(notNullValue())))
				.andExpect(jsonPath("ownerIdx", is(notNullValue())))
				.andExpect(jsonPath("liked", is(notNullValue())))
				.andExpect(jsonPath("createdDate", is(notNullValue())))
				.andExpect(jsonPath("modifiedDate", is(notNullValue())))
				.andExpect(jsonPath("_links", is(notNullValue())));
	}
	
	@Test
	public void deleteWsPrivate() throws Exception {
		mockMvc.perform(
					delete("/v1.0/users/{userIdx}/wses/{id}", 1, 2)
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNoContent())
				.andDo(document.document(
					pathParameters(
						parameterWithName("userIdx").description(Description.USER_IDX),
						parameterWithName("id").description(Description.ID)
					)
		));
	}
}
