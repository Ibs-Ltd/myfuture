package com.myfutureapp.jobDetail.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.jobDetail.model.JobDetailResponse.JobDetailModel.EligibilityCreteria;

import java.util.List;

public class JobEligibilityDivisionAdapter extends RecyclerView.Adapter<JobEligibilityDivisionAdapter.QuizViewHolder> {

    List<EligibilityCreteria> eligibilityCreterias;

    public JobEligibilityDivisionAdapter(List<EligibilityCreteria> eligibilityCreterias) {
        this.eligibilityCreterias = eligibilityCreterias;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_division_item, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        holder.divisionAreaRV.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL, false);
        holder.divisionAreaRV.setLayoutManager(mLayoutManager);
        holder.divisionAreaRV.setItemAnimator(new DefaultItemAnimator());
        holder.setIsRecyclable(false);
        if (eligibilityCreterias.get(position).data != null) {
            JobEligibilityAdapter jobEligibilityAdapter = new JobEligibilityAdapter(holder.itemView.getContext(), eligibilityCreterias.get(position).data);
            holder.divisionAreaRV.setAdapter(jobEligibilityAdapter);
        }


    }

    public void notifyItem(List<EligibilityCreteria> stateRegionResponseList) {
        eligibilityCreterias.clear();
        eligibilityCreterias.addAll(stateRegionResponseList);
        notifyDataSetChanged();
    }

    @Override
    public void onViewRecycled(@NonNull QuizViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return eligibilityCreterias.size();
    }


    public interface ViewLoaded {
        void viewLoadedRV(int position);

        void allIndiaDisable();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {

        public RecyclerView divisionAreaRV;
        public LinearLayout divisionMainLL;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            divisionAreaRV = itemView.findViewById(R.id.divisionAreaRV);
            divisionMainLL = itemView.findViewById(R.id.divisionMainLL);

        }
    }
}