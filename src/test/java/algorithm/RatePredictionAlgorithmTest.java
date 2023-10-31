package algorithm;

import org.junit.jupiter.api.Test;
import ru.liga.rateforecaster.forecast.algorithm.RatePredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.mist.MistPredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.year.YearPredictionAlgorithm;
import ru.liga.rateforecaster.model.CurrencyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RatePredictionAlgorithmTest {

    @Test
    void getRateForDate_WhenRateExists_ReturnsRate() {
        RatePredictionAlgorithm predictionAlgorithm = new MistPredictionAlgorithm();
        LocalDate targetDate = LocalDate.now();
        List<CurrencyData> currencyDataList = new ArrayList<>();
        CurrencyData expectedRate = new CurrencyData(targetDate, new BigDecimal("1.0"));
        currencyDataList.add(expectedRate);
        CurrencyData result = predictionAlgorithm.getRateForDate(currencyDataList, targetDate);
        assertEquals(expectedRate, result);
    }

    @Test
    void getRateForDate_WhenRateDoesNotExist_ReturnsNull() {
        RatePredictionAlgorithm predictionAlgorithm = new YearPredictionAlgorithm();
        LocalDate targetDate = LocalDate.now();
        List<CurrencyData> currencyDataList = new ArrayList<>();
        CurrencyData result = predictionAlgorithm.getRateForDate(currencyDataList, targetDate);
        assertNull(result);
    }
}