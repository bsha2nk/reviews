package com.bsha2nk.reviews.controller;

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

		ReviewResponseDTO reviewResponseDTO = ReviewResponseDTO.builder()
				.id(1)
				.review("Pero deberia de poder cambiarle el idioma a alexa")
				.author("WarcryxD")
				.reviewSource(StoreType.iTunes)
				.rating(4)
				.title("Excelente")
				.productName("Amazon Alexa")
				.reviewedDate(LocalDateTime.of(2018, 1, 12, 2, 27, 03))
				.build();

		when(reviewsService.createReview(any())).thenReturn(reviewResponseDTO);

		String result = mockMvc.perform(post("/api/v1/reviews")
				.contentType(MediaType.APPLICATION_JSON)
				.content(review))
				.andExpect(status().isCreated())
				.andReturn().getResponse().getContentAsString();

		JSONObject jsonObject = new JSONObject(result);

		assertEquals(reviewResponseDTO.getId(), jsonObject.get("id"));
		assertEquals(reviewResponseDTO.getReview(), jsonObject.get("review"));
		assertEquals(reviewResponseDTO.getAuthor(), jsonObject.get("author"));
		assertEquals(reviewResponseDTO.getReviewSource().toString(), jsonObject.get("review_source"));
		assertEquals(reviewResponseDTO.getRating(), jsonObject.get("rating"));
		assertEquals(reviewResponseDTO.getTitle(), jsonObject.get("title"));
		assertEquals(reviewResponseDTO.getProductName(), jsonObject.get("product_name"));
		assertEquals(reviewResponseDTO.getReviewedDate().toString(), jsonObject.get("reviewed_date"));
	}

	@Test
	void test_createReview_Exception() throws Exception {
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
				.review("review")
				.author("author")
				.reviewSource(StoreType.GooglePlayStore)
				.rating(5)
				.title("title")
				.productName("product")
				.reviewedDate(LocalDateTime.of(2024, 2, 2, 13, 7, 42))
				.build();
		
		List<ReviewResponseDTO> list = List.of(reviewResponseDTOOne, reviewResponseDTOTwo);
		
		when(reviewsService.getAllReviews(any())).thenReturn(list);
		
		String result = mockMvc.perform(get("/api/v1/reviews"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		JSONArray jsonArray = new JSONArray(result);
		
		assertEquals(2, jsonArray.length());
		
		assertEquals(list.get(0).getId(), ((JSONObject)jsonArray.get(0)).get("id"));
		assertEquals(list.get(0).getReview(), ((JSONObject)jsonArray.get(0)).get("review"));
		assertEquals(list.get(0).getAuthor(), ((JSONObject)jsonArray.get(0)).get("author"));
		assertEquals(list.get(0).getReviewSource().toString(), ((JSONObject)jsonArray.get(0)).get("review_source"));
		assertEquals(list.get(0).getRating(), ((JSONObject)jsonArray.get(0)).get("rating"));
		assertEquals(list.get(0).getTitle(), ((JSONObject)jsonArray.get(0)).get("title"));
		assertEquals(list.get(0).getProductName(), ((JSONObject)jsonArray.get(0)).get("product_name"));
		assertEquals(list.get(0).getReviewedDate().toString(), ((JSONObject)jsonArray.get(0)).get("reviewed_date"));
		
		assertEquals(list.get(1).getId(), ((JSONObject)jsonArray.get(1)).get("id"));
		assertEquals(list.get(1).getReview(), ((JSONObject)jsonArray.get(1)).get("review"));
		assertEquals(list.get(1).getAuthor(), ((JSONObject)jsonArray.get(1)).get("author"));
		assertEquals(list.get(1).getReviewSource().toString(), ((JSONObject)jsonArray.get(1)).get("review_source"));
		assertEquals(list.get(1).getRating(), ((JSONObject)jsonArray.get(1)).get("rating"));
		assertEquals(list.get(1).getTitle(), ((JSONObject)jsonArray.get(1)).get("title"));
		assertEquals(list.get(1).getProductName(), ((JSONObject)jsonArray.get(1)).get("product_name"));
		assertEquals(list.get(1).getReviewedDate().toString(), ((JSONObject)jsonArray.get(1)).get("reviewed_date"));
	}
	
	@Test
	void test_getAllreviews_Exception() throws UnsupportedEncodingException, Exception {
		when(reviewsService.getAllReviews(any())).thenThrow(new RuntimeException());

		String result = mockMvc.perform(get("/api/v1/reviews"))
				.andExpect(status().is5xxServerError())
				.andReturn().getResponse().getContentAsString();
		
		assertEquals("Something went wrong!", result);
	}

	@Test
	void test_getAverageRating() throws UnsupportedEncodingException, Exception {
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
		
		List<MonthlyRatingProjection> list = List.of(ratingOne, ratingTwo);
		
		when(reviewsService.getAverageRatingByStoreType(any())).thenReturn(list);
		
		String result = mockMvc.perform(get("/api/v1/reviews/average-rating/store-type/iTunes"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		JSONArray jsonArray = new JSONArray(result);
		
		assertEquals(2, jsonArray.length());
		
		assertEquals(list.get(0).getRating(), ((JSONObject)jsonArray.get(0)).get("rating"));
		assertEquals(list.get(0).getMonth(), ((JSONObject)jsonArray.get(0)).get("month"));
		assertEquals(list.get(0).getYear(), ((JSONObject)jsonArray.get(0)).get("year"));
		
		assertEquals(list.get(1).getRating(), ((JSONObject)jsonArray.get(1)).get("rating"));
		assertEquals(list.get(1).getMonth(), ((JSONObject)jsonArray.get(1)).get("month"));
		assertEquals(list.get(1).getYear(), ((JSONObject)jsonArray.get(1)).get("year"));
	}
	
	@Test
	void test_getAverageRating_Exception() throws UnsupportedEncodingException, Exception {
		String result = mockMvc.perform(get("/api/v1/reviews/average-rating/store-type/iTune"))
				.andExpect(status().isBadRequest())
				.andReturn().getResponse().getContentAsString();
		
		assertEquals("One or more parameters supplied by you is incorrect.", result);
	}
	
	@Test
	void test_getTotalRatings() throws UnsupportedEncodingException, Exception {
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
		
		List<TotalRatingProjection> list = List.of(categoryFive, categoryTwo);
		
		when(reviewsService.getTotalRatings()).thenReturn(list);
		
		String result = mockMvc.perform(get("/api/v1/reviews/total-ratings"))
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		
		JSONArray jsonArray = new JSONArray(result);
		
		assertEquals(2, jsonArray.length());
		
		assertEquals(list.get(0).getRating(), ((JSONObject)jsonArray.get(0)).get("rating"));
		assertEquals(list.get(0).getTotalRatings(), ((JSONObject)jsonArray.get(0)).get("totalRatings"));
		
		assertEquals(list.get(1).getRating(), ((JSONObject)jsonArray.get(1)).get("rating"));
		assertEquals(list.get(1).getTotalRatings(), ((JSONObject)jsonArray.get(1)).get("totalRatings"));
	}
	
	@Test
	void test_getTotalRatings_Exception() throws UnsupportedEncodingException, Exception {
		when(reviewsService.getTotalRatings()).thenThrow(new RuntimeException());

		String result = mockMvc.perform(get("/api/v1/reviews/total-ratings"))
				.andExpect(status().is5xxServerError())
				.andReturn().getResponse().getContentAsString();
		
		assertEquals("Something went wrong!", result);
	}

}