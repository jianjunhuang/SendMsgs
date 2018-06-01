package com.jianjunhuang.sendmsgs.model.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;

@Database(entities = {ContactInfo.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {

    private static ContactDatabase INSTANCE;

    public abstract ContactsDao contactsDao();

    public static void init(Context context) {
        INSTANCE = Room.databaseBuilder(context, ContactDatabase.class, "contact").build();
    }

    public static ContactDatabase getDatabase() {
        return INSTANCE;
    }

}
