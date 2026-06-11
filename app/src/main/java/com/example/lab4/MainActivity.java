package com.example.lab4;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editTextName, editTextPrice;
    Button buttonAdd;
    ListView listViewProducts;

    DatabaseReference databaseProducts;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseProducts = FirebaseDatabase.getInstance().getReference("products");

        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewProducts = findViewById(R.id.listViewProducts);

        productList = new ArrayList<>();

        buttonAdd.setOnClickListener(view -> addProduct());

        listViewProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product product = productList.get(position);
            showUpdateDeleteDialog(
                    product.getProductId(),
                    product.getProductName(),
                    product.getProductPrice()
            );
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseProducts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                productList.clear();

                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }

                ProductList adapter = new ProductList(MainActivity.this, productList);
                listViewProducts.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addProduct() {
        String name = editTextName.getText().toString().trim();
        String priceText = editTextPrice.getText().toString().trim();

        if (!name.isEmpty() && !priceText.isEmpty()) {
            double price = Double.parseDouble(priceText);
            String id = databaseProducts.push().getKey();

            Product product = new Product(id, name, price);
            databaseProducts.child(id).setValue(product);

            editTextName.setText("");
            editTextPrice.setText("");

            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter name and price", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUpdateDeleteDialog(String productId, String productName, double productPrice) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(productName);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 10);

        EditText editTextUpdatedName = new EditText(this);
        editTextUpdatedName.setText(productName);
        layout.addView(editTextUpdatedName);

        EditText editTextUpdatedPrice = new EditText(this);
        editTextUpdatedPrice.setText(String.valueOf(productPrice));
        editTextUpdatedPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(editTextUpdatedPrice);

        Button buttonUpdate = new Button(this);
        buttonUpdate.setText("UPDATE");
        layout.addView(buttonUpdate);

        Button buttonDelete = new Button(this);
        buttonDelete.setText("DELETE");
        layout.addView(buttonDelete);

        dialogBuilder.setView(layout);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonUpdate.setOnClickListener(v -> {
            String name = editTextUpdatedName.getText().toString().trim();
            String priceText = editTextUpdatedPrice.getText().toString().trim();

            if (!name.isEmpty() && !priceText.isEmpty()) {
                double price = Double.parseDouble(priceText);
                updateProduct(productId, name, price);
                alertDialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter name and price", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            deleteProduct(productId);
            alertDialog.dismiss();
        });
    }

    private void updateProduct(String id, String name, double price) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("products").child(id);
        Product product = new Product(id, name, price);
        dR.setValue(product);

        Toast.makeText(this, "Product updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteProduct(String id) {
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("products").child(id);
        dR.removeValue();

        Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
    }
}