package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myhouseapp0.rooms.BathroomFragment;
import com.example.myhouseapp0.rooms.BedroomFragment;
import com.example.myhouseapp0.rooms.CarRoomFragment;
import com.example.myhouseapp0.rooms.GreenhouseFragment;
import com.example.myhouseapp0.rooms.HallRoomFragment;
import com.example.myhouseapp0.rooms.KitchenFragment;
import com.example.myhouseapp0.rooms.StoreroomFragment;
import com.example.myhouseapp0.rooms.YardFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }
    public String APIKey = "5805dd66a332dedde152edfe026bb26f";
    private static final String site = "https://api.openweathermap.org/data/2.5/weather?q=Kazan&units=metric&appid=5805dd66a332dedde152edfe026bb26f&lang=ru";

    RequestQueue requestQueue;
    double temp = 0;
    String city = "";

    final Calendar calendar = GregorianCalendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, new Locale("ru"));
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_to_hallroom = (Button) view.findViewById(R.id.btn_hallroom);
        btn_to_hallroom.setOnClickListener(v -> replaceFragment(new HallRoomFragment()));

        Button btn_to_bedroom = (Button) view.findViewById(R.id.btn_bedroom);
        btn_to_bedroom.setOnClickListener(v -> replaceFragment(new BedroomFragment()));

        Button btn_to_bathroom = (Button) view.findViewById(R.id.btn_bathroom);
        btn_to_bathroom.setOnClickListener(v -> replaceFragment(new BathroomFragment()));

        Button btn_to_kitchen = (Button) view.findViewById(R.id.btn_kitchen);
        btn_to_kitchen.setOnClickListener(v -> replaceFragment(new KitchenFragment()));

        Button btn_to_carroom = (Button) view.findViewById(R.id.btn_carroom);
        btn_to_carroom.setOnClickListener(v -> replaceFragment(new CarRoomFragment()));

        Button btn_to_yard = (Button) view.findViewById(R.id.btn_yard);
        btn_to_yard.setOnClickListener(v -> replaceFragment(new YardFragment()));

        Button btn_to_greenhouse = (Button) view.findViewById(R.id.btn_greenhouse);
        btn_to_greenhouse.setOnClickListener(v -> replaceFragment(new GreenhouseFragment()));

        Button btn_to_storeroom = (Button) view.findViewById(R.id.btn_storeroom);
        btn_to_storeroom.setOnClickListener(v -> replaceFragment(new StoreroomFragment()));

        TextView temptext = (TextView) view.findViewById(R.id.textview_temp);
        TextView datetext = (TextView) view.findViewById(R.id.textview_date);
        datetext.setText(day + " " + month + " " + year);

        requestQueue = Volley.newRequestQueue(requireContext());

        // в случае возникновеня ошибки
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                site, null, new Response.Listener<JSONObject>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject weather = response.getJSONObject("main"); //получаем JSON-обьекты main и wind (в фигурных скобках - объекты, в квадратных - массивы (JSONArray).
                    temp = weather.getDouble("temp");
                    // присваеваем переменным соответствующие значения из API

                    temptext.setText(temp + "°");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

