package com.example.pizza.listPizza;

import android.util.Log;

import com.example.pizza.util.Util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

public class PizzaBean implements Serializable {
    private String titolo;
    private String descrizione;
    private String descrizione2;
    private String strEuro;
    private BigDecimal prezzo;

//    protected boolean pizzaBaby;
    protected boolean trasporto;
    protected boolean checked;

    public PizzaBean(String descrizione, BigDecimal prezzo) {
        this(descrizione, prezzo, false);
    }

    public PizzaBean(String descrizione, BigDecimal prezzo, boolean checked) {
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.checked = checked;
    }

    public PizzaBean(String titolo, String descrizione2, String strEuro, boolean checked) {
        this.titolo = titolo;
        this.descrizione2= descrizione2;
        this.strEuro = strEuro;
        if (descrizione2 != null && descrizione2.trim().length() != 0 && strEuro != null && strEuro.trim().length() != 0)
            this.descrizione = titolo + ": " + descrizione2 + " (€ " + strEuro + ")";
        else
            this.descrizione = titolo;

        if (strEuro != null && strEuro.trim().length() != 0)
            this.prezzo = Util.parseStrEuro(strEuro, Locale.ITALIAN);

        this.checked = checked;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getStrEuro() {
        return strEuro;
    }

    public void setStrEuro(String strEuro) {
        this.strEuro = strEuro;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione2() {
        return descrizione2;
    }

    public void setDescrizione2(String descrizione2) {
        this.descrizione2 = descrizione2;
    }

    public void setTrasporto(boolean trasporto) {
        this.trasporto = trasporto;
    }

    public boolean isTrasporto() {
        return trasporto;
    }

//    public boolean isPizzaBaby() {
//        return pizzaBaby;
//    }
//
//    public void setPizzaBaby(boolean pizzaBaby) {
//        this.pizzaBaby = pizzaBaby;
//    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PizzaBean)) {
            return false;
        }
        PizzaBean pizza = (PizzaBean) o;
        return  Objects.equals(descrizione, pizza.descrizione) &&
                Objects.equals(prezzo, pizza.prezzo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descrizione, prezzo);
    }
}
