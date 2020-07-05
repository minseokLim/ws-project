package com.wsproject.userservice.domain.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.wsproject.userservice.domain.enums.SocialType;

@Converter(autoApply = true)
public class SocialTypeConverter implements AttributeConverter<SocialType, Integer> {

	@Override
	public Integer convertToDatabaseColumn(SocialType attribute) {
		return attribute.getCode();
	}

	@Override
	public SocialType convertToEntityAttribute(Integer dbData) {
		return SocialType.ofCode(dbData);
	}

}
