package com.jianjunhuang.sendmsgs.contract;

import java.util.List;

public interface MainContract {

    interface View<T> {
        void onSendSuccess();

        void onSendFailed();

        void onGetContactSuccess(List<T> t);

        void onGetContactFailed();

        void onInsertContactSuccess(T t);

        void onInsertContactFailed();

        void onDelContactSuccess();

        void onDelContactFailed();
    }

    interface Model<T> {
        void sendMsg(List<T> t);

        void getContact();

        void addContact(T t);

        void delContact(T t);

        void setCallback(Callback callback);
    }

    interface Callback<T> {
        void onSendSuccess();

        void onSendFailed();

        void onGetContactSuccess(List<T> t);

        void onGetContactFailed();

        void onInsertContactSuccess(T t);

        void onInsertContactFailed();

        void onDelContactSuccess();

        void onDelContactFailed();
    }

    interface Presenter<T> {
        void sendMsg(List<T> t);

        void getContact();

        void addContact(T t);

        void delContact(T t);
    }

}
