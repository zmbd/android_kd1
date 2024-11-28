package com.example.android_kd_1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner productSpinner;
    private ArrayList<Product> selectedProducts;
    private ArrayAdapter<Product> listAdapter;
    private String phoneNumber = "";
    private ActivityResultLauncher<Intent> phoneActivityLauncher;

    private final List<Product> products = Arrays.asList(
            new Product("Kopūstai", 1.00),
            new Product("Burokai", 1.54),
            new Product("Sūris", 2.00),
            new Product("Kiaušiniai", 1.99),
            new Product("Dantų pasta", 5.00)
    );

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedProducts = new ArrayList<>();

        productSpinner = findViewById(R.id.productSpinner);
        ListView productListView = findViewById(R.id.productListView);
        Button phoneButton = findViewById(R.id.phoneButton);
        Button smsButton = findViewById(R.id.smsButton);
        Button addProductButton = findViewById(R.id.addButton);

        phoneActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != Activity.RESULT_OK) {
                        return;
                    }

                    Intent data = result.getData();
                    if (data != null) {
                        phoneNumber = data.getStringExtra("phone_number");
                    }
                }
        );

        ArrayAdapter<Product> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, products
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(spinnerAdapter);

        listAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, new ArrayList<>()
        );
        productListView.setAdapter(listAdapter);

        addProductButton.setOnClickListener(v -> {
            Product product = (Product) productSpinner.getSelectedItem();

            listAdapter.add(product);
            listAdapter.notifyDataSetChanged();
            selectedProducts.add(product);

            Toast.makeText(this,
                    "Kaina: €" + String.format("%.2f", product.getPrice()),
                    Toast.LENGTH_SHORT
            ).show();
        });

        phoneButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PhoneActivity.class);
            phoneActivityLauncher.launch(intent);
        });

        smsButton.setOnClickListener(v -> {
            if (phoneNumber.isEmpty() && selectedProducts.isEmpty()) {
                Toast.makeText(this, "Pridėkite bent vieną produktą ir įveskite telefono numerį.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Pridėkite telefono numerį.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedProducts.isEmpty()) {
                Toast.makeText(this, "Pridėkite bent vieną produktą", Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder productList = new StringBuilder();
            for (Product product : selectedProducts) {
                productList.append(product).append("\n");
            }

            Intent smsIntent = new Intent(Intent.ACTION_SEND);
            smsIntent.setType("text/plain");
            smsIntent.putExtra(Intent.EXTRA_TEXT,
                    "Užsakymo detalės:\n" + productList);
            smsIntent.putExtra("address", phoneNumber);
            startActivity(smsIntent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            phoneNumber = data.getStringExtra("phone_number");
        }
    }
}