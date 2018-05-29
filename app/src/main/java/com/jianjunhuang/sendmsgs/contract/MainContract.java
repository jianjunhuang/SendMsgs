package com.jianjunhuang.sendmsgs.contract;

import java.util.List;

public interface MainContract {

    interface View<T> {
        void onSendSuccess();

        void onSendFailed();

        void onGetContactSuccess(T t);

        void onGetContactFailed();
    }

    interface Model<T> {
        void sendMsg(List<T> t);

        void getContact();

        void addContact(T t);
    }

    interface Callback<T> {
        void onSendSuccess();

        void onSendFailed();

        void onGetContactSuccess(T t);

        void onGetContactFailed();
    }

    interface Presenter<T> {
        void sendMsg(List<T> t);

        void getContact();

        void addContact(T t);
    }

}
