package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.svalero.cybershopapp.adapters.ClientAdapter;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.util.List;

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
        fillData(client);

    }

    private void fillData(Client client) {
        TextView tvName = findViewById(R.id.clientName);
        TextView tvSurname = findViewById(R.id.clientSurname);
        TextView tvNumber = findViewById(R.id.clientNumber);
        tvName.setText(client.getName());
        tvSurname.setText(client.getSurname());
        tvNumber.setText(String.valueOf(client.getNumber()));
    }

    private void deleteClient(){
        if(clientPosition != -1){
            clientAdapter.deleteClient(clientPosition);
            finish();
        }
    }
}