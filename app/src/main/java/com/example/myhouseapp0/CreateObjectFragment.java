package com.example.myhouseapp0;

//import static androidx.core.app.AppOpsManagerCompat.Api23Impl.getSystemService;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myhouseapp0.adapter.BluetoothListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CreateObjectFragment extends Fragment {

    private ListView listView;
    private BluetoothAdapter adapter;

    private BluetoothAdapter btAdapter;
    private final int ENABLE_REQUEST = 15;


    private void init() {

        btAdapter = BluetoothAdapter.getDefaultAdapter();

    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//    public void onBackPressed() {
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        if (fm.getBackStackEntryCount() > 0)
//            fm.popBackStack();
//        else
//            finish();
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        init();
        return inflater.inflate(R.layout.fragment_create_object, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_back_from_create_object = (Button) view.findViewById(R.id.btn_back_from_create_object);
        btn_back_from_create_object.setOnClickListener(v -> replaceFragment(new HomeFragment()));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch_bluetooth = (Switch) view.findViewById(R.id.switch_for_bluetooth);
        btn_switch_bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableOrDisableBluetooth();
                } else {
                    enableOrDisableBluetooth();
                }
            }
        });


//        listView = (ListView) view.findViewById(R.id.listview);
//        List<BluetoothListItem> list = new ArrayList<>();
//        BluetoothListItem item = new BluetoothListItem();
//        item.setBtName("BT-1234");
//        list.add(item);
//        list.add(item);
//        list.add(item);
//        list.add(item);
//        adapter = new com.example.myhouseapp0.adapter.BtAdapter(this, R.layout.bt_list_item, list);
//        listView.setAdapter(adapter);

    }
    private void enableOrDisableBluetooth(){
        if (!btAdapter.isEnabled()) {
            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i, ENABLE_REQUEST);
        } else if (btAdapter.isEnabled()){
            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                btAdapter.disable();
                Toast.makeText(getActivity(), "Выключено", Toast.LENGTH_LONG).show();
            } else Toast.makeText(getActivity(), "Что-то пошло не так...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ENABLE_REQUEST){
            if(resultCode == -1){
                Toast.makeText(getActivity(), "Включено", Toast.LENGTH_LONG).show();

            }
        }

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}