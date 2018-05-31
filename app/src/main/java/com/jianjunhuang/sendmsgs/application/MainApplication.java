package com.jianjunhuang.sendmsgs.application;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.jianjunhuang.sendmsgs.model.db.ContactDatabase;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContactDatabase.init(this);
    }

}
