package com.bsha2nk.reviews.dto;

import java.time.LocalDateTime;

import com.bsha2nk.reviews.util.StoreType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
	private LocalDateTime reviewedDate;
	
}