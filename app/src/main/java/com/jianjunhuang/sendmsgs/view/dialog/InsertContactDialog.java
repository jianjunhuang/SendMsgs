package com.jianjunhuang.sendmsgs.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.jianjunhuang.sendmsgs.R;
import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;

public class InsertContactDialog extends Dialog implements View.OnClickListener {

    private TextView sureTv;
    private TextView cancelTv;
    private TextInputEditText nameEdt;
    private TextInputEditText phoneEdt;

    public InsertContactDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.insert_contact_dialog_layout, null);
        setContentView(view);

        sureTv = findViewById(R.id.insert_dialog_sure_tv);
        cancelTv = findViewById(R.id.insert_dialog_cancel_tv);
        nameEdt = findViewById(R.id.insert_dialog_name_edt);
        phoneEdt = findViewById(R.id.insert_dialog_phone_edt);

        sureTv.setOnClickListener(this);
        cancelTv.setOnClickListener(this);

        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        params.width = (int) (metrics.widthPixels * 0.8);
        window.setAttributes(params);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert_dialog_sure_tv: {
                if (insertListener != null) {
                    String name = nameEdt.getText().toString();
                    String phone = phoneEdt.getText().toString();
                    if ("".equals(name)) {
                        nameEdt.setError("请输入名字");
                        return;
                    }
                    if ("".equals(phone)) {
                        phoneEdt.setError("请输入电话");
                        return;
                    }
                    ContactInfo contactInfo = new ContactInfo();
                    contactInfo.setPhone(phone);
                    contactInfo.setName(name);
                    insertListener.onInsert(contactInfo);
                    dismiss();
                }
                break;
            }
            case R.id.insert_dialog_cancel_tv: {
                dismiss();
                break;
            }
        }
    }

    private OnInsertListener insertListener;

    public void setOnInsertListener(OnInsertListener onInsertListener) {
        this.insertListener = onInsertListener;
    }

    public interface OnInsertListener {
        void onInsert(ContactInfo info);
    }
}
