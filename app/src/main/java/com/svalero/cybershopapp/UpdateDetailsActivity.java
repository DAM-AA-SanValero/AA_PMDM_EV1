package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

public class UpdateDetailsActivity extends AppCompatActivity {
    private TextView tvName;
    private TextView tvSurname;
    private TextView tvNumber;
    private EditText etName;
    private EditText etSurname;
    private EditText etNumber;
    private AppDatabase database;
    private String originalName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        tvName = findViewById(R.id.etName);
        tvSurname = findViewById(R.id.etSurname);
        tvNumber = findViewById(R.id.etNumber);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNumber = findViewById(R.id.etNumber);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        if (name == null) return;

        database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries().build();

        Client client = database.clientDao().getByName(name);

        fillData(client);
        originalName = client.getName();
    }

    private void fillData(Client client) {
        tvName.setText(client.getName());
        tvSurname.setText(client.getSurname());
        tvNumber.setText(String.valueOf(client.getNumber()));
    }
    public void updateButton(View view){

        String currentName = originalName;
        String newName = etName.getText().toString();
        String newSurname = etSurname.getText().toString();
        int newNumber = 0;
        String numberText = etNumber.getText().toString();
        if(!numberText.isEmpty()){
            newNumber = Integer.parseInt(numberText);
        }

        database.clientDao().updateByName(currentName, newName, newSurname, newNumber);

        Client updatedClient = database.clientDao().getByName(currentName);

        if(updatedClient != null) {
        fillData(updatedClient);
}
        onBackPressed();

    }
    public void cancelButton(View view){onBackPressed();}

}

