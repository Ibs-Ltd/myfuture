package com.myfutureapp.dashboard.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.dashboard.home.model.JobTagsResponse;
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.util.DeBouncedClickListener;

import java.util.ArrayList;
import java.util.List;

public class JobTitleAdapter extends RecyclerView.Adapter<JobTitleAdapter.ViewHolder> {

    List<JobTagsResponse.JobTagsdetail> list = new ArrayList<>();
    CallApi callApi;
    private final Context context;
    private int row_index = -1;


    public JobTitleAdapter(Context context, List<JobTagsResponse.JobTagsdetail> list, CallApi callApi) {
        this.context = context;
        this.list = list;
        this.callApi = callApi;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_titile_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.id_title.setText(list.get(position).name);
        holder.id_title.setOnClickListener(new DeBouncedClickListener(2000) {
            @Override
            public void onDeBounceClick(View view) {
                if (row_index == position) {
                    row_index = -1;
                    callApi.calljobapi("0");
                    holder.id_title.setTextColor(context.getResources().getColor(R.color.black));
                    holder.titleMainLL.setBackground(context.getResources().getDrawable(R.drawable.dark_slate_blue_background_btn));
                } else {
                    row_index = position;
                    holder.id_title.setTextColor(context.getResources().getColor(R.color.white));
                    holder.titleMainLL.setBackground(context.getResources().getDrawable(R.drawable.darkgrey_background_btn));
                    notifyDataSetChanged();
                    callApi.calljobapi(list.get(position).id);
                }
            }
        });
       /* holder.id_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/

        if (row_index == position) {
            holder.id_title.setTextColor(context.getResources().getColor(R.color.white));
            holder.titleMainLL.setBackground(context.getResources().getDrawable(R.drawable.darkgrey_background_btn));
        } else {
            holder.id_title.setTextColor(context.getResources().getColor(R.color.black));
            holder.titleMainLL.setBackground(context.getResources().getDrawable(R.drawable.dark_slate_blue_background_btn));
            // checkbox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
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


    public interface CourseSelected {

        void courseChoosen(GraduationCourseResponse.GraduationCourseModel graduationCourseModel);
    }

    public interface CallApi {
        void calljobapi(String Tag_id);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_title;
        LinearLayout titleMainLL;

        ViewHolder(View v) {
            super(v);
            id_title = v.findViewById(R.id.id_title);
            titleMainLL = v.findViewById(R.id.titleMainLL);

        }
    }
}