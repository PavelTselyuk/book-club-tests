package api;

import io.qameta.allure.Step;
import models.clubs.ClubReviewModel;
import models.reviews.ReviewBodyModel;
import models.reviews.ReviewsListResponseModel;
import net.datafaker.Faker;

import static io.restassured.RestAssured.given;
import static specs.reviews.ReviewsSpec.duplicateReviewResponseSpec;
import static specs.reviews.ReviewsSpec.reviewsRequestSpec;
import static specs.reviews.ReviewsSpec.successfulCreateReviewResponseSpec;
import static specs.reviews.ReviewsSpec.successfulDeleteReviewResponseSpec;
import static specs.reviews.ReviewsSpec.successfulGetReviewResponseSpec;
import static specs.reviews.ReviewsSpec.successfulReviewsListResponseSpec;
import static specs.reviews.ReviewsSpec.successfulUpdateReviewResponseSpec;

public class ReviewsApiClient {

    private final Faker faker = new Faker();

    @Step("[API] Получение списка отзывов GET /clubs/reviews/")
    public ReviewsListResponseModel getReviews() {
        return given(reviewsRequestSpec)
                .when()
                .get("/clubs/reviews/")
                .then()
                .spec(successfulReviewsListResponseSpec)
                .extract()
                .as(ReviewsListResponseModel.class);
    }

    @Step("[API] Получение отзыва по id GET /clubs/reviews/{reviewId}/")
    public ClubReviewModel getReview(int reviewId) {
        return given(reviewsRequestSpec)
                .when()
                .get("/clubs/reviews/{reviewId}/", reviewId)
                .then()
                .spec(successfulGetReviewResponseSpec)
                .extract()
                .as(ClubReviewModel.class);
    }

    @Step("[API] Создание отзыва POST /clubs/reviews/")
    public ClubReviewModel createReview(String accessToken, ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(successfulCreateReviewResponseSpec)
                .extract()
                .as(ClubReviewModel.class);
    }

    @Step("[API] Создание рандомного отзыва для клуба {clubId}")
    public ClubReviewModel createRandomReview(String accessToken, int clubId) {
        return createReview(accessToken, randomReviewBody(clubId));
    }

    @Step("[API] Повторное создание отзыва на тот же клуб — ожидаем 500")
    public void createDuplicateReview(String accessToken, ReviewBodyModel body) {
        given(reviewsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(duplicateReviewResponseSpec);
    }

    @Step("[API] Полное обновление отзыва PUT /clubs/reviews/{reviewId}/")
    public ClubReviewModel updateReview(String accessToken, int reviewId, ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .put("/clubs/reviews/{reviewId}/", reviewId)
                .then()
                .spec(successfulUpdateReviewResponseSpec)
                .extract()
                .as(ClubReviewModel.class);
    }

    @Step("[API] Частичное обновление отзыва PATCH /clubs/reviews/{reviewId}/")
    public ClubReviewModel patchReview(String accessToken, int reviewId, ReviewBodyModel body) {
        return given(reviewsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .patch("/clubs/reviews/{reviewId}/", reviewId)
                .then()
                .spec(successfulUpdateReviewResponseSpec)
                .extract()
                .as(ClubReviewModel.class);
    }

    @Step("[API] Удаление отзыва DELETE /clubs/reviews/{reviewId}/")
    public void deleteReview(String accessToken, int reviewId) {
        given(reviewsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .delete("/clubs/reviews/{reviewId}/", reviewId)
                .then()
                .spec(successfulDeleteReviewResponseSpec);
    }

    public ReviewBodyModel randomReviewBody(int clubId) {
        return new ReviewBodyModel(
                clubId,
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 5),
                faker.number().numberBetween(1, 500)
        );
    }
}
