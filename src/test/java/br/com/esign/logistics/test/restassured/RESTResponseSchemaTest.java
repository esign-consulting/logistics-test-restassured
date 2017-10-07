/*
 * The MIT License
 *
 * Copyright 2015 Esign Consulting Ltda.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.esign.logistics.test.restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author gustavomunizdocarmo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RESTResponseSchemaTest {
    
    @Before
    public void setup() {
        String host = System.getProperty("server.host");
        if (host != null && !host.isEmpty())
            RestAssured.baseURI = host;
        String port = System.getProperty("server.port");
        if (port != null && !port.isEmpty())
            RestAssured.port = Integer.valueOf(port);
        RestAssured.basePath = "/logistics/api/maps";
    }
    
    /**
     * Creates a map.
     */
    @Test
    public void testA() {
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"REST-assured JSON Schema Test\"}")
            .when()
                .post()
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("map-response-schema.json"));
    }
    
    /**
     * Checks map existence.
     */
    @Test
    public void testB() {
        RestAssured
            .when()
                .get()
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("map-collection-response-schema.json"));
    }
    
    /**
     * Checks the unique map constraint.
     */
    @Test
    public void testC() {
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"REST-assured JSON Schema Test\"}")
            .when()
                .post()
            .then()
                .statusCode(500)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("bad-response-schema.json"));
    }
    
    /**
     * Creates routes for the map.
     */
    @Test
    public void testD() {
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"origin\": {\"name\": \"A\"}, \"destination\": {\"name\": \"B\"}, \"distance\": 10}")
                .pathParam("slug", "rest-assured-json-schema-test")
            .when()
                .post("/{slug}/routes")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("route-response-schema.json"));
        
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"origin\": {\"name\": \"B\"}, \"destination\": {\"name\": \"D\"}, \"distance\": 15}")
                .pathParam("slug", "rest-assured-json-schema-test")
            .when()
                .post("/{slug}/routes")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("route-response-schema.json"));
        
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"origin\": {\"name\": \"A\"}, \"destination\": {\"name\": \"C\"}, \"distance\": 20}")
                .pathParam("slug", "rest-assured-json-schema-test")
            .when()
                .post("/{slug}/routes")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("route-response-schema.json"));
    }
    
    /**
     * Checks the unique route constaint.
     */
    @Test
    public void testE() {
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"origin\": {\"name\": \"A\"}, \"destination\": {\"name\": \"C\"}, \"distance\": 20}")
                .pathParam("slug", "rest-assured-json-schema-test")
            .when()
                .post("/{slug}/routes")
            .then()
                .statusCode(500)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("bad-response-schema.json"));
    }
    
    /**
     * Removes last route.
     */
    @Test
    public void testF() {
        RestAssured
            .given()
                .pathParam("mapSlug", "rest-assured-json-schema-test")
                .pathParam("routeSlug", "a-c")
            .when()
                .delete("/{mapSlug}/routes/{routeSlug}")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("response-schema.json"));
    }
    
    /**
     * Tests best route functionality.
     */
    @Test
    public void testG() {
        RestAssured
            .given()
                .pathParam("mapSlug", "rest-assured-json-schema-test")
                .queryParam("originName", "A")
                .queryParam("destinationName", "D")
                .queryParam("autonomy", 10)
                .queryParam("gasPrice", 2.50)
            .when()
                .get("/{mapSlug}/bestRoute")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("best-route-response-schema.json"));
    }
    
    /**
     * Removes the map.
     */
    @Test
    public void testH() {
        RestAssured
            .given()
                .pathParam("slug", "rest-assured-json-schema-test")
            .when()
                .delete("/{slug}")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath("response-schema.json"));
    }
    
}
