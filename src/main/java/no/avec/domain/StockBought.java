package no.avec.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by avec on 13/12/15.
 */
public class StockBought {

    private Date date;
    private String kurs;
    private List<String> stocks;

    public StockBought() {
    }

    public StockBought(Date date, String kurs, List<String> stocks) {
        this.date = date;
        this.kurs = kurs;
        this.stocks = stocks;
    }

    public Date getDato() {
        return date;
    }

    public void setDato(String dato) {
        this.date = date;
    }

    public String getKurs() {
        return kurs;
    }

    public void setRetProsent(String kurs) {
        this.kurs = kurs;
    }

    public List<String> getStocks() {
        return stocks;
    }

    public void setStocks(List<String> stocks) {
        this.stocks = stocks;
    }
}
