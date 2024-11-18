package com.bsha2nk.reviews.controller;

import java.time.LocalDateTime;
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
	public ResponseEntity<List<ReviewResponseDTO>> getAllReviews(@RequestParam(required = false) LocalDateTime date,
																@RequestParam(required = false) StoreType storeType,
																@RequestParam(required = false) Integer rating) {
		Review reviewFilterCriteria = Review.builder()
				.reviewedDate(date)
				.reviewSource(storeType)
				.rating(rating)
				.build();
		
		return new ResponseEntity<List<ReviewResponseDTO>>(reviewService.getAllReviews(reviewFilterCriteria), HttpStatus.OK);
	}
	
	@GetMapping("/average-rating/store-type/{storeType}")
	public ResponseEntity<List<MonthlyRatingProjection>> getAverageRatingByStoreType(@PathVariable StoreType storeType) {
		return ResponseEntity.ok(reviewService.getAverageRatingByStoreType(storeType));
	}
	
	@GetMapping("/total-ratings")
	public ResponseEntity<List<TotalRatingProjection>> getTotalRatings() {
		return ResponseEntity.ok(reviewService.getTotalRatings());
	}
	
	@PostMapping("/multiple")
	public ResponseEntity<List<ReviewResponseDTO>> createMultipleReviews(@RequestBody List<ReviewRequestDTO> reviewRequestDTOs) {
		List<ReviewResponseDTO> reviewResponseDTOs = reviewService.createMultipleReviews(reviewRequestDTOs);
		
		return new ResponseEntity<List<ReviewResponseDTO>>(reviewResponseDTOs, HttpStatus.CREATED);
	}
	
}