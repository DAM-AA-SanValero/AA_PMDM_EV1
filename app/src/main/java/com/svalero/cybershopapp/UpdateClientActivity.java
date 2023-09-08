package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.R.string.client_registered;
import static com.svalero.cybershopapp.database.Constants.DATABASE_CLIENTS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Repair;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

public class UpdateClientActivity extends AppCompatActivity {

    private Client currentClient;
    private String originalName;

    private static final int SELECT_PICTURE = 100;

    private ImageView imageView;
    private TextView tvName, tvSurname, tvNumber, tvDate;
    private EditText etName, etSurname, etNumber, etDate;
    private CheckBox cbVip;

    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_client);

        imageView = findViewById(R.id.photoContact);
        tvName = findViewById(R.id.etName);
        tvSurname = findViewById(R.id.etSurname);
        tvNumber = findViewById(R.id.etNumber);
        tvDate = findViewById(R.id.tilDate);
        cbVip = findViewById(R.id.cbVip);

        imageView.setOnClickListener(v -> openGallery());
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNumber = findViewById(R.id.etNumber);
        etDate = findViewById(R.id.tilDate);

        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateClientActivity.this, (view, year1, month1, dayOfMonth) -> {
                String date = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                etDate.setText(date);
            }, year, month, day);
            datePickerDialog.show();
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name == null) return;

        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_CLIENTS)
                .allowMainThreadQueries().build();

        Client client = database.clientDao().getByName(name);
        this.currentClient = client;

        fillData(client);
        originalName = client.getName();
    }

    private void fillData(Client client) {
        tvName.setText(client.getName());
        tvSurname.setText(client.getSurname());
        tvNumber.setText(String.valueOf(client.getNumber()));
        tvDate.setText(String.valueOf(client.getRegister_date()));
        cbVip.setChecked(client.isVip());

        if (client.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(client.getImage(), 0, client.getImage().length);
            imageView.setImageBitmap(bitmap);
        }
    }

    public void updateButton(View view){

        String currentName = originalName;
        String newName = etName.getText().toString();
        String newSurname = etSurname.getText().toString();
        int newNumber = Integer.parseInt(etNumber.getText().toString());
        String newDate = etDate.getText().toString();
        boolean status = cbVip.isChecked();

        String sqlDate = convertDateToSqlFormat(newDate);

        Date dbDate;

        if (sqlDate != null) {
           dbDate = Date.valueOf(sqlDate);
        } else {
            dbDate = currentClient.getRegister_date();
        }

        database.clientDao().updateByName(currentName, newName, newSurname, newNumber,
                dbDate, status, currentClient.getImage());
        Toast.makeText(this, R.string.clientUpdated, Toast.LENGTH_LONG).show();

        Client updatedClient = database.clientDao().getByName(currentName);

        if(updatedClient != null) {
        fillData(updatedClient);
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
            if(currentClient != null) {
                currentClient.setImage(imageBytes);
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

    //Conversion de Date a formato SQL
    private String convertDateToSqlFormat(String dateInOriginalFormat) {
        try {
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            java.util.Date date = originalFormat.parse(dateInOriginalFormat);
            return sqlFormat.format(date);
        } catch (ParseException e) {
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

