package com.bsha2nk.reviews.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bsha2nk.reviews.dto.ReviewRequestDTO;
import com.bsha2nk.reviews.dto.ReviewResponseDTO;
import com.bsha2nk.reviews.entity.Review;
import com.bsha2nk.reviews.projection.MonthlyRatingProjection;
import com.bsha2nk.reviews.projection.TotalRatingProjection;
import com.bsha2nk.reviews.service.ReviewService;
import com.bsha2nk.reviews.util.StoreType;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {
	
	private final ReviewService reviewService;
	
	@PostMapping
	public ResponseEntity<ReviewResponseDTO> createReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
		ReviewResponseDTO reviewResponseDTO = reviewService.createReview(reviewRequestDTO);
		
		return new ResponseEntity<ReviewResponseDTO>(reviewResponseDTO, HttpStatus.CREATED);
	}
	
	@GetMapping
	public ResponseEntity<List<ReviewResponseDTO>> getAllReviews(@RequestParam(required = false) LocalDate date,
																@RequestParam(required = false) StoreType storeType,
																@RequestParam(required = false) Integer rating) {
		Review reviewFilterCriteria = Review.builder()
				.reviewedDate(date)
				.reviewSource(storeType)
				.rating(rating)
				.build();
		
		return new ResponseEntity<List<ReviewResponseDTO>>(reviewService.getAllReviews(reviewFilterCriteria), HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ReviewResponseDTO> getReviewById(@PathVariable int id) {
		return ResponseEntity.ok(reviewService.getReviewById(id));
	}
	
	@GetMapping("/average-rating/store-type/{storeType}")
	public ResponseEntity<List<MonthlyRatingProjection>> getAverageRating(@PathVariable StoreType storeType) {
		return ResponseEntity.ok(reviewService.getAverageRatingByStoreType(storeType));
	}
	
	@GetMapping("/total-ratings/store-type/{storeType}")
	public ResponseEntity<List<TotalRatingProjection>> getTotalRatings(@PathVariable StoreType storeType) {
		return ResponseEntity.ok(reviewService.getTotalRatingsByStoreType(storeType));
	}
	
}