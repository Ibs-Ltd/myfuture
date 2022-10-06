package com.myfutureapp.jobDetail.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.jobDetail.model.JobDetailResponse.JobDetailModel.EligibilityObject;
import com.myfutureapp.util.Helper;

import java.util.List;

public class JobEligibilityAdapter extends RecyclerView.Adapter<JobEligibilityAdapter.ViewHolder> {

    private final Context context;
    private final List<EligibilityObject> eligibilities;


    public JobEligibilityAdapter(Context context, List<EligibilityObject> eligibilities) {
        this.context = context;
        this.eligibilities = eligibilities;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_eligibile_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (Helper.isContainValue(eligibilities.get(position).key)) {
            holder.jobEligibileHeading.setText(eligibilities.get(position).key + ": ");
        }
        if (Helper.isContainValue(eligibilities.get(position).value)) {
            holder.jobEligibileValue.setText(eligibilities.get(position).value);
        }

        if (position == eligibilities.size() - 1) {
            holder.orTV.setVisibility(View.GONE);
        } else {
            holder.orTV.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return eligibilities.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void notifyItem(List<EligibilityObject> dataItemList) {
        this.eligibilities.addAll(dataItemList);
        notifyDataSetChanged();
    }

    private void setOnItemClick(LinearLayout itemLL, int position) {
        itemLL.setOnClickListener(view -> {
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView jobEligibileHeading, jobEligibileValue, orTV;

        ViewHolder(View v) {
            super(v);
            jobEligibileHeading = (TextView) v.findViewById(R.id.jobEligibileHeading);
            jobEligibileValue = (TextView) v.findViewById(R.id.jobEligibileValue);
            orTV = (TextView) v.findViewById(R.id.orTV);
        }
    }
}