package com.svalero.cybershopapp.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.svalero.cybershopapp.dao.ClientDao;
import com.svalero.cybershopapp.dao.ProductDao;
import com.svalero.cybershopapp.dao.RepairDao;
import com.svalero.cybershopapp.domain.Client;
import com.svalero.cybershopapp.domain.Product;
import com.svalero.cybershopapp.domain.Repair;
import com.svalero.cybershopapp.util.LocalDateConverter;

@Database(entities = {Client.class}, version = 1)
@TypeConverters({LocalDateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ClientDao clientDao();



}


