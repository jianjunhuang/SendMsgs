package com.jianjunhuang.sendmsgs.view.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telephony.SmsManager;
import android.widget.EditText;

import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;
import com.jianjunhuang.sendmsgs.contract.MainContract;
import com.jianjunhuang.sendmsgs.view.adapter.MainAdapter;
import com.jianjunhuang.sendmsgs.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainContract.View<ContactInfo>, MainAdapter.AdapterListener {

    private SmsManager smsManager;

    private PendingIntent pendingIntent;

    private EditText smsInputEdt;
    private FloatingActionButton sendFab;
    private RecyclerView contactRv;
    private List<ContactInfo> contactInfos = new ArrayList<>();
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        smsManager = SmsManager.getDefault();
        pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(), 0);
        smsInputEdt = findViewById(R.id.main_content_edt);
        sendFab = findViewById(R.id.main_send_contact_fab);
        initRecyclerView();
    }

    private void initRecyclerView() {
        contactRv = findViewById(R.id.main_contact_list_rv);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        contactRv.setLayoutManager(manager);
        initData();
        contactRv.setAdapter(mainAdapter);
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            ContactInfo info = new ContactInfo();
            info.setName("黄健俊");
            info.setPhone("13247563948");
            contactInfos.add(info);
        }
        if (mainAdapter == null) {
            mainAdapter = new MainAdapter(this, contactInfos);
        }
    }

    @Override
    public void onSendSuccess() {

    }

    @Override
    public void onSendFailed() {

    }

    @Override
    public void onGetContactSuccess(ContactInfo contactInfo) {

    }

    @Override
    public void onGetContactFailed() {

    }

    @Override
    public void onDelContact(ContactInfo contactInfo, int position) {

    }

    @Override
    public void onSelectedContact(ContactInfo contactInfo, int position) {

    }

    @Override
    public void onCancelSelectedContact(ContactInfo contactInfo, int position) {

    }

    @Override
    public void onAddContactByHand() {

    }

    @Override
    public void onAddContactInSystem() {

    }
}
