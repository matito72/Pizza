package com.example.pizza.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.pizza.Constant;
import com.example.pizza.MainActivity;
import com.example.pizza.R;
import com.example.pizza.bean.ItemBean;
import com.example.pizza.listPizza.ListPizzaActivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final ListView listItemView = root.findViewById(R.id.lstItemView);
//        final TextView txtPizzaSel = root.findViewById(R.id.txtPizzaSel);
//        final TextView txtTrasporto = root.findViewById(R.id.txtTrasporto);
//
//        homeViewModel.getStrPizzaSel().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                txtPizzaSel.setText(s);
//            }
//        });
//
//        homeViewModel.getStrTrasporto().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                txtTrasporto.setText(s);
//            }
//        });

        homeViewModel.getDataItemBean().observe(this, new Observer<List<ItemBean>>() {
            @Override
            public void onChanged(List<ItemBean> lstItemBean) {
                if (lstItemBean != null && lstItemBean.size() != 0) {
                    boolean pagato = false;
                    BigDecimal importoTotale = new BigDecimal(BigDecimal.ZERO.doubleValue());
                    List<ItemBean> lstItemBeanClone = new ArrayList<ItemBean>();

                    for (ItemBean itemBean : lstItemBean) {
                        if (itemBean.isPagato()) {
                            pagato = true;
                        }
                        if (itemBean.isPizzaBaby() && itemBean.getPrezzo() != null && itemBean.getPrezzo().doubleValue() > 0) {
                            itemBean.setPrezzo(itemBean.getPrezzo().subtract(new BigDecimal("0.50")));
                            itemBean.setStrEuro(String.valueOf(itemBean.getPrezzo()));
                        }
                        lstItemBeanClone.add(itemBean);

                        if (itemBean.getPrezzo() != null && !itemBean.getPrezzo().equals(BigDecimal.ZERO)) {
                            importoTotale = importoTotale.add(itemBean.getPrezzo());
                        }
                    }

                    ItemBean itemBeanNew = new ItemBean(Constant.IMPORTO_TOTALE + (pagato ? " pagato" : " da pagare"), "", "", importoTotale.toString(), -1, 3);
                    itemBeanNew.setOrder(3);
                    itemBeanNew.setPagato(pagato);
                    lstItemBean.add(itemBeanNew);
                }

                ListItemAdapter adapterList = new ListItemAdapter(inflater, container, lstItemBean);
                listItemView.setAdapter(adapterList);
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentActivity activity = getActivity();
        if (activity != null && activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity)activity;
            if (mainActivity.getPizzaBean() != null) {
//                String strTrasporto = mainActivity.getPizzaBean().isTrasporto() ? "Spese Trasporto: € 2,00" : "";
//                getHomeViewModel().changeStrPizzaSel(mainActivity.getPizzaBean().getDescrizione(), strTrasporto);
//
//                SmoothProgressBar smoothProgressBar = activity.findViewById(R.id.progressbar);
//                smoothProgressBar.setVisibility(View.INVISIBLE);
            }

            final SwipyRefreshLayout homeRefresh = mainActivity.findViewById(R.id.homeRefresh);
            homeRefresh.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh(SwipyRefreshLayoutDirection direction) {
                    Log.d("MainActivity", "Refresh triggered at "
                            + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));

                    getHomeViewModel().clearDataItemBean();
                    mainActivity.initVariabili();
                    mainActivity.getResultsFromApi();

                    homeRefresh.setRefreshing(false);
                }
            });
        }
    }

    public HomeViewModel getHomeViewModel() {
        return homeViewModel;
    }
}