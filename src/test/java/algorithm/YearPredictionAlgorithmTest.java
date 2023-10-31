package algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.rateforecaster.forecast.algorithm.year.YearPredictionAlgorithm;
import ru.liga.rateforecaster.model.CurrencyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class YearPredictionAlgorithmTest {

    private YearPredictionAlgorithm predictionAlgorithm;

    @BeforeEach
    void setUp() {
        predictionAlgorithm = new YearPredictionAlgorithm();
    }

    @Test
    void calculateRateForDate_WhenCurrentYearRateExists_ReturnsCurrentYearRate() {
        LocalDate targetDate = LocalDate.now();
        List<CurrencyData> currencyDataList = new ArrayList<>();
        currencyDataList.add(new CurrencyData(targetDate, new BigDecimal("1.0")));
        CurrencyData result = predictionAlgorithm.calculateRateForDate(currencyDataList, targetDate);
        assertEquals(new BigDecimal("1.0"), result.getRate(), String.valueOf(0.001));
    }

    @Test
    void calculateRateForDate_WhenCurrentYearRateDoesNotExist_ReturnsLastYearRate() {
        LocalDate targetDate = LocalDate.now();
        List<CurrencyData> currencyDataList = new ArrayList<>();
        currencyDataList.add(new CurrencyData(targetDate.minusYears(1), new BigDecimal("2.0")));
        CurrencyData result = predictionAlgorithm.calculateRateForDate(currencyDataList, targetDate);
        assertEquals(new BigDecimal("2.0"), result.getRate(), String.valueOf(0.001));
    }
}