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

public class CandidateinfoAdapter extends RecyclerView.Adapter<CandidateinfoAdapter.CandidateViewHolder> {

    private ArrayList<Candidate> candidates;
    private Context context;

    public CandidateinfoAdapter(ArrayList<Candidate> candidates, Context context) {
        this.candidates = candidates;
        this.context = context;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.candidateinfo, parent, false);
        CandidateViewHolder holder = new CandidateViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
        Glide.with(holder.itemView).load(candidates.get(position).getProfile()).into(holder.iv_candidateprofile);
        holder.tv_candidatename.setText(candidates.get(position).getName());
        holder.tv_candidategroup.setText(candidates.get(position).getGroup());
        holder.tv_candidatenote.setText(candidates.get(position).getNote());

    }

    @Override
    public int getItemCount() {
        return (candidates != null ? candidates.size() : 0);
    }

    public class CandidateViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_candidateprofile;
        TextView tv_candidatename;
        TextView tv_candidategroup;
        TextView tv_candidatenote;

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_candidateprofile = itemView.findViewById(R.id.iv_candidateprofile);
            this.tv_candidatename = itemView.findViewById(R.id.tv_candidatename);
            this.tv_candidategroup = itemView.findViewById(R.id.tv_candidategroup);
            this.tv_candidatenote = itemView.findViewById(R.id.tv_candidatenote);

            TextSizeSet();

        }
        private void TextSizeSet() {
            tv_candidatename.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
            tv_candidategroup.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
            tv_candidatenote.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/25));
        }
    }
}
