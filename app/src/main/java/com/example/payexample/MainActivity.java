package com.example.payexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPrice;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editName);
        editTextPrice = findViewById(R.id.editPrice);
        button = findViewById(R.id.buttonPay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                Integer price = Integer.parseInt(editTextPrice.getText().toString());

                PayActivity payActivity = new PayActivity(name, price);
                Intent intent = new Intent(getApplicationContext(), payActivity.getClass());
                startActivity(intent);
            }
        });
    }
}