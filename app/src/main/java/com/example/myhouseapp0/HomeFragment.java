package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    Button btnAdd;
//    , btn_edit;
//    private static final String TAG = "MainFragment";

    private DB_helper dbHelper;



    public String APIKey = "5805dd66a332dedde152edfe026bb26f";
    private static final String site = "https://api.openweathermap.org/data/2.5/weather?q=Kazan&units=metric&appid=5805dd66a332dedde152edfe026bb26f&lang=ru";

    RequestQueue requestQueue;
    double temp = 0;

    final Calendar calendar = GregorianCalendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_FORMAT, new Locale("ru"));
    int day = calendar.get(Calendar.DAY_OF_MONTH);

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dbHelper = new DB_helper(requireContext());
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.switch_mode);
        viewPager2 = view.findViewById(R.id.view_mode);
        viewPager2.setUserInputEnabled(false);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });




        btnAdd  = view.findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(v -> showAddButtonDialog());

        // ___________________________

       // ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.)

        TextView temptext = view.findViewById(R.id.textview_temp);
        TextView datetext = view.findViewById(R.id.textview_date);
        datetext.setText(day + " " + month + " " + year);
        requestQueue = Volley.newRequestQueue(requireContext());
        // в случае возникновеня ошибки
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, //GET - API-запрос для получение данных
                site, null, response -> {
                    try {
                        JSONObject weather = response.getJSONObject("main"); //получаем JSON-обьекты main и wind (в фигурных скобках - объекты, в квадратных - массивы (JSONArray).
                        temp = weather.getDouble("temp");
                        // присваеваем переменным соответствующие значения из API
                        temptext.setText(temp + "°");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

//    private void addButtonToCurrentTab() {
//        int currentPosition = viewPager2.getCurrentItem();
//        Fragment currentFragment = viewPagerAdapter.createFragment(currentPosition);
//
//        if (currentFragment instanceof ListOfObjectsFragment) {
//            ((ListOfObjectsFragment) currentFragment).createNewButton();
//        }
//    }

//    public void addButton(String name, String size) {
//        // Здесь вы можете добавить логику для добавления кнопки в ваш ViewPager
//        // Например, передать данные во ViewPagerAdapter и обновить адаптер
//    }


    // Получить ListOfObjectsFragment
    public ListOfObjectsFragment getListOfObjectsFragment() {
        Fragment fragment = viewPagerAdapter.getFragment(0); // позиция 0
        if (fragment instanceof ListOfObjectsFragment) {
            return (ListOfObjectsFragment) fragment;
        }
        return null;
    }

    // Получить InteractiveMapFragment
    public InteractiveMapFragment getInteractiveMapFragment() {
        Fragment fragment = viewPagerAdapter.getFragment(1); // позиция 1
        if (fragment instanceof InteractiveMapFragment) {
            return (InteractiveMapFragment) fragment;
        }
        return null;
    }



    private void showAddButtonDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_object);
        dialog.setTitle("Добавление объекта");


        EditText editName = dialog.findViewById(R.id.etObjectName);
        Spinner devices = dialog.findViewById(R.id.spinnerDevices);
        EditText editWidth = dialog.findViewById(R.id.etWidth);
        EditText editLength = dialog.findViewById(R.id.etLength);
        Button btnSave = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        List<String> devicesList = dbHelper.getDevices();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                devicesList
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devices.setAdapter(arrayAdapter);

        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString();
//            String devices = editDevices.getText().toString();
//            Integer sizeX = Integer.parseInt(String.valueOf(editWidth.getText()));
//            Integer sizeY = Integer.parseInt(String.valueOf(editLength.getText()));
            int sizeX = 50;
            int sizeY = 50;

//            if (!name.isEmpty() && !sizeStr.isEmpty()) {
//                createNewButton(name, sizeStr);
//                isWaitingForPosition = true;
//                dialog.dismiss();
//            }
//            if (!name.isEmpty() && !(sizeX == null) && !(sizeY == null)) {
//                // Передаём имя во фрагменты
//                passNameToCurrentFragment(name, sizeX, sizeY);
//                dialog.dismiss();
//            }

            String selectedDevice = devices.getSelectedItem().toString();
//            createNewFragment(name, selectedDevice);
            createDynamicFragment(name, devicesList);

            passNameToCurrentFragment(name, sizeX, sizeY);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();


//        DialogAddObject dialog = new DialogAddObject();
//        dialog.setOnObjectAddedListener((name, sizeX, sizeY) -> {
//            // Передаём данные в первый фрагмент (позиция 0)
//            Fragment firstFragment = pagerAdapter.getFragment(0);
//            if (firstFragment instanceof FirstModeFragment) {
//                ((FirstModeFragment) firstFragment).addButton(name, sizeX, sizeY);
//            }
//        });
//        dialog.show(getParentFragmentManager(), "AddObjectDialog");

    }

    private void createDynamicFragment(String name, List<String> devices) {
        // Создаём класс фрагмента динамически
        Class<?> fragmentClass = createFragmentClass(name);

        try {
            Fragment newFragment = (Fragment) fragmentClass.newInstance();
            // Здесь можно передать данные через Bundle
            Bundle args = new Bundle();
            args.putStringArrayList("devices", new ArrayList<>(devices));
            newFragment.setArguments(args);


            // Сохраняем ссылку на фрагмент для дальнейшего использования
            FragmentManager fm = getParentFragmentManager();
            fm.beginTransaction()
                    .add(R.id.frame_layout_devices, newFragment, name + "Fragment")
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Class<?> createFragmentClass(String name) {
        // В реальном приложении используйте динамическую генерацию классов
        // или предопределённый набор шаблонов
        return CustomFragment.class;
    }


//    private void createNewFragment(String name, String device) {
//        Bundle args = new Bundle();
//        args.putString("name", name);
//        args.putString("device", device);
//
//        CustomFragment newFragment = new CustomFragment();
//        newFragment.setArguments(args);
//
//        requireActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frame_layout, newFragment)
//                .addToBackStack(null)
//                .commit();
//    }

    private void passNameToCurrentFragment(String name, int sizeX, int sizeY) {

        ListOfObjectsFragment listFragment = getListOfObjectsFragment();
        listFragment.onObjectAdded(name, sizeX, sizeY);

        InteractiveMapFragment mapFragment = getInteractiveMapFragment();
        mapFragment.onObjectAdded(name, sizeX, sizeY);

//        Fragment currentFragment = getChildFragmentManager().findFragmentByTag(
//                "android:switcher:" + viewPager2.getId() + ":" + "0");

//        if (currentFragment instanceof DialogAddObject.OnObjectAddedListener) {
//            ((DialogAddObject.OnObjectAddedListener) currentFragment).onObjectAdded(name, sizeX, sizeY);
//        }
//        if (currentFragment != null) {
//            if (currentFragment instanceof OnObjectAddedListener) {
//                Toast.makeText(requireContext(), "object adding is going", Toast.LENGTH_SHORT).show();
//                                ((OnObjectAddedListener) currentFragment).onObjectAdded(name, sizeX, sizeY);
//            } else {
//                // Логика на случай, если фрагмент найден, но не реализует интерфейс
//                Toast.makeText(requireContext(), "кущий фрагмент не реализует OnObjectAddedListener", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            // Логика на случай, если фрагмент не найден
//            Toast.makeText(requireContext(), "Не удалось найти текущий фрагмент во ViewPager2", Toast.LENGTH_SHORT).show();
//        }
    }


    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_devices, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

