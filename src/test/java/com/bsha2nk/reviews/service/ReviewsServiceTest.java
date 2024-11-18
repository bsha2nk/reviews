package com.bsha2nk.reviews.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bsha2nk.reviews.dto.ReviewRequestDTO;
import com.bsha2nk.reviews.dto.ReviewResponseDTO;
import com.bsha2nk.reviews.entity.Review;
import com.bsha2nk.reviews.projection.MonthlyRatingProjection;
import com.bsha2nk.reviews.projection.TotalRatingProjection;
import com.bsha2nk.reviews.repository.ReviewsRepository;
import com.bsha2nk.reviews.util.StoreType;

@ExtendWith(SpringExtension.class)
public class ReviewsServiceTest {

	@Mock
	private ReviewsRepository reviewsRepository;

	@Spy
	private ModelMapper mapper;

	@InjectMocks
	private ReviewsService reviewsService;

	@Test
	void test_createReview() {
		ReviewRequestDTO reviewRequestDTO = getReviewRequestDTO();

		Review review = getReviews().get(0);

		when(reviewsRepository.save(any())).thenReturn(review);

		ReviewResponseDTO responseDTO = reviewsService.createReview(reviewRequestDTO);

		verify(reviewsRepository).save(any());

		assertEquals(review.getId(), responseDTO.getId());
		assertEquals(review.getReview(), responseDTO.getReview());
		assertEquals(review.getAuthor(), responseDTO.getAuthor());
		assertEquals(review.getReviewSource(), responseDTO.getReviewSource());
		assertEquals(review.getRating(), responseDTO.getRating());
		assertEquals(review.getTitle(), responseDTO.getTitle());
		assertEquals(review.getProductName(), responseDTO.getProductName());
		assertEquals(review.getReviewedDate(), responseDTO.getReviewedDate());
	}

	@Test
	void test_createReview_Exception() {
		ReviewRequestDTO reviewRequestDTO = getReviewRequestDTO();

		when(reviewsRepository.save(any())).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class, () -> reviewsService.createReview(reviewRequestDTO));
	}

	@Test
	void test_getAllReviews() {
		Review filterCrietria = new Review();
		Example<Review> reviewExample = Example.of(filterCrietria);
		List<Review> reviews = getReviews();

		when(reviewsRepository.findAll(reviewExample)).thenReturn(reviews);

		List<ReviewResponseDTO> reviewResponseDTOs = reviewsService.getAllReviews(filterCrietria);

		verify(reviewsRepository).findAll(reviewExample);

		assertEquals(reviews.get(0).getId(), reviewResponseDTOs.get(0).getId());
		assertEquals(reviews.get(0).getReview(), reviewResponseDTOs.get(0).getReview());
		assertEquals(reviews.get(0).getAuthor(), reviewResponseDTOs.get(0).getAuthor());
		assertEquals(reviews.get(0).getReviewSource(), reviewResponseDTOs.get(0).getReviewSource());
		assertEquals(reviews.get(0).getRating(), reviewResponseDTOs.get(0).getRating());
		assertEquals(reviews.get(0).getTitle(), reviewResponseDTOs.get(0).getTitle());
		assertEquals(reviews.get(0).getProductName(), reviewResponseDTOs.get(0).getProductName());
		assertEquals(reviews.get(0).getReviewedDate(), reviewResponseDTOs.get(0).getReviewedDate());

		assertEquals(reviews.get(1).getId(), reviewResponseDTOs.get(1).getId());
		assertEquals(reviews.get(1).getReview(), reviewResponseDTOs.get(1).getReview());
		assertEquals(reviews.get(1).getAuthor(), reviewResponseDTOs.get(1).getAuthor());
		assertEquals(reviews.get(1).getReviewSource(), reviewResponseDTOs.get(1).getReviewSource());
		assertEquals(reviews.get(1).getRating(), reviewResponseDTOs.get(1).getRating());
		assertEquals(reviews.get(1).getTitle(), reviewResponseDTOs.get(1).getTitle());
		assertEquals(reviews.get(1).getProductName(), reviewResponseDTOs.get(1).getProductName());
		assertEquals(reviews.get(1).getReviewedDate(), reviewResponseDTOs.get(1).getReviewedDate());
	}

	@Test
	void test_getAllReviews_Exception() {
		Review filterCrietria = new Review();
		Example<Review> reviewExample = Example.of(filterCrietria);

		when(reviewsRepository.findAll(reviewExample)).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class, () -> reviewsService.getAllReviews(filterCrietria));
	}

	@Test
	void test_getAverageRatingByStoreType() {
		List<MonthlyRatingProjection> list = getMonthlyRatingProjections();

		when(reviewsRepository.findAverageRatingByStoreType(any())).thenReturn(list);

		List<MonthlyRatingProjection> monthlyRatings = reviewsService.getAverageRatingByStoreType(StoreType.GooglePlayStore);

		verify(reviewsRepository).findAverageRatingByStoreType(any());

		assertEquals(list.get(0).getRating(), monthlyRatings.get(0).getRating());
		assertEquals(list.get(0).getMonth(), monthlyRatings.get(0).getMonth());
		assertEquals(list.get(0).getYear(), monthlyRatings.get(0).getYear());

		assertEquals(list.get(1).getRating(), monthlyRatings.get(1).getRating());
		assertEquals(list.get(1).getMonth(), monthlyRatings.get(1).getMonth());
		assertEquals(list.get(1).getYear(), monthlyRatings.get(1).getYear());
	}

	@Test
	void test_getAverageRatingByStoreType_Exception() {
		when(reviewsRepository.findAverageRatingByStoreType(any())).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class, () -> reviewsService.getAverageRatingByStoreType(StoreType.GooglePlayStore));
	}

	@Test
	void test_getTotalRatings() {
		List<TotalRatingProjection> list = getTotalRatingProjections();

		when(reviewsRepository.findTotalRatings()).thenReturn(list);

		List<TotalRatingProjection> ratingProjections = reviewsService.getTotalRatings();

		verify(reviewsRepository).findTotalRatings();

		assertEquals(list.get(0).getRating(), ratingProjections.get(0).getRating());
		assertEquals(list.get(0).getTotalRatings(), ratingProjections.get(0).getTotalRatings());

		assertEquals(list.get(1).getRating(), ratingProjections.get(1).getRating());
		assertEquals(list.get(1).getTotalRatings(), ratingProjections.get(1).getTotalRatings());
	}

	@Test
	void test_getTotalRatings_Exception() {
		when(reviewsRepository.findTotalRatings()).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class, () -> reviewsService.getTotalRatings());
	}

	//	@Test
	void test_createMultipleReviews() {
		ReviewRequestDTO reviewRequestDTOOne = getReviewRequestDTO();

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

	private ReviewRequestDTO getReviewRequestDTO() {
		return ReviewRequestDTO.builder()
				.review("Pero deberia de poder cambiarle el idioma a alexa")
				.author("WarcryxD")
				.reviewSource(StoreType.iTunes)
				.rating(4)
				.title("Excelente")
				.productName("Amazon Alexa")
				.reviewedDate(LocalDateTime.of(2017, 1, 2, 4, 5))
				.build();
	}

	private List<Review> getReviews() {
		Review reviewOne = Review.builder()
				.id(1)
				.review("Pero deberia de poder cambiarle el idioma a alexa")
				.author("WarcryxD")
				.reviewSource(StoreType.iTunes)
				.rating(4)
				.title("Excelente")
				.productName("Amazon Alexa")
				.reviewedDate(LocalDateTime.of(2017, 1, 2, 4, 5))
				.build();

		Review reviewTwo = Review.builder()
				.id(2)
				.review("review")
				.author("author")
				.reviewSource(StoreType.GooglePlayStore)
				.rating(5)
				.title("title")
				.productName("product")
				.reviewedDate(LocalDateTime.of(2024, 2, 2, 13, 7, 42))
				.build();

		return List.of(reviewOne, reviewTwo);
	}

	private List<MonthlyRatingProjection> getMonthlyRatingProjections() {
		MonthlyRatingProjection ratingOne = new MonthlyRatingProjection() {

			@Override
			public Integer getYear() {
				return 2017;
			}

			@Override
			public Double getRating() {
				return 2.34;
			}

			@Override
			public String getMonth() {
				return "January";
			}
		};

		MonthlyRatingProjection ratingTwo = new MonthlyRatingProjection() {

			@Override
			public Integer getYear() {
				return 2024;
			}

			@Override
			public Double getRating() {
				return 4.30;
			}

			@Override
			public String getMonth() {
				return "May";
			}
		};

		return List.of(ratingOne, ratingTwo);
	}

	private List<TotalRatingProjection> getTotalRatingProjections() {
		TotalRatingProjection categoryFive = new TotalRatingProjection() {

			@Override
			public Integer getTotalRatings() {
				return 1234;
			}

			@Override
			public Integer getRating() {
				return 5;
			}
		};

		TotalRatingProjection categoryTwo = new TotalRatingProjection() {

			@Override
			public Integer getTotalRatings() {
				return 2341;
			}

			@Override
			public Integer getRating() {
				return 2;
			}
		};

		return List.of(categoryFive, categoryTwo);
	}

}