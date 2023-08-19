package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.svalero.cybershopapp.adapters.ClientAdapter;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.io.File;
import java.util.Locale;

public class ClientDetailsActivity extends AppCompatActivity {
    private ClientAdapter clientAdapter;
    private int clientPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");


        if (name == null) return;

        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries().build();

        Client client = database.clientDao().getByName(name);

        Toast.makeText(this, client.getImagePath(), Toast.LENGTH_LONG).show();
        fillData(client);

    }

    private void fillData(Client client) {
        TextView tvName = findViewById(R.id.clientName);
        TextView tvSurname = findViewById(R.id.clientSurname);
        TextView tvNumber = findViewById(R.id.clientNumber);
        ImageView imageView = findViewById(R.id.clientPhoto);

        tvName.setText(client.getName());
        tvSurname.setText(client.getSurname());
        tvNumber.setText(String.valueOf(client.getNumber()));

        if (client.getImagePath() != null && !client.getImagePath().isEmpty()) {
            File imgFile = new File(client.getImagePath());
            if (imgFile.exists()) {
                Picasso.get()
                        .load(imgFile)
                        .into(imageView);
            }
        } else {
            imageView.setImageResource(R.drawable.person);
        }

    }

    private void deleteClient(){
        if(clientPosition != -1){
            clientAdapter.deleteClient(clientPosition);
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actonbar_mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (item.getItemId() == R.id.getMap) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.getPreferences){
            showLanguageSelectionDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLanguageSelectionDialog() {
        String[] languages = {"Español", "English"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select language");
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