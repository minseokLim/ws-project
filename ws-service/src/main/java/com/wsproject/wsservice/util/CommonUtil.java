package com.wsproject.wsservice.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

	private UriComponentsBuilder replacePageParams(UriComponentsBuilder original, Pageable pageable) {
		UriComponentsBuilder builder = original.cloneBuilder();
		builder.replaceQueryParam("page", pageable.getPageNumber());
		builder.replaceQueryParam("size", pageable.getPageSize());
		return builder;
	}
}
