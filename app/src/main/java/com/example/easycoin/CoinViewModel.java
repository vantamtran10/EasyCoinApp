package com.example.easycoin;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CoinViewModel extends AndroidViewModel {
    private LiveData<List<Coin>> coin;

    public CoinViewModel (Application application) {
        super(application);
        coin = CoinDatabase.getDatabase(getApplication()).coinDAO().getAll();
    }
    public void filterCoin(boolean favourite) {
        if (favourite)
            coin = CoinDatabase.getDatabase(getApplication()).coinDAO().getFavourited(true);
        else
            coin = CoinDatabase.getDatabase(getApplication()).coinDAO().getAll();

    }
    public LiveData<List<Coin>> getAllCoins() {
        return coin;
    }

}
