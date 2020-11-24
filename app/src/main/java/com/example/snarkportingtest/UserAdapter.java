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
        holder.tv_userid.setText(arrayList.get(position).getId());
        holder.tv_userpw.setText(arrayList.get(position).getPw());
        holder.tv_username.setText(arrayList.get(position).getName());
        holder.tv_usernumber.setText(String.valueOf(arrayList.get(position).getNumber()));
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class UserViewHoler extends RecyclerView.ViewHolder {
        TextView tv_userid;
        TextView tv_userpw;
        TextView tv_usernumber;
        TextView tv_username;

        public UserViewHoler(@NonNull View itemView) {
            super(itemView);
            this.tv_userid = itemView.findViewById(R.id.tv_userid);
            this.tv_userpw = itemView.findViewById(R.id.tv_userpw);
            this.tv_usernumber = itemView.findViewById(R.id.tv_usernumber);
            this.tv_username = itemView.findViewById(R.id.tv_username);
        }
    }
}
