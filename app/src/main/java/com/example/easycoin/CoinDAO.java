package com.example.easycoin;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CoinDAO {
    @Query("SELECT * FROM coin WHERE favourite = :favourite " +
            "ORDER BY rowid")
    LiveData<List<Coin>> getFavourited(boolean favourite);

    @Query("SELECT * FROM coin ORDER BY rowid")
    LiveData<List<Coin>> getAll();

    @Query("SELECT * FROM coin WHERE symbol = :coinSymbol")
    Coin getBySymbol(String coinSymbol);

    @Insert
    void insert(Coin... coins);

    @Update
    void update(Coin... coins);

    @Query("UPDATE coin SET favourite = :favourite WHERE symbol = :symbolInput")
    void update(String symbolInput, Boolean favourite);

    @Delete
    void delete(Coin... coins);

    @Query("DELETE FROM coin WHERE id = :coinID")
    void delete(int coinID);
}
