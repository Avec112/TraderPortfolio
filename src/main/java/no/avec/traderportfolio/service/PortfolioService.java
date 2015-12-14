package no.avec.traderportfolio.service;

import no.avec.traderportfolio.domain.HtmlMessage;
import no.avec.traderportfolio.domain.Stock;
import no.avec.traderportfolio.domain.StockBought;
import no.avec.traderportfolio.domain.StockSold;
import no.avec.traderportfolio.service.report.ReportService;
import no.avec.traderportfolio.service.social.MailService;
import no.avec.traderportfolio.service.social.TwitterService;
import no.avec.traderportfolio.util.Utils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
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

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by avec on 10/12/15.
 */

@Service
public class PortfolioService  {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MailService mailService;

    @Autowired
    private TwitterService twitterService;

    @Autowired
    private ReportService reportService;

    @Value("${investtech.username}")
    private String username;

    @Value("${investtech.password}")
    private String password;

    @Value("${investtech.login.url}")
    private String loginUrl;

    @Value("${investtech.portfolio.url}")
    private String portfolioUrl;

    @Value("${investtech.history.url}")
    private String historyUrl;

    @Value("${investtech.date.format:yyyyMMdd}")
    private String investtechDateFormat;

    @Value("${date.format:dd.MM.yyyy}")
    private String dateFormat;

    @Value("${state.filename:portfolio_state.txt}")
    private String stateFilename;

    private String lastMd5Hex; // holds MD5 of latest state


    @PostConstruct
    public void init() throws IOException {
        File f = new File(stateFilename);
        if(!f.exists()) {
            boolean isFileCreated = f.createNewFile();
            LOG.debug((isFileCreated)?"File " + stateFilename + " created.":"Could not create file " + stateFilename);
        } else {
            LOG.debug("File {} exists.", stateFilename);
        }
    }

    @Scheduled(cron = "${cron.schedule}")
    public void startJob() {
        try {

            if(StringUtils.isBlank(lastMd5Hex)) {
                lastMd5Hex = FileUtils.readFileToString(new File(stateFilename));
                LOG.debug("Found md5 state '{}' in file {}", lastMd5Hex, stateFilename);
            }

            // Connect to investech (and get a hold of session cookies)
            Map<String, String> cookies = session().cookies();

            List<Stock> portfolio = getPortfolio(cookies);
            List<StockBought> stockBoughts = getPortfolioHistory(cookies);

            // verify changed portfolio state
            String portfolioAsString = reportService.formatPortfolioAsString(portfolio, dateFormat);
            String currentMd5Hex = DigestUtils.md5Hex(portfolioAsString);

            LOG.debug("current '{}' vs last '{}' md5", currentMd5Hex, lastMd5Hex);

            int hourNow = LocalDateTime.now().getHour();

            if(!currentMd5Hex.equals(lastMd5Hex)) {

                // build mail message
                HtmlMessage htmlMessage = new HtmlMessage.HtmlMessageBuilder()
                        .h1("Nåværende traderportefølje")
                        .pre(portfolioAsString)
                        .h1("Porteføljens historikk (20 siste dager)")
                        .pre(reportService.formatStockBuyHistoryAsString(stockBoughts, dateFormat))
                        .build();

                mailService.send(htmlMessage.render());
                lastMd5Hex = currentMd5Hex;

                FileUtils.writeStringToFile(new File(stateFilename), currentMd5Hex);

            } else if(hourNow == 17){ // Etter børsens stengetid (16:30)
                HtmlMessage htmlMessage = new HtmlMessage.HtmlMessageBuilder()
                        .h1("Traderportefølje - " + Utils.formatDate(new Date(), "dd MMM"))
                        .p("Ingen endring i porteføljen idag.")
                        .h1("Porteføljens historikk (20 siste dager)")
                        .pre(reportService.formatStockBuyHistoryAsString(stockBoughts, dateFormat))
                        .build();

                mailService.send(htmlMessage.render());
            }


        } catch (IOException e) {
            LOG.error("Noe gikk galt.", e);
        } catch (EmailException e) {
            LOG.error("Fikk problemer e-post.", e);
        } catch (ParseException e) {
            LOG.error("Fikk problemer med parsing.", e);
        }
    }

    private List<StockBought> getPortfolioHistory(Map<String,String> cookies) throws IOException, ParseException {

        Document history = Jsoup.connect(historyUrl)
                .cookies(cookies)
                .get();

        Elements dailyBuysPre = history.select("pre:nth-of-type(3)");
        List<Element> spans = dailyBuysPre.get(0).children();
        List<Element> lastSpans = spans.subList(spans.size()-20, spans.size()); // only last 20
        List<StockBought> buys = new ArrayList<StockBought>();

        for(Element span : lastSpans) {
            String row = span.text();

            String [] res = row.split("\\s+"); // split on what ever how many spaces

            if(res.length == 7) {
                Date date = Utils.parseDate(res[0], investtechDateFormat);
                buys.add(new StockBought(date,res[1],Arrays.asList(res[2],res[3],res[4],res[5],res[6])));
            }
        }

        return buys;

    }

    private List<Stock> getPortfolio(Map<String,String> cookies) throws IOException, ParseException {

        Document traderportfolio = getTraderportfolioDocument(cookies);

        List<Stock> currentPortfolio = new ArrayList<Stock>();

        Elements table = traderportfolio.select("table.portfoliotable table.marketDataBox");

        for (Element row : table.select("tr:not(:first-child)")) {
            Elements tds = row.select("td");
            Element a = tds.get(0).select("a").first();
            Date date = Utils.parseDate(tds.get(1).text(), investtechDateFormat);
            Stock stock = new Stock(a.text(),a.attr("href").split("=")[1],date,tds.get(2).text(),tds.get(3).text(),tds.get(4).text(),tds.get(5).text());
            currentPortfolio.add(stock);

            // avslutt når vi har 5 aksjer (resten av tabellen er ikke relevant for porteføljen)
            if(currentPortfolio.size() == 5) {
                break;
            }

        }

        return currentPortfolio;
    }

    private List<StockSold> getStocksSold(Map<String,String> cookies) throws IOException {

        Document traderportfolio = getTraderportfolioDocument(cookies);

        List<StockSold> stocks = new ArrayList<StockSold>();

        Elements table = traderportfolio.select("table.recentsalestable table.marketDataBox");

        for(Element row : table) {
            Elements tds = row.select("td");
            StockSold stockSold = new StockSold();
        }

        return stocks;

    }

    private Document getTraderportfolioDocument(Map<String, String> cookies) throws IOException {
        return Jsoup.connect(portfolioUrl)
                    .cookies(cookies)
                    .get();
    }


    private Connection.Response session() throws IOException {
        return Jsoup.connect(loginUrl)
                        .data("LOGIN", username, "PASSWORD", password)
                        .method(Connection.Method.POST)
                        .execute();
    }
}
