package com.jianjunhuang.sendmsgs.model.impl;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.os.Handler;

import com.jianjunhuang.sendmsgs.contract.MainContract;
import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;
import com.jianjunhuang.sendmsgs.model.db.ContactDatabase;
import com.jianjunhuang.sendmsgs.model.db.ContactsDao;

import java.util.List;

public class ContactModel implements MainContract.Model<ContactInfo> {

    private MainContract.Callback<ContactInfo> mCallback;
    private Handler mHandler;

    public ContactModel() {
        mHandler = new Handler();
    }

    @Override
    public void sendMsg(List<ContactInfo> list) {

    }

    @Override
    public void getContact() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ContactsDao dao = ContactDatabase.getDatabase().contactsDao();
                final List<ContactInfo> contactInfos = dao.getContacts();
                if (contactInfos != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onGetContactSuccess(contactInfos);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void addContact(final ContactInfo contactInfo) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ContactsDao dao = ContactDatabase.getDatabase().contactsDao();
                try {
                    dao.insert(contactInfo);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onInsertContactSuccess(contactInfo);
                        }
                    });
                } catch (SQLiteConstraintException e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mCallback.onInsertContactFailed("该电话已添加");
                        }
                    });
                }
            }
        });

    }

    @Override
    public void delContact(final ContactInfo contactInfo) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ContactsDao dao = ContactDatabase.getDatabase().contactsDao();
                dao.delete(contactInfo);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mCallback.onDelContactSuccess();
                    }
                });
            }
        });

    }

    @Override
    public void setCallback(MainContract.Callback callback) {
        this.mCallback = callback;
    }

}
