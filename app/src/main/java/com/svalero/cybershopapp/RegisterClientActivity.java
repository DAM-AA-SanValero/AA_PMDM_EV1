package com.svalero.cybershopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.time.LocalDate;
import java.util.Locale;

public class RegisterClientActivity extends AppCompatActivity {

    private Client client;
    private EditText etName;
    private EditText etSurname;
    private TextInputEditText tilNumber;
    private TextInputEditText tilDate;

    private CheckBox cbVIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        tilNumber = findViewById(R.id.tilNumber);
        tilDate = findViewById(R.id.tilDate);
        cbVIP = findViewById(R.id.cbVip);
    }

    public void addButton(View view) {

        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String number = tilNumber.getText().toString();
        String date = tilDate.getText().toString();
        boolean vip = cbVIP.isActivated();

        client = new Client(name, surname, Integer.parseInt(number), date, vip);

        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "client")
                .allowMainThreadQueries().build();
        database.clientDao().insert(client);

        Toast.makeText(this, "Cliente añadido", Toast.LENGTH_LONG).show();
        etName.setText("");
        etSurname.setText("");
        tilNumber.setText("");
        tilDate.setText("");
    }

    public void cancelButton(View view){
        onBackPressed();
    }
}