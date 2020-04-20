package com.wsproject.wsservice.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.wsproject.wsservice.config.CustomProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CommonUtil {
	
	private static String applicationName;
	
	@Value("${spring.application.name}")
	public void setApplicationName(String value) {
		applicationName = value;
	}

	/**
	 * hateoas를 위해 next, prev, first, last 등의 url을 자동 입력해주는 함수
	 * @param model
	 * @param page
	 * @throws UnsupportedEncodingException 
	 */
	public static void setPageLinksAdvice(PagedModel<?> model, Page<?> page) {
		UriComponentsBuilder original = ServletUriComponentsBuilder.fromCurrentRequest();
		
		if(page.hasNext()) {
			String nextUri = getPageParamReplacedUri(original, page.nextPageable());
			setLinkAdvice(model, new Link(nextUri).withRel("next"));
		}
		
		if(page.hasPrevious()) {
			String prevUri = getPageParamReplacedUri(original, page.previousPageable());
			setLinkAdvice(model, new Link(prevUri).withRel("prev"));
		}
		
		String firstUri = getPageParamReplacedUri(original, PageRequest.of(0, page.getSize(), page.getSort()));
		setLinkAdvice(model, new Link(firstUri).withRel("first"));
		
		String lastUri = getPageParamReplacedUri(original, PageRequest.of(page.getTotalPages() - 1 >= 0 ? page.getTotalPages() - 1 : 0, page.getSize(), page.getSort()));
		setLinkAdvice(model, new Link(lastUri).withRel("last"));
	}

	/** original에 있는 page 관련 파라미터들을 replace
	 * @param original
	 * @param pageable
	 * @return page 관련 파라미터들이 replace된 uri string
	 * @throws UnsupportedEncodingException 
	 */
	private static String getPageParamReplacedUri(UriComponentsBuilder original, Pageable pageable) {
		
		UriComponentsBuilder builder = original.cloneBuilder();
		builder.replaceQueryParam("page", pageable.getPageNumber());
		builder.replaceQueryParam("size", pageable.getPageSize());
		
		try {
			String result = URLDecoder.decode(builder.build().toUriString(), "UTF-8");
			log.debug("result : {}", result);
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public static void setLinkAdvice(RepresentationModel<?> dto, Link link) {
		String linkStr = link.getHref();
		log.debug("link : {}", linkStr);
		dto.add(new Link(replaceBaseUri(linkStr), link.getRel()));
	}
	
	/**
	 * uri에서 baseuri를 zuulsvr의 uri로 변경하는 함수
	 * @param uri
	 * @return
	 */
	private static String replaceBaseUri(String uri) {
		String currentBaseUri = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
		CustomProperties properties = getBean(CustomProperties.class);
		String apiPublicBaseUri = properties.getApiPublicBaseUri();
		
		return uri.replace(currentBaseUri, apiPublicBaseUri + "/" + applicationName);
	}
	
	/** Bean객체를 얻는다
	 * @param <T>
	 * @param classType
	 * @return Bean 객체
	 */
	public static <T> T getBean(Class<T> classType) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		
		return applicationContext.getBean(classType);
	}
	
	public static String getLikeStr(String value) {
		return "%" + value + "%";
	}
}
