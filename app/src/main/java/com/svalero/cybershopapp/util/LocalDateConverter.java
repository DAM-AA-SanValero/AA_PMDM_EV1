package com.svalero.cybershopapp.util;
import androidx.room.TypeConverter;
import java.time.LocalDate;
public class LocalDateConverter {
    @TypeConverter
    public static LocalDate fromString(String value) {
        return value == null ? null : LocalDate.parse(value);
    }

    @TypeConverter
    public static String toString(LocalDate date) {
        return date == null ? null : date.toString();
    }
}
