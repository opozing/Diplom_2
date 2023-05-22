package api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.CreateUserPOJO;
import org.example.LoginUserPOJO;
import static io.restassured.RestAssured.given;
import static org.example.resources.Constants.*;


public class UserClient {

    @Step("Create user")
    public ValidatableResponse create(CreateUserPOJO createUserJson) {
        return given()
                .header("Content-type", "application/json")
                .body(createUserJson)
                .when().post(BASE_URL + REGISTER_URL)
                .then();
    }

    @Step("Login user")
    public ValidatableResponse login(LoginUserPOJO loginUserJson) {
        return given()
                .header("Content-type", "application/json")
                .body(loginUserJson)
                .when().post(BASE_URL + LOGIN_URL)
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse delete(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when().delete(BASE_URL + USER_URL)
                .then();
    }

    @Step("Update user")
    public ValidatableResponse patch(String token, CreateUserPOJO createUserJson) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(createUserJson)
                .when().patch(BASE_URL + USER_URL)
                .then();
    }

    @Step("Get current user")
    public ValidatableResponse get(String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when().get(BASE_URL + USER_URL)
                .then();
    }
}
