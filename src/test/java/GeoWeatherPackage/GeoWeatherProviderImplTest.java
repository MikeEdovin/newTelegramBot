package GeoWeatherPackage;

import Config.GeoWeatherConfig;
import Entities.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
@ExtendWith(SpringExtension.class)
//@Import(GeoWeatherConfig.class)
//@RestClientTest(GeoWeatherProviderImpl.class)
@SpringBootTest(classes = GeoWeatherProviderImpl.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebClient(registerRestTemplate = true)
@AutoConfigureMockRestServiceServer
@PropertySource("test.properties")
class GeoWeatherProviderImplTest {

    MockRestServiceServer mockRestServiceServer;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Value("${owm.lat}") String lat;
    @Value("${owm.lon}") String lon;
    @Value("${weather.APP_ID}") String APP_ID;
    @Value("${owm.response}") String response;


    GeoWeatherProviderImplTest() throws JsonProcessingException {
    }


    @BeforeEach
    void setUp() {
        mockRestServiceServer=MockRestServiceServer.createServer(restTemplate);
        //this.mockRestServiceServer.reset();
        System.out.println("lat "+lat);
    }
    @AfterEach
    void tearDown() {
        this.mockRestServiceServer.verify();
    }
    @Test
    void getWeatherDataAsyncShouldReturnExpectedValue() throws ExecutionException, InterruptedException {
        final String URL_API = "https://api.openweathermap.org/data/2.5/onecall?";
        String url = URL_API + "lat=" + lat + "&lon=" + lon
                + "&exclude=minutely,hourly" + "&appid=" + APP_ID + "&units=metric";
        this.mockRestServiceServer.expect(requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(response, MediaType.APPLICATION_JSON));
        WeatherData weather=geoWeatherProvider.getWeatherDataAsync(Double.parseDouble(lat),Double.parseDouble(lon)).get();
        Assertions.assertEquals(6.04,weather.getCurrent().getCurrentTemp());


    }

    @Test
    void getCityDataAsync() {
    }

    @Test
    void testGetCityDataAsync() {
    }
}