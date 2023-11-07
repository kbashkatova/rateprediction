package algorithm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.liga.rateforecaster.exception.InvalidPredictionDataException;
import ru.liga.rateforecaster.forecast.algorithm.mist.MistPredictionAlgorithm;
import ru.liga.rateforecaster.forecast.algorithm.mist.RandomNumberGenerator;
import ru.liga.rateforecaster.model.CurrencyData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class MistPredictionAlgorithmTest {

    private MistPredictionAlgorithm predictionAlgorithm;
    private RandomNumberGenerator randomNumberGenerator;

    @BeforeEach
    void setUp() {
        randomNumberGenerator = Mockito.mock(RandomNumberGenerator.class);
        predictionAlgorithm = new MistPredictionAlgorithm(randomNumberGenerator);
    }

    @Test
    void calculateRateForDate_WhenCurrentYearRateExists_ReturnsCurrentRate() {
        LocalDate targetDate = LocalDate.now();
        List<CurrencyData> currencyDataList = new ArrayList<>();
        currencyDataList.add(new CurrencyData(targetDate, new BigDecimal("1.0")));

        CurrencyData result = predictionAlgorithm.calculateRateForDate(currencyDataList, targetDate);
        assertEquals(new BigDecimal("1.0"), result.rate());
    }

    @Test
    void calculateRateForDate_WhenCurrentYearRateDoesNotExist_ReturnsRandomRate() {
        LocalDate targetDate = LocalDate.now();
        List<CurrencyData> currencyDataList = new ArrayList<>();
        when(randomNumberGenerator.nextInt(currencyDataList.size())).thenReturn(0);
        currencyDataList.add(new CurrencyData(targetDate.minusDays(1), new BigDecimal("2.0")));
        currencyDataList.add(new CurrencyData(targetDate.minusDays(2), new BigDecimal("3.0")));
        CurrencyData result = predictionAlgorithm.calculateRateForDate(currencyDataList, targetDate);
        assertEquals(new BigDecimal("2.0"), result.rate());
    }

    @Test
    void calculateRateForDate_WhenNoRatesAvailable_ThrowsInvalidPredictionDataException() {
        LocalDate targetDate = LocalDate.now();
        List<CurrencyData> currencyDataList = new ArrayList<>();

        assertThrows(InvalidPredictionDataException.class, () -> {
            predictionAlgorithm.calculateRateForDate(currencyDataList, targetDate);
        });
    }
}