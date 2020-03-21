package com.wsproject.wsservice.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CommonUtil {
	
	/**
	 * hateoas 준수를 위한 함수
	 * @param model
	 * @param page
	 */
	public void setPageLinksAdvice(PagedModel<?> model, Page<?> page) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		UriComponentsBuilder original = ServletUriComponentsBuilder.fromServletMapping(request).path(request.getRequestURI());
		
		for(Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
			for(String value : entry.getValue()) {
				original.queryParam(entry.getKey(), value);
			}
		}
		
		if(page.hasNext()) {
			UriComponentsBuilder nextBuilder = replacePageParams(original, page.nextPageable());
			model.add(new Link(nextBuilder.toUriString()).withRel("next"));
		}
		
		if(page.hasPrevious()) {
			UriComponentsBuilder prevBuilder = replacePageParams(original, page.previousPageable());
			model.add(new Link(prevBuilder.toUriString()).withRel("prev"));
		}
		
		UriComponentsBuilder firstBuilder = replacePageParams(original, PageRequest.of(0, page.getSize(), page.getSort()));
		model.add(new Link(firstBuilder.toUriString()).withRel("first"));
		
		UriComponentsBuilder lastBuilder = replacePageParams(original, PageRequest.of(page.getTotalPages() - 1 >= 0 ? page.getTotalPages() - 1 : 0, page.getSize(), page.getSort()));
		model.add(new Link(lastBuilder.toUriString()).withRel("last"));	
	}

	/**
	 * original에 있는 uri에서 page, size 파라미터를 pageable에 있는 값들도 대체
	 * @param original
	 * @param pageable
	 * @return
	 */
	private UriComponentsBuilder replacePageParams(UriComponentsBuilder original, Pageable pageable) {
		UriComponentsBuilder builder = original.cloneBuilder();
		builder.replaceQueryParam("page", pageable.getPageNumber());
		builder.replaceQueryParam("size", pageable.getPageSize());
		return builder;
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
}
