package LiveProject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GitHubRestAssured {

    //Request specification
    RequestSpecification reqSpec;
    //String to hold SSH Key
    String sshKey;
    //Int to hold the id of SSH Key
    int idSshKey;

    @BeforeClass
    public void setUp(){
        reqSpec = new RequestSpecBuilder().setBaseUri("https://api.github.com/user/keys").
                addHeader("Content-Type", "application/json").
                addHeader("Authorization","token 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").build();
            }

    @Test(priority = 1)
    public void postReq() {
    Map<String, Object> reqBody = new HashMap<>();

        //Request Body with SSH Key
        reqBody.put("title", "TestAPIKey");
        reqBody.put("key","ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQC8oBrVwTjhA9F0iGoOJp8nOpf3+ARJah5FTiZQs4rRLW3YovurqlzO7gWgX1H56UIqKLGpByez4rQc0NtlDaJBKGR3oAcEwCPGcr4uA27oZUGkpsWGE7Kj9gK7XhJloX/Sky9Izk0WE9gwigMsegLVE05ht34rY4gzC8nD9Uo+Mq6dNmAKpt8sA6u9we6EkPjG3rWGcYmilOmGFPwnEyKPzM4dnoWwFG4xAgEXZYOiWTziNNTBclWZW4vY91ACh75zek07eE8pwTeiPRNHP4zXFlS2Fx9qUnT6Bx/O4rfUEjT6/IfHCsV9X2zL44rzSE9atsjrWTQFQFh2W3rYprxJ");

        // Generate response
        Response response = given().spec(reqSpec).body(reqBody).log().all().
                            when().post();

        System.out.println(response.getBody().asPrettyString());

        // Extract SSH Key id from response
        idSshKey = response.then().extract().path("id");
        System.out.println(idSshKey);

        //Assertion check
        response.then().statusCode(201);

    }

    @Test(priority = 2)
    public void getReq(){
        //Get the response with all the SSH keys linked to the account
        //Response requestResponse = given().spec(reqSpec).pathParam("idSshKey",idSshKey).log().all().when().get("/{idSshKey}");
        Response requestResponse = given().spec(reqSpec).pathParam("keyId",idSshKey).log().all().when().get("/{keyId}");
        System.out.println(requestResponse.getBody().asPrettyString());
        //Assertion check
        requestResponse.then().statusCode(200);

    }

    @Test(priority = 3)
    public void delReq(){
        Response deleteResponse = given().spec(reqSpec).pathParam("keyId", idSshKey).log().all().when().delete("/{keyId}");
        System.out.println(deleteResponse.getBody().asPrettyString());
        //Assertion check
        deleteResponse.then().statusCode(204);

    }
}



