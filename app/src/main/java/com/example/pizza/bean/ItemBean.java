package com.example.pizza.bean;

import com.example.pizza.util.Util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class ItemBean implements Serializable {
    private String titolo;
    private String descrizione;
    private String descrizione2;
    private String strEuro;
    protected boolean pizzaBaby;
    protected boolean pagato;
    private String note;
    private BigDecimal prezzo;
    private int idImmagine;
    private int order;

    public ItemBean(String titolo, String descrizione, String descrizione2, String strEuro, int idImmagine, int order) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.descrizione2 = descrizione2;
        this.strEuro = strEuro;
        if (strEuro != null && strEuro.trim().length() != 0)
            this.prezzo = Util.parseStrEuro((String)strEuro, Locale.ITALIAN);
        this.idImmagine = idImmagine;
        this.order = order;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione2() {
        return descrizione2;
    }

    public void setDescrizione2(String descrizione2) {
        this.descrizione2 = descrizione2;
    }

    public String getStrEuro() {
        return strEuro;
    }

    public void setStrEuro(String strEuro) {
        this.strEuro = strEuro;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isPizzaBaby() {
        return pizzaBaby;
    }

    public void setPizzaBaby(boolean pizzaBaby) {
        this.pizzaBaby = pizzaBaby;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isPagato() {
        return pagato;
    }

    public void setPagato(boolean pagato) {
        this.pagato = pagato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemBean)) return false;
        ItemBean itemBean = (ItemBean) o;
        return getTitolo().equals(itemBean.getTitolo()) &&
                getDescrizione().equals(itemBean.getDescrizione()) &&
                getPrezzo().equals(itemBean.getPrezzo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitolo(), getDescrizione(), getPrezzo());
    }
}
