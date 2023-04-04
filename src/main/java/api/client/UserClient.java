package api.client;

import io.restassured.response.ValidatableResponse;
import org.example.CreateUserPOJO;
import org.example.LoginUserPOJO;

import static io.restassured.RestAssured.given;
import static org.example.resources.Constants.*;

public class UserClient {
    public ValidatableResponse create(CreateUserPOJO createUserJson) {
        return given()
                .header("Content-type", "application/json")
                .body(createUserJson)
                .when().post(BASE_URL + REGISTER_URL)
                .then();
    }

    public ValidatableResponse login(LoginUserPOJO loginUserJson) {
        return given()
                .header("Content-type", "application/json")
                .body(loginUserJson)
                .when().post(BASE_URL + LOGIN_URL)
                .then();
    }
    public ValidatableResponse delete(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when().delete(BASE_URL + USER_URL)
                .then();
    }

    public ValidatableResponse patch(String token, CreateUserPOJO createUserJson) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(createUserJson)
                .when().patch(BASE_URL + USER_URL)
                .then();
    }

    public ValidatableResponse get(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when().get(BASE_URL + USER_URL)
                .then();
    }
}
