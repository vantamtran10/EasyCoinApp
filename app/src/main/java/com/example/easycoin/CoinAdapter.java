package com.example.easycoin;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private static DecimalFormat df3 = new DecimalFormat("#.###");
    private ArrayList<CoinModel> coinModel;
    private Context context;
    public CoinAdapter(ArrayList<CoinModel> coinModel, Context context) {
        this.coinModel = coinModel;
        this.context = context;
    }

    // below is the method to filter our list.
    public void filterList(ArrayList<CoinModel> filterllist) {
        // adding filtered list to our
        // array list and notifying data set changed
        coinModel = filterllist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new CoinAdapter.CoinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, int position) {
        CoinModel modal = coinModel.get(position);
        holder.coinName.setText(modal.getName());
        holder.coinPrice.setText("$ " + df3.format(modal.getPrice()));
        holder.coinSymbol.setText(modal.getSymbol());
        holder.coinChange24h.setText(df3.format(modal.getChange24h()) + " %");
        if (modal.getChange24h() > 0){
            holder.coinChange24h.setTextColor(Color.GREEN);
        }
        else
        {
            holder.coinChange24h.setTextColor(Color.RED);
        }
        int id = context.getResources().getIdentifier("drawable/"+ modal.getSymbol().toString().toLowerCase(), null, context.getPackageName());
        holder.coinImg.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return coinModel.size();
    }

    public class CoinViewHolder extends RecyclerView.ViewHolder{
        private TextView coinName, coinSymbol, coinPrice, coinChange24h;
        private ImageView coinImg;
        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            coinName = itemView.findViewById(R.id.coinName);
            coinSymbol = itemView.findViewById(R.id.coinSymbol);
            coinPrice = itemView.findViewById(R.id.coinPrice);
            coinChange24h = itemView.findViewById(R.id.change24h);
            coinImg = itemView.findViewById(R.id.imgCrypto);
        }
    }
}
