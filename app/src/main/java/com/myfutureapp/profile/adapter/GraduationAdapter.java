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
import com.myfutureapp.profile.model.GraduationCourseResponse;
import com.myfutureapp.util.Helper;

import java.util.List;

public class GraduationAdapter extends RecyclerView.Adapter<GraduationAdapter.ViewHolder> {

    private final Context context;
    private final List<GraduationCourseResponse.GraduationCourseModel> graduationList;
    private final CourseSelected courseSelected;

    public GraduationAdapter(Context context, List<GraduationCourseResponse.GraduationCourseModel> graduationList, CourseSelected courseSelected) {
        this.context = context;
        this.graduationList = graduationList;
        this.courseSelected = courseSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_detail_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Helper.isContainValue(graduationList.get(position).course_name)) {
            holder.courseName.setText(graduationList.get(position).course_name);
        }
        holder.courseLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                courseSelected.courseChoosen(graduationList.get(position));
                for (int i = 0; i < graduationList.size(); i++) {
                    graduationList.get(i).isSelected = false;
                }
                graduationList.get(position).isSelected = true;
                notifyDataSetChanged();

            }
        });

        if (!graduationList.get(position).isSelected) {
            holder.courseLL.setBackground(context.getResources().getDrawable(R.drawable.grey_btn));
            holder.courseName.setTextColor(context.getResources().getColor(R.color.warm_gray));

        } else {
            holder.courseLL.setBackground(context.getResources().getDrawable(R.drawable.orange_btn));
            holder.courseName.setTextColor(context.getResources().getColor(R.color.white));

        }
    }

    public void setPoistionSelected(int poistion) {
        for (int i = 0; i < graduationList.size(); i++) {
            graduationList.get(i).isSelected = false;
        }
        graduationList.get(poistion).isSelected = true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return graduationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void notifyItem(List<GraduationCourseResponse.GraduationCourseModel> dataItemList) {
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

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView courseName;
        LinearLayout courseLL;

        ViewHolder(View v) {
            super(v);
            courseLL = v.findViewById(R.id.courseLL);
            courseName = v.findViewById(R.id.courseName);
        }
    }
}