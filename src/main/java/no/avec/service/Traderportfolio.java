package no.avec.service;

import no.avec.report.Report;
import no.avec.domain.Aksje;
import no.avec.social.MailClient;
import org.apache.commons.mail.EmailException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronny.ness on 10/12/15.
 */
@PersistJobDataAfterExecution
public class Traderportfolio implements Job {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    public final static String PORTFOLIO = "portfolio";

    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        JobDataMap data = jobExecutionContext.getJobDetail().getJobDataMap();


        try {
            Connection.Response session = login();

//            processTraderPortfolio(data, session);

            processTraderPortfolioHistory(data, session);

        } catch (IOException e) {
            LOG.error("", e);
//        } catch (EmailException e) {
//            LOG.error("", e);
        }
    }

    private void processTraderPortfolioHistory(JobDataMap data, Connection.Response session) throws IOException {
        Document history = getTraderportfolioHistory(session);

        Elements latestdaily = history.select("pre:nth-of-type(3) > span:nth-last-child(-n+20)");
        for(Element span : latestdaily) {
            String row = span.text();

            String [] res = row.split("\\s+");

            System.out.println(span.text());
        }


    }

    private void processTraderPortfolio(JobDataMap data, Connection.Response session) throws EmailException, IOException {

        Document traderportfolio = getTraderportfolio(session);

        @SuppressWarnings("unchecked")
        List<Aksje> lastPortfolio = (List<Aksje>) data.get(PORTFOLIO);

        List<Aksje> currentPortfolio = new ArrayList<Aksje>();

        Elements table = traderportfolio.select("table.portfoliotable table.marketDataBox");

        for (Element row : table.select("tr:not(:first-child)")) {

            Elements tds = row.select("td");

            Aksje aksje = new Aksje();

            Element a = tds.get(0).select("a").first();

            aksje.setAksje(a.text());
            aksje.setCompanyId(a.attr("href").split("=")[1]);
            aksje.setDato(tds.get(1).text());
            aksje.setInfo(tds.get(2).text());
            aksje.setKjopskurs(tds.get(3).text());
            aksje.setSluttkurs(tds.get(4).text());
            aksje.setProsentEndring(tds.get(5).text());

            currentPortfolio.add(aksje);

            // avslutt når vi har 5 aksjer (resten av tabellen er ikke relevant for porteføljen)
            if(currentPortfolio.size() == 5) {
                break;
            }

        }

        if(lastPortfolio.size() == 0 || !lastPortfolio.get(0).equals(currentPortfolio.get(0))) {
            LOG.debug("Oppdaterer porteføljen");
            data.put(PORTFOLIO, new ArrayList<Aksje>(currentPortfolio));

            Report.printPortfolio(currentPortfolio);

            new MailClient().sendMail(Report.printPortfolioString(currentPortfolio));

        } else {
            LOG.debug("Ingen endring.");
        }
    }

    private Document getTraderportfolio(Connection.Response session) throws IOException {
        return Jsoup.connect("http://www.investtech.no/main/market.php?product=58&MarketID=1")
                        .cookies(session.cookies())
                        .get();
    }

    private Document getTraderportfolioHistory(Connection.Response session) throws IOException {
        return Jsoup.connect("http://www.investtech.no/main/market.php?product=58&MarketID=1&fn=stattrader.phtm")
                .cookies(session.cookies())
                .get();
    }

    private Connection.Response login() throws IOException {
        return Jsoup.connect("http://www.investtech.no/main/market.php?CountryID=1")
                        .data("LOGIN", "Avec", "PASSWORD", "xozufy")
                        .method(Connection.Method.POST)
                        .execute();
    }
}
