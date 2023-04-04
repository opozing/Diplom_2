import api.client.UserClient;
import org.example.CreateUserPOJO;
import org.example.LoginUserPOJO;
import org.example.model.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;


public class LoginUserTest extends RandomGenerator {
    private UserClient userClient;
    private String token;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (!(token == null)) {
            userClient.delete(token);
        }
    }

    @Test
    public void canLoginWithCorrectUser() {
        CreateUserPOJO createUserJson = new CreateUserPOJO(getRandom() + "@ya.ru", getRandom(),
                getRandom());
        LoginUserPOJO loginUserJson = new LoginUserPOJO(createUserJson.getEmail(), createUserJson.getPassword());

        token = userClient.create(createUserJson)
                .extract().body().path("accessToken");

        userClient.login(loginUserJson)
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void canNotLoginWithWrongValues() {
        CreateUserPOJO createUserJson = new CreateUserPOJO(getRandom() + "@ya.ru", getRandom(),
                getRandom());
        LoginUserPOJO loginUserJson1 = new LoginUserPOJO(getRandom(), createUserJson.getPassword());
        LoginUserPOJO loginUserJson2 = new LoginUserPOJO(createUserJson.getEmail(), getRandom());
        LoginUserPOJO loginUserJson3 = new LoginUserPOJO(getRandom(), getRandom());

        token = userClient.create(createUserJson)
                .extract().body().path("accessToken");
        LoginUserPOJO[] userList = {loginUserJson1, loginUserJson2, loginUserJson3};

        for (LoginUserPOJO user : userList) {
            userClient.login(user)
                    .assertThat()
                    .statusCode(401)
                    .body("message", is("email or password are incorrect"));
        }
    }
}
