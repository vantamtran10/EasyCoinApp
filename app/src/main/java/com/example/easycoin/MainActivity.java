package com.example.easycoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements CoinAdapter.ItemClickListener {

    private EditText editTextSearch;
    private ProgressBar progressBarLoading;
    private CoinAdapter coinAdapter;
    private List<CoinModel> coinModelList;
    private CoinViewModel coinViewModel;
    private List<Coin> coins;
    private CoinModel coinmodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSearch = findViewById(R.id.editTextSearch);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        coins = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.lstCoin);
        coinAdapter = new CoinAdapter(coins, this);
        recyclerView.setAdapter(coinAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initialData();
        coinAdapter.setClickListener(this);

        coinViewModel = new ViewModelProvider(this).get(CoinViewModel.class);
        coinViewModel.getAllCoins().observe(this, coinAdapter::setCoin);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

            private void filter(String filter) {
                ArrayList<Coin> filteredlist = new ArrayList<>();
                for (Coin item : coins) {
                    if (item.name.toLowerCase().contains(filter.toLowerCase())) {
                        filteredlist.add(item);
                    }
                }
                if (filteredlist.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No currency found..", Toast.LENGTH_SHORT).show();
                } else {
                    coinAdapter.filterList(filteredlist);
                }
            }
        });
    }

    private void initialData() {
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=50";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBarLoading.setVisibility(View.GONE);
                try {
                    JSONArray dataArray = response.getJSONArray("data");
                    Log.d("DATA", dataArray.toString());
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject dataObj = dataArray.getJSONObject(i);
                        String symbol = dataObj.getString("symbol");
                        String name = dataObj.getString("name");
                        JSONObject quote = dataObj.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");
                        double price = USD.getDouble("price");
                        double change24h = USD.getDouble("percent_change_24h");
                        Coin temp = new Coin(0, name, symbol, price, change24h, false);
                        coins.add(temp);
                        CoinDatabase db = CoinDatabase.getDatabase(getApplicationContext());
                        db.coinDAO().insert(temp);

                    }
                    coinAdapter.notifyDataSetChanged();
                } catch (JSONException je) {
                    Log.d("JSON-Http-Request", "ERROR: " + je.getMessage());
                    Toast.makeText(MainActivity.this, "Error! Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }, error -> {
            Log.d("JSON-Http-Request", "ERROR: " + error.getMessage());
            Toast.makeText(MainActivity.this, "Error! Please try again later.", Toast.LENGTH_SHORT).show();
        })
        {
        @Override
        public Map<String, String> getHeaders() {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("X-CMC_PRO_API_KEY", "68d86b49-1f00-45b4-8b9d-25553d4ce513");
            return headers;
        }
        };
        queue.add(jsonObjectRequest);
    }


    @Override
    public void onClick(View view, int position) {
        Log.e("coins", coins.toString());
        Coin mydata = coins.get(position);
        CoinDatabase db = CoinDatabase.getDatabase(getApplicationContext());
        mydata.favourite = !mydata.favourite;
        db.coinDAO().update(mydata.symbol, mydata.favourite);

        //Log.i("Insert", mydata.getSymbol() + " - " + mydata.getFavourite());
    }
}