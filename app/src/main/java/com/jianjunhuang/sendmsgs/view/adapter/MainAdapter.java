package com.jianjunhuang.sendmsgs.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jianjunhuang.sendmsgs.model.bean.ContactInfo;
import com.jianjunhuang.sendmsgs.R;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ContactInfo> mList;
    private Context mContext;

    public static final int CONTACT_TYPE = 0;
    public static final int ADD_TYPE = 1;

    public MainAdapter(Context mContext, List<ContactInfo> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        if (viewType == CONTACT_TYPE) {
            return new ContactViewHolder(layoutInflater.inflate(R.layout.contact_item_layout, null));
        } else {
            return new AddItemViewHolder(layoutInflater.inflate(R.layout.contact_add_item_layout, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == CONTACT_TYPE) {
            final ContactInfo info = mList.get(position);
            ContactViewHolder viewHolder = (ContactViewHolder) holder;
            viewHolder.nameTv.setText(info.getName());
            viewHolder.phoneTv.setText(info.getPhone());
            viewHolder.delImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    Toast.makeText(mContext, "del", Toast.LENGTH_SHORT).show();
                    mListener.onDelContact(info, position);
                }
            });
            viewHolder.nameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO
                    info.setSelected(!info.isSelected());
                    mListener.onSelectedContact(info, position);
                    notifyDataSetChanged();
                }
            });
            if (info.isSelected()) {
                viewHolder.nameTv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            } else {
                viewHolder.nameTv.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorGray));
            }
        } else {
            AddItemViewHolder viewHolder = (AddItemViewHolder) holder;
            if (position == mList.size() + 1) {
                viewHolder.typeTv.setText("从通讯中添加");
            } else {
                viewHolder.typeTv.setText("手动添加");
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position == mList.size() + 1) {
                        Toast.makeText(mContext, "add from contacts", Toast.LENGTH_SHORT).show();
                        mListener.onAddContactInSystem();
                    } else {
                        Toast.makeText(mContext, "add your self", Toast.LENGTH_SHORT).show();
                        mListener.onAddContactByHand();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position > mList.size() - 1 ? ADD_TYPE : CONTACT_TYPE;
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTv;
        public TextView phoneTv;
        public ImageView delImg;

        public ContactViewHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.contact_item_name_tv);
            phoneTv = itemView.findViewById(R.id.contact_item_phone_tv);
            delImg = itemView.findViewById(R.id.contact_item_del_img);
        }
    }

    class AddItemViewHolder extends RecyclerView.ViewHolder {

        public TextView typeTv;

        public AddItemViewHolder(View itemView) {
            super(itemView);
            typeTv = itemView.findViewById(R.id.add_item_type_tv);
        }
    }

    public void setAdapterListener(AdapterListener adapterListener) {
        this.mListener = adapterListener;
    }

    private AdapterListener mListener;

    public interface AdapterListener {
        void onDelContact(ContactInfo contactInfo, int position);

        void onSelectedContact(ContactInfo contactInfo, int position);

        void onCancelSelectedContact(ContactInfo contactInfo, int position);

        void onAddContactByHand();

        void onAddContactInSystem();
    }
}
