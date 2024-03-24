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

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class HallRoomFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private static final String TAG = "bluetooth1";
    Handler h;
    final int RECIEVE_MESSAGE = 1; //статус для Hadnler;
    private static final int REQUEST_ENABLE_BT = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private OutputStream outputStream = null;
    private StringBuilder sb = new StringBuilder();
    private ConnectedThread mConnectedThread;

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

    @SuppressLint("HandlerLeak")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_back_from_hallroom = (Button) view.findViewById(R.id.btn_back_from_hallroom);
        btn_back_from_hallroom.setOnClickListener(v -> replaceFragment(new HomeFragment()));

        TextView tv_temp = (TextView) view.findViewById(R.id.tv_temp);
        TextView tv_humidity = (TextView) view.findViewById(R.id.tv_humidity);

        tv_temp.setText("%%°");
        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == RECIEVE_MESSAGE) {                                                   // если приняли сообщение в Handler
                    byte[] readBuf = (byte[]) msg.obj;
                    String strIncom = new String(readBuf, 0, msg.arg1);
                    sb.append(strIncom);                                                // формируем строку
                    int endOfLineIndex = sb.indexOf("\r\n");                            // определяем символы конца строки
                    if (endOfLineIndex > 0) {                                            // если встречаем конец строки,
                        String sbprint = sb.substring(0, endOfLineIndex);               // то извлекаем строку
                        sb.delete(0, sb.length());                                      // и очищаем sb
                        tv_temp.setText(sbprint+"°");             // обновляем TextView
//                            btnOff.setEnabled(true);
//                            btnOn.setEnabled(true);
                    }
                    //Log.d(TAG, "...Строка:"+ sb.toString() +  "Байт:" + msg.arg1 + "...");
                }
            };
        };

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch btn_switch = (Switch) view.findViewById(R.id.switch_for_servo);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBtState();

        btn_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mConnectedThread.write("1");
                    Toast.makeText(getContext(), "Включено", Toast.LENGTH_SHORT).show();
                } else {
                    mConnectedThread.write("0");
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
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "In onPause()...");
        try{
            btSocket.close();
        }catch (IOException e) {
            errorExit("Error", "Failed to flush output stream" + e.getMessage());
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

//    private class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            // Get the input and output streams, using temp objects because
//            // member streams are final
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) { }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        public void run() {
//            byte[] buffer = new byte[256];  // buffer store for the stream
//            int bytes; // bytes returned from read()
//
//            // Keep listening to the InputStream until an exception occurs
//            while (true) {
//                try {
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);        // Получаем кол-во байт и само собщение в байтовый массив "buffer"
//                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();     // Отправляем в очередь сообщений Handler
//                } catch (IOException e) {
//                    break;
//                }
//            }
//        }
//        public void write(String message) {
//            Log.d(TAG, "...Данные для отправки: " + message + "...");
//            byte[] msgBuffer = message.getBytes();
//            try {
//                mmOutStream.write(msgBuffer);
//            } catch (IOException e) {
//                Log.d(TAG, "...Ошибка отправки данных: " + e.getMessage() + "...");
//            }
//        }
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) { }
//        }
//    }

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