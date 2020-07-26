package com.wsproject.wsservice.util;

import java.net.URLDecoder;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.querydsl.core.types.dsl.BooleanExpression;
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
	 * 페이지 관련 hateoas 정보 주입
	 * @param <T> Entity class
	 * @param <R> dto class
	 * @param page 조회한 Page 객체
	 * @param transformToDto Entity 객체를 dto로 변환시켜줄 함수
	 * @return hateoas정보가 주입된 PagedModel 객체
	 */
	public static <T, R> PagedModel<R> setPageLinksAdvice(Page<T> page, Function<T, R> transformToDto) {
		
		List<R> content = page.stream().map(transformToDto).collect(Collectors.toList());
		
		PageMetadata pageMetadata = new PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements());
		PagedModel<R> result = new PagedModel<>(content, pageMetadata);
		setLinkAdvice(result, new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString()).withSelfRel());
		setPageLinksAdvice(result, page);
		
		return result;
	}
	
	/**
	 * hateoas를 위해 next, prev, first, last 등의 url을 자동 입력해주는 함수
	 * @param model
	 * @param page
	 */
	private static void setPageLinksAdvice(PagedModel<?> model, Page<?> page) {
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

	/** 
	 * original에 있는 page 관련 파라미터들을 replace
	 * @param original
	 * @param pageable
	 * @return page 관련 파라미터들이 replace된 uri string
	 */
	private static String getPageParamReplacedUri(UriComponentsBuilder original, Pageable pageable) {
		
		UriComponentsBuilder builder = original.cloneBuilder();
		builder.replaceQueryParam("page", pageable.getPageNumber());
		builder.replaceQueryParam("size", pageable.getPageSize());
		
		return builder.build().toUriString();
	}
	
	/**
	 * DTO 클래스에 링크정보를 주입한다.
	 * @param dto
	 * @param link
	 */
	public static void setLinkAdvice(RepresentationModel<?> dto, Link link) {
		String linkStr = link.getHref();
		log.debug("link : {}", linkStr);
		dto.add(new Link(replaceBaseUri(linkStr), link.getRel()));
	}
	
	/**
	 * uri에서 baseuri를 Zuul server의 uri로 변경하는 함수 <br>
	 * HATEOAS Link 정보를 받는 클라이언트에서는 Zuul server를 통해서 이 서버에 접근하기 때문에 base uri를 바꿔서 반환할 필요가 있음.
	 * @param uri
	 * @return
	 */
	private static String replaceBaseUri(String uri) {
		String currentBaseUri = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
		CustomProperties properties = getBean(CustomProperties.class);
		String apiPublicBaseUri = properties.getApiPublicBaseUri();
		
		return uri.replace(currentBaseUri, apiPublicBaseUri + "/" + applicationName);
	}
	
	/** 
	 * Bean객체를 얻는다
	 * @param <T>
	 * @param classType
	 * @return Bean 객체
	 */
	public static <T> T getBean(Class<T> classType) {
		ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
		
		return applicationContext.getBean(classType);
	}
	
	/**
	 * search파라미터를 파싱하여 적절한 QuerydslPredicate 형태로 반환
	 * @param search
	 * @param searchFunc
	 * @return QuerydslPredicate
	 */
	public static BooleanExpression extractSearchParameter(String search, Function<String, BooleanExpression> searchFunc) {
		try {
			BooleanExpression result = null;
			
			if(search != null && !"".equals(search)) {
				search = URLDecoder.decode(search, "UTF-8");
				
				String[] keyValues = search.split(",");
				
				for(String param : keyValues) {
					BooleanExpression predicate = searchFunc.apply(param);
					
					if(predicate != null) {
						if(result == null) {
							result = predicate;
						} else {
							result = result.and(predicate);
						}
					}
				}
			}
			
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 발생한 에러 메시지를 delimiter를 이용해 하나의 String으로 합쳐서 반환
	 * @param errors
	 * @param delimiter
	 * @return
	 */
	public static String getErrorMessages(Errors errors, String delimiter) {
		return errors.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(delimiter));
	}
}
