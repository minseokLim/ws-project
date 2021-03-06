package com.wsproject.wsservice.domain.search;

import static com.wsproject.wsservice.domain.QWsPrivate.wsPrivate;

import org.springframework.util.Assert;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.wsproject.wsservice.domain.enums.WsType;

public class WsPrivateSearch {

	public static BooleanExpression getBooleanExpression(String param) {
		String[] keyValue = param.split("=");
		Assert.isTrue(keyValue.length == 2, "Search Parameter must be look like 'key=value'");
		
		String key = keyValue[0];
		String value = keyValue[1];
		
		switch (key) {
		case "id":
			return wsPrivate.id.eq(Long.parseLong(value));
		case "content":
			return wsPrivate.content.containsIgnoreCase(value);
		case "author":
			return wsPrivate.author.containsIgnoreCase(value);
		case "type":
			return wsPrivate.type.eq(WsType.valueOf(value));
		case "ownerIdx":
			return wsPrivate.ownerIdx.eq(Long.parseLong(value));
		case "liked":
			return wsPrivate.liked.eq(Boolean.parseBoolean(value));
		}
		
		throw new IllegalArgumentException("Invalid Search Parameter");
	}
}
