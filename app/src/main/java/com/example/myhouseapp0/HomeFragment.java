package com.example.myhouseapp0;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import java.util.Arrays;
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
    Button btnAdd, btn_edit;
    private DialogAddObject.OnDevicesSelectedListener listener;
    private boolean[] checkedDevices;
    private String[] deviceList = {"Устройство1", "2", "3", "4", "Наушники"};
    private boolean isInEditMode = false; // Флаг режима редактирования
    private Button selectedButtonForEdit; // Выбранная для редактирования кнопка
//    private static final String TAG = "MainFragment";

    private DB_helper dbHelper;

    private InteractiveMapFragment mapFragment;

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



        mapFragment = (InteractiveMapFragment) viewPagerAdapter.createFragment(1);

        btnAdd  = view.findViewById(R.id.btn_add);
        btn_edit = view.findViewById(R.id.btn_edit);

        btnAdd.setOnClickListener(v -> showAddButtonDialog());
        btn_edit.setOnClickListener(v -> mapFragment.enterEditMode());


//        btn_edit.setOnClickListener(v -> showEditDialog());
        // ___________________________
// Инициализируем массив здесь — после загрузки разметки
        checkedDevices = new boolean[deviceList.length];
        Arrays.fill(checkedDevices, false); // Явно задаём все значения как false

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

    private void setupButtonListeners() {
                // Кнопка "Редактировать"
        btn_edit.setOnClickListener(v -> {
            getInteractiveMapFragment().enterEditMode();
        });
    }

    private void showAddButtonDialog() {
// Защита от null
        if (checkedDevices == null) {
            Toast.makeText(requireContext(), "Ошибка инициализации диалога", Toast.LENGTH_SHORT).show();
            return;
        }

        // Обработчики кнопок
        setupButtonListeners();
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_add_object);
        dialog.setTitle("Добавление объекта");


        EditText editName = dialog.findViewById(R.id.etObjectName);
        ListView devices = dialog.findViewById(R.id.spinnerDevices);
        EditText editWidth = dialog.findViewById(R.id.etWidth);
        EditText editLength = dialog.findViewById(R.id.etLength);
        Button btnSave = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_multiple_choice,
                deviceList
        );
//
//        List<String> devicesList = dbHelper.getDevices();
//        DeviceMultiSelectAdapter arrayAdapter = new DeviceMultiSelectAdapter(
//                requireContext(),
//                devicesList
//        );
//        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devices.setAdapter(arrayAdapter);
        devices.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        for (int i = 0; i < deviceList.length; i++) {
            if (checkedDevices[i]) {
                devices.setItemChecked(i, true);
            }
        }


        btnSave.setOnClickListener(v -> {
            String name = editName.getText().toString();
//            int sizeX = Integer.parseInt(String.valueOf(editWidth.getText())) / 5;
//            int sizeY = Integer.parseInt(String.valueOf(editLength.getText())) / 5;
            String length = editLength.getText().toString();
            String width = editWidth.getText().toString();
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
//
            List<String> selectedDevices = new ArrayList<>();
            for (int i = 0; i < deviceList.length; i++) {
                if (devices.isItemChecked(i)) {
                    selectedDevices.add(deviceList[i]);
                    checkedDevices[i] = true;
                } else {
                    checkedDevices[i] = false;
                }
            }
            boolean isValid = true;


            // Проверка на заполнение имени
            if (name.isEmpty()) {
                editName.setError("Поле обязательно для заполнения");
                isValid = false;
            } else if (!isNameUnique(name)) {
                editName.setError("Название должно быть уникальным");
                isValid = false;
            } else {
                editName.setError(null);
            }

            int sizeX, sizeY;
            // Проверка sizeX
            if (width.isEmpty()) {
                editWidth.setError("Поле обязательно для заполнения");
                isValid = false;
            } else {
                try {
                    sizeX = Integer.parseInt(width);
                    if (sizeX < 100 || sizeX > 1000) {
                        editWidth.setError("Значение допустимо от 100 до 1000");
                        isValid = false;
                    } else {
                        editWidth.setError(null);
                    }
                } catch (NumberFormatException e) {
                    editWidth.setError("Введите корректное число");
                    isValid = false;
                }
            }

            // Проверка sizeY
            if (length.isEmpty()) {
                editLength.setError("Поле обязательно для заполнения");
                isValid = false;
            } else {
                try {
                    sizeY = Integer.parseInt(length);
                    if (sizeY < 100 || sizeY > 1000) {
                        editLength.setError("Значение допустимо от 100 до 1000");
                        isValid = false;
                    } else {
                        editLength.setError(null);
                    }
                } catch (NumberFormatException e) {
                    editLength.setError("Введите корректное число");
                    isValid = false;
                }
            }

            if (selectedDevices.isEmpty()) {
                Toast.makeText(requireContext(), "Выберите хотя бы одно устройство", Toast.LENGTH_SHORT).show();
                isValid = false;
            }
            if (isValid) {
                // Все проверки пройдены — выполняем основное действие
                if (viewPager2.getCurrentItem() != 1) {viewPager2.setCurrentItem(1, true);}

                handleSelectedDevices(String.valueOf(editName), selectedDevices);
                passNameToCurrentFragment(name, Integer.parseInt(length)/5, Integer.parseInt(width)/5);
                dialog.dismiss();

            }

//            listener.onDevicesSelected(selectedDevices);
//            String selectedDevice = devices.getSelectedItem().toString();
//            createNewFragment(name, selectedDevice);
//            createDynamicFragment(name, devicesList);


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
    private boolean isNameUnique(String name) {
        // Здесь может быть проверка в базе данных, SharedPreferences или списке
        // Пример с простым списком:
        List<String> existingNames = getExistingNames(); // ваш источник данных
        return !existingNames.contains(name);
    }

    private List<String> getExistingNames() {
        // Реализация получения существующих имён
        // Может быть запрос к БД, чтение из файла и т. д.
        return new ArrayList<>(); // замените на реальную реализацию
    }

    private void handleSelectedDevices(String name, List<String> selectedDevices) {
        String message = String.format("Объект: %s\nУстройства: %s",
                name, String.join(", ", selectedDevices));
//        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        // Здесь можно добавить логику сохранения данных
    }
    public void showEditDialog(Button buttonToEdit) {
        Dialog dialog_edit = new Dialog(requireContext());
        dialog_edit.setTitle("Редактирование кнопки");
        dialog_edit.setContentView(R.layout.dialog_edit_object);

        EditText editName_edit = dialog_edit.findViewById(R.id.etObjectName_edit);
        Spinner devices_edit = dialog_edit.findViewById(R.id.spinnerDevices_edit);

        List<String> devicesList = dbHelper.getDevices();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                devicesList
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        devices_edit.setAdapter(arrayAdapter);
        EditText editWidth_edit = dialog_edit.findViewById(R.id.etWidth_edit);
        EditText editLength_edit = dialog_edit.findViewById(R.id.etLength_edit);
        Button btnSave_edit = dialog_edit.findViewById(R.id.btnConfirm_edit);
        Button btnCancel_edit = dialog_edit.findViewById(R.id.btnCancel_edit);
        Button btnDelete = dialog_edit.findViewById(R.id.btnDelete_edit);

        btnSave_edit.setOnClickListener(v -> {
            String new_name = editName_edit.getText().toString();
//            String devices = editDevices.getText().toString();
            int sizeX_new = Integer.parseInt(String.valueOf(editWidth_edit.getText())) / 5;
            int sizeY_new = Integer.parseInt(String.valueOf(editLength_edit.getText())) / 5;

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

            // Передаём изменения в MainFragment для применения
            mapFragment.applyButtonEdit(buttonToEdit, new_name, sizeX_new, sizeY_new);

            dialog_edit.dismiss();
        });

        btnCancel_edit.setOnClickListener(v -> dialog_edit.dismiss());
        dialog_edit.show();


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
        mapFragment.createButton(name, sizeX, sizeY, ContextCompat.getColor(requireContext(), R.color.white_green));

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
    private void passNameToCurrentFragment_edit(String name, int sizeX, int sizeY) {

//        ListOfObjectsFragment listFragment = getListOfObjectsFragment();
//        listFragment.onObjectAdded(name, sizeX, sizeY);

        InteractiveMapFragment mapFragment = getInteractiveMapFragment();
        mapFragment.highlightAllButtons();

    }



    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_devices, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

