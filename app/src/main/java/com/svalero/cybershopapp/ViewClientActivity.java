package com.svalero.cybershopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.svalero.cybershopapp.adapters.ClientAdapter;
import com.svalero.cybershopapp.domain.Client;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ViewClientActivity extends AppCompatActivity  {

    public ArrayList<Client> clientList;
    public ClientAdapter clientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_client);

        populateClientList();

        RecyclerView recyclerView = findViewById(R.id.clientList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this); //Pauta para ajustar el recycler view
        recyclerView.setLayoutManager(layoutManager);


        clientAdapter = new ClientAdapter(clientList);
        recyclerView.setAdapter(clientAdapter);

        List<Client> clientList = new ArrayList<>();
        clientAdapter = new ClientAdapter(clientList);
    }

    private void populateClientList(){
        clientList = new ArrayList<>();
        clientList.add(new Client("Jose","Gimeno",633086205, LocalDate.of(2023,1,23), true));
        clientList.add(new Client("Alejandra","Gimeno",633086205, LocalDate.of(2023,1,23), true));
    }



}