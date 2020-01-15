package com.example.pizza.util;

import android.util.Log;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Util {

    public static String getStrDataAttuale() {
        return getTimestampMillisecond("dd/MM/yyyy");
    }

    public static String getTimestampMillisecond(String pattern) {
        String ret = "";
        Timestamp date = new Timestamp((new Date()).getTime());
        if (date != null) {
            try {
                ret = (new SimpleDateFormat(pattern)).format(date);
            } catch (Exception e) {
                ret = date.toString();
            }
        }

        return ret;
    }

    public static BigDecimal parseStrEuro(final String amount, final Locale locale)  {
        final NumberFormat format = NumberFormat.getNumberInstance(locale);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }

        BigDecimal out = null;
        try {
            out = (BigDecimal) format.parse(amount.replaceAll("[^\\d.,]", ""));
        } catch(Exception e) {
            Log.e("PIZZA", e.getMessage());
        }

        return out;
    }
}
