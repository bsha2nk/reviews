package com.bsha2nk.reviews.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bsha2nk.reviews.dto.ReviewRequestDTO;
import com.bsha2nk.reviews.entity.Review;
import com.bsha2nk.reviews.repository.ReviewsRepository;
import com.bsha2nk.reviews.util.StoreType;

@ExtendWith(SpringExtension.class)
public class ReviewsServiceTest {
	
	@Mock
	private ReviewsRepository reviewsRepository;
	
	@Mock
	private ModelMapper mapper;
	
	@InjectMocks
	private ReviewsService reviewsService;
	
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
		
		reviewsService.createReview(reviewRequestDTO);
		
		verify(reviewsRepository).save(any());
	}
	
	@Test
	void test_getAllReviews() {
		Review filterCrietria = new Review();
		Example<Review> reviewExample = Example.of(filterCrietria);
		
		Review review = Review.builder()
				.review("Pero deberia de poder cambiarle el idioma a alexa")
				.author("WarcryxD")
				.reviewSource(StoreType.iTunes)
				.rating(4)
				.title("Excelente")
				.productName("Amazon Alexa")
				.reviewedDate(LocalDateTime.of(2017, 1, 2, 4, 5))
				.build();
		
		when(reviewsRepository.findAll(reviewExample)).thenReturn(List.of(review));
		
		reviewsService.getAllReviews(filterCrietria);
		
		verify(reviewsRepository).findAll(reviewExample);
	}
	
	@Test
	void test_getAverageRatingByStoreType() {
		reviewsService.getAverageRatingByStoreType(StoreType.GooglePlayStore);
		
		verify(reviewsRepository).findAverageRatingByStoreType(any());
	}
	
	@Test
	void test_getTotalRatings() {
		reviewsService.getTotalRatings();
		
		verify(reviewsRepository).findTotalRatings();
	}
	
	@Test
	void test_createMultipleReviews() {
		ReviewRequestDTO reviewRequestDTOOne = ReviewRequestDTO.builder()
				.review("Pero deberia de poder cambiarle el idioma a alexa")
				.author("WarcryxD")
				.reviewSource(StoreType.iTunes)
				.rating(4)
				.title("Excelente")
				.productName("Amazon Alexa")
				.reviewedDate(LocalDateTime.of(2017, 1, 2, 4, 5))
				.build();

		ReviewRequestDTO reviewRequestDTOTwo = ReviewRequestDTO.builder()
        		.review("review")
        		.author("author")
        		.reviewSource(StoreType.GooglePlayStore)
        		.rating(2)
        		.title("title")
        		.productName("product")
        		.reviewedDate(LocalDateTime.of(2017, 5, 4, 3, 2))
        		.build();
        
        List<ReviewRequestDTO> requestDTOs = List.of(reviewRequestDTOOne, reviewRequestDTOTwo);
        
        reviewsService.createMultipleReviews(requestDTOs);
        
        verify(reviewsRepository, times(2)).save(any());
	}

}
