package com.bsha2nk.reviews.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.bsha2nk.reviews.dto.ReviewRequestDTO;
import com.bsha2nk.reviews.dto.ReviewResponseDTO;
import com.bsha2nk.reviews.entity.Review;
import com.bsha2nk.reviews.exception.ResourceNotFoundException;
import com.bsha2nk.reviews.projection.MonthlyRatingProjection;
import com.bsha2nk.reviews.projection.TotalRatingProjection;
import com.bsha2nk.reviews.repository.ReviewRepository;
import com.bsha2nk.reviews.util.StoreType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {
	
	private final ReviewRepository reviewRepository;
	
	private final ModelMapper mapper;
	
	public ReviewResponseDTO createReview(ReviewRequestDTO requestDTO) {
		Review review = Review.builder()
				.review(requestDTO.getReview())
				.author(requestDTO.getAuthor())
				.reviewSource(requestDTO.getReviewSource())
				.rating(requestDTO.getRating())
				.title(requestDTO.getTitle())
				.productName(requestDTO.getProductName())
				.reviewedDate(requestDTO.getReviewedDate())
				.build();
		
		Review newReview = reviewRepository.save(review);
		
		return mapper.map(newReview, ReviewResponseDTO.class);
	}

	public List<ReviewResponseDTO> getAllReviews(Review reviewFilterCriteria) {
		List<ReviewResponseDTO> reviewResponseDTOs = new ArrayList<>();
		
		Example<Review> reviewExample = Example.of(reviewFilterCriteria);
		
		for (Review review : reviewRepository.findAll(reviewExample)) {
			reviewResponseDTOs.add(mapper.map(review, ReviewResponseDTO.class));
		}
		
		return reviewResponseDTOs;
	}

	public ReviewResponseDTO getReviewById(int id) {
		Optional<Review> review = reviewRepository.findById(id);
		
		if (review.isEmpty()) {
			throw new ResourceNotFoundException(String.format("Review with ID %d was not found.", id));
		}
		
		return mapper.map(review.get(), ReviewResponseDTO.class);
	}

	public List<MonthlyRatingProjection> getAverageRatingByStoreType(StoreType storeType) {
		return reviewRepository.findAverageRatingByStoreType(storeType.toString()); 
	}
	
	public List<TotalRatingProjection> getTotalRatingsByStoreType() {
		return reviewRepository.findTotalRatingsByStoreType();
	}
	
	public List<ReviewResponseDTO> createMultipleReviews(List<ReviewRequestDTO> requestDTOs) {
		List<ReviewResponseDTO> reviewResponseDTOs = new ArrayList<>();
		
		for (ReviewRequestDTO requestDTO : requestDTOs) {
			reviewResponseDTOs.add(createReview(requestDTO));
		}
		
		return reviewResponseDTOs;
	}

}