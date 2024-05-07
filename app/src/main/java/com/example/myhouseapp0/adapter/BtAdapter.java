package com.example.myhouseapp0.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myhouseapp0.R;

import java.util.List;

public class BtAdapter extends ArrayAdapter<BluetoothListItem> {
    public BtAdapter(@NonNull Context context, int resource, List<BluetoothListItem> btList) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.bt_list_item, null, false);
            viewHolder.tvBtName = convertView.findViewById(R.id.textView);
            viewHolder.chBtSelected = convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.tvBtName.setText("ajfia");
        viewHolder.chBtSelected.setChecked(true);


        return convertView;
    }
    static class ViewHolder{
        TextView tvBtName;
        CheckBox chBtSelected;
    }
}
