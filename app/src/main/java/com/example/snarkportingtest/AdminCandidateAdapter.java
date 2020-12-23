package com.example.snarkportingtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdminCandidateAdapter extends RecyclerView.Adapter<AdminCandidateAdapter.CandidateViewHolder> {

    private ArrayList<Candidate> candidates;
    private Context context;

    private int indexcolor = -1;

    private OnItemClickListener mOnItemClickListener;

    interface OnItemClickListener{
        void onItemClick(int position);
    }

    public AdminCandidateAdapter(ArrayList<Candidate> candidates, Context context, OnItemClickListener onItemClickListener) {
        this.candidates = candidates;
        this.context = context;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_candidate, parent, false);
        CandidateViewHolder holder = new CandidateViewHolder(view, mOnItemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        Glide.with(holder.itemView).load(candidates.get(position).getProfile()).into(holder.iv_candidateprofile);
        holder.tv_candidatename.setText(candidates.get(position).getName());
        holder.tv_candidategroup.setText(candidates.get(position).getGroup());
    }

    @Override
    public int getItemCount() {
        return (candidates != null ? candidates.size() : 0);
    }

    public class CandidateViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_candidateprofile;
        TextView tv_candidatename;
        TextView tv_candidategroup;
        LinearLayout lo_candidate;

        OnItemClickListener onItemClickListener;

        public CandidateViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            this.iv_candidateprofile = itemView.findViewById(R.id.iv_candidateprofile);
            this.tv_candidatename = itemView.findViewById(R.id.tv_candidatename);
            this.tv_candidategroup = itemView.findViewById(R.id.tv_candidategroup);
            this.lo_candidate = itemView.findViewById(R.id.lo_candidate);

            TextSizeSet();

            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onItemClick(position);
                    indexcolor = position;
                    notifyDataSetChanged();
                }
            });
        }

        private void TextSizeSet() {
            tv_candidatename.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
            tv_candidategroup.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        }
    }
}
