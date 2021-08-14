import model.Comment;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import specifications.RequestSpecifications;
import specifications.ResponseSpecifications;
import sun.misc.Request;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;

public class CommentsTests extends Base{

    //v1.POST("/comment/:postid"
    @Test(description = "Test that creates a comment in a post", groups = "usePost")
    public void testCreateComment(){
        Comment comment = new Comment("Comment author", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam at sapien efficitur, pharetra orci et, volutpat lorem. Vestibulum aliquam, tortor sed efficitur volutpat, ipsum elit tempor massa, at feugiat mi mi sed nisi.");

        given()
            .spec(RequestSpecifications.useBasicAuthentication())
            .body(comment)
        .when()
            .post("/v1/comment/"+postId)
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse())
            .body("message", Matchers.equalTo("Comment created"));
    }

    @Test(description = "Test that creates a comment in an invalid post")
    public void testCreateCommentOnInvalidPost(){
        given()
            .spec(RequestSpecifications.useBasicAuthentication())
            .body(new Comment("",""))
        .when()
            .post("/v1/comment/invalidPost")
        .then()
            .statusCode(406)
            .body("message", Matchers.equalTo("PostID is missing"));
    }


    //v1.GET("/comments/:postid"
    @Test(description = "Test that retrieve all comments in a post", groups = "useComment")
    public void testGetAllComments(){
        createComment();

        //System.out.println("En la prueba tengo el comment id " + commentId + " del post " + postId);

        given()
            .spec(RequestSpecifications.useBasicAuthentication())
        .when()
            .get("/v1/comments/" + postId)
        .then()
            .statusCode(200)
            .body("results[0].data[0].id", Matchers.equalTo(commentId));

        deleteComment();
    }


    @Test(description = "Test to get all comments using invalid user")
    public void testGetAllCommentsUsingInvalidPost(){
        given()
            .spec(RequestSpecifications.useBasicAuthentication())
        .when()
            .get("/v1/comments/invaliEndPoint")
        .then()
            .statusCode(200)
            .body(Matchers.blankOrNullString());
    }

    //v1.GET("/comment/:postid/:id"
    @Test(description = "Test to retrieve a comment by Id", groups = "useComment")
    public void testGetACommentbyId(){
        //createComment();

        System.out.println("In the test, the used post is " + postId);

        given()
            .spec(RequestSpecifications.useBasicAuthentication())
        .when()
            .get("/v1/comment/" + postId + "/" + commentId)
        .then()
            .statusCode(200)
            .body("data.id", Matchers.equalTo(commentId))
            .body("data.post_id", Matchers.equalTo(String.valueOf(postId)));

        //deleteComment();
    }

    @Test(description = "Test to retrieve a comment using an invalid id", groups = "usePost")
    public void testGetACommentUsingInvalidId(){
        given()
            .spec(RequestSpecifications.useBasicAuthentication())
        .when()
            .get("/v1/comment/" + postId + "/0")
        .then()
            .statusCode(404)
            .body("Message", Matchers.equalTo("Comment not found"));
    }

    //v1.PUT("/comment/:postid/:id"
    @Test(description = "Test that updates a comment by Id")
    public void testUpdateCommentById(){
        createComment();

        given()
            .spec(RequestSpecifications.useBasicAuthentication())
            .body(new Comment("Updated name", "updated comment"))
        .when()
            .put("/v1/comment/" + postId + "/" + commentId)
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse())
            .body("message", Matchers.equalTo("Comment updated"));

        deleteComment();
    }

    @Test(description = "Test that udpates an invalid comment")
    public void testUpdateInvalidComment(){
        createComment();

        given()
            .spec(RequestSpecifications.useBasicAuthentication())
            .body(new Comment("Updated name", "updated comment"))
        .when()
            .put("/v1/comment/" + postId + "/000")
        .then()
            .statusCode(406)
            .body("message", Matchers.equalTo("Comment could not be updated"));

        deleteComment();
    }

    //v1.DELETE("/comment/:postid/:id"
    @Test(description = "Test that deletes a comment")
    public void testDeleteComment(){
        createComment();

        given()
            .spec(RequestSpecifications.useBasicAuthentication())
        .when()
            .delete("/v1/comment/" + postId + "/" + commentId)
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse());
    }

    @Test(description = "Test that deletes an invalid comment", groups = "usePost")
    public void testDeleteInvalidComment(){
        given()
            .spec(RequestSpecifications.useBasicAuthentication())
        .when()
            .delete("/v1/comment/" + postId + "/000")
        .then()
            .statusCode(406)
            .body("message", Matchers.equalTo("Comment could not be deleted"))
            .body("error", Matchers.equalTo("Comment not found"));
    }
}
