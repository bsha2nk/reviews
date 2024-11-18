package com.bsha2nk.reviews.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bsha2nk.reviews.dto.ReviewRequestDTO;
import com.bsha2nk.reviews.dto.ReviewResponseDTO;
import com.bsha2nk.reviews.projection.MonthlyRatingProjection;
import com.bsha2nk.reviews.projection.TotalRatingProjection;
import com.bsha2nk.reviews.service.ReviewsService;
import com.bsha2nk.reviews.util.StoreType;

@ExtendWith(SpringExtension.class)
public class ReviewsControllerTest {

	@Mock
	private ReviewsService reviewsService;
	
	@InjectMocks
	private ReviewsController reviewsController;
	
	@Test
	void test_createReview() {
        ReviewRequestDTO reviewRequestDTO = ReviewRequestDTO.builder()
        		.review("Pero deberia de poder cambiarle el idioma a alexa")
        		.author("WarcryxD")
        		.reviewSource(StoreType.iTunes)
        		.rating(4)
        		.title("Excelente")
        		.productName("Amazon Alexa")
        		.reviewedDate(LocalDateTime.of(2017, 1, 2, 4, 5))
        		.build();
        
//        ReviewResponseDTO reviewResponseDTO = ReviewResponseDTO.builder()
//        		.id(1)
//        		.review("Pero deberia de poder cambiarle el idioma a alexa")
//        		.author("WarcryxD")
//        		.reviewSource(StoreType.iTunes)
//        		.rating(4)
//        		.title("Excelente")
//        		.productName("Amazon Alexa")
//        		.reviewedDate(LocalDateTime.of(2017, 1, 2, 4, 5))
//        		.build();
        
//        when(reviewService.createReview(reviewRequestDTO)).thenReturn(reviewResponseDTO);
        
        ResponseEntity<ReviewResponseDTO> response = reviewsController.createReview(reviewRequestDTO);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(response.getBody()).isInstanceOf(ReviewResponseDTO.class);
        verify(reviewsService).createReview(any());
	}
	
	@Test
	void test_getAllreviews() {
		ResponseEntity<List<ReviewResponseDTO>> response = reviewsController.getAllReviews(null, null, null);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(reviewsService).getAllReviews(any());
	}
	
	@Test
	void test_getAverageRating() {
		ResponseEntity<List<MonthlyRatingProjection>> response = reviewsController.getAverageRatingByStoreType(StoreType.GooglePlayStore);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(reviewsService).getAverageRatingByStoreType(any());
	}

	// TODO
	@Test
	void test_getAverageRating_Incorrect_StoreType {
		ResponseEntity<List<MonthlyRatingProjection>> response = reviewsController.getAverageRatingByStoreType(StoreType.GooglePlayStore);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(reviewsService).getAverageRatingByStoreType(any());
	}
	
	@Test
	void test_getTotalRatings() {
		ResponseEntity<List<TotalRatingProjection>> response = reviewsController.getTotalRatings();
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		verify(reviewsService).getTotalRatings();
	}
	
}