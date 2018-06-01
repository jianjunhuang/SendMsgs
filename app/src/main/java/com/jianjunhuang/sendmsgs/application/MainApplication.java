package com.jianjunhuang.sendmsgs.application;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.jianjunhuang.sendmsgs.model.db.ContactDatabase;
import com.tencent.bugly.Bugly;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContactDatabase.init(this);
        Bugly.init(this,"53b8dd4d78",false);
    }

}
