package com.example.pizza.util;

import com.example.pizza.listPizza.PizzaBean;

import java.util.ArrayList;

public class UtilAddressImgData {

    public static ArrayList<PizzaBean> lstAddressDataExtSel = new ArrayList<PizzaBean>();

    public static void addAddressExt(PizzaBean item) {
        if (item != null && !lstAddressDataExtSel.contains(item))
            lstAddressDataExtSel.add(item);
    }

    public static void addLstAddress(ArrayList<PizzaBean> lstItem) {
        if (lstItem != null)
            for (PizzaBean item : lstItem) {
                addAddressExt(new PizzaBean(item.getDescrizione(), item.getPrezzo(), false));
            }
    }

    public static void replaceLstAddress(ArrayList<PizzaBean> lstAddressDataExtSeNew) {
        UtilAddressImgData.lstAddressDataExtSel = lstAddressDataExtSeNew;
    }

    public static void clearLstAddressSel() {
        lstAddressDataExtSel.clear();
    }

    public static ArrayList<PizzaBean> getLstAddressImgData() {
        ArrayList<PizzaBean> lstItem = null;

        if (lstAddressDataExtSel != null && lstAddressDataExtSel.size() != 0) {
            lstItem = new ArrayList<PizzaBean>(lstAddressDataExtSel.size());

            for (PizzaBean addressImgDataExt : lstAddressDataExtSel) {
                lstItem.add(new PizzaBean(addressImgDataExt.getDescrizione(), addressImgDataExt.getPrezzo(), addressImgDataExt.isChecked()));
            }
        }

        return lstItem;
    }

    public static boolean lstAddressImgDataContains(PizzaBean item) {
        if (lstAddressDataExtSel == null || lstAddressDataExtSel.size() == 0 || item == null)
            return false;

        return getLstAddressImgData().contains(item);
    }
}
