package com.example.pizza.listPizza;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        final EditText editText = menu.findItem(R.id.menu_search).getActionView().findViewById(R.id.addresSearch);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                System.out.println(s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != lstPizzaAdapter && s != null) {
                    lstPizzaAdapter.getFilter().filter(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
//                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        menu.findItem(R.id.menu_search).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//              imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

                return true;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
