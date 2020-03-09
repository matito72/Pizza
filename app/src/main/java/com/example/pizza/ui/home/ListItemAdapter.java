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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import com.example.pizza.Constant;
import com.example.pizza.MainActivity;
import com.example.pizza.R;
import com.example.pizza.bean.ItemBean;
import com.example.pizza.listPizza.ListPizza;
import com.example.pizza.listPizza.ListPizzaActivity;
import com.example.pizza.listSnack.ListSnackActivity;
import com.example.pizza.util.DrawableProvider;

import java.util.List;

import static com.example.pizza.MainActivity.SELECT_PIZZA;
import static com.example.pizza.MainActivity.SELECT_SNACK;

public class ListItemAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ViewGroup container;
    private List<ItemBean> mDataList;
    private HomeViewModel homeViewModel;

    public ListItemAdapter(LayoutInflater inflater, ViewGroup container, List<ItemBean> mDataList, HomeViewModel homeViewModel) {
        this.inflater = inflater;
        this.container = container;
        this.mDataList = mDataList;
        this.homeViewModel = homeViewModel;
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

    public void changeDataItemBeanSel(boolean reload, List<ItemBean> lstItemBean){
        homeViewModel.changeDataItemBeanSel(lstItemBean);

        if (reload)
            notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListItemAdapter.ViewHolder holder;
        ItemBean itemBean = mDataList.get(position);
        MainActivity mainActivity = (MainActivity)parent.getContext();

        if (convertView == null) {
            if (itemBean.getOrder() == 1 || itemBean.getOrder() == 2) {
                convertView = inflater.inflate(R.layout.list_item, container, false);
            } else if (itemBean.getOrder() == 23) {
                convertView = inflater.inflate(R.layout.list_item_descrizione, container, false);
            } else if (itemBean.getOrder() == 3) {
                convertView = inflater.inflate(R.layout.list_item_totale, container, false);
            } else {
                convertView = inflater.inflate(R.layout.list_item, container, false);
            }

            holder = new ListItemAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }

        if (itemBean.getOrder() == 1 || itemBean.getOrder() == 2) {
            // --------------------
            // PIZZA e SNACK
            // --------------------
            CardView cardPizza = convertView.findViewById(R.id.cardPizza);
            ImageView imgItem = convertView.findViewById(R.id.imgItem);
            TextView titleItem = convertView.findViewById(R.id.titoloItem);
            TextView descItem = convertView.findViewById(R.id.descrizioneItem);
            TextView txtBabyPizza = convertView.findViewById(R.id.txtBabyPizza);
            TextView prezzoItem = convertView.findViewById(R.id.prezzoItem);

            titleItem.setTextSize(20);

            if (Constant.NESSUNA_PIZZA_SELEZIONATA.equals(itemBean.getTitolo())) {
                titleItem.setText("Nessuna pizza selezionata");
//                imgItem.setVisibility(View.GONE);
                imgItem.setImageResource(R.drawable.piatto_vuoto);
                descItem.setText("");
                prezzoItem.setText("€ 0");
            } else if (Constant.NESSUN_SNACK_SELEZIONATO.equals(itemBean.getTitolo())) {
                titleItem.setText("Nessun snack selezionato");
                imgItem.setVisibility(View.GONE);
                descItem.setText("");
//                cardPizza.setVisibility(View.GONE);
                prezzoItem.setText("€ 0");
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
            }
        } else if(itemBean.getOrder() == 23) {
            // --------------------
            // NOTE e BABY PIZZA
            // --------------------
            CardView cardView = convertView.findViewById(R.id.cardNote);
            TextView txtTitolo = convertView.findViewById(R.id.txtTitolo);
            TextView txtDescrizione = convertView.findViewById(R.id.txtDescrizione);

            CheckBox ckBabyPizza = convertView.findViewById(R.id.ckBabyPizza);
            ckBabyPizza.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            mainActivity.setPizzaBabySel(isChecked);
                            mainActivity.execMakRequestTask();
                        }
                    }
            );

            if (itemBean.getDescrizione() != null && itemBean.getDescrizione().trim().length() != 0) {
                txtTitolo.setText("Note");
                txtDescrizione.setText(itemBean.getDescrizione());
            }
//            else {
//                cardView.setVisibility(View.GONE);
//                txtTitolo.setVisibility(View.GONE);
//                txtDescrizione.setVisibility(View.GONE);
//                convertView.setVisibility(View.GONE);
//            }

            ckBabyPizza.setChecked(itemBean.isPizzaBaby());

            if ((parent.getContext() instanceof  MainActivity)) {
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mainActivity.openPopupNoteAndBabyPizza(view);
                    }
                });
            }
        } else if (itemBean.getOrder() == 3) {
            // ----------------
            // TOTALE
            // ----------------
            TextView txtTotale = convertView.findViewById(R.id.txtTotale);
            txtTotale.setText(itemBean.getTitolo());

            TextView txtImportoTotale = convertView.findViewById(R.id.txtImportoTotale);
            txtImportoTotale.setText("€ " + itemBean.getStrEuro());

            TextView txtTrasporto = convertView.findViewById(R.id.txtTrasporto);
            if (itemBean.isTrasportoDaPagare()) {
                txtTrasporto.setText(" Trasporto: € 2,00");
                txtTrasporto.setVisibility(View.VISIBLE);
            } else {
                txtTrasporto.setVisibility(View.GONE);
            }

            CardView cardTotale = convertView.findViewById(R.id.cardTotale);
            if (itemBean.isPagato()) {
                cardTotale.setBackgroundColor(Color.parseColor("#85edb9"));
            } else {
                cardTotale.setBackgroundColor(Color.parseColor("#f18db3"));
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
