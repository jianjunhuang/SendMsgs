package com.jianjunhuang.sendmsgs.model.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;

import java.util.List;

@Dao
public interface ContactsDao {
    @Query("SELECT * FROM ContactInfo")
    List<ContactInfo> getContacts();

    @Insert
    void insert(ContactInfo contactInfo);

    @Delete
    void delete(ContactInfo contactInfo);

}
