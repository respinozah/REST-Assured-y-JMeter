import model.Article;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;
import specifications.RequestSpecifications;
import specifications.ResponseSpecifications;
import static io.restassured.RestAssured.given;


public class ArticlesTests extends Base{

    @Test(description = "Test to register an article.")
    public void testCreateArticle(){
        Article testArticle = new Article("randomTitle", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc felis quam, volutpat a pharetra luctus, facilisis sit amet ipsum. Aliquam porttitor iaculis urna et ultrices. Mauris aliquam augue velit, id condimentum quam varius blandit. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam et tellus nisi. Donec aliquet odio sit amet nisl accumsan, in laoreet ligula euismod. Morbi tincidunt mauris ac turpis semper sagittis. Sed nec quam elit. Suspendisse finibus auctor neque facilisis feugiat.");

        given()
            .spec(RequestSpecifications.useJWTAuthentication())
            .body(testArticle)
        .when()
            .post("/v1/article")
        .then()
            .spec(ResponseSpecifications.validatePositiveResponse())
            .body("message", Matchers.equalTo("Article created"));
    }

    @Test(description = "Test to retrieve all articles.", groups = "useArticle")
    public void testGetAllArticles(){
        given()
            .spec(RequestSpecifications.useJWTAuthentication())
        .when()
            .get("/v1/articles")
        .then()
            .statusCode(200)
            .body("results[0].data[0].id", Matchers.equalTo(articleId));
    }

    @Test(description = "Test to retrieve an article by id.", groups = "useArticle")
    public void testGetAnArticle(){
            given()
                .spec(RequestSpecifications.useJWTAuthentication())
            .when()
                .get("/v1/article/" + articleId)
            .then()
                .body("data.id", Matchers.equalTo(articleId));
    }
}
