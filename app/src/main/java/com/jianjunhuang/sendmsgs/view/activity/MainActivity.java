package com.jianjunhuang.sendmsgs.view.activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.telephony.SmsManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;
import com.jianjunhuang.sendmsgs.contract.MainContract;
import com.jianjunhuang.sendmsgs.presenter.MainPresenter;
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
    private MainPresenter mPresenter;

    private static final int SELECT_PHONE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this);
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
        Toast.makeText(this, "insert success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInsertContactFailed() {
        Toast.makeText(this, "insert failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelContactSuccess() {
        Toast.makeText(this, "del success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelContactFailed() {
        Toast.makeText(this, "del failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelContact(ContactInfo contactInfo, int position) {
        mPresenter.delContact(contactInfo);
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
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, SELECT_PHONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHONE) {
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
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getContact();
    }
}
