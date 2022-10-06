package com.myfutureapp.profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.profile.model.CityListResponse;
import com.myfutureapp.util.Helper;
import com.myfutureapp.util.OnItemClickEvent;

import java.util.List;


public class CityManuallyAdapter extends RecyclerView.Adapter<CityManuallyAdapter.ViewHolder> {

    private final Context context;
    private final List<CityListResponse.CityModel> cityModels;
    private final CitySelected citySelected;
    private OnItemClickEvent onItemClickEvent;

    public CityManuallyAdapter(Context context, List<CityListResponse.CityModel> cityModels, CitySelected citySelected) {
        this.context = context;
        this.cityModels = cityModels;
        this.citySelected = citySelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manually_city_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Helper.isContainValue(cityModels.get(position).name)) {
            holder.cityName.setText(cityModels.get(position).name);
        }
        holder.cityMainItemLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citySelected.citySelect(cityModels.get(position));
                onItemClickEvent.onItemClicked(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityModels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void clearAll() {
        cityModels.clear();
        notifyDataSetChanged();
    }

    public void notifyItem(List<CityListResponse.CityModel> dataItemList) {
        this.cityModels.addAll(dataItemList);
        notifyDataSetChanged();
    }

    public void notifyItemPostion(int postion, CityListResponse.CityModel dataItemList) {
        this.cityModels.set(postion, dataItemList);
        notifyDataSetChanged();
    }

    private void setOnItemClick(LinearLayout itemLL, int position) {
        itemLL.setOnClickListener(view -> {
        });
    }

    public void setOnItemClicked(OnItemClickEvent onItemClickEvent) {
        this.onItemClickEvent = onItemClickEvent;
    }

    public interface CitySelected {
        void citySelect(CityListResponse.CityModel cityModel);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView cityName;
        LinearLayout cityMainItemLL;

        ViewHolder(View v) {
            super(v);
            cityName = v.findViewById(R.id.cityName);
            cityMainItemLL = v.findViewById(R.id.cityMainItemLL);
        }

        @Override
        public void onClick(View v) {
            onItemClickEvent.onItemClicked(v, getAdapterPosition());
        }
    }
}