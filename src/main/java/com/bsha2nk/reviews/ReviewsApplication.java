package com.bsha2nk.reviews;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

@SpringBootApplication
public class ReviewsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewsApplication.class, args);
	}
	
	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean
	FormattingConversionService conversionService() {

		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

		conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
		
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendPattern("yyyy-MM-dd[ HH:mm:ss]")
		        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		        .toFormatter();

		DateTimeFormatterRegistrar dateTimeRegistrar = new DateTimeFormatterRegistrar();
		dateTimeRegistrar.setDateFormatter(formatter);
		dateTimeRegistrar.registerFormatters(conversionService);

		return conversionService;
	}
	
}