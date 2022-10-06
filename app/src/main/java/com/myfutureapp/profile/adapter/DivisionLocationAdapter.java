package com.myfutureapp.profile.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myfutureapp.R;
import com.myfutureapp.profile.model.StateWithRegionResponse.StateRegionResponse;

import java.util.List;

public class DivisionLocationAdapter extends RecyclerView.Adapter<DivisionLocationAdapter.QuizViewHolder> {

    public ViewLoaded viewLoaded;
    List<StateRegionResponse> stateRegionResponses;
    private final Context context;

    public DivisionLocationAdapter(Context context, List<StateRegionResponse> stateRegionResponses, ViewLoaded viewLoaded) {
        this.context = context;
        this.stateRegionResponses = stateRegionResponses;
        this.viewLoaded = viewLoaded;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.divison_area_item, parent, false);
        return new QuizViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        holder.divisionAreaRV.setHasFixedSize(true);
        GridLayoutManager mLayoutManager = new GridLayoutManager(context, 3);
        holder.divisionAreaRV.setLayoutManager(mLayoutManager);
        holder.divisionAreaRV.setItemAnimator(new DefaultItemAnimator());
        holder.setIsRecyclable(false);

        holder.divisionAreaRV.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("poistion", holder.getAdapterPosition() + " d");
                viewLoaded.viewLoadedRV(holder.getAdapterPosition());
            }
        });
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        DivisionLocationCityAdapter divisionLocationCityAdapter = new DivisionLocationCityAdapter(context, stateRegionResponses.get(position).state, new DivisionLocationCityAdapter.CitySelectedUnselect() {
            @Override
            public void citySelected() {
                viewLoaded.allIndiaDisable();
            }
        });

        holder.divisionAreaRV.setAdapter(divisionLocationCityAdapter);

      /*  holder.quizTrendingRL.setOnClickListener(view -> {
            Intent intent = new Intent(context, AllQuizActivity.class);
            intent.putExtra("offset", trendingQuizResponse.next_offset);
            intent.putExtra("screenName", AppConstants.SECTION_QUIZ_TRENDING_LAYOUT);
            intent.putExtra("sectionTitle", sectionTitle);
            intent.putExtra("type", "");
            intent.putParcelableArrayListExtra("dataItemList", trendingQuizResponse.data);
            context.startActivity(intent);
        });*/
    }

    public void notifyItem(List<StateRegionResponse> stateRegionResponseList) {
        stateRegionResponses.clear();
        stateRegionResponses.addAll(stateRegionResponseList);
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
        return stateRegionResponses.size();
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