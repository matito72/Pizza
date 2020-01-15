package com.example.pizza.listPizza;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.pizza.Constant;
import com.example.pizza.R;
import com.example.pizza.util.UtilAddressImgData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ListPizzaAdapter extends BaseAdapter implements Filterable {
    private ItemFilter mFilter = new ItemFilter();
    private ListPizzaActivity listPizzaActivity;
    private List<PizzaBean> mDataList;
    private List<PizzaBean> mDataListFiltered;
    private TextDrawable.IBuilder mDrawableBuilder;

    public ListPizzaAdapter(ListPizzaActivity listPizzaActivity, List<PizzaBean> mDataList, List<PizzaBean> mDataListFiltered, TextDrawable.IBuilder mDrawableBuilder) {
        this.listPizzaActivity = listPizzaActivity;
        this.mDataList = mDataList;
        this.mDataListFiltered = mDataListFiltered;
        this.mDrawableBuilder = mDrawableBuilder;
    }

    @Override
    public int getCount() {
        return mDataListFiltered.size();
    }

    @Override
    public PizzaBean getItem(int position) {
        return mDataListFiltered.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(listPizzaActivity, R.layout.list_pizza_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PizzaBean item = getItem(position);

        // provide support for selected state
        updateCheckedState(holder, item, true);

//        holder.imageView.setOnClickListener(new View.OnClickListener() {
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // when the image is clicked, update the selected state
                PizzaBean data = getItem(position);
                data.setChecked(!data.isChecked());
                updateCheckedState(holder, data, false);

                // Return to the MainActivity
                ListPizzaAdapter.this.listPizzaActivity.addRemovePizza(data);
            }
        });
        if (item.getTitolo() != null) {
            if (item.getDescrizione2() != null)
                holder.txtDescPizza.setText(item.getTitolo() + ": " + item.getDescrizione2());
            else
                holder.txtDescPizza.setText(item.getTitolo());

            if (item.getPrezzo().compareTo(BigDecimal.ZERO)  > 0)
                holder.txtPrezzo.setText("€ " + String.valueOf(item.getPrezzo()));
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<PizzaBean> list = mDataList;

            int count = list.size();
            final ArrayList<PizzaBean> nlist = new ArrayList<PizzaBean>(count);

            PizzaBean filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.getDescrizione().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDataListFiltered = (ArrayList<PizzaBean>) results.values;
            notifyDataSetChanged();
        }
    }

    private void updateCheckedState(ViewHolder holder, PizzaBean item, boolean isInit) {
        if (isInit && UtilAddressImgData.lstAddressImgDataContains(item)) {
            item.setChecked(true);
        }

        if (item.isChecked()) {
            holder.imageView.setImageDrawable(mDrawableBuilder.build(" ", 0xff616161));
            holder.view.setBackgroundColor(Constant.HIGHLIGHT_COLOR);
            holder.checkIcon.setVisibility(View.VISIBLE);
        } else {
            TextDrawable drawable = mDrawableBuilder.build(String.valueOf(item.getDescrizione().charAt(0)), Constant.mColorGenerator.getColor(item.getDescrizione().substring(0, 1)));
            holder.imageView.setImageDrawable(drawable);
            holder.view.setBackgroundColor(Color.TRANSPARENT);
            holder.checkIcon.setVisibility(View.GONE);
        }
    }

    private static class ViewHolder {
        private View view;
        private ImageView imageView;
        private TextView txtDescPizza;
        private TextView txtPrezzo;
        private ImageView checkIcon;

        private ViewHolder(View view) {
            this.view = view;
            imageView = view.findViewById(R.id.imageView);
            txtDescPizza = view.findViewById(R.id.txtDescPizza);
            txtPrezzo = view.findViewById(R.id.txtPrezzo);
            checkIcon = view.findViewById(R.id.check_icon);
        }
    }
}
