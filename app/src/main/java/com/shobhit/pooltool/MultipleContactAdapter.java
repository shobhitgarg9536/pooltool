package com.shobhit.pooltool;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Shobhit-pc on 12/29/2016.
 */

public class MultipleContactAdapter extends RecyclerView.Adapter<MultipleContactAdapter.MyViewHolder> {

    private List<MultipleContactObject> mutiplecontactLists;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name , number;
        public CheckBox selected;
        final RecyclerView.ViewHolder holder = null;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewmultipleContactName);
            number = (TextView) view.findViewById(R.id.textviewMultipleContactNumber);
            selected = (CheckBox) view.findViewById(R.id.checkboxMultipleselected);

        }
    }


    public MultipleContactAdapter(List<MultipleContactObject> multiplecontactLists) {
        this.mutiplecontactLists = multiplecontactLists;
    }

    @Override
    public MultipleContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mutiple_contact_select_view, parent, false);

        return new MultipleContactAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MultipleContactAdapter.MyViewHolder holder, int position) {
            final MultipleContactObject mutiplecontactObject = mutiplecontactLists.get(position);
            holder.name.setText(mutiplecontactObject.getName());
            holder.number.setText(mutiplecontactObject.getNumber());
            holder.selected.setSelected(mutiplecontactObject.isSelected());
        holder.selected.setOnCheckedChangeListener(null);
        holder.selected.setChecked(mutiplecontactObject.isSelected());

            holder.selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton vw,
                                             boolean isChecked) {

                    mutiplecontactObject.setSelected(
                            isChecked);
                }
            });

        }


    @Override
    public int getItemCount() {
        return mutiplecontactLists.size();
    }
}