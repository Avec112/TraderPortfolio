package no.avec.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ronny.ness on 10/12/15.
 */
public class Aksje {
    private DateFormat SIMPLE_DATE_FORMAT_FROM = new SimpleDateFormat("yyyyMMdd");
    private DateFormat SIMPLE_DATE_FORMAT_TO = new SimpleDateFormat("dd.MM.yyyy");
    private String aksje;
    private String companyId;
    private Date dato;
    private String info; // stort sett tom ser det ut til
    private String kjopskurs;
    private String sluttkurs;
    private String prosentEndring;


    public String getAksje() {
        return aksje;
    }

    public void setAksje(String aksje) {
        this.aksje = aksje;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDato() {
        return SIMPLE_DATE_FORMAT_TO.format(dato);
    }

    public void setDato(String dato) {
        Date date = null;
        try {
            date = SIMPLE_DATE_FORMAT_FROM.parse(dato);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.dato = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getKjopskurs() {
        return kjopskurs;
    }

    public void setKjopskurs(String kjopskurs) {
        this.kjopskurs = kjopskurs;
    }

    public String getSluttkurs() {
        return sluttkurs;
    }

    public void setSluttkurs(String sluttkurs) {
        this.sluttkurs = sluttkurs;
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
                .append("aksje", aksje)
                .append("companyId", companyId)
                .append("dato", getDato())
                .append("info", info)
                .append("kjopskurs", kjopskurs)
                .append("sluttkurs", sluttkurs)
                .append("prosentEndring", prosentEndring)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Aksje aksje1 = (Aksje) o;

        return new EqualsBuilder()
                .append(getAksje(), aksje1.getAksje())
                .append(getCompanyId(), aksje1.getCompanyId())
                .append(getDato(), aksje1.getDato())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getAksje())
                .append(getCompanyId())
                .append(getDato())
                .toHashCode();
    }
}