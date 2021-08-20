import Helpers.RequestHelper;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

public class Base {

    protected int articleId;
    protected int postId;
    protected int commentId;

    @Parameters("host")

    @BeforeSuite(alwaysRun = true)
    public void setup(String host){
        RestAssured.baseURI = host;
    }


    //Articles
    @BeforeMethod(groups = "useArticle")
    public void createArticle(){ articleId = RequestHelper.createRandomArticleAndGetId(); }

    @AfterMethod(groups = "useArticle")
    public void deleteArticle(){ RequestHelper.cleanUpArticle(articleId); }

    //Posts
    @BeforeMethod(groups = "usePost")
    public void createPost(){
        postId = RequestHelper.createRandomPostAndGetId();
    }

    @AfterMethod(groups = "usePost")
    public void deletePost(){
        RequestHelper.cleanUpPost(postId);
    }

    //Comments
    @BeforeMethod(groups = "tagToMakeItWork")
    public void createComment(){
        postId = RequestHelper.createRandomPostAndGetId();
        commentId = RequestHelper.createRandomCommentAndGetId(postId);
    }

    @AfterMethod(groups = "useComment")
    public void deleteComment(){
        RequestHelper.cleanUpComment(postId, commentId);
        RequestHelper.cleanUpPost(postId);
    }
}
