package com.svalero.cybershopapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteConstraintException;
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
    private EditText etNumber;
    private TextInputEditText tilDate;

    private CheckBox cbVIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNumber = findViewById(R.id.etNumber);
        tilDate = findViewById(R.id.tilDate);
        cbVIP = findViewById(R.id.cbVip);
    }

    public void addButton(View view) {

        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String number = etNumber.getText().toString();
        String date = tilDate.getText().toString();
        boolean vip = cbVIP.isActivated();

        if (name.isEmpty() || surname.isEmpty() || number.isEmpty()){
            // Mostrar un mensaje de error o notificación al usuario
            Toast.makeText(this, "Por favor, rellena los campos requeridos", Toast.LENGTH_SHORT).show();
            return; // Salir del método sin registrar si algún campo está vacío
        }

        client = new Client(name, surname, Integer.parseInt(number), date, vip);


        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, "clients")
                .allowMainThreadQueries().build();
        try {
            database.clientDao().insert(client);

            Toast.makeText(this, "Cliente añadido", Toast.LENGTH_LONG).show();
            etName.setText("");
            etSurname.setText("");
            etNumber.setText("");
            tilDate.setText("");
            onBackPressed();

        } catch (SQLiteConstraintException sce){
            Toast.makeText(this, "Error al registrar", Toast.LENGTH_LONG).show();
        }

    }

    public void cancelButton(View view){
        onBackPressed();
    }
}