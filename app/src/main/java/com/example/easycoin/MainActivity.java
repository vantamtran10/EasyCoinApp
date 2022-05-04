package com.example.easycoin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
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
    private boolean filtered = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        coins = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.lstCoin);
        coinAdapter = new CoinAdapter(coins, this);
        recyclerView.setAdapter(coinAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        initialData();
        coinAdapter.setClickListener(this);

        coinViewModel = new ViewModelProvider(this).get(CoinViewModel.class);
        coinViewModel.getAllCoins().observe(this, coinAdapter::setCoin);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Coin> filteredlist = new ArrayList<>();
                for (Coin item : coins) {
                    if (item.name.toLowerCase().contains(s.toLowerCase())) {
                        filteredlist.add(item);
                    }
                }
                if (filteredlist.isEmpty()) {
                    Toast.makeText(MainActivity.this, "No currency found..", Toast.LENGTH_SHORT).show();
                } else {
                    coinAdapter.filterList(filteredlist);
                }
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                filtered = !filtered;
                coinViewModel.filterCoin(filtered);
                coinViewModel.getAllCoins().observe(this, coinAdapter::setCoin);
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingFragment.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initialData() {
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?start=1&limit=50";
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url,null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                        Coin temp = new Coin( 0, name, symbol, price, change24h, false);
                        coins.add(temp);
                        CoinDatabase db = CoinDatabase.getDatabase(getApplicationContext());
                        db.insert(temp);

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
        Coin coin = coins.get(position);
        Log.e("update coins id", coin.symbol + "-"  + position);
        coin.favourite = !coin.favourite;
        CoinDatabase.update(coin.favourite, position+1);

    }

    @Override
    public void onClick2(View view, int position) {
        Log.e("onCLick2", ".....");
        Coin mydata = coins.get(position);
        displaySetup(mydata.symbol);
    }

    public void displaySetup(String s) {
        CoinDatabase.getCoin(s, data -> {
            Bundle args = new Bundle();
            args.putInt("id", data.id);
            args.putString("coinSymbol", data.symbol);

            DisplaySetupDialog setupDialog = new DisplaySetupDialog();
            setupDialog.setArguments(args);
            setupDialog.show(getSupportFragmentManager(), "setupDialog");
        });
    }

    public static class DisplaySetupDialog extends DialogFragment {
        int joke_id;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            joke_id = getArguments().getInt("id");
            final String coinSymbol = getArguments().getString("coinSymbol");
            builder.setTitle(coinSymbol)
                    .setMessage("setup")
                    .setPositiveButton("Punchline",
                            (dialog, id) -> {handleOK();})
                    .setNegativeButton("Cancel",
                            (dialog, id) -> {});
            return builder.create();
        }

        private void handleOK() {
            Toast.makeText(getActivity(), "OK was clicked.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            outState.putString("JJB", "tester");
        }
    }
}