package com.example.pizza.listSnack;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.pizza.R;
import com.example.pizza.listPizza.ListPizzaAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListSnackAdapter extends BaseAdapter {

    List<SnackBean> mDataList;
    private ListSnackActivity listSnackActivity;

    public ListSnackAdapter(ListSnackActivity listSnackActivity, List<SnackBean> mDataList) {
        this.listSnackActivity = listSnackActivity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ListSnackAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(listSnackActivity, R.layout.list_snack, null);
            holder = new ListSnackAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ListSnackAdapter.ViewHolder) convertView.getTag();
        }

        ImageView imgSnack = convertView.findViewById(R.id.imgSnack);
        TextView titleSnack = convertView.findViewById(R.id.titleSnack);
        TextView descSnack = convertView.findViewById(R.id.descSnack);

        SnackBean snackBean = mDataList.get(position);
        titleSnack.setText(snackBean.getTitolo());
        descSnack.setText(snackBean.getDescrizione() + "\r\n" + snackBean.getDescrizione2() + " € " + snackBean.getStrEuro());
        if (snackBean.getIdImmagine() != -1) {
            imgSnack.setImageResource(snackBean.getIdImmagine());
            imgSnack.setVisibility(View.VISIBLE);
            titleSnack.setTextSize(20);
        } else {
            imgSnack.setVisibility(View.GONE);
            titleSnack.setTextSize(18);
        }

        final View view = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return to the MainActivity
                ListSnackAdapter.this.listSnackActivity.selectSnack(snackBean);
            }
        });

        return view;
    }

    private static class ViewHolder {
        private View view;
        private ImageView imageView;
        private TextView txtTitleSnack;
        private TextView txtDescSnack;

        private ViewHolder(View view) {
            this.view = view;
            imageView = view.findViewById(R.id.imgSnack);
            txtTitleSnack = view.findViewById(R.id.titleSnack);
            txtDescSnack = view.findViewById(R.id.descSnack);
        }
    }
}
