package com.example.easycoin;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Coin.class}, version = 1, exportSchema = false)
public abstract class CoinDatabase extends RoomDatabase {



    public interface CoinListener {
        void onCoinReturned(Coin coin);
    }

    public abstract CoinDAO coinDAO();

    private static CoinDatabase INSTANCE;

    public static CoinDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (CoinDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CoinDatabase.class, "coin_database")
                            .addCallback(createCoinDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Note this call back will be run
    private static RoomDatabase.Callback createCoinDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };

    public static void getCoin(String symbol, CoinListener listener) {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                listener.onCoinReturned((Coin) msg.obj);
            }
        };

        (new Thread(() -> {
            Message msg = handler.obtainMessage();
            msg.obj = INSTANCE.coinDAO().getBySymbol(symbol);
            handler.sendMessage(msg);
        })).start();
    }

    public static void insert(Coin coin) {
        (new Thread(()-> INSTANCE.coinDAO().insert(coin))).start();
    }

    public static void delete(int coinId) {
        (new Thread(() -> INSTANCE.coinDAO().delete(coinId))).start();
    }


    public static void update(Coin coin) {
        Log.e("update...", coin.symbol.toString() );
        (new Thread(() -> INSTANCE.coinDAO().update(coin))).start();
    }

    public static void update(boolean favourite, int id) {

        (new Thread(() -> INSTANCE.coinDAO().update(favourite, id))).start();
    }
}
