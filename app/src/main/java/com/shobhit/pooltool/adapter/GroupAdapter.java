package com.shobhit.pooltool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shobhit.pooltool.R;
import com.shobhit.pooltool.model.Group;

import java.util.List;


public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {

    private List<Group> groupLists;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView group , date;
        ImageView groupIcon;

        public MyViewHolder(View view) {
            super(view);
            group = (TextView) view.findViewById(R.id.textViewGroupViewName);
            date = (TextView) view.findViewById(R.id.textViewGroupViewDate);
            groupIcon = (ImageView) view.findViewById(R.id.imageViewGroupViewImage);
        }
    }


    public GroupAdapter(List<Group> groupList) {
        this.groupLists = groupList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.groupview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Group groupList = groupLists.get(position);
        holder.group.setText(groupList.getgroupName());
        holder.date.setText(groupList.getDate());
        holder.groupIcon.setImageResource(groupList.getImage());
    }

    @Override
    public int getItemCount() {
        return groupLists.size();
    }
}