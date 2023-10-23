package parser;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.liga.rateforecaster.enums.Currency;
import ru.liga.rateforecaster.model.ParsedRequest;
import ru.liga.rateforecaster.enums.RateType;
import ru.liga.rateforecaster.parser.RequestParser;

public class RequestParserTest {

    @Test
    public void testParseValidRequest() {
        RequestParser parser = new RequestParser();
        ParsedRequest parsedRequest = parser.parseRequest("rate EUR tomorrow");
        Assertions.assertThat(parsedRequest).isNotNull();
        Assertions.assertThat(parsedRequest.getCurrency()).isEqualTo(Currency.EUR);
        Assertions.assertThat(parsedRequest.getRateType()).isEqualTo(RateType.TOMORROW);
    }

    @Test
    public void testParseInValidCurrencyCaseRequest() {
        RequestParser parser = new RequestParser();
        ParsedRequest parsedRequest = parser.parseRequest("rate eur tomorrow");
        Assertions.assertThat(parsedRequest).isNull();
    }

    @Test
    public void testParseInvalidRequest() {
        RequestParser parser = new RequestParser();
        ParsedRequest parsedRequest = parser.parseRequest("invalid request");
        Assertions.assertThat(parsedRequest).isNull();
    }

    @Test
    public void testParseRequestWithExit() {
        RequestParser parser = new RequestParser();
        ParsedRequest parsedRequest = parser.parseRequest("exit");
        Assertions.assertThat(parsedRequest).isNull();
    }
}