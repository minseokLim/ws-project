package com.wsproject.wsservice.domain.search;

import static com.wsproject.wsservice.domain.QWsAdmin.wsAdmin;

import org.springframework.util.Assert;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.wsproject.wsservice.domain.enums.WsType;

public class WsAdminSearch {

	public static BooleanExpression getBooleanExpression(String param) {
		String[] keyValue = param.split("=");
		Assert.isTrue(keyValue.length == 2, "Search Parameter must be look like 'key=value'");
		
		String key = keyValue[0];
		String value = keyValue[1];
		
		switch (key) {
		case "id":
			return wsAdmin.id.eq(Long.parseLong(value));
		case "content":
			return wsAdmin.content.containsIgnoreCase(value);
		case "author":
			return wsAdmin.author.containsIgnoreCase(value);
		case "type":
			return wsAdmin.type.eq(WsType.valueOf(value));
		}
		
		throw new IllegalArgumentException("Invalid Search Parameter");
	}
}
