package specs.reviews;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;

public class ReviewsSpec {

    public static RequestSpecification reviewsRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulReviewsListResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/reviews_list_response_schema.json"))
            .expectBody("count", notNullValue())
            .expectBody("count", greaterThanOrEqualTo(0))
            .expectBody("results", notNullValue())
            .build();

    public static ResponseSpecification successfulGetReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/review_response_schema.json"))
            .expectBody("id", notNullValue())
            .build();

    public static ResponseSpecification successfulCreateReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/review_response_schema.json"))
            .expectBody("id", notNullValue())
            .build();

    public static ResponseSpecification successfulUpdateReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/reviews/review_response_schema.json"))
            .expectBody("id", notNullValue())
            .build();

    public static ResponseSpecification successfulDeleteReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();

    public static ResponseSpecification duplicateReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(500)
            .build();
}
