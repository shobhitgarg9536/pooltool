package com.shobhit.pooltool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shobhit.pooltool.model.ContactObject;
import com.shobhit.pooltool.R;

import java.util.List;

/**
 * Created by Shobhit-pc on 12/28/2016.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private List<ContactObject> contactLists;
    private int flag;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name , number; ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.textViewContactName);
            number = (TextView) view.findViewById(R.id.textViewContactno);
            image = (ImageView) view.findViewById(R.id.imageViewContactImage);
        }
    }


    public ContactAdapter(List<ContactObject> contactLists , int flag) {
        this.contactLists = contactLists;
        this.flag = flag;
    }

    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_view, parent, false);
        TextView invite = (TextView) itemView.findViewById(R.id.tvcontactinvite);
        if(flag == 1)
            invite.setVisibility(View.GONE);
        else
        invite.setVisibility(View.VISIBLE);

        return new ContactAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.MyViewHolder holder, int position) {
        ContactObject contactObject = contactLists.get(position);
        holder.name.setText(contactObject.getName());
        holder.number.setText(contactObject.getNumber());

        holder.image.setImageResource(contactObject.getImage());
    }

    @Override
    public int getItemCount() {
        return contactLists.size();
    }
}