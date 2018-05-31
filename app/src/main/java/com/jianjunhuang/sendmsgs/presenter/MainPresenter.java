package com.jianjunhuang.sendmsgs.presenter;

import com.jianjunhuang.sendmsgs.contract.MainContract;
import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;
import com.jianjunhuang.sendmsgs.model.impl.ContactModel;

import java.util.List;

public class MainPresenter implements MainContract.Presenter<ContactInfo> {

    private MainContract.View<ContactInfo> mView;
    private MainContract.Model<ContactInfo> mModel;

    public MainPresenter(final MainContract.View<ContactInfo> mView) {
        this.mView = mView;
        mModel = new ContactModel();
        mModel.setCallback(new MainContract.Callback<ContactInfo>() {
            @Override
            public void onSendSuccess() {
                mView.onSendSuccess();
            }

            @Override
            public void onSendFailed() {
                mView.onSendFailed();
            }

            @Override
            public void onGetContactSuccess(List t) {
                mView.onGetContactSuccess(t);
            }

            @Override
            public void onGetContactFailed() {
                mView.onGetContactFailed();
            }

            @Override
            public void onInsertContactSuccess(ContactInfo contactInfo) {
                mView.onInsertContactSuccess(contactInfo);
            }

            @Override
            public void onInsertContactFailed() {
                mView.onInsertContactFailed();
            }

            @Override
            public void onDelContactSuccess() {
                mView.onDelContactSuccess();
            }

            @Override
            public void onDelContactFailed() {
                mView.onDelContactFailed();
            }
        });
    }

    @Override
    public void sendMsg(List<ContactInfo> t) {

    }

    @Override
    public void getContact() {
        mModel.getContact();
    }

    @Override
    public void addContact(ContactInfo contactInfo) {
        if (contactInfo == null) {
            mView.onInsertContactFailed();
            return;
        }
        if (contactInfo.getPhone() == null || "".equals(contactInfo.getPhone())) {
            mView.onInsertContactFailed();
            return;
        }
        mModel.addContact(contactInfo);
    }

    @Override
    public void delContact(ContactInfo contactInfo) {
        if (contactInfo == null) {
            mView.onDelContactFailed();
            return;
        }
        if (contactInfo.getPhone() == null || "".equals(contactInfo.getPhone())) {
            mView.onDelContactFailed();
            return;
        }
        mModel.delContact(contactInfo);
    }
}
