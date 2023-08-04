package GeoWeatherPackage;

import Config.GeoWeatherConfig;
import Entities.CityData;
import Entities.Current;
import Entities.WeatherData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.ExecutionException;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
@RestClientTest
@ContextConfiguration(classes = GeoWeatherConfig.class)
@AutoConfigureMockRestServiceServer
@PropertySource("config.properties")
class GeoWeatherProviderImplTest {

    MockRestServiceServer mockRestServiceServer;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    GeoWeatherProvider geoWeatherProvider;
    @Value("${weather.APP_ID}") String APP_ID;

    @BeforeEach
    void setUp() {
        this.mockRestServiceServer=MockRestServiceServer.bindTo(restTemplate).ignoreExpectOrder(true).build();
    }
    @AfterEach
    void tearDown() {
        this.mockRestServiceServer.verify();
    }
    @Test
    void getWeatherDataAsyncShouldReturnExpectedValue() throws ExecutionException, InterruptedException, JsonProcessingException {
        String lat="59.9387";
        String lon="30.3162";
        Current current=new Current(1690056285,1689988570,1690052028,16.66f,16.42f,1002,78,12.81f,0,4,9000,2,230,9,null);
        WeatherData weatherData=new WeatherData(59.9387,30.3162,"Europe/Moscow",current,null);
        String weatherResponse = objectMapper.writeValueAsString(weatherData);
        final String URL_API = "https://api.openweathermap.org/data/2.5/onecall?";
        String url = URL_API + "lat=" + lat + "&lon=" + lon
                + "&exclude=minutely,hourly" + "&appid=" + APP_ID + "&units=metric";
        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(),requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(weatherResponse, MediaType.APPLICATION_JSON));
        WeatherData weather=geoWeatherProvider.getWeatherDataAsync(Double.parseDouble(lat),Double.parseDouble(lon)).get();
        Assertions.assertNotNull(weatherResponse);
        Assertions.assertEquals(16.66f,weather.getCurrent().getCurrentTemp());
    }
    @Test
    void getCityDataAsyncShouldReturnEmptyArray() throws JsonProcessingException, ExecutionException, InterruptedException {
        String cityRequest="aksdjfhlaskhfal";
        CityData[] mockedCities=new CityData[1];
        String cityResponse=objectMapper.writeValueAsString(mockedCities);
        final String URL_API = "http://api.openweathermap.org/geo/1.0/direct?q=";
        String url = URL_API + cityRequest + "&limit=3&appid=" + APP_ID;
        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(),requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(cityResponse, MediaType.APPLICATION_JSON));
        CityData[] cities=geoWeatherProvider.getCityDataAsync(cityRequest).get();
        Assertions.assertNotNull(cities);
        Assertions.assertNull(cities[0]);

    }
    @Test
    void getCityDataAsyncShouldReturnExpectedValue() throws ExecutionException, InterruptedException, JsonProcessingException {
        String cityRequest="Piter";
        CityData[] mockedCities=new CityData[]{new CityData("Piter",59.9387,30.3162,"Russia","St.Petersburg",null)
                ,new CityData("AnotherPiter",18.222,19.333,"Milky way","AlfaCentavra",null)
                ,new CityData("SunnyPiter",30.444,23.555,"SunnyCounrty","SunnyState",null)};
        String cityResponse=objectMapper.writeValueAsString(mockedCities);
        final String URL_API = "http://api.openweathermap.org/geo/1.0/direct?q=";
        String url = URL_API + cityRequest + "&limit=3&appid=" + APP_ID;
        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(),requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(cityResponse, MediaType.APPLICATION_JSON));
        CityData[] cities=geoWeatherProvider.getCityDataAsync(cityRequest).get();
        Assertions.assertNotNull(cities);
        Assertions.assertEquals("SunnyPiter",cities[2].getName());
    }
    @Test
    void testGetCityDataAsyncShouldReturnExpectedValue() throws JsonProcessingException, ExecutionException, InterruptedException {
        double lat=59.9387;
        double lon=30.3162;
        CityData[] mockedCity=new CityData[]{new CityData("Piter",59.9387,30.3162,"Russia","St.Petersburg",null)};
        String cityResponse=objectMapper.writeValueAsString(mockedCity);
        final String URL_API = "http://api.openweathermap.org/geo/1.0/reverse?lat=";
        String url = URL_API + lat + "&lon=" + lon + "&limit=1&appid=" + APP_ID;
        this.mockRestServiceServer.expect(ExpectedCount.manyTimes(),requestTo(url))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(cityResponse, MediaType.APPLICATION_JSON));
        CityData city=geoWeatherProvider.getCityDataAsync(lat,lon).get();
        Assertions.assertNotNull(city);
        Assertions.assertEquals("Piter",city.getName());
    }
}