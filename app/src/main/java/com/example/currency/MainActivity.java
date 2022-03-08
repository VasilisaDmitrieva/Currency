package com.example.currency;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements CurrencyList.ChangeCurrency, View.OnClickListener {
    private Currency currency = null;
    private EditText value;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        value = findViewById(R.id.rubble_value);
        textView = findViewById(R.id.label);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    public void changeCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public void onClick(View view) {
        if (currency == null) {
            textView.setText("Choose currency");
            return;
        }
        if (value.getText().toString().isEmpty()) {
            textView.setText("Fill the field");
            return;
        }
        double rubble = Double.parseDouble(value.getText().toString());
        textView.setText(Double.toString(rubble / currency.getValue() * currency.getNominal()));
    }
}