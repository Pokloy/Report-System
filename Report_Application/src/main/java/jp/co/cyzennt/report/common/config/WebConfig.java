package jp.co.cyzennt.report.common.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * linking message config 
 * @author lj
 *
 * 9/21/2023
 */
@Configuration
public class WebConfig {

	@Bean
	public MessageSource messageSource() {
		
		ReloadableResourceBundleMessageSource bean = new ReloadableResourceBundleMessageSource();
		
		// specifying the messsages propaties fiel name（messages.properties）
		bean.setBasename("classpath:messages");	
		
		// specifying character code if the messsages propaties
		bean.setDefaultEncoding("UTF-8");
		
		
		return bean;
	}

	@Bean
	public LocalValidatorFactoryBean localValidatorFactoryBean() {
		
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		
		localValidatorFactoryBean.setValidationMessageSource(messageSource());
		
		return localValidatorFactoryBean;
	}

}

