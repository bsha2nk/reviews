package com.bsha2nk.reviews.entity;

import java.time.LocalDate;

import com.bsha2nk.reviews.util.StoreType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reviews")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String review;
	
	@Column
	private String author;
	
	@Enumerated(EnumType.STRING)
	private StoreType reviewSource;
	
	@Column
	private Integer rating;
	
	@Column
	private String title;
	
	@Column
	private String productName;
	
	@Column
	private LocalDate reviewedDate;
	
}