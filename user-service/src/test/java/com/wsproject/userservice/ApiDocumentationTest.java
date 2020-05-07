package com.wsproject.userservice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

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

import com.wsproject.userservice.domain.User;
import com.wsproject.userservice.domain.enums.RoleType;
import com.wsproject.userservice.domain.enums.SocialType;
import com.wsproject.userservice.repository.UserRepository;

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
	private UserRepository userRepository;
	
	@Value("${spring.application.name}")
	private String applicationName;
	
	private MockMvc mockMvc;
	
	private RestDocumentationResultHandler document;
	
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
		for(int i = 0; i < 5; i++) {
			User user = User.builder().name("임민석 " + i)
									.email("mslim" + i + "@naver.com")
									.principal(String.valueOf(Math.abs(new Random().nextLong())))
									.socialType(SocialType.GITHUB)
									.pictureUrl("https://www.mslim8803.shop/images/profile.png")
									.roles(Collections.singletonList(RoleType.USER.getValue()))
									.build();
			
			userRepository.save(user);
		}
	}
	
	/**
	 * Field Description 상수화
	 * @author mslim
	 *
	 */
	private static class Description {
		private static final String USER_IDX = "사용자별로 부여되는 고유한 Key Value";
		private static final String NAME = "사용자 이름";
		public static final String EMAIL = "사용자 e-mail 주소";
		public static final String PRINCIPAL = "Social Service (카카오, 구글 등) 별로 주어지는 고유한 Key Value";
		public static final String SOCIAL_TYPE = "Social Service 종류 (FACEBOOK, GOOGLE, KAKAO, GITHUB). FACEBOOK은 정책상 문제로 현재 사용하지 않고 있음";
		public static final String PICTURE_URL = "사용자 프로필 사진 url";
		public static final String UID = "사용자별로 Unique한 ID (현재는 사용하지 않고 있음)";
		public static String AUTHORITY = "권한 리스트 (ROLE_USER, ROLE_ADMIN)";
		private static final String CREATED_DATE = "생성 날짜";
		private static final String MODIFIED_DATE = "수정 날짜";
	}
	
	@Test
	public void selectLoginUser() throws Exception {
		
		mockMvc.perform(
					get("/v1.0/users/me")
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER)
					.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					responseFields(
						fieldWithPath("idx").type(Long.class).description(Description.USER_IDX),
						fieldWithPath("name").description(Description.NAME),
						fieldWithPath("email").description(Description.EMAIL),
						fieldWithPath("principal").description(Description.PRINCIPAL),
						fieldWithPath("socialType").description(Description.SOCIAL_TYPE),
						fieldWithPath("pictureUrl").description(Description.PICTURE_URL),
						fieldWithPath("uid").description(Description.UID),
						fieldWithPath("roles").description(Description.AUTHORITY),
						fieldWithPath("authorities[].authority").description(Description.AUTHORITY),
						fieldWithPath("createdDate").type(LocalDateTime.class).description(Description.CREATED_DATE),
						fieldWithPath("modifiedDate").type(LocalDateTime.class).description(Description.MODIFIED_DATE)
					)
				))
				.andExpect(jsonPath("idx", is(notNullValue())))
				.andExpect(jsonPath("name", is(notNullValue())))
				.andExpect(jsonPath("principal", is(notNullValue())))
				.andExpect(jsonPath("roles", is(notNullValue())))
				.andExpect(jsonPath("createdDate", is(notNullValue())))
				.andExpect(jsonPath("modifiedDate", is(notNullValue())));
	}
	
	@Test
	public void getMaxUserIdx() throws Exception {
		mockMvc.perform(
					get("/v1.0/users/maxIdx")
					.header(HttpHeaders.AUTHORIZATION, AUTH_HEADER))
				.andDo(print())
				.andExpect(status().isOk());
	}
}
