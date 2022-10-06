package com.myfutureapp.dashboard.profile.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.jobApplyLocationSelection.model.JobCitySelectModel;
import com.myfutureapp.util.ItemEventsListener;

import java.util.List;

/**
 * Created by Deepak Chaudhary on 05-12-2020.
 */
public class ChooseOtherCitiesAdapter extends RecyclerView.Adapter<ChooseOtherCitiesAdapter.MyHolder> {
    List<JobCitySelectModel> jobCitySelectModel;
    ItemEventsListener itemEventsListener;
    CitySelect citySelects;
    private final Context context;

    public ChooseOtherCitiesAdapter(Context context, List<JobCitySelectModel> jobCitySelectModel, CitySelect citySelects) {
        this.context = context;
        this.jobCitySelectModel = jobCitySelectModel;
        this.citySelects = citySelects;


    }

    public void notifyItem(List<JobCitySelectModel> updatedList) {
        jobCitySelectModel.clear();
        jobCitySelectModel.addAll(updatedList);
        notifyDataSetChanged();
    }

    public void notifyItemPositionList(int position) {
        jobCitySelectModel.get(position).isCitySelected = !jobCitySelectModel.get(position).isCitySelected;
        notifyItemChanged(position);

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.other_city_adapter_view, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if (jobCitySelectModel.get(position).isCitySelected) {
            holder.mainLLCity.setBackground(context.getResources().getDrawable(R.drawable.border_black_white_background));
            holder.tvCityName.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.mainLLCity.setBackground(context.getResources().getDrawable(R.drawable.black_background_normal));
            holder.tvCityName.setTextColor(context.getResources().getColor(R.color.white));
        }
        holder.tvCityName.setText(String.valueOf(jobCitySelectModel.get(position).ciyName));
        holder.tvCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!jobCitySelectModel.get(position).isCitySelected) {
                    citySelects.citySelected(jobCitySelectModel.get(position).ciyName, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobCitySelectModel.size();
    }

    public void setOnItemClickListner(ItemEventsListener itemEventsListener) {
        this.itemEventsListener = itemEventsListener;
    }

    public void notifyItemPosition(int position) {
        jobCitySelectModel.get(position).isCitySelected = !jobCitySelectModel.get(position).isCitySelected;
        notifyItemChanged(position);
    }

    public interface CitySelect {
        void citySelected(String cityName, int position);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private final TextView tvCityName;
        private final LinearLayout mainLLCity;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tvCityName = itemView.findViewById(R.id.tv_cityNameOtherCity);
            mainLLCity = itemView.findViewById(R.id.mainLLCity);

        }

    }

}



