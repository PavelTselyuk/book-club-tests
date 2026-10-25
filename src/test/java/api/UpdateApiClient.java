package api;

import models.update.FailureUpdateResponseBodyModel;
import models.update.SuccessfulUpdateResponseBodyModel;
import models.update.UpdateBodyModel;

import static io.restassured.RestAssured.given;
import static specs.update.UpdateSpec.*;

public class UpdateApiClient {
    public SuccessfulUpdateResponseBodyModel successfullUpdate(UpdateBodyModel updateBody) {
        String token = updateBody.access();
        return given(updateRequestSpec)
                .auth().oauth2(token)
                .body(updateBody)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateResponseSpec)
                .extract().as(SuccessfulUpdateResponseBodyModel.class);
    }

    public FailureUpdateResponseBodyModel failureUpdate(UpdateBodyModel updateBody) {
        String token = updateBody.access();
        return given(updateRequestSpec)
                .auth().oauth2(token)
                .body(updateBody)
                .when()
                .put("/users/me/")
                .then()
                .spec(failureUpdateResponseSpec)
                .extract().as(FailureUpdateResponseBodyModel.class);
    }
}

