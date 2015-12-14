package no.avec.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by avec on 10/12/15.
 */
public class Stock {

    private String name;
    private String companyId;
    private Date date;
    private String info; // May contain "ny" same day as bought
    private String buyPrice;
    private String sellPrice;
    private String prosentEndring;

    public Stock() {
    }

    public Stock(String name, String companyId, Date date, String info, String buyPrice, String sellPrice, String prosentEndring) throws ParseException {
        this.name = name;
        this.companyId = companyId;
        this.date = date;
        this.info = info;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
        this.prosentEndring = prosentEndring;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) throws ParseException {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(String sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getProsentEndring() {
        return prosentEndring;
    }

    public void setProsentEndring(String prosentEndring) {
        this.prosentEndring = prosentEndring;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("companyId", companyId)
                .append("date", getDate())
                .append("info", info)
                .append("buyPrice", buyPrice)
                .append("sellPrice", sellPrice)
                .append("prosentEndring", prosentEndring)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Stock stock1 = (Stock) o;

        return new EqualsBuilder()
                .append(getName(), stock1.getName())
                .append(getCompanyId(), stock1.getCompanyId())
                .append(getDate(), stock1.getDate())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getName())
                .append(getCompanyId())
                .append(getDate())
                .toHashCode();
    }
}