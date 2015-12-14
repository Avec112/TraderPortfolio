package no.avec.report;

import no.avec.domain.Stock;
import no.avec.domain.StockBought;
import no.avec.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by avec on 11/12/15.
 */
@Component
public class ReportService {

    @Value("${portfolio.table.format:| %-30s | %-10s | %9s | %9s | %10s |%n}")
    private String portfolioFormat;

    @Value("${portfolio.history.table.format:| %-10s | %8s | %-8s | %-8s | %-8s | %-8s | %-8s |%n}")
    private String portfolioHistoryFormat;

    @Value("${portfolio.table.labels}")
    private String[] portfolio_labels;

    @Value("${portfolio.history.table.labels}")
    private String[] portfolio_history_labels;

    // will be generated
    private String PORTFOLIO_FORMAT_SEPARATOR = "";
    private String PORTFOLIO_HISTORY_FORMAT_SEPARATOR = "";

    public ReportService() {
    }

/*    public ReportService(String portfolioFormat, String portfolioHistoryFormat, String portfolio_labels, String portfolio_history_labels) {
        this.portfolioFormat = portfolioFormat;
        this.portfolioHistoryFormat = portfolioHistoryFormat;
        this.portfolio_labels = portfolio_labels;
        this.portfolio_history_labels = portfolio_history_labels;
        init();
    }*/

    @PostConstruct
    public void init() {
        for(int i = 0; i < calculateTableWidth(portfolioFormat); i++) {
            PORTFOLIO_FORMAT_SEPARATOR += "-";
        }
        PORTFOLIO_FORMAT_SEPARATOR += "\n";
        for(int i = 0; i < calculateTableWidth(portfolioHistoryFormat); i++) {
            PORTFOLIO_HISTORY_FORMAT_SEPARATOR += "-";
        }
        PORTFOLIO_HISTORY_FORMAT_SEPARATOR += "\n";

    }

    public String formatStockBuyHistoryAsString(List<StockBought> buys, String dateFormat) {
        StringBuilder sb = new StringBuilder()
                .append(PORTFOLIO_HISTORY_FORMAT_SEPARATOR)
                .append(String.format(portfolioHistoryFormat,portfolio_history_labels))
                .append(PORTFOLIO_HISTORY_FORMAT_SEPARATOR);

        for(StockBought buy : buys) {
            sb.append(
                    String.format(
                            portfolioHistoryFormat,
                            Utils.formatDate(buy.getDato(), dateFormat),
                            buy.getKurs(),
                            buy.getStocks().get(0),
                            buy.getStocks().get(1),
                            buy.getStocks().get(2),
                            buy.getStocks().get(3),
                            buy.getStocks().get(4)
                    )
            );
        }

        sb.append(PORTFOLIO_HISTORY_FORMAT_SEPARATOR);

        return sb.toString();

    }

    public String formatPortfolioAsString(List<Stock> portfolio, String dateFormat) {
        StringBuilder sb = new StringBuilder()
        .append(PORTFOLIO_FORMAT_SEPARATOR)
        .append(String.format(portfolioFormat, portfolio_labels))
        .append(PORTFOLIO_FORMAT_SEPARATOR);

        for(Stock stock : portfolio) {
            sb.append(
                    String.format(
                            portfolioFormat,
                            stock.getName(),
                            Utils.formatDate(stock.getDate(), dateFormat),
                            stock.getBuyPrice(),
                            stock.getSellPrice(),
                            stock.getProsentEndring()
                    )
            );
        }

        sb.append(PORTFOLIO_FORMAT_SEPARATOR);

        return sb.toString();
    }


    private int calculateTableWidth(String formatString) {
        int length = 0;
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(formatString);
        while(matcher.find()) { // column widths
            length += Integer.parseInt(matcher.group());
        }

        length += StringUtils.countMatches(formatString, " ");
        length += StringUtils.countMatches(formatString, "|");
        return length;

    }
}
