package com.jianjunhuang.sendmsgs.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;
import com.jianjunhuang.sendmsgs.contract.MainContract;
import com.jianjunhuang.sendmsgs.presenter.MainPresenter;
import com.jianjunhuang.sendmsgs.receiver.SmsReceiver;
import com.jianjunhuang.sendmsgs.view.adapter.MainAdapter;
import com.jianjunhuang.sendmsgs.R;
import com.jianjunhuang.sendmsgs.view.dialog.InsertContactDialog;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View<ContactInfo>,
        MainAdapter.AdapterListener, View.OnClickListener, InsertContactDialog.OnInsertListener {

    private SmsManager smsManager;

    private PendingIntent pendingIntent;

    private TextInputEditText smsInputEdt;
    private FloatingActionButton sendFab;
    private RecyclerView contactRv;
    private List<ContactInfo> contactInfos = new ArrayList<>();
    private MainAdapter mainAdapter;
    private MainPresenter mPresenter;

    private static final int SELECT_PHONE = 200;
    private static final int REQUEST_PERMISSION = 201;

    private InsertContactDialog insertContactDialog;
    private LinkedList<ContactInfo> sendQue = new LinkedList<>();

    private Snackbar snackbar;
    private BroadcastReceiver mReceiver;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this);
        smsManager = SmsManager.getDefault();

        smsInputEdt = findViewById(R.id.main_content_edt);
        sendFab = findViewById(R.id.main_send_contact_fab);
        sendFab.setOnClickListener(this);

        initRecyclerView();
        insertContactDialog = new InsertContactDialog(this);
        insertContactDialog.setOnInsertListener(this);

        snackbar = Snackbar.make(contactRv, "", Snackbar.LENGTH_SHORT);
    }

    private void initRecyclerView() {
        contactRv = findViewById(R.id.main_contact_list_rv);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        contactRv.setLayoutManager(manager);
        mainAdapter = new MainAdapter(this, contactInfos);
        mainAdapter.setAdapterListener(this);
        contactRv.setAdapter(mainAdapter);
    }

    @Override
    public void onSendSuccess() {

    }

    @Override
    public void onSendFailed() {

    }

    @Override
    public void onGetContactSuccess(List<ContactInfo> t) {
        this.contactInfos = t;
        mainAdapter.setOnDataChange(contactInfos);
    }


    @Override
    public void onGetContactFailed() {
        Toast.makeText(this, "get failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInsertContactSuccess(ContactInfo contactInfo) {
        contactInfos.add(contactInfo);
        mainAdapter.setOnDataChange(contactInfos);
        snackbar.setText("insert success");
        snackbar.show();
    }

    @Override
    public void onInsertContactFailed(String reason) {
        snackbar.setText("insert failed :"+reason);
        snackbar.show();
    }

    @Override
    public void onDelContactSuccess() {
        snackbar.setText("delete success");
        snackbar.show();
    }

    @Override
    public void onDelContactFailed() {
        snackbar.setText("delete failed");
        snackbar.show();
    }

    @Override
    public void onDelContact(ContactInfo contactInfo, int position) {
        mPresenter.delContact(contactInfo);
        contactInfos.remove(position);
        sendQue.remove(contactInfo);
        mainAdapter.setOnDataChange(contactInfos);
    }

    @Override
    public void onSelectedContact(ContactInfo contactInfo, int position) {
        sendQue.add(contactInfo);
    }

    @Override
    public void onCancelSelectedContact(ContactInfo contactInfo, int position) {
        sendQue.remove(contactInfo);
    }

    @Override
    public void onAddContactByHand() {
        insertContactDialog.show();
    }

    @Override
    public void onAddContactInSystem() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, SELECT_PHONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_PHONE: {
                Uri contactUri = data.getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
                Cursor cursor = getContentResolver().query(contactUri, projection, null
                        , null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int displayName = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setName(cursor.getString(displayName));
                    contactInfo.setPhone(cursor.getString(numberIndex));
                    mPresenter.addContact(contactInfo);
                }
                break;
            }
            case REQUEST_PERMISSION: {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {
                    sendMsg();
                }
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getContact();
        if (mReceiver == null) {
            mIntentFilter = new IntentFilter();
            mIntentFilter.addAction("com.jianjunhuang.sms");
            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String name = intent.getStringExtra("name");
                    String phone = intent.getStringExtra("phone");
                    snackbar.setText(name + " " + phone + " 已发送！");
                    snackbar.show();
                }
            };
        }
        registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_send_contact_fab) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        REQUEST_PERMISSION);
            } else {
                sendMsg();
            }
        }
    }

    private void sendMsg() {
        if (sendQue.size() == 0) {
            snackbar.setText("请点击姓名挑选需要发送的人员");
            snackbar.show();
            return;
        }
        String msg = smsInputEdt.getText().toString();
        if ("".equals(msg)) {
            smsInputEdt.setError("请输入信息内容");
            return;
        }
        for (ContactInfo info : sendQue) {
            if (info.isSelected()) {
                Intent intent = new Intent("com.jianjunhuang.sms");
                intent.putExtra("phone", info.getPhone());
                intent.putExtra("name", info.getName());
                pendingIntent = PendingIntent.getBroadcast(this,
                        0,
                        intent,
                        0);
                smsManager.sendTextMessage(info.getPhone(),
                        null,
                        msg,
                        pendingIntent,
                        null);
            }
        }
        sendQue.clear();
    }

    @Override
    public void onInsert(ContactInfo info) {
        mPresenter.addContact(info);
    }

}
