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

    @Query("SELECT * FROM coin ORDER BY favouriteCoin COLLATE NOCASE, id")
    LiveData<List<Coin>> getAll();

    @Query("SELECT * FROM coin WHERE id = :coinId")
    Coin getById(int coinId);

    @Insert
    void insert(Coin... coins);

    @Update
    void update(Coin... coins);

    @Delete
    void delete(Coin... coins);

    @Query("DELETE FROM coin WHERE id = :coinID")
    void delete(int jokeId);
}
