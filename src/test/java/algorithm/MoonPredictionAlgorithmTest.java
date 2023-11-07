package algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.rateforecaster.exception.InvalidPredictionDataException;
import ru.liga.rateforecaster.forecast.algorithm.moon.MoonPredictionAlgorithm;
import ru.liga.rateforecaster.model.CurrencyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoonPredictionAlgorithmTest {

    private MoonPredictionAlgorithm predictionAlgorithm;
    private LocalDate targetDate;

    @BeforeEach
    void setUp() {
        predictionAlgorithm = new MoonPredictionAlgorithm();
        targetDate = LocalDate.of(2023, 3, 15);
    }

    @Test
    void calculateRateForDate_WhenLastMonthDataExists_ReturnsPredictedRate() {
        List<CurrencyData> lastMonthData = new ArrayList<>();
        IntStream.rangeClosed(1, 30).forEach(day -> lastMonthData.add(new CurrencyData(LocalDate.of(2023, 3, day), new BigDecimal("1.0"))));
        double expectedRate = 1.0;

        MoonPredictionAlgorithm predictionAlgorithm = new MoonPredictionAlgorithm();
        CurrencyData result = predictionAlgorithm.calculateRateForDate(lastMonthData, targetDate);

        assertEquals(BigDecimal.valueOf(expectedRate), result.rate());
    }

    @Test
    void calculateRateForDate_WhenLastMonthDataDoesNotExist_ReturnsPredictedRate() {
        List<CurrencyData> lastMonthData = new ArrayList<>();
        IntStream.rangeClosed(1, 30).forEach(day -> lastMonthData.add(new CurrencyData(LocalDate.of(2023, 4, day), new BigDecimal("1.0"))));
        double expectedRate = 1.0;
        MoonPredictionAlgorithm predictionAlgorithm = new MoonPredictionAlgorithm();

        CurrencyData result = predictionAlgorithm.calculateRateForDate(lastMonthData, targetDate);

        assertEquals(BigDecimal.valueOf(expectedRate), result.rate());
    }

    @Test
    void calculateRateForDate_WhenNoDataAvailable_ThrowsInvalidPredictionDataException() {
        List<CurrencyData> emptyData = new ArrayList<>();
        org.junit.jupiter.api.Assertions.assertThrows(InvalidPredictionDataException.class, () -> predictionAlgorithm.calculateRateForDate(emptyData, targetDate));
    }
}
