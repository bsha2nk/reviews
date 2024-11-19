package com.bsha2nk.reviews.controller;

import static com.bsha2nk.reviews.util.Constants.AUTHOR;
import static com.bsha2nk.reviews.util.Constants.ID;
import static com.bsha2nk.reviews.util.Constants.PRODUCT_NAME;
import static com.bsha2nk.reviews.util.Constants.RATING;
import static com.bsha2nk.reviews.util.Constants.REVIEW;
import static com.bsha2nk.reviews.util.Constants.REVIEWED_DATE;
import static com.bsha2nk.reviews.util.Constants.REVIEW_SOURCE;
import static com.bsha2nk.reviews.util.Constants.TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bsha2nk.reviews.dto.ReviewResponseDTO;
import com.bsha2nk.reviews.projection.MonthlyRatingProjection;
import com.bsha2nk.reviews.projection.TotalRatingProjection;
import com.bsha2nk.reviews.service.ReviewsService;
import com.bsha2nk.reviews.util.StoreType;

@WebMvcTest(ReviewsController.class)
public class ReviewsControllerTest {

	@MockBean
	private ReviewsService reviewsService;

	@Autowired
	private MockMvc mockMvc; 

	@Test
	void test_createReview() throws Exception {
		String review = "{ \"review\": \"Pero deberia de poder cambiarle el idioma a alexa\", \"author\": \"WarcryxD\",\r\n"
				+ "    \"review_source\": \"iTunes\", \"rating\": 4,\r\n"
				+ "    \"title\": \"Excelente\", \"product_name\": \"Amazon Alexa\",\r\n"
				+ "    \"reviewed_date\": \"2018-01-12T02:27:03.000Z\"\r\n"
				+ "}";

		ReviewResponseDTO reviewResponseDTO = getReviewResponseDTOs().get(0);

		when(reviewsService.createReview(any())).thenReturn(reviewResponseDTO);

		String response = mockMvc.perform(post("/api/v1/reviews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(review))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		JSONObject jsonObject = new JSONObject(response);

		assertEquals(reviewResponseDTO.getId(), jsonObject.get(ID));
		assertEquals(reviewResponseDTO.getReview(), jsonObject.get(REVIEW));
		assertEquals(reviewResponseDTO.getAuthor(), jsonObject.get(AUTHOR));
		assertEquals(reviewResponseDTO.getReviewSource().toString(), jsonObject.get(REVIEW_SOURCE));
		assertEquals(reviewResponseDTO.getRating(), jsonObject.get(RATING));
		assertEquals(reviewResponseDTO.getTitle(), jsonObject.get(TITLE));
		assertEquals(reviewResponseDTO.getProductName(), jsonObject.get(PRODUCT_NAME));
		assertEquals(reviewResponseDTO.getReviewedDate().toString(), jsonObject.get(REVIEWED_DATE));
	}

	@Test
	void test_createReview_Exception_Wrong_Date_Type() throws Exception {
		String review = "{ \"review\": \"Pero deberia de poder cambiarle el idioma a alexa\", \"author\": \"WarcryxD\",\r\n"
				+ "    \"review_source\": \"iTunes\", \"rating\": 4,\r\n"
				+ "    \"title\": \"Excelente\", \"product_name\": \"Amazon Alexa\",\r\n"
				+ "    \"reviewed_date\": \"date\"\r\n"
				+ "}";

		mockMvc.perform(post("/api/v1/reviews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(review))
		.andExpect(status().isBadRequest());
	}

	@Test
	void test_getAllReviews() throws UnsupportedEncodingException, Exception {
		List<ReviewResponseDTO> list = getReviewResponseDTOs();

		when(reviewsService.getAllReviews(any())).thenReturn(list);

		String response = mockMvc.perform(get("/api/v1/reviews"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JSONArray jsonArray = new JSONArray(response);

		assertEquals(2, jsonArray.length());

		assertEquals(list.get(0).getId(), ((JSONObject)jsonArray.get(0)).get(ID));
		assertEquals(list.get(0).getReview(), ((JSONObject)jsonArray.get(0)).get(REVIEW));
		assertEquals(list.get(0).getAuthor(), ((JSONObject)jsonArray.get(0)).get(AUTHOR));
		assertEquals(list.get(0).getReviewSource().toString(), ((JSONObject)jsonArray.get(0)).get(REVIEW_SOURCE));
		assertEquals(list.get(0).getRating(), ((JSONObject)jsonArray.get(0)).get(RATING));
		assertEquals(list.get(0).getTitle(), ((JSONObject)jsonArray.get(0)).get(TITLE));
		assertEquals(list.get(0).getProductName(), ((JSONObject)jsonArray.get(0)).get(PRODUCT_NAME));
		assertEquals(list.get(0).getReviewedDate().toString(), ((JSONObject)jsonArray.get(0)).get(REVIEWED_DATE));

		assertEquals(list.get(1).getId(), ((JSONObject)jsonArray.get(1)).get(ID));
		assertEquals(list.get(1).getReview(), ((JSONObject)jsonArray.get(1)).get(REVIEW));
		assertEquals(list.get(1).getAuthor(), ((JSONObject)jsonArray.get(1)).get(AUTHOR));
		assertEquals(list.get(1).getReviewSource().toString(), ((JSONObject)jsonArray.get(1)).get(REVIEW_SOURCE));
		assertEquals(list.get(1).getRating(), ((JSONObject)jsonArray.get(1)).get(RATING));
		assertEquals(list.get(1).getTitle(), ((JSONObject)jsonArray.get(1)).get(TITLE));
		assertEquals(list.get(1).getProductName(), ((JSONObject)jsonArray.get(1)).get(PRODUCT_NAME));
		assertEquals(list.get(1).getReviewedDate().toString(), ((JSONObject)jsonArray.get(1)).get(REVIEWED_DATE));
	}

	@Test
	void test_getAllreviews_Exception() throws UnsupportedEncodingException, Exception {
		when(reviewsService.getAllReviews(any())).thenThrow(new RuntimeException());

		String response = mockMvc.perform(get("/api/v1/reviews"))
				.andExpect(status().is5xxServerError())
				.andReturn().getResponse().getContentAsString();

		assertEquals("Something went wrong!", response);
	}

	@Test
	void test_getAverageRating() throws UnsupportedEncodingException, Exception {
		List<MonthlyRatingProjection> list = getMonthlyRatingProjections();

		when(reviewsService.getAverageRatingByStoreType(any())).thenReturn(list);

		String response = mockMvc.perform(get("/api/v1/reviews/average-rating/store-type/iTunes"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JSONArray jsonArray = new JSONArray(response);

		assertEquals(2, jsonArray.length());

		assertEquals(list.get(0).getRating(), ((JSONObject)jsonArray.get(0)).get(RATING));
		assertEquals(list.get(0).getMonth(), ((JSONObject)jsonArray.get(0)).get("month"));
		assertEquals(list.get(0).getYear(), ((JSONObject)jsonArray.get(0)).get("year"));

		assertEquals(list.get(1).getRating(), ((JSONObject)jsonArray.get(1)).get(RATING));
		assertEquals(list.get(1).getMonth(), ((JSONObject)jsonArray.get(1)).get("month"));
		assertEquals(list.get(1).getYear(), ((JSONObject)jsonArray.get(1)).get("year"));
	}

	@Test
	void test_getAverageRating_Exception() throws UnsupportedEncodingException, Exception {
		String response = mockMvc.perform(get("/api/v1/reviews/average-rating/store-type/iTune"))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();

		assertEquals("One or more parameters supplied by you is incorrect.", response);
	}

	@Test
	void test_getTotalRatings() throws UnsupportedEncodingException, Exception {
		List<TotalRatingProjection> list = getTotalRatingProjections();

		when(reviewsService.getTotalRatings()).thenReturn(list);

		String response = mockMvc.perform(get("/api/v1/reviews/total-ratings"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();

		JSONArray jsonArray = new JSONArray(response);

		assertEquals(2, jsonArray.length());

		assertEquals(list.get(0).getRating(), ((JSONObject)jsonArray.get(0)).get(RATING));
		assertEquals(list.get(0).getTotalRatings(), ((JSONObject)jsonArray.get(0)).get("totalRatings"));

		assertEquals(list.get(1).getRating(), ((JSONObject)jsonArray.get(1)).get(RATING));
		assertEquals(list.get(1).getTotalRatings(), ((JSONObject)jsonArray.get(1)).get("totalRatings"));
	}

	@Test
	void test_getTotalRatings_Exception() throws UnsupportedEncodingException, Exception {
		when(reviewsService.getTotalRatings()).thenThrow(new RuntimeException());

		String response = mockMvc.perform(get("/api/v1/reviews/total-ratings"))
				.andExpect(status().is5xxServerError())
				.andReturn().getResponse().getContentAsString();

		assertEquals("Something went wrong!", response);
	}
	
	@Test
	void test_createMultipleReviews() throws Exception {
		String reviews = "[\r\n"
				+ "    {\r\n"
				+ "        \"review\": \"Pero deberia de poder cambiarle el idioma a alexa\", \"author\": \"WarcryxD\",\r\n"
				+ "        \"review_source\": \"iTunes\", \"rating\": 4, \"title\": \"Excelente\",\r\n"
				+ "        \"product_name\": \"Amazon Alexa\", \"reviewed_date\": \"2018-01-12T02:27:03.000Z\"\r\n"
				+ "    },\r\n"
				+ "    {\r\n"
				+ "        \"review\": \"review\",\r\n"
				+ "        \"author\": \"author\", \"review_source\": \"GooglePlayStore\", \"rating\": 5,\r\n"
				+ "        \"title\": \"title\", \"product_name\": \"product_name\",\r\n"
				+ "        \"reviewed_date\": \"2024-02-02T13:07:42.000Z\"\r\n"
				+ "    }\r\n"
				+ "]";

		List<ReviewResponseDTO> reviewResponseDTOs = getReviewResponseDTOs();

		when(reviewsService.createMultipleReviews(any())).thenReturn(reviewResponseDTOs);

		String response = mockMvc.perform(post("/api/v1/reviews/multiple")
				.contentType(MediaType.APPLICATION_JSON)
				.content(reviews))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		JSONArray jsonArray = new JSONArray(response);
		
		assertEquals(reviewResponseDTOs.get(0).getId(), ((JSONObject)jsonArray.get(0)).get(ID));
		assertEquals(reviewResponseDTOs.get(0).getReview(), ((JSONObject)jsonArray.get(0)).get(REVIEW));
		assertEquals(reviewResponseDTOs.get(0).getAuthor(), ((JSONObject)jsonArray.get(0)).get(AUTHOR));
		assertEquals(reviewResponseDTOs.get(0).getReviewSource().toString(), ((JSONObject)jsonArray.get(0)).get(REVIEW_SOURCE));
		assertEquals(reviewResponseDTOs.get(0).getRating(), ((JSONObject)jsonArray.get(0)).get(RATING));
		assertEquals(reviewResponseDTOs.get(0).getTitle(), ((JSONObject)jsonArray.get(0)).get(TITLE));
		assertEquals(reviewResponseDTOs.get(0).getProductName(), ((JSONObject)jsonArray.get(0)).get(PRODUCT_NAME));
		assertEquals(reviewResponseDTOs.get(0).getReviewedDate().toString(), ((JSONObject)jsonArray.get(0)).get(REVIEWED_DATE));

		assertEquals(reviewResponseDTOs.get(1).getId(), ((JSONObject)jsonArray.get(1)).get(ID));
		assertEquals(reviewResponseDTOs.get(1).getReview(), ((JSONObject)jsonArray.get(1)).get(REVIEW));
		assertEquals(reviewResponseDTOs.get(1).getAuthor(), ((JSONObject)jsonArray.get(1)).get(AUTHOR));
		assertEquals(reviewResponseDTOs.get(1).getReviewSource().toString(), ((JSONObject)jsonArray.get(1)).get(REVIEW_SOURCE));
		assertEquals(reviewResponseDTOs.get(1).getRating(), ((JSONObject)jsonArray.get(1)).get(RATING));
		assertEquals(reviewResponseDTOs.get(1).getTitle(), ((JSONObject)jsonArray.get(1)).get(TITLE));
		assertEquals(reviewResponseDTOs.get(1).getProductName(), ((JSONObject)jsonArray.get(1)).get(PRODUCT_NAME));
		assertEquals(reviewResponseDTOs.get(1).getReviewedDate().toString(), ((JSONObject)jsonArray.get(1)).get(REVIEWED_DATE));
	}

	private List<ReviewResponseDTO> getReviewResponseDTOs() {
		ReviewResponseDTO reviewResponseDTOOne = ReviewResponseDTO.builder()
				.id(1)
				.review("Pero deberia de poder cambiarle el idioma a alexa")
				.author("WarcryxD")
				.reviewSource(StoreType.iTunes)
				.rating(4)
				.title("Excelente")
				.productName("Amazon Alexa")
				.reviewedDate(LocalDateTime.of(2018, 1, 12, 2, 27, 03))
				.build();

		ReviewResponseDTO reviewResponseDTOTwo = ReviewResponseDTO.builder()
				.id(2)
				.review(REVIEW)
				.author(AUTHOR)
				.reviewSource(StoreType.GooglePlayStore)
				.rating(5)
				.title(TITLE)
				.productName(PRODUCT_NAME)
				.reviewedDate(LocalDateTime.of(2024, 2, 2, 13, 7, 42))
				.build();

		return List.of(reviewResponseDTOOne, reviewResponseDTOTwo);
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