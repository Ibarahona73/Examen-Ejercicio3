package hn.uth.examen201930010221.ui.evento;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import hn.uth.examen201930010221.R;
import hn.uth.examen201930010221.databinding.FragmentEventosBinding;
import hn.uth.examen201930010221.entity.Contacto;
import hn.uth.examen201930010221.entity.Fitnes;
import hn.uth.examen201930010221.entity.GPSLocation;
import hn.uth.examen201930010221.ui.acompaniantes.AcompaniantesViewModel;
import hn.uth.examen201930010221.ui.inicio.EventoViewModel;

public class EventoFragment  extends Fragment implements LocationListener, OnItemClickListenerContactosSelect<Contacto>{

    private ImageView selectedImageIcon;
    private List<Integer> imageOptions;
    private int selectedImageResId = -1;
    private FragmentEventosBinding binding;

    private EventoViewModel viewModelNotas;

    private AcompaniantesViewModel viewModelAcompaniantes;

    private SelectContactosAdapter adapter;

    private Fitnes eventEditar;

    private List<Contacto> lSelectedContact;

    private String fecha, latitud = "", longitud = "";
    private int img;

    private static final int REQUEST_CODE_GPS = 123;
    private LocationManager locationManager;

    private GPSLocation ubicacion;

    private DatePickerDialog datePickerDialog;

    private Button dateButton;
    Animation animation1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentEventosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


      /*  initDatePicker();
        dateButton = binding.datePickerButton.findViewById(R.id.datePickerButton);
        dateButton.setText(getTodayDate());*/

        ubicacion = null;

        lSelectedContact = new ArrayList<>();

        viewModelNotas = new ViewModelProvider(this).get(EventoViewModel.class);

        viewModelAcompaniantes = new ViewModelProvider(this).get(AcompaniantesViewModel.class);

        //selectedImageIcon = binding.selectedImageIcon;

        binding.btnGuardar.setOnClickListener(v -> {
            if (verificar()) {
                String nombre_lugar = binding.etLugar.getEditText().getText().toString();
                String ejercicio = binding.etEje.getEditText().getText().toString();
                String coordinador = binding.etCord.getEditText().getText().toString();
                String acom = "";

                Fitnes nEvento = eventEditar;

                for (Contacto contacto : lSelectedContact) {
                    String idContac = String.valueOf(contacto.getId());
                    acom = acom + idContac + "\n";
                    contacto.setFav((contacto.getFav() + 1));
                    viewModelAcompaniantes.update(contacto);
                }

                if (eventEditar == null) {
                    nEvento = new Fitnes(nombre_lugar, fecha, coordinador, ejercicio, acom, latitud, longitud);

                    viewModelNotas.insert(nEvento);

                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.navigation_home);
                } else {
                    nEvento.setLugar(nombre_lugar);
                    nEvento.setCoordinador(coordinador);
                    nEvento.setEjercicio(ejercicio);
                    nEvento.setAcompaniante(acom);
                    //nEvento.setImg(img);
                    viewModelNotas.update(nEvento);

                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.navigation_home);
                }

            } else {
                Snackbar.make(binding.etLugar, R.string.falta_informacion, Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.btnAcompaniantes.setOnClickListener(v -> {
            binding.lNota.setVisibility(View.GONE);
            binding.lContactos.setVisibility(View.VISIBLE);
            obtenerAcompaniantes();
        });

        binding.btnRegresar.setOnClickListener(v -> {
            binding.lContactos.setVisibility(View.GONE);
            binding.lNota.setVisibility(View.VISIBLE);
        });

        binding.btnCancelar.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.navigation_home);
        });

        binding.btnMostrarContactos.setOnClickListener(v -> {
            binding.btnMostrarContactos.setVisibility(View.GONE);
            obtenerChips();
        });

        setupRecycler();

        //setupListImage();

        notaEditar();

        mostrarEventoEditar();

        obtenerAcompaniantesNotaEditar();



        //Button button = (Button) findViewById(R.id.btnDate);


        return root;
    }
/*
    private String getTodayDate() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);;
        month = month + 1;
        int day = cal.get(Calendar.DATE);;
        return makeDataString(day , month , year);

    }*/

/*
    private void initDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                {
                    month = month + 1;
                    String date = makeDataString(day, month, year);
                    dateButton.setText(date);

                }
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);;
        int day = cal.get(Calendar.DATE);;

        int style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

        datePickerDialog = new DatePickerDialog(this.getContext() , style,dateSetListener,day,month,year);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());


    }

    public void openDatePicker(View view){

        datePickerDialog.show();
    }


    private String makeDataString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;

    }

    private String getMonthFormat(int month) {

        if (month == 1) {
            return "JAN";
        }
        if (month == 2) {
            return "FEB";
        }
        if (month == 3) {
            return "MAR";
        }
        if (month == 4) {
            return "APR";
        }
        if (month == 5) {
            return "MAY";
        }
        if (month == 6) {
            return "JUN";
        }
        if (month == 7) {
            return "JUL";
        }
        if (month == 8) {
            return "AUG";
        }
        if (month == 9) {
            return "SEP";
        }
        if (month == 10) {
            return "OCT";
        }
        if (month == 11) {
            return "NOV";
        }
        if (month == 12) {
            return "DEC";
        }
        return "JAN";
    }
*/




   /* private void setupListImage() {
        imageOptions = Arrays.asList(
                R.drawable.cap1, // ID de tu nueva imagen
                R.drawable.cap2,
                R.drawable.cap3,
                R.drawable.cap4,
                R.drawable.cap5,
                R.drawable.cap6,
                R.drawable.cap7,
                R.drawable.cap8,
                R.drawable.cap9,
                R.drawable.cap10,
                R.drawable.cap11,
                R.drawable.cap12,
                R.drawable.cap13,
                R.drawable.cap14,
                R.drawable.cap15,
                R.drawable.cap16,
                R.drawable.cap17
        );

        binding.opcion1.setOnClickListener(v -> onImageItemClick(0));
        binding.opcion2.setOnClickListener(v -> onImageItemClick(1));
        binding.opcion3.setOnClickListener(v -> onImageItemClick(2));
        binding.opcion4.setOnClickListener(v -> onImageItemClick(3));
        binding.opcion5.setOnClickListener(v -> onImageItemClick(4));
        binding.opcion6.setOnClickListener(v -> onImageItemClick(5));
        binding.opcion7.setOnClickListener(v -> onImageItemClick(6));
        binding.opcion8.setOnClickListener(v -> onImageItemClick(7));
        binding.opcion9.setOnClickListener(v -> onImageItemClick(8));
        binding.opcion10.setOnClickListener(v -> onImageItemClick(9));
        binding.opcion11.setOnClickListener(v -> onImageItemClick(10));
        binding.opcion12.setOnClickListener(v -> onImageItemClick(11));
        binding.opcion13.setOnClickListener(v -> onImageItemClick(12));
        binding.opcion14.setOnClickListener(v -> onImageItemClick(13));
        binding.opcion15.setOnClickListener(v -> onImageItemClick(14));
        binding.opcion16.setOnClickListener(v -> onImageItemClick(15));
        binding.opcion17.setOnClickListener(v -> onImageItemClick(16));
    }
    */





    private boolean verificar(){
        if(!"".equals(binding.etLugar.getEditText().getText().toString().trim()) &&
                !"".equals(binding.etCord.getEditText().getText().toString().trim()) &&
                !"".equals(binding.etEje.getEditText().getText().toString().trim()) &&
                !"".equals(latitud) && !"".equals(longitud)){
            return true;
        }else {
            return false;
        }
    }
    private void obtenerChips() {
        binding.chipGroupAcompaniantes.removeAllViews();
        binding.chipGroupSelectAcompaniantes.removeAllViews();
        Drawable icon_contacto= requireActivity().getDrawable(R.drawable.ic_contact);
        if(lSelectedContact !=null && lSelectedContact.size()>0) {
            for (Contacto c : lSelectedContact) {
                Chip chip= new Chip(getContext());
                Chip chip2= new Chip(getContext());
                chip.setText(c.getName());
                chip2.setText(c.getName());
                chip.setTextSize(10);
                chip2.setTextSize(10);
                chip.setPadding(0,0,0,0);
                chip2.setPadding(0,0,0,0);
                chip.setChipIcon(icon_contacto);
                chip2.setChipIcon(icon_contacto);
                binding.chipGroupAcompaniantes.addView(chip);
                binding.chipGroupSelectAcompaniantes.addView(chip2);
            }
        }
    }

    private void obtenerAcompaniantesNotaEditar() {
        if (eventEditar != null) {
            String a = eventEditar.getAcompaniante();
            if (!"".equals(a)) {
                String[] a_ids = a.split("\n");
                List<String> list_ids = new ArrayList<>(Arrays.asList(a_ids));

                for (String numero : list_ids) {
                    if (!"".equals(numero)) {
                        LiveData<Contacto> contacto;
                        contacto = viewModelAcompaniantes.getDataContactoId(Integer.valueOf(numero));
                        if (contacto != null) {
                            contacto.observe(getViewLifecycleOwner(), contactoId -> {
                                if (contactoId != null) {
                                    if (!lSelectedContact.contains(contactoId)) {
                                        lSelectedContact.add(contactoId);
                                    }
                                }

                            });
                        }
                    }
                }

            }
        }
    }

    private void notaEditar() {
        try {
            eventEditar = (Fitnes) getArguments().getSerializable("evento");
            binding.btnCancelar.setVisibility(View.VISIBLE);

        } catch (Exception e) {
            eventEditar = null;
            solicitarPermisosGPS(getContext());
            fecha = obtenerFecha();

           // img = selectedImageResId;
        }
    }

    private void mostrarEventoEditar() {
       if (eventEditar != null) {

            binding.etLugar.getEditText().setText(eventEditar.getLugar());
            binding.etCord.getEditText().setText(eventEditar.getCoordinador());//cordinador
            binding.etEje.getEditText().setText(eventEditar.getEjercicio());
            latitud=eventEditar.getLatitud();
            longitud= eventEditar.getLongitud();

            if (!"".equals(eventEditar.getAcompaniante())) {
                binding.tvSinContactosSeleccionados.setVisibility(View.GONE);
                binding.btnMostrarContactos.setVisibility(View.VISIBLE);
                binding.lSelectAcompaniantes.setVisibility(View.VISIBLE);
            } else {
                binding.tvSinContactosSeleccionados.setVisibility(View.VISIBLE);
                binding.btnMostrarContactos.setVisibility(View.GONE);
                binding.lSelectAcompaniantes.setVisibility(View.GONE);
            }

            binding.etLugar.requestFocus();
            //onImageItemClick(eventEditar.getImg());
            //selectedImageIcon.setImageResource(notaEditar.getImg());
            //Toast.makeText(getContext(), String.valueOf(notaEditar.getImg()), Toast.LENGTH_SHORT).show();


        } else {
            binding.tvSinContactosSeleccionados.setVisibility(View.VISIBLE);
            binding.btnMostrarContactos.setVisibility(View.GONE);

            //onImageItemClick(0);
        }
    }

    private void setupRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rvAcompaniantes.setLayoutManager(linearLayoutManager);
    }

    private void obtenerAcompaniantes() {
        LiveData<List<Contacto>> contacto1 = viewModelAcompaniantes.getDataSet();
        contacto1.observe(getViewLifecycleOwner(), contactos -> {
            if (contactos != null) {
                adapter = new SelectContactosAdapter(contactos, this, new ArrayList<>(lSelectedContact));
                binding.rvAcompaniantes.setAdapter(adapter);
            }
        });
    }

   /* private void onImageItemClick(int position) {
        selectedImageResId = imageOptions.get(position);
        updateSelectedImage(position);
        img=position;
    }*/

 /*   private void updateSelectedImage(int position) {
        if (position >= 0 && position < imageOptions.size()) {
            int selectedImageResId = imageOptions.get(position);
            selectedImageIcon.setImageResource(selectedImageResId);
        }
        ocultarSelect(position);
    }

    private void ocultarSelect(int position) {
        binding.imgSelect1.setVisibility(View.GONE);
        binding.imgSelect2.setVisibility(View.GONE);
        binding.imgSelect3.setVisibility(View.GONE);
        binding.imgSelect4.setVisibility(View.GONE);
        binding.imgSelect5.setVisibility(View.GONE);
        binding.imgSelect6.setVisibility(View.GONE);
        binding.imgSelect7.setVisibility(View.GONE);
        binding.imgSelect8.setVisibility(View.GONE);
        binding.imgSelect9.setVisibility(View.GONE);
        binding.imgSelect10.setVisibility(View.GONE);
        binding.imgSelect11.setVisibility(View.GONE);
        binding.imgSelect12.setVisibility(View.GONE);
        binding.imgSelect13.setVisibility(View.GONE);
        binding.imgSelect14.setVisibility(View.GONE);
        binding.imgSelect15.setVisibility(View.GONE);
        binding.imgSelect16.setVisibility(View.GONE);
        binding.imgSelect17.setVisibility(View.GONE);

        switch (position){
            case 0:
                binding.imgSelect1.setVisibility(View.VISIBLE);
                break;
            case 1:
                binding.imgSelect2.setVisibility(View.VISIBLE);
                break;
            case 2:
                binding.imgSelect3.setVisibility(View.VISIBLE);
                break;
            case 3:
                binding.imgSelect4.setVisibility(View.VISIBLE);
                break;
            case 4:
                binding.imgSelect5.setVisibility(View.VISIBLE);
                break;
            case 5:
                binding.imgSelect6.setVisibility(View.VISIBLE);
                break;
            case 6:
                binding.imgSelect7.setVisibility(View.VISIBLE);
                break;
            case 7:
                binding.imgSelect8.setVisibility(View.VISIBLE);
                break;
            case 8:
                binding.imgSelect9.setVisibility(View.VISIBLE);
                break;
            case 9:
                binding.imgSelect10.setVisibility(View.VISIBLE);
                break;
            case 10:
                binding.imgSelect11.setVisibility(View.VISIBLE);
                break;
            case 11:
                binding.imgSelect12.setVisibility(View.VISIBLE);
                break;
            case 12:
                binding.imgSelect13.setVisibility(View.VISIBLE);
                break;
            case 13:
                binding.imgSelect14.setVisibility(View.VISIBLE);
                break;
            case 14:
                binding.imgSelect15.setVisibility(View.VISIBLE);
                break;
            case 15:
                binding.imgSelect16.setVisibility(View.VISIBLE);
                break;
            case 16:
                binding.imgSelect17.setVisibility(View.VISIBLE);
                break;
        }

    }*/

// -----------------------------------------------------------------------------

   private String obtenerFecha() {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha = dateFormat.format(currentDate);

        return fecha;
    }

    private void solicitarPermisosGPS(Context context) {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            useFineLocation();
            animarObtenerUbicacion(1);
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
        }
    }


    private void animarObtenerUbicacion(int a) {
        animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.loading);
        if (a == 1) {
            //binding.imgLoading.startAnimation(animation1);
            binding.lUbicacion.setVisibility(View.GONE);
            binding.lLoading.setVisibility(View.VISIBLE);
        } else {
           // binding.imgLoading.clearAnimation();
            binding.lLoading.setVisibility(View.GONE);
            binding.lUbicacion.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_GPS) {
            animarObtenerUbicacion(1);
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                useFineLocation();
            } else if (grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                useCoarseLocation();
            }
        } else {
            Snackbar.make(binding.etLugar, R.string.permiso_denegado, Snackbar.LENGTH_SHORT).show();
        }
    }


    @SuppressLint({"ServiceCast", "MissingPermission"})
    private void useCoarseLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
    }

    @SuppressLint({"ServiceCast", "MissingPermission"})
    private void useFineLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        ubicacion = new GPSLocation(location.getLatitude(), location.getLongitude());

        latitud = ubicacion.getLatitudeStr();
        longitud = ubicacion.getLongitudeStr();

        locationManager.removeUpdates(this);
        animarObtenerUbicacion(2);
    }

    @Override
    public boolean onItemClick(Contacto data) {
        boolean send = false;
        boolean exist = false;

        if (lSelectedContact != null) {
            if (lSelectedContact.size() > 0) {
                for (Contacto c : lSelectedContact) {
                    if (c.getId() == data.getId()) {
                        lSelectedContact.remove(c);
                        exist = true;
                        send = true;
                        break;
                    }
                }
            }
        }

        if (!exist) {
            if (lSelectedContact.size() < 6) {
                lSelectedContact.add(data);
                send = true;
            } else {
                Snackbar.make(binding.etLugar, R.string.companieros_maximos, Snackbar.LENGTH_SHORT).show();
            }
        }


        if (lSelectedContact.size() > 0) {
            binding.lSelectAcompaniantes.setVisibility(View.VISIBLE);
            binding.tvSinContactosSeleccionados.setVisibility(View.GONE);
        } else {
            binding.lSelectAcompaniantes.setVisibility(View.GONE);
            binding.tvSinContactosSeleccionados.setVisibility(View.VISIBLE);
        }

        obtenerChips();

        return send;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
            locationManager = null;
            ubicacion = null;
        }
        binding = null;
    }


}

