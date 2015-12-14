package no.avec.report;

import no.avec.domain.Aksje;
import no.avec.domain.StockBuy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ronny.ness on 11/12/15.
 */
@Component
public class Report {
    private static String PORTFOLIO_FORMAT = "| %-30s | %-10s | %9s | %9s | %10s |%n";
    private static String PORTFOLIO_HISTORY_FORMAT = "| %-9s | %8s | %-8s | %-8s | %-8s | %-8s | %-8s |%n";
    private static String PORTFOLIO_FORMAT_SEPARATOR = "";
    private static String PORTFOLIO_HISTORY_FORMAT_SEPARATOR = "";

    @PostConstruct
    public void init() {
        for(int i = 0; i < getCorrectFormatLength(PORTFOLIO_FORMAT); i++) {
            PORTFOLIO_FORMAT_SEPARATOR += "-";
        }
        PORTFOLIO_FORMAT_SEPARATOR += "\n";
        for(int i = 0; i < getCorrectFormatLength(PORTFOLIO_HISTORY_FORMAT); i++) {
            PORTFOLIO_HISTORY_FORMAT_SEPARATOR += "-";
        }
        PORTFOLIO_HISTORY_FORMAT_SEPARATOR += "\n";

    }

    public static void printPortfolioHistory(List<StockBuy> buys) {
        System.out.print(formatStockBuyHistoryAsString(buys));
    }

    public static String formatStockBuyHistoryAsString(List<StockBuy> buys) {

        String s = PORTFOLIO_HISTORY_FORMAT_SEPARATOR;

        s += String.format(PORTFOLIO_HISTORY_FORMAT,
                "Kjøpsdato", "Retur(%)", "Aksje(1)", "Aksje(2)","Aksje(3)","Aksje(4)","Aksje(5)" );

        s+= PORTFOLIO_HISTORY_FORMAT_SEPARATOR;


        for(StockBuy buy : buys) {
            s += String.format(PORTFOLIO_HISTORY_FORMAT,
                    buy.getDato(),
                    buy.getKurs(),
                    buy.getAksje1(),
                    buy.getAksje2(),
                    buy.getAksje3(),
                    buy.getAksje4(),
                    buy.getAksje5());
        }

        s+= PORTFOLIO_HISTORY_FORMAT_SEPARATOR;

        return s;

    }

    public static void printPortfolio(List<Aksje> portfolio) {
        System.out.print(formatPortfolioAsString(portfolio));
    }

    public static String formatPortfolioAsString(List<Aksje> portfolio) {
        String s = PORTFOLIO_FORMAT_SEPARATOR;

        s += String.format(PORTFOLIO_FORMAT,
                "Aksje", "Dato inn", "Kjøpskurs", "Sluttkurs", "Endring(%)");

        s += PORTFOLIO_FORMAT_SEPARATOR;


        for(Aksje aksje : portfolio) {
            s += String.format(PORTFOLIO_FORMAT,
                    aksje.getAksje(),
                    aksje.getDato(),
                    aksje.getKjopskurs(),
                    aksje.getSluttkurs(),
                    aksje.getProsentEndring());
        }
        s += PORTFOLIO_FORMAT_SEPARATOR;
        return s;
    }


    private static int getCorrectFormatLength(String formatString) {
        int length = 0;
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(formatString);
        while(matcher.find()) {
            length += Integer.parseInt(matcher.group());
        }

        length += StringUtils.countMatches(formatString, " ");
        length += StringUtils.countMatches(formatString, "|");
        return length;

    }
}
