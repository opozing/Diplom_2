import api.client.UserClient;
import io.qameta.allure.junit4.DisplayName;
import org.example.CreateUserPOJO;
import org.example.model.RandomGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.is;


public class UpdateUserTest extends RandomGenerator {
    private String token;
    private String token2;
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        if (!(token == null)) {
            userClient.delete(token);
        }
        if (!(token2 == null)) {
            userClient.delete(token2);
        }
    }

    @Test
    @DisplayName("Can update authorized user")
    public void canUpdateAuthorizedUser() {
        CreateUserPOJO createUserJson1 = new CreateUserPOJO(getRandom() + "@ya.ru", getRandom(),
                getRandom());
        CreateUserPOJO createUserJson2 = new CreateUserPOJO(getRandom() + "@ya.ru", createUserJson1.getPassword(),
                createUserJson1.getName());
        CreateUserPOJO createUserJson3 = new CreateUserPOJO(createUserJson2.getEmail(), getRandom(),
                createUserJson2.getName());
        CreateUserPOJO createUserJson4 = new CreateUserPOJO(createUserJson3.getEmail(),
                createUserJson3.getPassword(), getRandom());

        CreateUserPOJO[] userList = {createUserJson2, createUserJson3, createUserJson4};
        token = userClient.create(createUserJson1)
                .extract().body().path("accessToken");

        for (CreateUserPOJO user : userList) {
            userClient.patch(token, user)
                    .assertThat().statusCode(200)
                    .body("user.email", is(user.getEmail().toLowerCase()))
                    .body("user.name", is(user.getName()));

            userClient.get(token)
                    .assertThat().statusCode(200)
                    .body("user.email", is(user.getEmail().toLowerCase()))
                    .body("user.name", is(user.getName()));
        }
    }

    @Test
    @DisplayName("Can't update unauthorized user")
    public void canNotUpdateUnauthorizedUser() {
        CreateUserPOJO createUserJson1 = new CreateUserPOJO(getRandom() + "@ya.ru", getRandom(),
                getRandom());
        CreateUserPOJO createUserJson2 = new CreateUserPOJO(getRandom() + "@ya.ru",
                createUserJson1.getPassword(), createUserJson1.getName());
        CreateUserPOJO createUserJson3 = new CreateUserPOJO(createUserJson2.getEmail(), getRandom(),
                createUserJson2.getName());
        CreateUserPOJO createUserJson4 = new CreateUserPOJO(createUserJson3.getEmail(),
                createUserJson3.getPassword(), getRandom());
        CreateUserPOJO[] userList = {createUserJson2, createUserJson3, createUserJson4};

        token = userClient.create(createUserJson1)
                .extract().body().path("accessToken");

        for (CreateUserPOJO user : userList) {
            userClient.patch("", user)
                    .assertThat().statusCode(401)
                    .body("message", is("You should be authorised"));

            userClient.get(token)
                    .assertThat().statusCode(200)
                    .body("user.email", is(createUserJson1.getEmail().toLowerCase()))
                    .body("user.name", is(createUserJson1.getName()));
        }
    }

    @Test
    @DisplayName("Can't update if wrong user email")
    public void canNotUpdateWithAnotherUserEmail() {
        CreateUserPOJO createUserJson1 = new CreateUserPOJO(getRandom() + "@ya.ru", getRandom(),
                getRandom());
        CreateUserPOJO createUserJson2 = new CreateUserPOJO(getRandom() + "@ya.ru", getRandom(),
                getRandom());
        CreateUserPOJO createUserJson3 = new CreateUserPOJO(createUserJson1.getEmail(), getRandom(),
                getRandom());

        token = userClient.create(createUserJson1)
                .extract().body().path("accessToken");

        token2 = userClient.create(createUserJson2)
                .extract().body().path("accessToken");

        userClient.patch(token2, createUserJson3)
                .assertThat().statusCode(403)
                .body("message", is("User with such email already exists"));
    }
}
