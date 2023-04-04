import api.client.OrderClient;
import api.client.UserClient;
import org.example.CreateOrderPOJO;
import org.example.CreateUserPOJO;
import org.example.IngredientResponsePOJO2;
import org.example.model.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.is;


public class GetOrdersCurrentUser extends RandomGenerator {
    private OrderClient orderClient;
    private IngredientResponsePOJO2 ingredientsList;
    private UserClient userClient;
    private String token;
    private List<String> ingredientsIdList;
    private List<Integer> orderNumberList;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        userClient = new UserClient();
        ingredientsIdList = new ArrayList<>();
        orderNumberList = new ArrayList<>();
    }

    @After
    public void tearDown() {
        if (!(token == null)) {
            userClient.delete(token);
        }
    }

    @Test
    public void canGetOrdersCurrentAuthorizedUser() {
        CreateUserPOJO createUserJson = new CreateUserPOJO( getRandom() + "@ya.ru", getRandom(),
                getRandom());
        token = userClient.create(createUserJson)
                .extract().body().path("accessToken");
        ingredientsList = orderClient.getIngredients();
        ingredientsIdList.add(ingredientsList.getData().get(0).get_id());
        CreateOrderPOJO createOrderJson = new CreateOrderPOJO(ingredientsIdList);

        orderNumberList.add(orderClient.createOrder(createOrderJson, token)
                .extract().body().path("order.number"));

        orderClient.getOrdersCurrentUser(token)
                .assertThat().statusCode(200)
                .body("orders.number", is(orderNumberList));
    }

    @Test
    public void canNotGetOrdersCurrentUnauthorizedUser() {
        orderClient.getOrdersCurrentUser("")
                .assertThat().statusCode(401)
                .body("message", is("You should be authorised"));
    }
}
