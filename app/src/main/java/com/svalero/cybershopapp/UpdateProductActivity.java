package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;
import static com.svalero.cybershopapp.database.Constants.DATABASE_PRODUCTS;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.squareup.picasso.Picasso;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Product;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class UpdateProductActivity extends AppCompatActivity {

    private String originalName;
    private Product currentProduct;

    private static final int SELECT_PICTURE = 100;

    private ImageView imageView;
    private TextView tvName, tvPrice, tvType, tvOrigin;
    private EditText etName, etType, etPrice, etOrigin;
    private CheckBox cbStock;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        imageView = findViewById(R.id.productPhoto);
        tvName = findViewById(R.id.etName);
        tvType = findViewById(R.id.etType);
        tvPrice = findViewById(R.id.etPrice);
        tvOrigin = findViewById(R.id.etOrigin);
        cbStock = findViewById(R.id.cbStock);

        imageView.setOnClickListener(v -> openGallery());
        etName = findViewById(R.id.etName);
        etType = findViewById(R.id.etType);
        etPrice = findViewById(R.id.etPrice);
        etOrigin = findViewById(R.id.etOrigin);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name == null) return;

        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_PRODUCTS)
                .allowMainThreadQueries().build();

        Product product = database.productDao().getByName(name);
        this.currentProduct = product;

        fillData(product);
        originalName = product.getName();
    }

    private void fillData(Product product) {
        tvName.setText(product.getName());
        tvType.setText(product.getType());
        tvPrice.setText(String.valueOf(product.getPrice()));
        tvOrigin.setText(product.getOrigin());
        cbStock.setChecked(product.isInStock());

        if (product.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            imageView.setImageBitmap(bitmap);
        }
    }
    public void updateButton(View view){

        String currentName = originalName;
        String newName = etName.getText().toString();
        String newType = etType.getText().toString();
        double newPrice = Double.parseDouble(etPrice.getText().toString());
        String newOrigin = etOrigin.getText().toString();
        boolean isInStock = cbStock.isChecked();

        database.productDao().updateByName(currentName, newName, newType, newPrice,
                newOrigin, isInStock, currentProduct.getImage());
        Toast.makeText(this, R.string.productUpdated, Toast.LENGTH_LONG).show();

        Product updatedProduct = database.productDao().getByName(currentName);

        if(updatedProduct != null) {
        fillData(updatedProduct);
        }

        onBackPressed();

    }
    public void cancelButton(View view){onBackPressed();}

    //Parte donde selecciono la IMAGEN de la galería
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen de perfil"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri rutaArchivo = data.getData();
            Picasso.get().load(rutaArchivo).into(imageView);
            byte[] imageBytes = uriABitArray(rutaArchivo);
            if(currentProduct != null) {
                currentProduct.setImage(imageBytes);
            }
        }
    }

    private byte[] uriABitArray(Uri uri) {
        try {
            InputStream flujoEntrada = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream bufferByte = new ByteArrayOutputStream();

            int tamanoBuffer = 1024;
            byte[] buffer = new byte[tamanoBuffer];

            int len;
            while ((len = flujoEntrada.read(buffer)) != -1) {
                bufferByte.write(buffer, 0, len);
            }
            return bufferByte.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //ACTION BAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actonbar_preferencesmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.getPreferences){
            showLanguageSelectionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //IDIOMA

    private void showLanguageSelectionDialog() {
        String[] languages = {getString(R.string.Spanish), getString(R.string.English)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selectLanguage);
        builder.setItems(languages, (dialog, which) ->{
            switch (which){
                case 0:
                    setLocale("es");
                    break;
                case 1:
                    setLocale("en");
                    break;
            }
        });
        builder.create().show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources()
                .getDisplayMetrics());
        recreate();
    }
}

