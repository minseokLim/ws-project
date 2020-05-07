package com.wsproject.authsvr;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.GsonBuilder;

/**
 * 실제 인증 후 Test를 실행할 수는 없으므로 MockBean을 이용해 리스판스를 조작<br>
 * API 문서화만을 위한 Class로 Test로서의 의미는 전혀 없음
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
	
	@Value("${spring.application.name}")
	private String applicationName;
	
	@MockBean
	private TokenEndpoint tokenEndpoint;
	
	@MockBean
	private AuthorizationEndpoint authorizationEndpoint;
	
	private MockMvc mockMvc;
	
	private RestDocumentationResultHandler document;
	
	private GsonBuilder gsonBuilder = new GsonBuilder();
	
	private static final String AUTHORIZATION = "Basic " + new String(Base64.encodeBase64("clientId:clientSecret".getBytes()));
	
	private static final String ACCESS_TOKEN_EXAMPLE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
			+ ".eyJleHAiOjE1ODg3MjE4MjYsInVzZXJfbmFtZSI6IjEiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl0sImp0aSI6ImNhZTI1NDI3LTRhZGEtNGIyMS04YTQyLTQ3ZWNkNjk4OTJjOSIsImNsaWVudF9pZCI6IndzLXByb2plY3QiLCJzY29wZSI6WyJtb2JpbGUiXX0"
			+ ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

	private static final String REFRESH_TOKEN_EXAMPLE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
			+ ".eyJ1c2VyX25hbWUiOiIxIiwic2NvcGUiOlsibW9iaWxlIl0sImF0aSI6IjM0YjU5NGU1LTk2YzctNGZmNi04NmQ2LWJjODE1YTc1OWQ4ZCIsImV4cCI6MTU4ODc1MTQ1NiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiJdLCJqdGkiOiIzMWI5OTI4OC1mNTUxLTRhZDctYmViZS03ZjU0M2I3MWZlZmIiLCJjbGllbnRfaWQiOiJ3cy1wcm9qZWN0In0"
			+ ".SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
	
	@Before // 매 테스트가 실행될 때마다 실행된다는 것에 주의해야한다.
	public void setup() throws Exception {
		this.document = document("{class-name}/{method-name}", preprocessResponse(prettyPrint()));
		
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
							.apply(documentationConfiguration(this.restDocumentation)
								.uris().withScheme("https").withHost("api.mslim8803.shop/api/" + applicationName).withPort(443))
							.alwaysDo(document)
							.build();
		
		setFakeTokenResponse();
		setFakeAuthenticationResponse();
	}
	
	/**
	 * "/oauth/token" 호출 시의 Response를 조작
	 * @throws HttpRequestMethodNotSupportedException
	 */
	private void setFakeTokenResponse() throws HttpRequestMethodNotSupportedException {		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, 3600);
		Date expiration = new Date(cal.getTimeInMillis());
		
		Map<String, Object> info = new HashMap<String, Object>();
		info.put("jti", "cae25427-4ada-4b21-8a42-47ecd69892c9");
		
		DefaultOAuth2AccessToken oAuth2AccessToken = new DefaultOAuth2AccessToken(ACCESS_TOKEN_EXAMPLE);
		oAuth2AccessToken.setTokenType("bearer");
		oAuth2AccessToken.setRefreshToken(new DefaultOAuth2RefreshToken(REFRESH_TOKEN_EXAMPLE));
		oAuth2AccessToken.setExpiration(expiration);
		oAuth2AccessToken.setScope(Collections.singleton("mobile"));
		oAuth2AccessToken.setAdditionalInformation(info);
		
		Mockito.when(tokenEndpoint.postAccessToken(any(), any())).thenReturn(ResponseEntity.ok(oAuth2AccessToken));
	}
	
	private void setFakeAuthenticationResponse() {
		Mockito.when(authorizationEndpoint.authorize(any(), any(), any(), any())).thenReturn(new ModelAndView("login", HttpStatus.OK));
	}
	
	/**
	 * Field Description 상수화
	 * @author mslim
	 *
	 */
	private static class Description {
		public static final String CODE = "인증 후 인증서버로부터 받은 코드 값";
		public static final String GRANT_TYPE = "인증 타입";
		public static final String REDIRECT_URI = "서비스 제공자가 인가 요청에 대한 응답을 전달할 리다이렉션 엔드포인트";
		
		public static final String ACCESS_TOKEN = "액세스 토큰";
		public static final String TOKEN_TYPE = "토큰 종류";
		public static final String REFRESH_TOKEN = "리프레쉬 토큰. 토큰이 만료되었을 때, 토큰 재발급에 쓰임.";
		public static final String EXPIRES_IN = "토큰 유효 시간(초)";
		public static final String SCOPE = "요청하는 접근 범위 (현재 mobile, admin 두 종류)";
		public static final String JTI = "JWT의 고유 식별자로서, 주로 중복적인 처리를 방지하기 위하여 사용.";
		public static final String RESPONSE_TYPE = "Authorization_code 그랜트 타입을 사용한다는 것을 나타내기 위한 파라미터. code로 셋팅되어야 한다";
		public static final String CLIENT_ID = "Client를 식별하기 위한 ID";
		public static final String STATE = "클라이언트의 요청과 그에 따른 콜백 간의 상태를 유지하기 위해 사용되며, 클라이언트가 서비스 제공자에게 전달하면 서비스 제공자는 이 값을 다시 응답에 포함해서 전달한다. CSRF 공격을 차단하가 위한 수단이 될 수 있다.";
	}
	
	@Test
	public void authenticationPage() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)
					.uris().withScheme("https").withHost("auth.mslim8803.shop").withPort(443))
				.alwaysDo(document)
				.build();
		
		mockMvc.perform(
				get("/oauth/authorize")
				.param("response_type", "code")
				.param("client_id", "ws-project")
				.param("scope", "mobile")
				.param("state", "ABCDE")
				.param("redirect_uri", "https://mslim8803.shop/login/oauth2/code"))
			.andDo(print())
			.andExpect(status().isOk())
			.andDo(document.document(
					requestParameters(
						parameterWithName("response_type").description(Description.RESPONSE_TYPE),
						parameterWithName("client_id").description(Description.CLIENT_ID),
						parameterWithName("scope").description(Description.SCOPE),
						parameterWithName("state").description(Description.STATE),
						parameterWithName("redirect_uri").description(Description.REDIRECT_URI)
					)
			));
	}
	
	@Test
	public void getTokenInfo() throws Exception {
		MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<String, Object>();
		bodyParams.add("code", "ABC");
		bodyParams.add("grant_type", "authorization_code");
		bodyParams.add("redirect_uri", "https://mslim8803.shop/login/oauth2/code");
		
		String jsonString = gsonBuilder.setPrettyPrinting().create().toJson(bodyParams);
		
		mockMvc.perform(
					post("/oauth/token")
					.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(jsonString))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					requestFields(
						fieldWithPath("code").description(Description.CODE),
						fieldWithPath("grant_type").description(Description.GRANT_TYPE),
						fieldWithPath("redirect_uri").description(Description.REDIRECT_URI)
					),
					responseFields(
						fieldWithPath("access_token").description(Description.ACCESS_TOKEN),
						fieldWithPath("token_type").description(Description.TOKEN_TYPE),
						fieldWithPath("refresh_token").description(Description.REFRESH_TOKEN),
						fieldWithPath("expires_in").description(Description.EXPIRES_IN),
						fieldWithPath("scope").description(Description.SCOPE),
						fieldWithPath("jti").description(Description.JTI)
					)
				));
	}

	@Test
	public void refreshTokenInfo() throws Exception {
		MultiValueMap<String, Object> bodyParams = new LinkedMultiValueMap<String, Object>();
		bodyParams.add("grant_type", "refresh_token");
		bodyParams.add("refresh_token", REFRESH_TOKEN_EXAMPLE);
		
		String jsonString = gsonBuilder.setPrettyPrinting().create().toJson(bodyParams);
		
		mockMvc.perform(
					post("/oauth/token")
					.header(HttpHeaders.AUTHORIZATION, AUTHORIZATION)
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.content(jsonString))
				.andDo(print())
				.andExpect(status().isOk())
				.andDo(document.document(
					requestFields(
						fieldWithPath("grant_type").description(Description.GRANT_TYPE),
						fieldWithPath("refresh_token").description(Description.REFRESH_TOKEN)
					),
					responseFields(
						fieldWithPath("access_token").description(Description.ACCESS_TOKEN),
						fieldWithPath("token_type").description(Description.TOKEN_TYPE),
						fieldWithPath("refresh_token").description(Description.REFRESH_TOKEN),
						fieldWithPath("expires_in").description(Description.EXPIRES_IN),
						fieldWithPath("scope").description(Description.SCOPE),
						fieldWithPath("jti").description(Description.JTI)
					)
				));
	}
}
