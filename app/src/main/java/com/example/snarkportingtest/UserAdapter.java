package com.example.snarkportingtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHoler> {

    private ArrayList<User> arrayList;
    private Context context;

    public UserAdapter(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user, parent, false);
        UserViewHoler holder = new UserViewHoler(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHoler holder, int position) {
        holder.tv_useruid.setText(arrayList.get(position).getUid());
        holder.tv_uservotelist.setText(arrayList.get(position).getVotelist());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class UserViewHoler extends RecyclerView.ViewHolder {
        TextView tv_useruid;
        TextView tv_uservotelist;

        public UserViewHoler(@NonNull View itemView) {
            super(itemView);
            this.tv_useruid = itemView.findViewById(R.id.tv_userid);
            this.tv_uservotelist = itemView.findViewById(R.id.tv_userpw);
        }
    }
}
