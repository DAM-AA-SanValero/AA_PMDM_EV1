package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.database.Constants.DATABASE_REPAIRS;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.svalero.cybershopapp.adapters.ProductAdapter;
import com.svalero.cybershopapp.adapters.RepairAdapter;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Repair;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ViewRepairActivity extends AppCompatActivity  {

    public List<Repair> repairList;
    public RepairAdapter repairAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_repair);

        repairList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.repairList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        repairAdapter = new RepairAdapter(repairList, this);
        recyclerView.setAdapter(repairAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

        final AppDatabase database = Room.databaseBuilder(this,
                        AppDatabase.class, DATABASE_REPAIRS).allowMainThreadQueries().build();
        repairList.clear();
        repairList.addAll(database.repairDao().getAll());
        repairAdapter.notifyDataSetChanged();
    }

    //ACTION BAR

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_addrepair_preferences, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.getPreferences){
            showLanguageSelectionDialog();
            return true;
        } else if (id == R.id.registerRepair){
            Intent intent = new Intent(this, RegisterRepairActivity.class);
            startActivity(intent);
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