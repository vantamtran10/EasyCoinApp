package com.example.easycoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private ProgressBar progressBarLoading;
    private CoinAdapter coinAdapter;
    private ArrayList<CoinModel> coinModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextSearch = findViewById(R.id.editTextSearch);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        coinModelArrayList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.lstCoin);
        coinAdapter = new CoinAdapter(coinModelArrayList, this);
        recyclerView.setAdapter(coinAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        initialData();

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // on below line calling a
                // method to filter our array list
                filter(s.toString());
            }

            private void filter(String filter) {
                ArrayList<CoinModel> filteredlist = new ArrayList<>();
                // running a for loop to search the data from our array list.
                for (CoinModel item : coinModelArrayList) {
                    // on below line we are getting the item which are
                    // filtered and adding it to filtered list.
                    if (item.getName().toLowerCase().contains(filter.toLowerCase())) {
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

                        coinModelArrayList.add(new CoinModel(name, symbol, price, change24h));
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
}