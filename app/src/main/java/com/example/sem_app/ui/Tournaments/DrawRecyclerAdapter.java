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

public class DrawRecyclerAdapter extends RecyclerView.Adapter<DrawRecyclerAdapter.ProgrammingViewHolderDraws>{

    private List<Draw> data;
    private OnDrawItemClickedListner mOnDrawItemClickedListner;
    private Context mContext;
    Boolean isTeam;
    public DrawRecyclerAdapter(Context context,List<Draw> d,OnDrawItemClickedListner onDrawItemClickedListner,Boolean t){

        mContext = context;
        this.data = d;
        mOnDrawItemClickedListner = onDrawItemClickedListner;
        isTeam = t;
    }
    @NonNull
    @Override
    public DrawRecyclerAdapter.ProgrammingViewHolderDraws onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.draw_list_item, parent, false);
        return new ProgrammingViewHolderDraws(view, mOnDrawItemClickedListner);
    }

    @Override
    public void onBindViewHolder(@NonNull DrawRecyclerAdapter.ProgrammingViewHolderDraws holder, int position) {
        int n=position+1;
        holder.match.setText("Match "+n);

        if(!isTeam)
        {
            holder.player1Label.setText("Player 1:");
            holder.player2Label.setText("Player 2:");

        }else{
            holder.player1Label.setText("Team 1:");
            holder.player2Label.setText("Team 2:");
        }
        Draw draw = data.get(position);
        holder.player1.setText(draw.getPlayer1Name());
        holder.player2.setText(draw.getPlayer2Name());




            holder.score1.setText(draw.getScore1());
            holder.score2.setText(draw.getScore2());

            String w = draw.getWinner();

            if(w!=null) {

                if (w.contentEquals(draw.getPlayer1Name())) {
                    holder.player1.setTextColor(Color.GREEN);
                    holder.score1.setTextColor(Color.GREEN);
                } else {

                    holder.player2.setTextColor(Color.GREEN);
                    holder.score2.setTextColor(Color.GREEN);
                }
            }

    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }



    public static class ProgrammingViewHolderDraws extends RecyclerView.ViewHolder implements View.OnClickListener {
        /*Declare all the textviews, imageviews here*/

        TextView match,player1,player1Label,player2Label,player2,score1,score2;

        OnDrawItemClickedListner mOnDrawItemClickedListner;
        
        public ProgrammingViewHolderDraws(@NonNull View itemView,
                                          OnDrawItemClickedListner onDrawItemClickedListner) {
            super(itemView);
            //Initialize all the textviews,imageviews here...
            match = itemView.findViewById(R.id.match_number);
            player1 = (TextView) itemView.findViewById(R.id.player1);
            player1Label=itemView.findViewById(R.id.player1_label);
            player2Label=itemView.findViewById(R.id.player2_label);
            player2 = (TextView) itemView.findViewById(R.id.player2);
            score1 = (TextView) itemView.findViewById(R.id.score1);
            score2 = (TextView) itemView.findViewById(R.id.score2);

            this.mOnDrawItemClickedListner = onDrawItemClickedListner;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnDrawItemClickedListner.OnItemClicked(getAdapterPosition());
        }
    }

    public void setData (List<Draw> newData){
        data = newData;
        notifyDataSetChanged();
    }

    public interface OnDrawItemClickedListner {
        void OnItemClicked(int position);
    }

}
