package com.example.snarkportingtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminVotelistAdapter extends RecyclerView.Adapter<AdminVotelistAdapter.VotelistViewHolder> {

    private ArrayList<Votedetail> arrayList;
    private Context context;

    private OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener{
        void onItemClick(int position);
    }

    public AdminVotelistAdapter(ArrayList<Votedetail> arrayList, Context context, OnItemClickListener onItemClickListener) {
        this.arrayList = arrayList;
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public VotelistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_vote, parent, false);
        VotelistViewHolder holder = new VotelistViewHolder(view, mOnItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VotelistViewHolder holder, int position) {
        holder.tv_votetitle.setText(arrayList.get(position).getTitle());
        holder.tv_votestart.setText("시작 : "+arrayList.get(position).getStart());
        holder.tv_voteend.setText("종료 : "+arrayList.get(position).getEnd());
        holder.tv_votetype.setText(arrayList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class VotelistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_votetitle;
        TextView tv_votestart;
        TextView tv_voteend;
        TextView tv_votetype;

        private AdminVotelistAdapter mAdapter;

        OnItemClickListener onItemClickListener;

        public VotelistViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);


            this.tv_votetitle = itemView.findViewById(R.id.tv_votetitle);
            this.tv_votestart = itemView.findViewById(R.id.tv_votestart);
            this.tv_voteend = itemView.findViewById(R.id.tv_voteend);
            this.tv_votetype = itemView.findViewById(R.id.tv_votetype);

            TextSizeSet();

            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }

        private void TextSizeSet() {
            tv_votetitle.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/15));
            tv_votestart.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/22));
            tv_voteend.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/22));
            tv_votetype.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/30));
        }
    }

}
