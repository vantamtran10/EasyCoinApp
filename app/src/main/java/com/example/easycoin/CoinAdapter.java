package com.example.easycoin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private static DecimalFormat df3 = new DecimalFormat("#.###");
    private ArrayList<CoinModel> coinModel;
    private Context context;
    private List<Coin> coinsList;

    public void setCoin(List<Coin> coins) {
        this.coinsList = coins;
        notifyDataSetChanged();
    }


    public interface ItemClickListener {
        void onClick(View view, int position);

        void onClick2(View view, int adapterPosition);
    }

    private ItemClickListener clickListener;
    public CoinAdapter(List<Coin> coinModel, Context context) {
        this.coinsList = coinModel;
        this.context = context;
    }


    public void filterList(ArrayList<Coin> filterllist) {
        coinsList = filterllist;
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
        Coin c = coinsList.get(position);
        holder.current = c;
        holder.coinName.setText(c.name);
        holder.coinPrice.setText("$ " + df3.format(c.price));
        holder.coinSymbol.setText(c.symbol);
        holder.coinChange24h.setText(df3.format(c.change24h) + " %");
        if (c.change24h > 0){
            holder.coinChange24h.setTextColor(Color.GREEN);
        }
        else
        {
            holder.coinChange24h.setTextColor(Color.RED);
            holder.priceTrend.setImageResource(R.drawable.down);
        }
        int id = context.getResources().getIdentifier("drawable/"+ c.symbol.toLowerCase(), null, context.getPackageName());
        holder.coinImg.setImageResource(id);

        if (c.favourite){
            holder.imgFavourite.setImageResource(R.drawable.ic_thumb_up);
        }else {
            holder.imgFavourite.setImageResource(R.drawable.ic_thumb_down);
        }


    }

    @Override
    public int getItemCount() {
        if (coinsList != null)
            return coinsList.size();
        else return 0;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }



    public class CoinViewHolder extends RecyclerView.ViewHolder{
        public Coin current;
        private TextView coinName, coinSymbol, coinPrice, coinChange24h;
        private ImageView coinImg, priceTrend, imgFavourite;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            coinName = itemView.findViewById(R.id.coinName);
            coinSymbol = itemView.findViewById(R.id.coinSymbol);
            coinPrice = itemView.findViewById(R.id.coinPrice);
            coinChange24h = itemView.findViewById(R.id.change24h);
            coinImg = itemView.findViewById(R.id.imgCrypto);
            priceTrend = itemView.findViewById(R.id.priceTrend);
            imgFavourite = itemView.findViewById(R.id.imgFavourite);
            imgFavourite.setOnClickListener(view -> {
//                Log.e("Test", current.symbol + "- position: " + getAdapterPosition());
//                current.favourite = !current.favourite;
//                CoinDatabase.update(current);
                if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
            });
            itemView.setOnClickListener(view -> {
                if (clickListener != null) clickListener.onClick2(view, getAdapterPosition());
            });
        }


//        @Override
//        public void onClick(View view, int position) {
//            Coin current = coins.get(position);
//            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
//            Log.e("Test", coinSymbol.getText().toString() + "- position: " + getAdapterPosition());
//        }
    }
}
