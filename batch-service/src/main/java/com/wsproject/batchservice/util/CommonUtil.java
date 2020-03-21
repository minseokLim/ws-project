package com.wsproject.batchservice.util;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.wsproject.batchservice.provider.ApplicationContextProvider;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CommonUtil {
	
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
