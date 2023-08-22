package com.svalero.cybershopapp.util;
import androidx.room.TypeConverter;

import java.sql.Date;

public class LocalDateConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}