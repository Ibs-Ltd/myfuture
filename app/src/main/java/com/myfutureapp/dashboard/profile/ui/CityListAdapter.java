package com.myfutureapp.dashboard.profile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.profile.model.ProfileCityListRsponseModel;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.myHolder> {

    private final List<ProfileCityListRsponseModel.ProfileCityListDataModel> citrylistG;
    private final Context context;
    private final CitySelected citySelected;

    public CityListAdapter(Context context, List<ProfileCityListRsponseModel.ProfileCityListDataModel> citrylistG, CitySelected citySelected) {
        this.context = context;
        this.citrylistG = citrylistG;
        this.citySelected = citySelected;
    }

    @NonNull
    @Override
    public CityListAdapter.myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ciry_list_adapter_layout, parent, false);
        return new CityListAdapter.myHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int position) {
        holder.tv_city_name.setText("   " + citrylistG.get(position).name);
        holder.tv_city_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citySelected.citySelected(citrylistG.get(position));
            }
        });


    }

    @Override
    public int getItemCount() {
        return citrylistG.size();
    }

    public void notifyItem(List<ProfileCityListRsponseModel.ProfileCityListDataModel> dataItemList) {
        citrylistG.clear();
        this.citrylistG.addAll(dataItemList);
        notifyDataSetChanged();
    }

    public void notifyItemPostion(int postion, ProfileCityListRsponseModel.ProfileCityListDataModel dataItemList) {
        this.citrylistG.set(postion, dataItemList);
        notifyDataSetChanged();
    }

    public void clearAll() {
        citrylistG.clear();
        notifyDataSetChanged();
    }

    public interface CitySelected {
        void citySelected(ProfileCityListRsponseModel.ProfileCityListDataModel cityListDataModel);
    }

    public class myHolder extends RecyclerView.ViewHolder {
        TextView tv_city_name;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            tv_city_name = itemView.findViewById(R.id.tv_city_name);
        }

    }
}
