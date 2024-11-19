package com.bsha2nk.reviews.service;

import static com.bsha2nk.reviews.util.Constants.AUTHOR;
import static com.bsha2nk.reviews.util.Constants.PRODUCT_NAME;
import static com.bsha2nk.reviews.util.Constants.REVIEW;
import static com.bsha2nk.reviews.util.Constants.TITLE;
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
		ReviewRequestDTO reviewRequestDTO = getReviewRequestDTOs().get(0);

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
		ReviewRequestDTO reviewRequestDTO = getReviewRequestDTOs().get(0);

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

		assertEquals(list.get(1).getRating(), monthlyRatings.get(1).getRating());
		assertEquals(list.get(1).getMonth(), monthlyRatings.get(1).getMonth());
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

	@Test
	void test_createMultipleReviews() {
		List<ReviewRequestDTO> reviewRequestDTOs = getReviewRequestDTOs();

		List<Review> reviews = getReviews();

		when(reviewsRepository.save(any())).thenReturn(reviews.get(0)).thenReturn(reviews.get(1));

		List<ReviewResponseDTO> responseDTOs = reviewsService.createMultipleReviews(reviewRequestDTOs);

		verify(reviewsRepository, times(2)).save(any());

		assertEquals(reviews.get(0).getId(), responseDTOs.get(0).getId());
		assertEquals(reviews.get(0).getReview(), responseDTOs.get(0).getReview());
		assertEquals(reviews.get(0).getAuthor(), responseDTOs.get(0).getAuthor());
		assertEquals(reviews.get(0).getReviewSource(), responseDTOs.get(0).getReviewSource());
		assertEquals(reviews.get(0).getRating(), responseDTOs.get(0).getRating());
		assertEquals(reviews.get(0).getTitle(), responseDTOs.get(0).getTitle());
		assertEquals(reviews.get(0).getProductName(), responseDTOs.get(0).getProductName());
		assertEquals(reviews.get(0).getReviewedDate(), responseDTOs.get(0).getReviewedDate());

		assertEquals(reviews.get(1).getId(), responseDTOs.get(1).getId());
		assertEquals(reviews.get(1).getReview(), responseDTOs.get(1).getReview());
		assertEquals(reviews.get(1).getAuthor(), responseDTOs.get(1).getAuthor());
		assertEquals(reviews.get(1).getReviewSource(), responseDTOs.get(1).getReviewSource());
		assertEquals(reviews.get(1).getRating(), responseDTOs.get(1).getRating());
		assertEquals(reviews.get(1).getTitle(), responseDTOs.get(1).getTitle());
		assertEquals(reviews.get(1).getProductName(), responseDTOs.get(1).getProductName());
		assertEquals(reviews.get(1).getReviewedDate(), responseDTOs.get(1).getReviewedDate());
	}

	@Test
	void test_createMultipleReviews_Exception() {
		List<ReviewRequestDTO> reviewRequestDTOs = getReviewRequestDTOs();

		when(reviewsRepository.save(any())).thenThrow(new RuntimeException());

		assertThrows(RuntimeException.class, () -> reviewsService.createMultipleReviews(reviewRequestDTOs));
	}

	private List<ReviewRequestDTO> getReviewRequestDTOs() {
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
				.review(REVIEW)
				.author(AUTHOR)
				.reviewSource(StoreType.GooglePlayStore)
				.rating(2)
				.title(TITLE)
				.productName(PRODUCT_NAME)
				.reviewedDate(LocalDateTime.of(2017, 5, 4, 3, 2))
				.build();

		return List.of(reviewRequestDTOOne, reviewRequestDTOTwo);
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
				.review(REVIEW)
				.author(AUTHOR)
				.reviewSource(StoreType.GooglePlayStore)
				.rating(5)
				.title(TITLE)
				.productName(PRODUCT_NAME)
				.reviewedDate(LocalDateTime.of(2024, 2, 2, 13, 7, 42))
				.build();

		return List.of(reviewOne, reviewTwo);
	}

	private List<MonthlyRatingProjection> getMonthlyRatingProjections() {
		MonthlyRatingProjection ratingOne = new MonthlyRatingProjection() {

			@Override
			public Double getRating() {
				return 2.34;
			}

			@Override
			public String getMonth() {
				return "January, 2017";
			}
		};

		MonthlyRatingProjection ratingTwo = new MonthlyRatingProjection() {

			@Override
			public Double getRating() {
				return 4.30;
			}

			@Override
			public String getMonth() {
				return "May, 2024";
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