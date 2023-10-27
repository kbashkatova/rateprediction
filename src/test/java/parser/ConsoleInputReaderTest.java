package parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.liga.rateforecaster.parser.ConsoleInputReader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ConsoleInputReaderTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final InputStream inputStream = new ByteArrayInputStream("User input\n".getBytes());
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outputStream));
        System.setIn(inputStream);
    }

    @Test
    public void testReadConsoleInput() {
        ConsoleInputReader reader = new ConsoleInputReader("en");
        ByteArrayInputStream in = new ByteArrayInputStream("User input\n".getBytes());
        System.setIn(in);
        String input = reader.readConsoleInput();
        assertThat(input).isEqualTo("User input");
        System.setIn(System.in);
    }
    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }
}