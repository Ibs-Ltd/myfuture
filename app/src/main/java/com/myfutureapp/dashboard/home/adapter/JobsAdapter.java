package com.myfutureapp.dashboard.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.perf.metrics.AddTrace;
import com.myfutureapp.R;
import com.myfutureapp.dashboard.home.model.JobsForYouResponse;
import com.myfutureapp.util.AppConstants;
import com.myfutureapp.util.AppPreferences;
import com.myfutureapp.util.DateUtil;
import com.myfutureapp.util.DeBouncedClickListener;

import java.util.List;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    private final List<JobsForYouResponse.JobDetail> details;
    private final JobSelectedForDetail jobSelectedForDetail;


    public JobsAdapter(List<JobsForYouResponse.JobDetail> details, JobSelectedForDetail jobSelectedForDetail) {
        this.details = details;
        this.jobSelectedForDetail = jobSelectedForDetail;

    }

    @NonNull
    @Override
    @AddTrace(name = "onCreateViewHolderTrace", enabled = true /* optional */)
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (details.get(position).company_name != null)
            holder.id_company_name.setText(details.get(position).company_name);
        if (details.get(position).designation != null)
            holder.id_job_title.setText(details.get(position).designation);
        if (details.get(position).work_experience != null) {
            holder.id_work_experience.setText("Work Exp: " + details.get(position).work_experience);
        }
        if (details.get(position).no_of_vacancies != null) {
            holder.jobVacancies.setText("Vacancies: " + details.get(position).no_of_vacancies);
        }

        if (details.get(position).city_name != null)
            holder.id_location.setText(details.get(position).city_name);
        if (details.get(position).compensation_show != null)
            holder.id_salary.setText("Salary: " + details.get(position).compensation_show);
        if (details.get(position).role_show != null)
            holder.id_job_timing_status.setText(details.get(position).role_show);
        if (details.get(position).logo != null) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder);
            Glide.with(holder.logo).load(details.get(position).logo).apply(options).into(holder.logo);
        }


        if (details.get(position).deadline != null) {
            holder.id_deadline_date.setText(DateUtil.convertyyyymmddhhmmsstoddmmyyyy(details.get(position).deadline));

            holder.deadlineLL.setVisibility(View.VISIBLE);
        } else {
            holder.deadlineLL.setVisibility(View.GONE);
        }
        if (details.get(position).is_bookmarked == 0) {
            holder.id_bookmark.setImageResource(R.drawable.ic_bookmarks_unselected);
        } else {
            holder.id_bookmark.setImageResource(R.drawable.ic_bookmarks_selected);
        }

        holder.jobItemMainLL.setOnClickListener(new DeBouncedClickListener(2000) {
            @Override
            public void onDeBounceClick(View view) {
                jobSelectedForDetail.JobDetailSelected(details.get(position));
            }
        });

        holder.id_bookmark.setOnClickListener(new DeBouncedClickListener(2000) {
            @Override
            public void onDeBounceClick(View view) {
                if (!AppPreferences.getInstance(view.getContext()).getPreferencesString(AppConstants.AUTH).equalsIgnoreCase("true")) {
                    Toast.makeText(view.getContext(), "Please do login first", Toast.LENGTH_SHORT).show();
                } else {
                    if (details.get(position).is_bookmarked == 0) {
                        jobSelectedForDetail.JobBookMarkTask("1", details.get(position), position);
                    } else {
                        jobSelectedForDetail.JobBookMarkTask("0", details.get(position), position);

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void notifyItem(List<JobsForYouResponse.JobDetail> dataItemList) {
        this.details.clear();
        this.details.addAll(dataItemList);
        notifyDataSetChanged();
    }

    public void addMoreData(List<JobsForYouResponse.JobDetail> dataItemList) {
        int initialPos = details.size();
        this.details.addAll(dataItemList);
        notifyItemRangeInserted(initialPos, details.size());
    }

    public void notifyItemPosition(int position, int statusChanged) {
        this.details.get(position).is_bookmarked = statusChanged;
        notifyItemChanged(position);
    }

    public int notifyBookMarkPosition(int position, int statusChanged) {
        details.remove(position);
        notifyDataSetChanged();
        return details.size();
    }

    private void setOnItemClick(LinearLayout itemLL, int position) {
        itemLL.setOnClickListener(view -> {
        });
    }

    public interface JobSelectedForDetail {
        void JobDetailSelected(JobsForYouResponse.JobDetail selectedJob);

        void JobBookMarkTask(String bookMarkType, JobsForYouResponse.JobDetail jobDetail, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_company_name;
        ImageView logo;
        ImageView id_bookmark;
        TextView id_job_title;
        TextView id_job_timing_status;
        TextView id_salary;
        TextView id_work_experience;
        TextView id_location;
        TextView jobVacancies;
        TextView id_deadline_date;
        CardView jobItemMainLL;
        LinearLayout deadlineLL;


        ViewHolder(View v) {
            super(v);
            id_company_name = v.findViewById(R.id.id_company_name);
            logo = v.findViewById(R.id.logo);
            id_bookmark = v.findViewById(R.id.id_bookmark);
            id_job_title = v.findViewById(R.id.id_job_title);
            id_job_timing_status = v.findViewById(R.id.id_job_timing_status);
            id_salary = v.findViewById(R.id.id_salary);
            id_work_experience = v.findViewById(R.id.id_work_experience);
            id_location = v.findViewById(R.id.id_location);
            jobVacancies = v.findViewById(R.id.jobVacancies);
            id_deadline_date = v.findViewById(R.id.id_deadline_date);
            deadlineLL = v.findViewById(R.id.deadlineLL);
            jobItemMainLL = v.findViewById(R.id.jobItemMainLL);

        }
    }
}