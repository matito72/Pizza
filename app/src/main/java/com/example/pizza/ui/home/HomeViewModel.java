package com.example.pizza.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pizza.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<ItemBean>> dataItemBean;

    private MutableLiveData<String> strPizzaSel;
    private MutableLiveData<String> strTrasporto;

    public HomeViewModel() {
        dataItemBean = new MutableLiveData<List<ItemBean>>();

        strPizzaSel = new MutableLiveData<>();
        strPizzaSel.setValue("... recupero dati ...");

        strTrasporto = new MutableLiveData<>();
        strTrasporto.setValue(" ");
    }

    public LiveData<List<ItemBean>> getDataItemBean() {
        return dataItemBean;
    }

    public void clearDataItemBean() {
        this.changeDataItemBeanSel(new ArrayList<ItemBean>());
    }

    public synchronized List<ItemBean> addItemBean(ItemBean itemBean) {
        List<ItemBean> lstItemBean = new ArrayList<ItemBean>();

        if (this.dataItemBean.getValue() == null) {
            if (itemBean != null)
                lstItemBean.add(itemBean);
            this.changeDataItemBeanSel(lstItemBean);
        } else {
            for (ItemBean item : this.dataItemBean.getValue()) {
                if (itemBean != null && item.getOrder() == itemBean.getOrder()) {
                    lstItemBean.add(itemBean);
                } else {
                    lstItemBean.add(item);
                }
            }
            if (itemBean != null && !lstItemBean.contains(itemBean)) {
                lstItemBean.add(itemBean);
            }

            if (getDataItemBean().getValue().size() != lstItemBean.size()) {
                this.changeDataItemBeanSel(lstItemBean);
            } else {
                for (ItemBean item : getDataItemBean().getValue()) {
                    for (ItemBean itemNew : lstItemBean) {
                        if (item.getOrder() == itemNew.getOrder() && item.getDescrizione() != itemNew.getDescrizione()) {
                            this.changeDataItemBeanSel(lstItemBean);
                            break;
                        }
                    }
                }
            }
        }

        return lstItemBean;
    }

    public void changeDataItemBeanSel(List<ItemBean> lstItemBean){
        this.dataItemBean.postValue(lstItemBean);
    }

    public LiveData<String> getStrPizzaSel() {
        return strPizzaSel;
    }

    public LiveData<String> getStrTrasporto() {
        return strTrasporto;
    }

    public void changeStrPizzaSel(String strPizzaSel){
        this.strPizzaSel.postValue(strPizzaSel);
    }

    public void changeStrPizzaSel(String strPizzaSel, String strTrasporto){
        this.strPizzaSel.postValue(strPizzaSel);
        this.strTrasporto.postValue(strTrasporto);
    }
}