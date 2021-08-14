import model.Post;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import specifications.RequestSpecifications;
import specifications.ResponseSpecifications;
import static io.restassured.RestAssured.given;

public class PostsTests extends Base{

    //v1.POST("/post"
    @Test(description = "Test that creates a post.")
    public void testCreatePost(){
        Post post = new Post("Post title from create post test","Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc felis quam, volutpat a pharetra luctus, facilisis sit amet ipsum. Aliquam porttitor iaculis urna et ultrices. Mauris aliquam augue velit, id condimentum quam varius blandit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam et tellus nisi. Donec aliquet odio sit amet nisl accumsan, in laoreet ligula euismod. Morbi tincidunt mauris ac turpis semper sagittis. Sed nec quam elit. Suspendisse finibus auctor neque facilisis feugiat.");

        given()
            .spec(RequestSpecifications.useJWTAuthentication())
            .body(post)
        .when()
            .post("/v1/post")
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse())
            .body("message", Matchers.equalTo("Post created"));
    }

    @Test(description = "Test that validates a post missing title creation.")
    public void testCreatePostInvalidTitle(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
            .body(new Post("", "Post without title"))
        .when()
            .post("/v1/post")
        .then()
            .statusCode(406)
            .body("message", Matchers.equalTo("Invalid form"));
    }

    //v1.GET("/posts"
    @Test(description = "Test to get all posts.", groups = "usePost")
    public void testGetAllPosts(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
        .when()
            .get("/v1/posts")
        .then()
            .statusCode(200)
            .body("results[0].data[0].id", Matchers.equalTo(postId));
    }

    @Test(description = "Test to get all posts using invalid user.", groups = "usePost")
    public void testGetAllPostsUsingInvalidLogin(){
        given()
            .spec(RequestSpecifications.useInvalidJWTAuthentication())
        .when()
            .get("/v1/posts")
        .then()
            .statusCode(401)
            .body("message", Matchers.equalTo("Please login first"));
    }

    //v1.GET("/post/:id"
    @Test(description = "Test to retrieve a specific post.", groups = "usePost")
    public void testGetAPost(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
        .when()
            .get("/v1/post/" + postId)
        .then()
            .statusCode(200)
            .body("data.id", Matchers.equalTo(postId));
    }

    @Test(description = "Test to retrieve an invalid post.")
    public void testGetAnInvalidPost(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
        .when()
            .get("/v1/post/invalidPost")
        .then()
            .statusCode(404)
            .body("Message", Matchers.equalTo("Invalid parameter"));
    }

    //v1.PUT("/post/:id"
    @Test(description = "Test that updates post by id.", groups = "usePost")
    public void testUpdatePost(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
            .body(new Post("Post title (updated)", "Updated post content."))
        .when()
            .put("/v1/post/" + postId)
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse())
            .body("message", Matchers.equalTo("Post updated"));
    }

    @Test(description = "Test that updates an invalid post.")
    public void testUpdateInvalidPost(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
            .body(new Post("Post title (updated)", "Updated post content."))
        .when()
            .put("/v1/post/000")
        .then()
            .statusCode(406)
            .body("message", Matchers.equalTo("Post could not be updated"));
    }

    //v1.DELETE("/post/:id"
    @Test(description = "Test that deletes a post by id.", groups = "usePost")
    public void testDeletePost(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
        .when()
            .delete("/v1/post/" + postId)
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse())
            .body("message", Matchers.equalTo("Post deleted"));
    }

    @Test(description = "Test that deletes an invalid post.")
    public void testDeleteInvalidPost(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
        .when()
            .delete("/v1/post/invalidPost")
        .then()
            .statusCode(404)
            .body("message", Matchers.equalTo("Invalid parameter"));
    }
}
