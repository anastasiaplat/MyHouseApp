package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

public class HallRoomFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private static final String TAG = "bluetooth1";
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outputStream = null;

    private static final UUID my_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static String macAddress = "00:22:12:01:91:44";

    public HallRoomFragment() {
        // Required empty public constructor
    }

    public static HallRoomFragment newInstance(String param1, String param2) {
        HallRoomFragment fragment = new HallRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hall_room, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_back_from_hallroom = (Button) view.findViewById(R.id.btn_back_from_hallroom);
        btn_back_from_hallroom.setOnClickListener(v -> replaceFragment(new HomeFragment()));


        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch = (Switch) view.findViewById(R.id.switch_for_servo);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBtState();

        btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendData("1");
                    Toast.makeText(getContext(), "Включено", Toast.LENGTH_SHORT).show();
                } else {
                    sendData("0");
                    Toast.makeText(getContext(), "Выключено", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Попытка соединения");
        BluetoothDevice device = btAdapter.getRemoteDevice(macAddress);
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                btSocket = device.createRfcommSocketToServiceRecord(my_UUID);
            }


        }catch (IOException e) {
            errorExit("Error","In onResume() and socket create failed: " + e.getMessage());
        }
        btAdapter.cancelDiscovery();
        Log.d(TAG, "Соединение...");
        try{
            btSocket.connect();
            Log.d(TAG, "Соединение установлено");
        } catch (IOException e) {
            try{
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Error", "In onResume() and unable to close socket" + e2.getMessage());
            }
        }

        Log.d(TAG, "Создание сокета...");
        try{
            outputStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Error", "In onResume() and outputStream creation failed" + e.getMessage());
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "In onPause()...");
        if(outputStream != null) {
            try{
                outputStream.flush();
            }catch (IOException e) {
                errorExit("Error", "Failed to flush output stream" + e.getMessage());
            }
        }

        try{
            btSocket.close();
        } catch (IOException e) {
            errorExit("Error", "Failed to close socket" + e.getMessage());
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(requireContext(), title + "-" + message, Toast.LENGTH_LONG).show();
    }
    private void sendData(String message){
        byte[] msgBuffer = message.getBytes();
        Log.d(TAG, "Посылаем данные:" + message + "...");

        try{
            outputStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "An exception occurred during write:" + e.getMessage();
        }
    }


    private void checkBtState() {
        if(btAdapter == null) {
            errorExit("Error", "Bt is not supporting");
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "Bt is on");
            } else {
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }

        }


    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }



}