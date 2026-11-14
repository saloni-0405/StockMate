
package com.demo.stockmate;

import android.os.Bundle;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtQuantity, edtPrice, edtProductId;
    Button btnAddProduct, btnLoadProducts;
    Button btnUpdateProduct, btnDeleteProduct;
    TextView txtProducts;

    private static final String API_URL =
            "http://127.0.0.1:5049/api/product";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtPrice = findViewById(R.id.edtPrice);
        edtProductId = findViewById(R.id.edtProductId);

        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnLoadProducts = findViewById(R.id.btnLoadProducts);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);

        txtProducts = findViewById(R.id.txtProducts);

        btnAddProduct.setOnClickListener(v -> addProduct());
        btnLoadProducts.setOnClickListener(v -> loadProducts());
        btnUpdateProduct.setOnClickListener(v -> updateProduct());
        btnDeleteProduct.setOnClickListener(v -> deleteProduct());
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    // POST - ADD PRODUCT
    private void addProduct() {

        String name = edtName.getText().toString().trim();
        String quantityText = edtQuantity.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();

        if (name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Fill all product fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);

            if (quantity < 0 || price < 0) {
                Toast.makeText(this, "Values cannot be negative",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject product = new JSONObject();
            product.put("name", name);
            product.put("quantity", quantity);
            product.put("price", price);

            sendRequest("POST", API_URL, product, "Product added successfully!");

        } catch (Exception e) {
            Toast.makeText(this, "Enter valid product details",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // PUT - UPDATE PRODUCT
    private void updateProduct() {

        String idText = edtProductId.getText().toString().trim();
        String name = edtName.getText().toString().trim();
        String quantityText = edtQuantity.getText().toString().trim();
        String priceText = edtPrice.getText().toString().trim();

        if (idText.isEmpty() || name.isEmpty()
                || quantityText.isEmpty() || priceText.isEmpty()) {
            Toast.makeText(this, "Enter ID and all product details",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int id = Integer.parseInt(idText);
            int quantity = Integer.parseInt(quantityText);
            double price = Double.parseDouble(priceText);

            if (id <= 0 || quantity < 0 || price < 0) {
                Toast.makeText(this, "Enter valid positive ID and values",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject product = new JSONObject();
            product.put("name", name);
            product.put("quantity", quantity);
            product.put("price", price);

            sendRequest("PUT", API_URL + "/" + id,
                    product, "Product updated successfully!");

        } catch (Exception e) {
            Toast.makeText(this, "Enter valid product details",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // DELETE - DELETE PRODUCT
    private void deleteProduct() {

        String idText = edtProductId.getText().toString().trim();

        if (idText.isEmpty()) {
            Toast.makeText(this, "Enter Product ID",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            if (id <= 0) {
                Toast.makeText(this, "Enter a valid Product ID",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            sendRequest("DELETE", API_URL + "/" + id,
                    null, "Product deleted successfully!");

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Product ID",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // COMMON HTTP REQUEST HANDLER
    private void sendRequest(String method, String urlString,
                             JSONObject data, String successMessage) {

        setButtonsEnabled(false);
        txtProducts.setText("Processing request...");

        new Thread(() -> {

            HttpURLConnection connection = null;

            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod(method);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                if (data != null) {
                    connection.setRequestProperty(
                            "Content-Type", "application/json");
                    connection.setDoOutput(true);

                    try (OutputStream output = connection.getOutputStream()) {
                        output.write(data.toString().getBytes("UTF-8"));
                    }
                }

                int responseCode = connection.getResponseCode();

                boolean success =
                        (method.equals("POST") && responseCode == 201)
                                || (method.equals("PUT") && responseCode == 200)
                                || (method.equals("DELETE")
                                && (responseCode == 200 || responseCode == 204));

                if (success) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, successMessage,
                                Toast.LENGTH_SHORT).show();

                        if (method.equals("POST")) {
                            edtName.setText("");
                            edtQuantity.setText("");
                            edtPrice.setText("");
                        }

                        hideKeyboard();
                        loadProducts();
                    });
                } else {
                    runOnUiThread(() ->
                            txtProducts.setText(
                                    "Request failed. HTTP: " + responseCode));
                }

            } catch (Exception e) {
                runOnUiThread(() ->
                        txtProducts.setText("Error: " + e.getMessage()));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                runOnUiThread(() -> setButtonsEnabled(true));
            }

        }).start();
    }

    private void setButtonsEnabled(boolean enabled) {
        btnAddProduct.setEnabled(enabled);
        btnLoadProducts.setEnabled(enabled);
        btnUpdateProduct.setEnabled(enabled);
        btnDeleteProduct.setEnabled(enabled);
    }

    // GET - LOAD PRODUCTS
    private void loadProducts() {

        btnLoadProducts.setEnabled(false);
        txtProducts.setText("Loading products...");

        new Thread(() -> {

            HttpURLConnection connection = null;

            try {
                URL url = new URL(API_URL);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                int responseCode = connection.getResponseCode();

                if (responseCode == 200) {

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );

                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    reader.close();

                    JSONArray products = new JSONArray(response.toString());
                    StringBuilder result = new StringBuilder();

                    for (int i = 0; i < products.length(); i++) {

                        JSONObject product = products.getJSONObject(i);

                        result.append("Product ID: ")
                                .append(product.getInt("id"))
                                .append("\n");

                        result.append("Name: ")
                                .append(product.getString("name"))
                                .append("\n");

                        result.append("Quantity: ")
                                .append(product.getInt("quantity"))
                                .append("\n");

                        result.append("Price: ₹")
                                .append(String.format("%.2f",
                                        product.getDouble("price")))
                                .append("\n\n");
                    }

                    String finalResult = result.length() == 0
                            ? "No products found."
                            : result.toString();

                    runOnUiThread(() ->
                            txtProducts.setText(finalResult));

                } else {
                    runOnUiThread(() ->
                            txtProducts.setText("GET failed: " + responseCode));
                }

            } catch (Exception e) {
                runOnUiThread(() ->
                        txtProducts.setText("Error: " + e.getMessage()));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                runOnUiThread(() -> btnLoadProducts.setEnabled(true));
            }

        }).start();
    }
}
