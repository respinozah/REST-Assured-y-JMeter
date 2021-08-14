import Helpers.DataHelper;
import model.User;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import specifications.RequestSpecifications;
import specifications.ResponseSpecifications;
import static io.restassured.RestAssured.given;

public class AuthenticationTests extends Base{

    @Test(description = "Test that registers a new user.")
    public void testRegister(){
        User testUser = new User(DataHelper.generateRandomEmail(), "Test User","password");

        given()
            .body(testUser)
        .when()
            .post("/v1/user/register")
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse())
            .body("user.email", Matchers.equalTo(testUser.getEmail()))
            .body("user.name", Matchers.equalTo(testUser.getName()));

        //System.out.println("Usuario registrado: " + testUser.getEmail());
    }

    @Test(description = "Test to login an existing user.")
    public void testLogin(){
        User testUser = new User("osyseby@testemail.com", "Test User","password");

        given()
            .body(testUser)
        .when()
            .post("/v1/user/login")
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse())
            .body("token.access_token", Matchers.notNullValue())
            .body("user.email", Matchers.equalTo(testUser.getEmail()));
    }

    @Test(description = "Test to validate invalid user authentication.")
    public void testInvalidLogin(){
        User testUser = new User("nonExistingUser@testemail.com", "Test User","password");

        given()
            .body(testUser)
        .when()
            .post("/v1/user/login")
        .then()
            .statusCode(404)
            .body("message", Matchers.equalTo("Invalid login details"));
    }

    @Test(description = "Test that validates duplicate user registration.")
    public void testDuplicateRegister(){
        User testUser = new User("osyseby@testemail.com", "Test User","password");

        given()
            .body(testUser)
        .when()
            .post("/v1/user/register")
        .then()
            .statusCode(406)
            .body("message", Matchers.equalTo("User already exists"));
    }

    @Test(description= "Test to logout.")
    public void testLogOut(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
            //.spec(RequestSpecifications.useBasicAuthentication())
        .when()
            .get("/v1/user/logout")
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse())
            .body("message", Matchers.equalTo("Successfully logged out"));
    }

    @Test(description= "Test to logout invalid user.")
    public void testLogOutInvalidUser(){
        given()
                .spec(RequestSpecifications.useInvalidJWTAuthentication())
        .when()
            .get("/v1/user/logout")
        .then()
            .statusCode(400)
            .body("message", Matchers.equalTo("User not logged in"));
    }
}
