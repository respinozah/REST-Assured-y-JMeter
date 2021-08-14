package Helpers;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Article;
import model.Comment;
import model.Post;
import model.User;
import specifications.RequestSpecifications;
import static io.restassured.RestAssured.given;

public class RequestHelper {

    public static String TOKEN = "";

    public static String getAuthToken(){

        if(!TOKEN.equals("")){
            return TOKEN;
        }

        User testUser = new User("osyseby@testemail.com", "Test User","password");
        Response response = given().body(testUser).post("/v1/user/login");
        JsonPath jsonPath = response.jsonPath();
        TOKEN = jsonPath.get("token.access_token");
        return TOKEN;
    }

    public static int createRandomArticleAndGetId(){
        Article randomArticle = new Article("New article title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc felis quam, volutpat a pharetra luctus, facilisis sit amet ipsum. Aliquam porttitor iaculis urna et ultrices. Mauris aliquam augue velit, id condimentum quam varius blandit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam et tellus nisi. Donec aliquet odio sit amet nisl accumsan, in laoreet ligula euismod. Morbi tincidunt mauris ac turpis semper sagittis. Sed nec quam elit. Suspendisse finibus auctor neque facilisis feugiat.");
        Response response =
            given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .body(randomArticle)
            .when()
                .post("/v1/article");

        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get("id");
    }

    public static void cleanUpArticle(int id){
        Response response =
            given()
                .spec(RequestSpecifications.useJWTAuthentication())
            .delete("/v1/article/" + id);
    }

    public static int createRandomPostAndGetId(){
        Post randomPost = new Post("New post title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc felis quam, volutpat a pharetra luctus, facilisis sit amet ipsum. Aliquam porttitor iaculis urna et ultrices. Mauris aliquam augue velit, id condimentum quam varius blandit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam et tellus nisi. Donec aliquet odio sit amet nisl accumsan, in laoreet ligula euismod. Morbi tincidunt mauris ac turpis semper sagittis. Sed nec quam elit. Suspendisse finibus auctor neque facilisis feugiat.");

        Response response =
            given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .body(randomPost)
            .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get("id");
    }

    public static void cleanUpPost(int id){
        Response response =
            given()
                .spec(RequestSpecifications.useJWTAuthentication())
            .delete("/v1/post/" + id);
    }

    public static int createRandomCommentAndGetId(int postId) {
        Comment comment = new Comment("Comment author", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam at sapien efficitur, pharetra orci et, volutpat lorem. Vestibulum aliquam, tortor sed efficitur volutpat, ipsum elit tempor massa, at feugiat mi mi sed nisi.");

        Response response =
            given()
                .spec(RequestSpecifications.useBasicAuthentication())
                .body(comment)
            .when()
                .post("/v1/comment/" + postId);

        JsonPath jsonPath = response.jsonPath();

        System.out.println("Comment created in createRandomCommentAndGetId using post: " + postId);

        return jsonPath.get("id");
    }

    public static void cleanUpComment(int postId, int commentId) {
        Response response =
            given()
                .spec(RequestSpecifications.useBasicAuthentication())
            .delete("/v1/comment/" + postId + "/" + commentId);
    }
}
