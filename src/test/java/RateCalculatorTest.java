import org.junit.jupiter.api.Test;
import ru.liga.rateforecaster.forecast.algorithm.RateCalculator;
import ru.liga.rateforecaster.model.CurrencyData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RateCalculatorTest {

    @Test
    public void testCalculateRateForDate() {
        List<CurrencyData> currencyDataList = new ArrayList<>();
        LocalDate targetDate = LocalDate.now();

        CurrencyData currencyData = new CurrencyData(targetDate, BigDecimal.valueOf(1.0));
        currencyDataList.add(currencyData);

        CurrencyData result = RateCalculator.calculateRateForDate(currencyDataList, targetDate);
        assertEquals(currencyData, result);
    }

    @Test
    public void testCalculateAverageRateFromLastSevenRates() {
        List<CurrencyData> currencyDataList = new ArrayList<>();

        for (int i = 1; i <= 7; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            BigDecimal rate = BigDecimal.valueOf(1.0 * i);
            currencyDataList.add(new CurrencyData(date, rate));
        }

        LocalDate currentDate = LocalDate.now();

        BigDecimal expectedAverage = BigDecimal.valueOf((1.0 + 2.0 + 3.0 + 4.0 + 5.0 + 6.0 + 7.0) / 7).setScale(2, RoundingMode.HALF_UP);

        BigDecimal result = RateCalculator.calculateAverageRateFromLastSevenRates(currencyDataList, currentDate);
        assertEquals(expectedAverage, result);
    }

    @Test
    public void testCalculateAverageRateFromLastSevenRatesWithInsufficientData() {
        List<CurrencyData> currencyDataList = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            LocalDate date = LocalDate.now().minusDays(i);
            BigDecimal rate = BigDecimal.valueOf(1.0 * i);
            currencyDataList.add(new CurrencyData(date, rate));
        }

        LocalDate currentDate = LocalDate.now();

        BigDecimal result = RateCalculator.calculateAverageRateFromLastSevenRates(currencyDataList, currentDate);
        assertEquals(BigDecimal.valueOf(0.0), result);
    }
}