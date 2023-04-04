import api.client.UserClient;
import org.example.CreateUserPOJO;
import org.example.LoginUserPOJO;
import org.example.model.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.is;


public class CreateUserTest extends RandomGenerator {
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
    public void canCreateCorrectUser() {
        CreateUserPOJO createUserJson = new CreateUserPOJO( getRandom() + "@ya.ru", getRandom(),
                getRandom());
        LoginUserPOJO loginUserJson = new LoginUserPOJO(createUserJson.getEmail(), createUserJson.getPassword());

        token = userClient.create(createUserJson)
                .statusCode(200)
                .extract().body().path("accessToken");

        userClient.login(loginUserJson)
                .statusCode(200)
                .assertThat().body("success", is(true));
    }

    @Test
    public void canNotCreateSameUser() {
        CreateUserPOJO createUserJson = new CreateUserPOJO( getRandom() + "@ya.ru", getRandom(),
                getRandom());

        token = userClient.create(createUserJson)
                .extract().body().path("accessToken");

        userClient.create(createUserJson)
                .statusCode(403)
                .assertThat().body("message", is("User already exists"));
    }

    @Test
    public void canNotCreateUserWithoutValue() {
        CreateUserPOJO createUserJson1 = new CreateUserPOJO( null, getRandom(), getRandom());
        CreateUserPOJO createUserJson2 = new CreateUserPOJO( getRandom() + "@ya.ru", null,
                getRandom());
        CreateUserPOJO createUserJson3 = new CreateUserPOJO( getRandom() + "@ya.ru", getRandom(),
                null);
        CreateUserPOJO[] userList = {createUserJson1, createUserJson2, createUserJson3};

        for (CreateUserPOJO user : userList ) {
            userClient.create(user)
                    .statusCode(403)
                    .assertThat().body("message", is("Email, password and name are required fields"));
        }
    }
}
