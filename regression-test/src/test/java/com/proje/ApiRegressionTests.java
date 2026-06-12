package com.proje;

import io.restassured.RestAssured;
import io.restassured.config.LogConfig;          //logları bizim oluşturduğumuz stream'e yönlendirmek için
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;     // test öncesi hazırlık
import org.junit.jupiter.api.BeforeAll;    // test sonrası kayıt
import org.junit.jupiter.api.Test;         

import java.io.ByteArrayOutputStream;    // log toplamak için
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;             // dosya oluşturma/yazma işlemleri için
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;      // test çalışma zamanını loglamak için

import static io.restassured.RestAssured.given; 
import static org.hamcrest.Matchers.*;  // hamcrest matcher importları

public class ApiRegressionTests {

    // Detaylı logları hafızada toplamak için Java'nın standart çıktı akışını kullanıyoruz
    private static ByteArrayOutputStream logOutputStream = new ByteArrayOutputStream();
    private static PrintStream logPrintStream = new PrintStream(logOutputStream, true, StandardCharsets.UTF_8);

    @BeforeAll
    public static void setup() {
        // Test edilecek ana URL
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        
        // Rest Assured'a logları konsol yerine bizim oluşturduğumuz akışa (stream) yazmasını söylüyoruz
        RestAssured.config = RestAssuredConfig.config().logConfig(
                LogConfig.logConfig().defaultStream(logPrintStream)
        );

        logPrintStream.println("=== API REGRESSION TEST RUN - " + LocalDateTime.now() + " ===\n");
    }

    // Test Senaryosu 1: GET İsteği Kontrolü
    @Test
    public void testGetSingleUser_ValidateResponse() {
        logPrintStream.println("[STARTING TEST] testGetSingleUser_ValidateResponse");

        given()
            .contentType(ContentType.JSON)
            .log().all() // Giden HTTP isteğinin tüm detaylarını (URL, Header vb.) loga yazar
        .when()
            .get("/users/1")
        .then()
            .log().all() // Sunucudan gelen yanıtın tüm detaylarını (Status, Body vb.) loga yazar
            .statusCode(200)
            .body("id", equalTo(1))
            .body("username", equalTo("Bret"))
            .body("email", equalTo("Sincere@april.biz"))
            .time(lessThan(3000L)); // süre sınırı (3 saniye)

        logPrintStream.println("[PASSED] testGetSingleUser_ValidateResponse completed successfully.");
        logPrintStream.println("------------------------------------------------------------------\n");
    }

    // Test Senaryosu 2: Request Body İçeren POST İsteği Kontrolü
    @Test
    public void testCreateUser_WithRequestBody() {
        logPrintStream.println("[STARTING TEST] testCreateUser_WithRequestBody");

        String requestBody = "{\n" +
                "    \"title\": \"TestTitle\",\n" +
                "    \"body\": \"QA Automation Regression Test Content\",\n" +
                "    \"userId\": 1\n" +
                "}";

        given()
            .contentType(ContentType.JSON)
            .body(requestBody)
            .log().all() // Giden POST isteğini ve içindeki Request Body'yi loga yazar
        .when()
            .post("/posts")
        .then()
            .log().all() // Sunucudan dönen yanıtı ve üretilen JSON çıktısını loga yazar
            .statusCode(201)
            .body("title", equalTo("TestTitle"))
            .body("body", equalTo("QA Automation Regression Test Content"))
            .body("id", notNullValue())
            .time(lessThan(3000L)); // süre sınırı (3 saniye)

        logPrintStream.println("[PASSED] testCreateUser_WithRequestBody completed successfully.");
        logPrintStream.println("==================================================================");
    }

    // Tüm testler bittikten sonra detaylı logları .txt dosyasına kaydeder
    @AfterAll
    public static void saveLogsToFile() throws IOException {
        // target/rest-assured-logs klasörünü oluşturur
        Files.createDirectories(Paths.get("target/rest-assured-logs"));

        // Hafızadaki veriyi String formatına dönüştürüyoruz
        String finalLogs = logOutputStream.toString(StandardCharsets.UTF_8);

        // Hazırlanan detaylı log metnini dosyaya yazar
        Files.writeString(
                Paths.get("target/rest-assured-logs/api-regression-test-log.txt"),
                finalLogs,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }
}