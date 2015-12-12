package no.avec.report;

import no.avec.domain.Aksje;

import java.util.List;

/**
 * Created by ronny.ness on 11/12/15.
 */
public class Report {
    private static String PORTFOLIO_FORMAT = "%-30s%-15s%12s%12s%10s%n";
    private static String PORTFOLIO_HISTORY_FORMAT = "%-10s%10s%-12s%-12s%-12s%-12s%-12s%n";


    /*public static String printPortfolioHistoryString(String [] rows) {
        String s = String.format(PORTFOLIO_HISTORY_FORMAT,
                "Dato", "");
    }*/

    public static void printPortfolio(List<Aksje> portfolio) {
        System.out.print(printPortfolioString(portfolio));
    }

    public static String printPortfolioString(List<Aksje> portfolio) {
        String s = String.format(PORTFOLIO_FORMAT,
                "Aksje", "Dato inn", "Kjøpskurs", "Sluttkurs", "+/- %");

        for(int i=0;i< (30+15+12+12+10);i++)
            s += "-";

        s += "\n";

        for(Aksje aksje : portfolio) {
            s += String.format(PORTFOLIO_FORMAT,
                    aksje.getAksje(),
                    aksje.getDato(),
                    aksje.getKjopskurs(),
                    aksje.getSluttkurs(),
                    aksje.getProsentEndring());
        }

        return s;
    }


}
