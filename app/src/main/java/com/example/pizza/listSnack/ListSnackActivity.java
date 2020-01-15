package com.example.pizza.listSnack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizza.Constant;
import com.example.pizza.R;
import com.example.pizza.listPizza.ListPizzaActivity;

import java.util.ArrayList;

public class ListSnackActivity extends AppCompatActivity {

    private static SnackBean SNACK_BEAN_VUOTO = new SnackBean(Constant.NESSUN_SNACK_SELEZIONATO, "", "()", "0", -1);
    private static ArrayList<SnackBean> list = createLstSnackBean();

    ListView listView;
    ListSnackAdapter adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack);

        listView = findViewById(R.id.lstViewSnack);

//        listShow();

        adapterList = new ListSnackAdapter(this, list);
        listView.setAdapter(adapterList);

        getSupportActionBar().setTitle("Lista Snack ...");
    }

    public void selectSnack(SnackBean snackBean) {
        Intent intent = new Intent();
        intent.putExtra(Constant.SNACK_SEL, snackBean);
        setResult(ListSnackActivity.RESULT_OK, intent);
        finish();
    }

    public static int findSnackBeanImageFromTitolo(String titolo, String descrizione2) {
        int img = R.drawable.pizza;

        if (titolo != null && !titolo.trim().equals("")) {
            if ("ALETTE DI POLLO".equals(titolo)) {
                img = R.drawable.ali_pollo;
            } else if ("ANELLI DI CIPOLLA".equals(titolo)) {
                img = R.drawable.anelli_cipolla;
            } else if ("MOZZARELLINE FRITTE".equals(titolo)) {
                img = R.drawable.mozzarelle_fritte;
            } else if ("NUGGETS DI POLLO".equals(titolo)) {
                img = R.drawable.nuggets;
            } else if ("PATATINE FRITTE".equals(titolo) && descrizione2.equals("(GRANDE)")) {
                img = R.drawable.patatine_grande;
            } else if ("PATATINE FRITTE".equals(titolo) && descrizione2.equals("(PICCOLA)")) {
                img = R.drawable.patatine_piccola;
            } else if ("VASCHETTA DI FALAFEL".equals(titolo)) {
                img = R.drawable.falafel;
            }
        }

        return img;
    }

    private static ArrayList<SnackBean> createLstSnackBean() {
        ArrayList<SnackBean> l = new ArrayList<SnackBean>();

        l.add(new SnackBean("Alette di pollo", "Porzione alette di pollo", "(5 pezzi)", "3,50", R.drawable.ali_pollo));
        l.add(new SnackBean("Anelli di cipolla", "Porzione anelli di cipolla", "(10 pezzi)", "3,00", R.drawable.anelli_cipolla));
        l.add(new SnackBean("Mozzarelline fritte", "Porzione mozzarelline fritte", "(10 pezzi)", "4,00", R.drawable.mozzarelle_fritte));
        l.add(new SnackBean("Nuggets di pollo", "Porzione nuggets di pollo", "(10 pezzi)", "4,00", R.drawable.nuggets));
        l.add(new SnackBean("Patatine fritte", "Porzione patatine fritte", "(GRANDE)", "3,00", R.drawable.patatine_piccola));
        l.add(new SnackBean("Patatine fritte", "Porzione patatine fritte", "(PICCOLA)","2,00", R.drawable.patatine_grande));
        l.add(new SnackBean("Vaschetta di Falafel", "Porzione vaschetta di falafel", "(5 pezzi)", "4,00", R.drawable.falafel));
        l.add(SNACK_BEAN_VUOTO);

        return l;
    }
}