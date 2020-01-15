package com.example.pizza.listPizza;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.pizza.Constant;
import com.example.pizza.R;
import com.example.pizza.util.DrawableProvider;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

public class ListPizzaActivity extends AppCompatActivity {
    private Cursor c;
    private TextDrawable.IBuilder mDrawableBuilder;

    private List<PizzaBean> mDataList = new ArrayList<PizzaBean>();
    private ListPizzaAdapter lstPizzaAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        int type = intent.getIntExtra(Constant.TYPE, DrawableProvider.SAMPLE_RECT);

        // initialize the builder based on the "TYPE"
        switch (type) {
            case DrawableProvider.SAMPLE_RECT:
                mDrawableBuilder = TextDrawable.builder()
                        .rect();
                break;
            case DrawableProvider.SAMPLE_ROUND_RECT:
                mDrawableBuilder = TextDrawable.builder()
                        .roundRect(10);
                break;
            case DrawableProvider.SAMPLE_ROUND:
                mDrawableBuilder = TextDrawable.builder()
                        .round();
                break;
            case DrawableProvider.SAMPLE_RECT_BORDER:
                mDrawableBuilder = TextDrawable.builder()
                        .beginConfig()
                        .withBorder(4)
                        .endConfig()
                        .rect();
                break;
            case DrawableProvider.SAMPLE_ROUND_RECT_BORDER:
                mDrawableBuilder = TextDrawable.builder()
                        .beginConfig()
                        .withBorder(4)
                        .endConfig()
                        .roundRect(10);
                break;
            case DrawableProvider.SAMPLE_ROUND_BORDER:
                mDrawableBuilder = TextDrawable.builder()
                        .beginConfig()
                        .withBorder(4)
                        .endConfig()
                        .round();
                break;
        }

        mDataList = ((ListPizza)intent.getExtras().get(Constant.LIST_PIZZA)).getmDataList();
        if (mDataList != null)
            initList();

        final SwipyRefreshLayout mSwipyRefreshLayout = findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d("ListPizzaActivity", "Refresh triggered at "
                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));

                 mSwipyRefreshLayout.setRefreshing(false);
            }
        });

        getSupportActionBar().setTitle("Lista Pizze ...");
    }

    public void addRemovePizza(PizzaBean pizzaBeanSel) {
        Intent intent = new Intent();
        intent.putExtra(Constant.PIZZA_SEL, pizzaBeanSel);
        setResult(ListPizzaActivity.RESULT_OK, intent);
        finish();
    }

    private void initList() {
        // init the list view and its adapter
        lstPizzaAdapter = new ListPizzaAdapter(ListPizzaActivity.this, mDataList, mDataList, mDrawableBuilder);

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(lstPizzaAdapter);
    }
}
