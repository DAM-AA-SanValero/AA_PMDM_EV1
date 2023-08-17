package com.svalero.cybershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Insert;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.svalero.cybershopapp.adapters.ClientAdapter;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViewClientActivity extends AppCompatActivity  {

    public List<Client> clientList;
    public ClientAdapter clientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);

        clientList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.clientList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); //Pauta para ajustar el recycler view
        recyclerView.setLayoutManager(layoutManager);
        clientAdapter = new ClientAdapter(clientList);
        recyclerView.setAdapter(clientAdapter);

        /*List<Client> clientList = new ArrayList<>();
        clientAdapter = new ClientAdapter(clientList);*/
    }


    @Override
    protected void onResume() {
        super.onResume();

        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "client")
                .allowMainThreadQueries().build();
        clientList.clear();
        clientList.addAll(database.clientDao().getAll());
        clientAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.registerClient) {
            Intent intent = new Intent(this, RegisterClientActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }



}