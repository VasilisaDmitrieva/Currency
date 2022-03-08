package com.example.currency;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurrencyList extends ListFragment implements View.OnClickListener {
    private static class Data {
        @SerializedName("Valute")
        private Map<String, Currency> currencyMap;
    }

    public interface ChangeCurrency {
        public void changeCurrency(Currency currency);
    }

    ChangeCurrency changeCurrency;

    private ArrayList<Currency> data;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final String BUNDLE_KEY = "BUNDLE_KEY";

    private class NetworkThread extends Thread {
        public NetworkThread(@NonNull String name) {
            super(name);
        }

        @Override
        public void run() {
            try {
                URLConnection urlConnection = (new URL("https://www.cbr-xml-daily.ru/daily_json.js")).openConnection();
                urlConnection.connect();
                Gson gson = new Gson();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

                String str;
                do {
                    bufferedReader.mark(1024);
                    str = bufferedReader.readLine();
                    System.out.println(str);
                } while (!str.contains("{"));
                bufferedReader.reset();
                Data object = gson.fromJson(bufferedReader, Data.class);
                data = new ArrayList<>(object.currencyMap.values());

                handler.post(setData);
            } catch (IOException e) {
                System.out.println("Error occurred. Cannot load currency information.");
            }
        }
    }

    public void loadData() {
        new NetworkThread("NetworkThread").start();
    }

    private final Runnable setData = new Runnable() {
        @Override
        public void run() {
            ArrayAdapter<Currency> adapter = new ArrayAdapter<>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, data);

            setListAdapter(adapter);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (data == null) {
            if (savedInstanceState != null) {
                super.onViewStateRestored(savedInstanceState);
                data = savedInstanceState.getParcelableArrayList(BUNDLE_KEY);
                setData.run();
            } else {
                loadData();
            }
        }
        changeCurrency = (ChangeCurrency) getActivity();
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().findViewById(R.id.refresh_button).setOnClickListener(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(BUNDLE_KEY, data);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        changeCurrency.changeCurrency(data.get(position));
    }

    @Override
    public void onClick(View view) {
        loadData();
    }
}
