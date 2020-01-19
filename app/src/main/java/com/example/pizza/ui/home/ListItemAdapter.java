package com.example.pizza.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.cardview.widget.CardView;
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
        ItemBean itemBean = mDataList.get(position);
        MainActivity mainActivity = (MainActivity)parent.getContext();

        if (convertView == null) {
            if (itemBean.getOrder() != 23) {
                convertView = inflater.inflate(R.layout.list_item, container, false);
            } else if (itemBean.getOrder() == 23) {
                convertView = inflater.inflate(R.layout.list_item_descrizione, container, false);
            }

            holder = new ListItemAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }

        if(itemBean.getOrder() != 23) {
            ImageView imgItem = convertView.findViewById(R.id.imgItem);
            TextView titleItem = convertView.findViewById(R.id.titoloItem);
            TextView descItem = convertView.findViewById(R.id.descrizioneItem);
            TextView txtBabyPizza = convertView.findViewById(R.id.txtBabyPizza);
            TextView prezzoItem = convertView.findViewById(R.id.prezzoItem);

            titleItem.setTextSize(20);

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
//                titleItem.setTextSize(20);
                descItem.setText("");
                descItem.setVisibility(View.GONE);
                prezzoItem.setText("€ " + itemBean.getStrEuro());
            } else if (itemBean.getTitolo() != null) {
                titleItem.setText(itemBean.getTitolo());
                if (itemBean.getTitolo().trim().length() >= 18) {
                    titleItem.setTextSize(18);
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
                    txtBabyPizza.setVisibility(View.VISIBLE);
//                    txtBabyPizza.setText("Pizza Baby");

                    SpannableString mySpannableString = new SpannableString("Pizza Baby");
                    mySpannableString.setSpan(new UnderlineSpan(), 0, mySpannableString.length(), 0);
                    txtBabyPizza.setText(mySpannableString);

                    txtBabyPizza.setTypeface(null, Typeface.BOLD_ITALIC);
                    txtBabyPizza.setHighlightColor(Color.MAGENTA);
                } else {
                    txtBabyPizza.setVisibility(View.GONE);
                }
                prezzoItem.setText("€ " + itemBean.getStrEuro());
            }

            if (itemBean.getOrder() == 1) {
                // PIZZA
                if ((parent.getContext() instanceof  MainActivity)) {
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
        } else if(itemBean.getOrder() == 23) {
            CardView cardView = convertView.findViewById(R.id.cardNote);
            TextView txtTitolo = convertView.findViewById(R.id.txtTitolo);
            TextView txtDescrizione = convertView.findViewById(R.id.txtDescrizione);

            if (itemBean.getDescrizione() != null && itemBean.getDescrizione().trim().length() != 0) {
                txtTitolo.setText("Note");
                txtDescrizione.setText(itemBean.getDescrizione());
            } else {
                cardView.setVisibility(View.GONE);
                txtTitolo.setVisibility(View.GONE);
                txtDescrizione.setVisibility(View.GONE);
                convertView.setVisibility(View.GONE);
            }

            if ((parent.getContext() instanceof  MainActivity)) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainActivity.withMultiChoiceItems(view);
                    }
                });
            }
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
