package com.example.myhouseapp0;

//import static androidx.core.app.AppOpsManagerCompat.Api23Impl.getSystemService;

import android.annotation.SuppressLint;
import android.app.LauncherActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myhouseapp0.adapter.BluetoothListItem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static android.R.layout.simple_list_item_1;

public class CreateObjectFragment extends Fragment {

    private ListView listView;

    private BluetoothAdapter btAdapter;


    //new tutorial
    BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private final int ENABLE_REQUEST = 15;
    ArrayList<String> pairedDeviceArrayList;
    ListView listViewPairedDevice;
    ArrayAdapter<String> pairedDeviceAdapter;
    ThreadConnectBTdevice myThreadConnectBTdevice;
    ThreadConnected myThreadConnected;
    private UUID myUUID;
    private StringBuilder sb = new StringBuilder();


    //    private void init() {
//
//        btAdapter = BluetoothAdapter.getDefaultAdapter();
//
//    }
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
//        init();
        return inflater.inflate(R.layout.fragment_create_object, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_back_from_create_object = (Button) view.findViewById(R.id.btn_back_from_create_object);
        btn_back_from_create_object.setOnClickListener(v -> replaceFragment(new HomeFragment()));

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

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


        //new tut 12/03
        final String UUID_STRING_WELL_KNOWN_SPP = "00001101-0000-1000-8000-00805F9B34FB";
        listViewPairedDevice = (ListView) view.findViewById(R.id.listview);
        myUUID = UUID.fromString(UUID_STRING_WELL_KNOWN_SPP);
//        String stInfo = bluetoothAdapter.getName() + " " + bluetoothAdapter.getAddress();
//        textInfo.setText(String.format("Это устройство: %s", stInfo));


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

    //12/03
    private void setup() { // Создание списка сопряжённых Bluetooth-устройств
        Set<BluetoothDevice> pairedDevices = null;
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            pairedDevices = bluetoothAdapter.getBondedDevices();

        }
        if (pairedDevices.size() > 0) { // Если есть сопряжённые устройства
            pairedDeviceArrayList = new ArrayList<>();
            for (BluetoothDevice device : pairedDevices) {
                pairedDeviceArrayList.add(device.getName() + "\n" + device.getAddress());

            }
            pairedDeviceAdapter = new ArrayAdapter<>(requireContext(), simple_list_item_1, pairedDeviceArrayList);
            listViewPairedDevice.setAdapter(pairedDeviceAdapter);
            listViewPairedDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //переход на другой фрагмент аналог скрывания листа
                    String itemValue = (String) listViewPairedDevice.getItemAtPosition(position);
                    String MAC = itemValue.substring(itemValue.length() - 17);
                    BluetoothDevice device2 = bluetoothAdapter.getRemoteDevice(MAC);
                    myThreadConnectBTdevice = new ThreadConnectBTdevice(device2);
                    myThreadConnectBTdevice.start();
                }
            });

        }
    }

    private class ThreadConnectBTdevice extends Thread {
        private BluetoothSocket bluetoothSocket = null;

        private ThreadConnectBTdevice(BluetoothDevice device) {
            try {
                if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    bluetoothSocket = device.createRfcommSocketToServiceRecord(myUUID);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            boolean success = false;
            try {
                if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    bluetoothSocket.connect();
                    success  =true;
                }

            } catch (IOException e) {
                e.printStackTrace();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getActivity(), "No connect", Toast.LENGTH_LONG).show();
//
//                    }
//                });
                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if(success) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // switch fragment
//                        replaceFragment(new HomeFragment());
//                    }
//
//                });
                myThreadConnected = new ThreadConnected(bluetoothSocket);
                myThreadConnected.start();
            }
        }
        public void cancel() {
            Toast.makeText(getContext().getApplicationContext(), "close - bluettoth socket", Toast.LENGTH_LONG).show();
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//destroy надо не???

    private class ThreadConnected extends Thread {
        private final InputStream connectedInputStream;
        private final OutputStream connectedOutputStream;
        private String sbprint;
        public ThreadConnected(BluetoothSocket socket) {
            InputStream in = null;
            OutputStream out = null;

            try {
                in = socket.getInputStream();
                out = socket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connectedInputStream = in;
            connectedOutputStream = out;
        }
        @Override
        public  void run() {
            while (true) {
                try {
                    byte[] buffer = new byte[1];
                    int bytes = connectedInputStream.read(buffer);
                    String strIncom = new String(buffer, 0, bytes);
                    sb.append(strIncom);
                    int endOfLineIndex = sb.indexOf("\r\n");
                    if (endOfLineIndex > 0) {
                        sbprint = sb.substring(0, endOfLineIndex);
                        sb.delete(0, sb.length());
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run()
//
//
                        }

                    } catch (IOException e) {
                    break;
                }
            }
        }
    }



        private void enableOrDisableBluetooth () {
            if (!btAdapter.isEnabled()) {
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i, ENABLE_REQUEST);
            } else if (btAdapter.isEnabled()) {
                if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    btAdapter.disable();
                    Toast.makeText(getActivity(), "Выключено", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getActivity(), "Что-то пошло не так...", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == ENABLE_REQUEST) {
                if (resultCode == -1) {
                    Toast.makeText(getActivity(), "Включено", Toast.LENGTH_LONG).show();

                }
            }

        }

        private void replaceFragment (Fragment fragment){
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        }
    }