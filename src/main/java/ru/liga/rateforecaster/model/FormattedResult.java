package ru.liga.rateforecaster.model;

import lombok.Getter;
import org.jfree.chart.ChartPanel;

/**
 * A class representing a formatted result that can include text, a chart image, or an error message.
 */
@Getter
public class FormattedResult {

    private String textResult;
    private ChartPanel chartImage;
    private ErrorMessage errorMessage;

    public FormattedResult(String textResult, ChartPanel chartImage) {
        this.textResult = textResult;
        this.chartImage = chartImage;
    }

    public FormattedResult(ErrorMessage errorMessage) {
        this.errorMessage = errorMessage;
    }

    public FormattedResult(String textResult) {
        this.textResult = textResult;
    }

    public FormattedResult(ChartPanel chartImage) {
        this.chartImage = chartImage;
    }

}
