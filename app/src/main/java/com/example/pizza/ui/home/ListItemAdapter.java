package com.example.pizza.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.fragment.app.FragmentActivity;

import com.example.pizza.Constant;
import com.example.pizza.MainActivity;
import com.example.pizza.R;
import com.example.pizza.bean.ItemBean;
import com.example.pizza.listPizza.ListPizza;
import com.example.pizza.listPizza.ListPizzaActivity;
import com.example.pizza.listSnack.ListSnackActivity;
import com.example.pizza.util.DrawableProvider;

import java.math.BigDecimal;
import java.util.List;

import static com.example.pizza.MainActivity.SELECT_PIZZA;
import static com.example.pizza.MainActivity.SELECT_SNACK;

public class ListItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ViewGroup container;
    private List<ItemBean> mDataList;

    public ListItemAdapter(LayoutInflater inflater, ViewGroup container, List<ItemBean> mDataList) {
        this.inflater = inflater;
        this.container = container;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListItemAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, container, false);

            holder = new ListItemAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } /* else {
            holder = (ListItemAdapter.ViewHolder) convertView.getTag();
        } */

        ImageView imgItem = convertView.findViewById(R.id.imgItem);
        TextView titleItem = convertView.findViewById(R.id.titoloItem);
        TextView descItem = convertView.findViewById(R.id.descrizioneItem);
        TextView prezzoItem = convertView.findViewById(R.id.prezzoItem);

        ItemBean itemBean = mDataList.get(position);

        if (Constant.NESSUNA_PIZZA_SELEZIONATA.equals(itemBean.getTitolo())) {
            titleItem.setText("Nessuna pizza selezionata");
            imgItem.setVisibility(View.GONE);
            descItem.setText("");
        } else if (Constant.NESSUN_SNACK_SELEZIONATO.equals(itemBean.getTitolo())) {
            titleItem.setText("Nessun snack selezionato");
            imgItem.setVisibility(View.GONE);
            descItem.setText("");
        } else if (itemBean.getTitolo().startsWith(Constant.IMPORTO_TOTALE)) {
            titleItem.setText(itemBean.getTitolo());
            imgItem.setVisibility(View.GONE);
            descItem.setText(itemBean.getStrEuro());
        } else {
            titleItem.setText(itemBean.getTitolo());

            if (itemBean.getTitolo().trim().length() >= 18) {
                titleItem.setTextSize(18);
            } else {
                titleItem.setTextSize(20);
            }

            imgItem.setImageResource(itemBean.getIdImmagine());

            if (itemBean.getOrder() == 1) {
                // PIZZA
                descItem.setText(itemBean.getDescrizione() + "\r\n" + itemBean.getDescrizione2());
            } else if (itemBean.getOrder() == 2) {
                // SNACK
                descItem.setText("\r\n" + itemBean.getDescrizione2());
            }

            if (itemBean.isPizzaBaby()) {
//                itemBean.setPrezzo(itemBean.getPrezzo().subtract(new BigDecimal("0.50")));
                prezzoItem.setText("Pizza Baby   € " + itemBean.getStrEuro());
            } else {
                prezzoItem.setText("€ " + itemBean.getStrEuro());
            }
        }

        if (itemBean.getOrder() == 1) {
            // PIZZA
            if ((parent.getContext() instanceof  MainActivity)) {
                MainActivity mainActivity = (MainActivity)parent.getContext();
                if (mainActivity.getPizzaBean() != null) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mainActivity, ListPizzaActivity.class) ;
                            intent.putExtra(Constant.TYPE, DrawableProvider.SAMPLE_ROUND);
                            intent.putExtra(Constant.LIST_PIZZA, new ListPizza(mainActivity.getDataList()));
                            mainActivity.startActivityForResult(intent, SELECT_PIZZA);
                        }
                    });
                }
            }
        } else if (itemBean.getOrder() == 2) {
            // SNACK
            if ((parent.getContext() instanceof  MainActivity)) {
                MainActivity mainActivity = (MainActivity) parent.getContext();

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mainActivity, ListSnackActivity.class) ;
                        mainActivity.startActivityForResult(intent, SELECT_SNACK);
                    }
                });
            }
        } else if (itemBean.getOrder() == 3) {
            if (itemBean.isPagato())
                convertView.setBackgroundColor(Color.GREEN);
            else
                convertView.setBackgroundColor(Color.RED);
        }

        final View view = convertView;
        return view;
    }


    private static class ViewHolder {
        private View view;
        private ImageView imgItem;
        private TextView txtTitoloItem;
        private TextView txtDescrizioneItem;

        private ViewHolder(View view) {
            this.view = view;
            imgItem = view.findViewById(R.id.imgItem);
            txtTitoloItem = view.findViewById(R.id.titoloItem);
            txtDescrizioneItem = view.findViewById(R.id.descrizioneItem);
        }
    }
}
