package specifications;

import Helpers.RequestHelper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import model.User;

public class RequestSpecifications{

    public static RequestSpecification useJWTAuthentication(){
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", "Bearer " + RequestHelper.getAuthToken());
        return builder.build();
    }

    public static RequestSpecification useInvalidJWTAuthentication(){
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", "Bearer InvalidTokenValue");
        return builder.build();
    }

    public static RequestSpecification useBasicAuthentication(){
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", "Basic dGVzdHVzZXI6dGVzdHBhc3M=");
        //builder.addHeader("Authorization", "Basic b3N5c2VieUB0ZXN0ZW1haWwuY29tOnBhc3N3b3Jk");
        return builder.build();
    }
}
