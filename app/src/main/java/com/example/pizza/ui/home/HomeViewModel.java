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

    public MutableLiveData<List<ItemBean>> getDataItemBean() {
        return dataItemBean;
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

    public void clearDataItemBean() {
//        this.changeDataItemBeanSel(new ArrayList<ItemBean>());

        if (this.dataItemBean.getValue() != null && this.dataItemBean.getValue().size() != 0 && this.dataItemBean.getValue().get(0) != null)
            this.dataItemBean.postValue(new ArrayList<ItemBean>());
    }

    public void changeDataItemBeanSel(List<ItemBean> lstItemBean){
        /*if (lstItemBean == null || lstItemBean.size() == 0) {
            dataItemBean = new MutableLiveData<List<ItemBean>>();
            this.dataItemBean.setValue(lstItemBean);
            return;
        }
        this.dataItemBean = new MutableLiveData<List<ItemBean>>(lstItemBean);*/

//        if (this.dataItemBean.getValue() == null || this.dataItemBean.getValue().size() == 0)
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
        // this.strPizzaSel = new MutableLiveData<String>();
        this.strPizzaSel.postValue(strPizzaSel);

        // this.strTrasporto = new MutableLiveData<String>();
        this.strTrasporto.postValue(strTrasporto);
    }
}