package no.avec.service;

import no.avec.domain.Aksje;
import no.avec.domain.HtmlMessage;
import no.avec.domain.StockBuy;
import no.avec.report.Report;
import no.avec.social.MailService;
import no.avec.social.TwitterService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.mail.EmailException;
import org.apache.tools.ant.taskdefs.Javadoc;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ronny.ness on 10/12/15.
 */

@Service
public class PortfolioService  {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MailService mailService;

    @Autowired
    private TwitterService twitterService;

    @Value("${investtech.login}")
    private String login;

    @Value("${investtech.password}")
    private String password;

    @Value("${investtech.login.url}")
    private String loginUrl;

    @Value("${investtech.portfolio.url}")
    private String portfolioUrl;

    @Value("${investtech.history.url}")
    private String historyUrl;

    private String lastMd5Hex;

    @Scheduled(cron = "${cron.schedule}")
    public void startJob() {
        try {
            Connection.Response session = login();

            // TODO create as MailMessage (object)
            String mailMessage;


            List<Aksje> portfolio = getPortfolio(session.cookies());
            List<StockBuy> stockBuys = getPortfolioHistory(session.cookies());

            // verify changed portfolio state
            String portfolioAsString = Report.formatPortfolioAsString(portfolio);
            String currentMd5Hex = DigestUtils.md5Hex(portfolioAsString);

            if(!currentMd5Hex.equals(lastMd5Hex)) {

                // build mail message
                HtmlMessage htmlMessage = new HtmlMessage.HtmlMessageBuilder()
                        .h1("Nåværende traderportefølje")
                        .pre(portfolioAsString)
                        .h1("Porteføljens historikk (20 siste dager)")
                        .pre(Report.formatStockBuyHistoryAsString(stockBuys))
                        .build();

//                System.out.println(mailMessage);
                mailService.send(htmlMessage.render());
                lastMd5Hex = currentMd5Hex;



            } else if(LocalDateTime.now().getHour() == 17){ // Etter børsens stengetid (16:30)
                mailService.send("Ingen endring idag");
            }

            LOG.debug("Mail sendt");

        } catch (IOException e) {
            LOG.error("", e);
        } catch (EmailException e) {
            LOG.error("", e);
        }
    }

    private List<StockBuy> getPortfolioHistory(Map<String,String> cookies) throws IOException {

        Document history = Jsoup.connect(historyUrl)
                .cookies(cookies)
                .get();

        Elements dailyBuysPre = history.select("pre:nth-of-type(3)");
        List<Element> spans = dailyBuysPre.get(0).children();
        List<Element> lastSpans = spans.subList(spans.size()-20, spans.size()); // only last 20
        List<StockBuy> buys = new ArrayList<StockBuy>();

        for(Element span : lastSpans) {
            String row = span.text();

            String [] res = row.split("\\s+"); // split on what ever how many spaces

            if(res.length == 7) {
                StockBuy stockBuy = new StockBuy();

                stockBuy.setDato(res[0]);
                stockBuy.setRetProsent(res[1]);
                stockBuy.setAksje1(res[2]);
                stockBuy.setAksje2(res[3]);
                stockBuy.setAksje3(res[4]);
                stockBuy.setAksje4(res[5]);
                stockBuy.setAksje5(res[6]);

                buys.add(stockBuy);
            }

        }

        return buys;

    }

    private List<Aksje> getPortfolio(Map<String,String> cookies) throws IOException {

        Document traderportfolio = Jsoup.connect(portfolioUrl)
                .cookies(cookies)
                .get();

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

        return currentPortfolio;
    }




    private Connection.Response login() throws IOException {
        return Jsoup.connect(loginUrl)
                        .data("LOGIN", login, "PASSWORD", password)
                        .method(Connection.Method.POST)
                        .execute();
    }
}
