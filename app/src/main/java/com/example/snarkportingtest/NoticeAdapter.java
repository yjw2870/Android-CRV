package com.example.snarkportingtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHoler> {

    private ArrayList<Notice> arrayList;
    private Context context;

    private OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener{
        void onItemClick(int position);
    }

    public NoticeAdapter(ArrayList<Notice> arrayList, Context context, OnItemClickListener onItemClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public NoticeViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_vote, parent, false);
        NoticeViewHoler holder = new NoticeViewHoler(view, mOnItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHoler holder, int position) {
        holder.tv_notititle.setText(arrayList.get(position).getNoti_title());
        holder.tv_notibody.setText(arrayList.get(position).getNoti_body());
        holder.tv_data.setText(arrayList.get(position).getData());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class NoticeViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_notititle;
        TextView tv_notibody;
        TextView tv_data;

        OnItemClickListener onItemClickListener;

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
        public NoticeViewHoler(@NonNull View itemView, OnItemClickListener mOnItemClickListener) {
            super(itemView);
            this.tv_notititle = itemView.findViewById(R.id.tv_notititle);
            this.tv_notibody = itemView.findViewById(R.id.tv_notibody);
            this.tv_data = itemView.findViewById(R.id.tv_data);

            TextSizeSet();

            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
        }

        private void TextSizeSet() {
            tv_notititle.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/15));
            tv_notibody.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/22));
            tv_data.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/22));
        }

    }
}
