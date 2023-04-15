import api.client.OrderClient;
import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import org.example.CreateOrderPOJO;
import org.example.CreateUserPOJO;
import org.example.IngredientResponsePOJO2;
import org.example.model.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import static org.hamcrest.Matchers.*;


public class CreateOrderTest extends RandomGenerator {
    private OrderClient orderClient;
    private IngredientResponsePOJO2 ingredientsList;
    private UserClient userClient;
    private String token;
    private List<String> ingredientsIdList;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        ingredientsIdList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        if (!(token == null)) {
            userClient.delete(token);
        }
    }

    @Test
    @DisplayName("Can create order by authorized user")
    public void canCreateOrderAuthorizedUser() {
        CreateUserPOJO createUserJson = new CreateUserPOJO( getRandom() + "@ya.ru", getRandom(),
                getRandom());
        token = userClient.create(createUserJson)
                .extract().body().path("accessToken");
        ingredientsList = orderClient.getIngredients();
        ingredientsIdList.add(ingredientsList.getData().get(0).get_id());
        CreateOrderPOJO createOrderJson = new CreateOrderPOJO(ingredientsIdList);

        orderClient.createOrder(createOrderJson, token)
                .assertThat().statusCode(200)
                .body("name", is("Флюоресцентный бургер"));

        orderClient.getOrdersCurrentUser(token)
                .assertThat().statusCode(200)
                .body("orders", hasSize(1));
    }

    @Test
    @DisplayName("Can create order by unauthorized user")
    public void canCreateOrderUnauthorizedUser() {
        ingredientsList = orderClient.getIngredients();
        ingredientsIdList.add(ingredientsList.getData().get(0).get_id());
        CreateOrderPOJO createOrderJson = new CreateOrderPOJO(ingredientsIdList);

        orderClient.createOrder(createOrderJson, "")
                .assertThat().statusCode(200)
                .body("name", is("Флюоресцентный бургер"));
    }

    @Test
    @DisplayName("Can't create order without ingredients")
    public void canNotCreateOrderWithoutIngredients() {
        CreateUserPOJO createUserJson = new CreateUserPOJO( getRandom() + "@ya.ru", getRandom(),
                getRandom());
        token = userClient.create(createUserJson)
                .extract().body().path("accessToken");
        CreateOrderPOJO createOrderJson = new CreateOrderPOJO(ingredientsIdList);

        orderClient.createOrder(createOrderJson, token)
                .assertThat().statusCode(400)
                .body("message", is("Ingredient ids must be provided"));

        orderClient.getOrdersCurrentUser(token)
                .assertThat().statusCode(200)
                .body("orders", empty());
    }

    @Test
    @DisplayName("Can't create order with wrong ingredient ID")
    public void canNotCreateOrderWithWrongIngredient() {
        CreateUserPOJO createUserJson = new CreateUserPOJO( getRandom() + "@ya.ru", getRandom(),
                getRandom());
        token = userClient.create(createUserJson)
                .extract().body().path("accessToken");
        ingredientsIdList.add(getRandom());
        CreateOrderPOJO createOrderJson = new CreateOrderPOJO(ingredientsIdList);

        orderClient.createOrder(createOrderJson, token)
                .assertThat().statusCode(500);

        orderClient.getOrdersCurrentUser(token)
                .assertThat().statusCode(200)
                .body("orders", empty());
    }
}
