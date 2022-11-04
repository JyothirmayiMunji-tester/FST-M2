package LiveProject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Headers
    Map<String, String> headers = new HashMap<>();
    //Resource Path
    String resourcePath = "/api/users";

    //Create the Contract
    @Pact(consumer = "UserConsumer",provider = "UserProvider")
    public RequestResponsePact consumerTest(PactDslWithProvider builder){
        //Set the headers
        headers.put("Content-Type","application/json");

        //Create the request and response body
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id", 123)
                .stringType("firstName", "Jyothirmayi")
                .stringType("lastName", "Munji")
                .stringType("email","jyo.mouli@gmail.com");

        //Create the Contract
        return builder.given("A request to create a user")
                .uponReceiving("A request to create a user")
                    .method("Post")
                    .path(resourcePath)
                    .headers(headers)
                    .body(requestResponseBody)
                .willRespondWith()
                    .status(201)
                    .body(requestResponseBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider",port = "8282")
    public void consumerTest(){
        //Set the BaseURI
        String baseURI = "http://localhost:8282"+resourcePath;

        //Set the requestBody
        Map<String, Object> reqBody = new HashMap<>();
        reqBody.put("id", 123);
        reqBody.put("firstName", "Jyothirmayi");
        reqBody.put("lastName", "Munji");
        reqBody.put("email","jyo.mouli@gmail.com");

        //Generate response and assertion
        given().headers(headers).body(reqBody).log().all()
                .when().post(baseURI)
                .then().statusCode(201).log().all();
    }

}
