package com.jianjunhuang.sendmsgs.model.impl;

import com.jianjunhuang.sendmsgs.contract.MainContract;
import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;
import com.jianjunhuang.sendmsgs.model.db.ContactDatabase;
import com.jianjunhuang.sendmsgs.model.db.ContactsDao;

import java.util.List;

public class ContactModel implements MainContract.Model<ContactInfo> {

    private MainContract.Callback<ContactInfo> mCallback;
    private ContactsDao dao;

    public ContactModel() {
        dao = ContactDatabase.getDatabase().contactsDao();
    }

    @Override
    public void sendMsg(List<ContactInfo> list) {

    }

    @Override
    public void getContact() {
        List<ContactInfo> contactInfos = dao.getContacts();
        if (contactInfos != null) {
            mCallback.onGetContactSuccess(contactInfos);
        }
    }

    @Override
    public void addContact(ContactInfo contactInfo) {
        dao.insert(contactInfo);
        mCallback.onInsertContactSuccess(contactInfo);
    }

    @Override
    public void delContact(ContactInfo contactInfo) {
        dao.delete(contactInfo);
        mCallback.onDelContactSuccess();
    }

    @Override
    public void setCallback(MainContract.Callback callback) {
        this.mCallback = callback;
    }

}
