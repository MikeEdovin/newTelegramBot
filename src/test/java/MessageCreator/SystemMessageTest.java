package MessageCreator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;

import static org.junit.jupiter.api.Assertions.*;

class SystemMessageTest {


    @BeforeEach
    void setUp() {


    }

    @Test
    void getSendMessage() {
    }

    @Test
    void getFormattedTimeShouldReturnExpectedValue() {
        ZonedDateTime zdt=ZonedDateTime
                .of(LocalDate.of(2023,8,10),
                        LocalTime.of(13,44,52),
                        ZoneId.of("Europe/Moscow"));
        long time=zdt.getLong(ChronoField.INSTANT_SECONDS);
        String formattedTime=SystemMessage.getFormattedTime(time,"Europe/Moscow");
        Assertions.assertNotNull(formattedTime);
        Assertions.assertEquals("13:44:52",formattedTime);
    }

    @Test
    void getFormattedDateShouldReturnExpectedValue() {
        ZonedDateTime zdt=ZonedDateTime
                .of(LocalDate.of(2023,8,10),
                        LocalTime.of(13,44,52),
                        ZoneId.of("Europe/Moscow"));
        long time=zdt.getLong(ChronoField.INSTANT_SECONDS);
        String formattedDate=SystemMessage.getFormattedDate(time,"Europe/Moscow");
        Assertions.assertNotNull(formattedDate);
        Assertions.assertEquals("10-08-23",formattedDate);
    }
}