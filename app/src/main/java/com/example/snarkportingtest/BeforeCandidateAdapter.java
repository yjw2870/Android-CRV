package com.example.snarkportingtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BeforeCandidateAdapter extends RecyclerView.Adapter<BeforeCandidateAdapter.CandidateViewHolder> {

    private ArrayList<Candidate> candidates;
    private Context context;

    public BeforeCandidateAdapter(ArrayList<Candidate> candidates, Context context) {
        this.candidates = candidates;
        this.context = context;
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_candidate, parent, false);
        CandidateViewHolder holder = new CandidateViewHolder(view);

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

        public CandidateViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_candidateprofile = itemView.findViewById(R.id.iv_candidateprofile);
            this.tv_candidatename = itemView.findViewById(R.id.tv_candidatename);
            this.tv_candidategroup = itemView.findViewById(R.id.tv_candidategroup);
            this.lo_candidate = itemView.findViewById(R.id.lo_candidate);

            TextSizeSet();
        }

        private void TextSizeSet() {
            tv_candidatename.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/12));
            tv_candidategroup.setTextSize((float) (((MainActivity)MainActivity.context_main).standardSize_X/20));
        }
    }
}
