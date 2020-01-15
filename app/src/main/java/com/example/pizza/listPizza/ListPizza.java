package com.example.pizza.listPizza;

import java.io.Serializable;
import java.util.List;

public class ListPizza implements Serializable {
    List<PizzaBean> mDataList;

    public ListPizza(List<PizzaBean> mDataList) {
        this.mDataList = mDataList;
    }

    public List<PizzaBean> getmDataList() {
        return mDataList;
    }

    public void setmDataList(List<PizzaBean> mDataList) {
        this.mDataList = mDataList;
    }
}
