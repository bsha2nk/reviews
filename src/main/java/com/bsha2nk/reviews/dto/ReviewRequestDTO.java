package com.bsha2nk.reviews.dto;

import java.time.LocalDate;

import com.bsha2nk.reviews.util.StoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewRequestDTO {

	private String review;
	
	private String author;
	
	@JsonProperty("review_source")
	private StoreType reviewSource;
	
	private Integer rating;
	
	private String title;
	
	@JsonProperty("product_name")
	private String productName;
	
	@JsonProperty("reviewed_date")
	private LocalDate reviewedDate;
	
}