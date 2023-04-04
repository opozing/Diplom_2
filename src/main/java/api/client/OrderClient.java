package api.client;

import io.restassured.response.ValidatableResponse;
import org.example.CreateOrderPOJO;
import org.example.IngredientResponsePOJO2;
import static io.restassured.RestAssured.given;
import static org.example.resources.Constants.*;


public class OrderClient {

    public IngredientResponsePOJO2 getIngredients() {
         return given()
                 .header("Content-type", "application/json")
                 .when().get(BASE_URL + INGREDIENTS_URL)
                 .body().as(IngredientResponsePOJO2.class);

    }

    public ValidatableResponse createOrder(CreateOrderPOJO createOrderJson, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(createOrderJson)
                .when().post(BASE_URL + ORDERS_URL)
                .then();
    }
}
