package com.wsproject.wsservice.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.wsproject.wsservice.domain.enums.WsType;

@Converter(autoApply = true)
public class WsTypeConverter implements AttributeConverter<WsType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(WsType attribute) {
		return attribute.getCode();
	}

	@Override
	public WsType convertToEntityAttribute(Integer dbData) {
		return WsType.ofCode(dbData);
	}

}
