package com.example.pizza.listSnack;

import com.example.pizza.util.Util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Locale;

public class SnackBean implements Serializable {
    private String titolo;
    private String descrizione;
    private String descrizione2;
    private String strEuro;
    private BigDecimal prezzo;

    private int idImmagine;

    public SnackBean(String titolo, String descrizione, String descrizione2, String strEuro, int idImmagine) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.descrizione2 = descrizione2;
        this.strEuro = strEuro;
        if (strEuro != null && strEuro.trim().length() != 0)
            this.prezzo = Util.parseStrEuro(strEuro, Locale.ITALIAN);
        this.idImmagine = idImmagine;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public String getDescrizione2() {
        return descrizione2;
    }

    public String getStrEuro() {
        return strEuro;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }
}
