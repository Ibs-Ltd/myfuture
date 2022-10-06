package com.myfutureapp.dashboard.profile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.util.ItemEventsListener;

import java.util.ArrayList;

public class ChoosePreferredCityAdapter extends RecyclerView.Adapter<ChoosePreferredCityAdapter.MyHolder> {
    ArrayList<String> showChoosePreferred;
    ItemEventsListener itemEventsListener;
    PreferredCityRemoved cityRemoved;
    private final Context context;


    public ChoosePreferredCityAdapter(Context context, ArrayList<String> showChoosePreferred, PreferredCityRemoved cityRemoved) {
        this.context = context;
        this.showChoosePreferred = showChoosePreferred;
        this.cityRemoved = cityRemoved;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_preferred_city_adapter_view, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tvCityName.setText(String.valueOf(showChoosePreferred.get(position)));
        int a = position + 1;
        holder.tv_preference.setText(a + " Preference");
        holder.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cityRemoved.preferredCityRemoved(showChoosePreferred.get(position), position);
            }
        });
    }

    public void remove(int position) {
        showChoosePreferred.remove(position);
        notifyDataSetChanged();
    }

    public int selectCitySize() {
        return showChoosePreferred.size();
    }

    @Override
    public int getItemCount() {
        return showChoosePreferred.size();
    }

    public void setOnItemClickListner(ItemEventsListener itemEventsListener) {
        this.itemEventsListener = itemEventsListener;
    }

    public void notifyItem(String dataItemList) {
        this.showChoosePreferred.add(dataItemList);
        notifyDataSetChanged();
    }

    public void removeAt(int position) {
        showChoosePreferred.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, showChoosePreferred.size());
        notifyDataSetChanged();
    }

    public interface PreferredCityRemoved {
        void preferredCityRemoved(String cityRemovedName, int position);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private final TextView tvCityName;
        private final TextView tv_preference;
        private final ImageView ivCancel;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tv_cityName);
            ivCancel = itemView.findViewById(R.id.iv_cancel);
            tv_preference = itemView.findViewById(R.id.tv_preference);
        }

    }
}
