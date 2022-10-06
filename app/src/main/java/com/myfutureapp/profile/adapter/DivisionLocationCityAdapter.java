package com.myfutureapp.profile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myfutureapp.R;
import com.myfutureapp.profile.model.StateWithRegionResponse.StateRegionResponse.StateResponse;
import com.myfutureapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class DivisionLocationCityAdapter extends RecyclerView.Adapter<DivisionLocationCityAdapter.ViewHolder> {

    private final Context context;
    private final List<StateResponse> stateResponses;
    private final CitySelectedUnselect citySelectedUnselect;

    public DivisionLocationCityAdapter(Context context, List<StateResponse> stateResponses, CitySelectedUnselect citySelectedUnselect) {
        this.context = context;
        this.stateResponses = stateResponses;
        this.citySelectedUnselect = citySelectedUnselect;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_location_item, parent, false);
        return new ViewHolder(v);
    }

    public void notifyNewData(List<StateResponse> stateResponsess) {
        stateResponses.clear();
        stateResponses.addAll(stateResponsess);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cityMainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateResponses.get(position).citySelected = !stateResponses.get(position).citySelected;
                if (stateResponses.get(position).citySelected) {
                    holder.cityunselected.setVisibility(View.GONE);
                    holder.citySelected.setVisibility(View.VISIBLE);
                    stateResponses.get(position).citySelected = true;
//                    citySelectedUnselect.citySelected(stateResponses.get(position).name);
                } else {
                    holder.cityunselected.setVisibility(View.VISIBLE);
                    holder.citySelected.setVisibility(View.GONE);
                    stateResponses.get(position).citySelected = false;
//                    citySelectedUnselect.cityUnSelected(stateResponses.get(position).name);
                }
                citySelectedUnselect.citySelected();
//                PreferredJobLocationFragment.settingAllIndiaButton();
            }
        });

        if (stateResponses.get(position).citySelected) {
            holder.cityunselected.setVisibility(View.GONE);
            holder.citySelected.setVisibility(View.VISIBLE);
        } else {
            holder.cityunselected.setVisibility(View.VISIBLE);
            holder.citySelected.setVisibility(View.GONE);
        }
        holder.cityNameTV.setText(stateResponses.get(position).name);

        if (Helper.isContainValue(stateResponses.get(position).selected_url)) {
            Glide.with(context)
                    .load(stateResponses.get(position).selected_url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_vector_default_profile)
                    .into(holder.citySelectedIV);

            Glide.with(context)
                    .load(stateResponses.get(position).unselected_url)
                    .centerCrop()
                    .placeholder(R.drawable.ic_vector_default_profile)
                    .into(holder.cityunSelectedIV);
        } else {
            holder.citySelectedIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_vector_default_profile));
            holder.cityunSelectedIV.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_vector_default_profile));
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return stateResponses.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void notifyItem(ArrayList dataItemList) {
//        this.graduationList.addAll(dataItemList);
        notifyDataSetChanged();
    }

    private void setOnItemClick(LinearLayout itemLL, int position) {
        itemLL.setOnClickListener(view -> {
        });
    }

    public interface CitySelectedUnselect {
        void citySelected();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameTV;
        LinearLayout cityunselected, cityMainLL;
        CardView citySelected;
        ImageView cityunSelectedIV, citySelectedIV;

        ViewHolder(View v) {
            super(v);
            cityMainLL = v.findViewById(R.id.cityMainLL);
            cityunselected = v.findViewById(R.id.cityunselected);
            citySelected = v.findViewById(R.id.citySelected);
            cityunSelectedIV = v.findViewById(R.id.cityunSelectedIV);
            citySelectedIV = v.findViewById(R.id.citySelectedIV);
            cityNameTV = v.findViewById(R.id.cityNameTV);
        }
    }
}