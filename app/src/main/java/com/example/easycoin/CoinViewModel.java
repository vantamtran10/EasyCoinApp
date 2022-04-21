package com.example.easycoin;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CoinViewModel extends AndroidViewModel {
    private LiveData<List<Coin>> coins;

    public CoinViewModel (Application application) {
        super(application);
        coins = CoinDatabase.getDatabase(getApplication()).coinDAO().getAll();
    }

    public LiveData<List<Coin>> getAllCoins() {
        return coins;
    }
}
