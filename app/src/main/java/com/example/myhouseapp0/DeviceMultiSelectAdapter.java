package com.example.myhouseapp0;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;

import java.util.ArrayList;
import java.util.List;

public class DeviceMultiSelectAdapter extends ArrayAdapter<String> {
    private final List<String> devices;
    final SparseBooleanArray checkedItems;

    public DeviceMultiSelectAdapter(Context context, List<String> devices) {
        super(context, android.R.layout.simple_list_item_multiple_choice, devices);
        this.devices = devices;
        this.checkedItems = new SparseBooleanArray();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        androidx.appcompat.widget.AppCompatCheckedTextView checkBox = view.findViewById(android.R.id.text1);
        checkBox.setChecked(checkedItems.get(position, false));
        return view;
    }

    public void setItemChecked(int position, boolean isChecked) {
        checkedItems.put(position, isChecked);
    }

    public List<String> getSelectedItems() {
        List<String> selected = new ArrayList<>();
        for (int i = 0; i < devices.size(); i++) {
            if (checkedItems.get(i, false)) {
                selected.add(devices.get(i));
            }
        }
        return selected;
    }
}
