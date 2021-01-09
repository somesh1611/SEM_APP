package com.example.sem_app.ui.Tournaments;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sem_app.R;

import java.util.List;

public class TournamentRecyclerAdapter extends RecyclerView.Adapter<TournamentRecyclerAdapter.ProgrammingViewholderTournaments> {

    private List<Tournament> data;
    private OnTournamentItemClickedListner mOnTournamentItemClickedListner;
    private Context mContext;

    public TournamentRecyclerAdapter(List<Tournament> d, OnTournamentItemClickedListner onTournamentItemClickedListner, Context context) {
        this.data = d;
        this.mOnTournamentItemClickedListner = onTournamentItemClickedListner;
        this.mContext = context;
    }

    @NonNull
    @Override
    public TournamentRecyclerAdapter.ProgrammingViewholderTournaments onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.tournament_list_item, parent, false);
        return new ProgrammingViewholderTournaments(view, mOnTournamentItemClickedListner);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentRecyclerAdapter.ProgrammingViewholderTournaments holder, int position) {

        Tournament tournament = data.get(position);

        holder.titleText.setText(tournament.getName());
        holder.hostText.setText(tournament.getHost());
        holder.status.setText(tournament.getStatus());

        if(holder.status.getText()=="Over")
        {
            holder.status.setTextColor(Color.RED);
        }else if(holder.status.getText()=="Live")
        {
            holder.status.setTextColor(Color.GREEN);
        }else {
            holder.status.setTextColor(Color.BLUE);
        }

    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    public class ProgrammingViewholderTournaments extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleText,hostText,status;
        OnTournamentItemClickedListner mOnTournamentItemClickedListner;
        public ProgrammingViewholderTournaments(@NonNull View itemView,OnTournamentItemClickedListner onTournamentItemClickedListner) {
            super(itemView);
            titleText = itemView.findViewById(R.id.title);

            hostText = itemView.findViewById(R.id.subtitle);

            status = itemView.findViewById(R.id.status_textview);

            this.mOnTournamentItemClickedListner=onTournamentItemClickedListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mOnTournamentItemClickedListner.OnItemClicked(getAdapterPosition());

        }
    }

    public void setData (List<Tournament> newData){
        data = newData;
        notifyDataSetChanged();
    }

    public interface OnTournamentItemClickedListner {
        void OnItemClicked(int position);
    }
}
