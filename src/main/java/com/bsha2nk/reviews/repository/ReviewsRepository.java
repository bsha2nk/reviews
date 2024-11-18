package com.bsha2nk.reviews.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import com.bsha2nk.reviews.entity.Review;
import com.bsha2nk.reviews.projection.MonthlyRatingProjection;
import com.bsha2nk.reviews.projection.TotalRatingProjection;

public interface ReviewsRepository extends JpaRepository<Review, Integer>, QueryByExampleExecutor<Review> {

	@Query(value = """
			SELECT
				ROUND(AVG(rating), 2) AS rating,
				MONTHNAME(reviewed_date) AS month,
				YEAR(reviewed_date) AS year
			FROM
				reviews
			WHERE
				review_source = :storeType
			GROUP BY
				MONTHNAME(reviewed_date),
				YEAR(reviewed_date)
			ORDER BY
				YEAR(reviewed_date)
			""", nativeQuery = true)
	public List<MonthlyRatingProjection> findAverageRatingByStoreType(String storeType);
	
	@Query(value = """
			SELECT 
				rating,
				COUNT(rating) AS totalRatings
			FROM
				reviews
			GROUP BY
				rating
			ORDER BY
				rating DESC
			""", nativeQuery = true)
	public List<TotalRatingProjection> findTotalRatings();

}