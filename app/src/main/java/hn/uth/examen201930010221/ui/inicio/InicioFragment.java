package hn.uth.examen201930010221.ui.inicio;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.button.MaterialButtonToggleGroup;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hn.uth.examen201930010221.R;
import hn.uth.examen201930010221.databinding.FragmentInicioBinding;
import hn.uth.examen201930010221.entity.Contacto;
import hn.uth.examen201930010221.entity.Fitnes;
import hn.uth.examen201930010221.ui.acompaniantes.AcompaniantesViewModel;

public class InicioFragment extends Fragment implements OnItemClickListener<Fitnes> {

    private EventosAdapter adapter;
    private EventoViewModel viewModel;
    private FragmentInicioBinding binding;
    private StaggeredGridLayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInicioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        viewModel = new ViewModelProvider(this).get(EventoViewModel.class);

        adapter = new EventosAdapter(new ArrayList<>(), this);

        viewModel.getDataSet().observe(getViewLifecycleOwner(), fitt -> {
            adapter.setItems(fitt);
            if(adapter.getItemCount()>0){
                binding.imgBack.setVisibility(View.GONE);
            }else{
                binding.imgBack.setVisibility(View.VISIBLE);
            }
        });


        binding.bFab.setOnClickListener(v -> {
            nuevaNota();
        });

        setupRecyclerView();

        return root;
    }

    private void setupRecyclerView() {
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        binding.rvNotas.setLayoutManager(layoutManager);

        binding.rvNotas.setAdapter(adapter);
    }

    private void nuevaNota() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_dashboard);

    }

    private void DialogConfirm(Fitnes fit) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_dialog);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }

        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelar);
        TextView tvMsg = dialog.findViewById(R.id.tvMsg);
        String msg = getString(R.string.eliminar_nota) + " \"" + fit.getLugar() + "\"?";
        tvMsg.setText(msg);

        btnAceptar.setOnClickListener(v -> {
            viewModel.delete(fit);
            dialog.dismiss();
        });
        btnCancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();

    }


    private void DialogShare(Fitnes fit) {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_dialog_shared);


        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }

        RadioGroup rG = dialog.findViewById(R.id.rgAcompaniantes);
        Button btnMsm = dialog.findViewById(R.id.btnSMS);
        Button btnWatsapp = dialog.findViewById(R.id.btnWhatsapp);
        Button btnCorreo = dialog.findViewById(R.id.btnCorreo);
        MaterialButtonToggleGroup mbtg = dialog.findViewById(R.id.gShareOptions);
        Button btnCompartir = dialog.findViewById(R.id.btnCompatir);
        Button btnCancelar = dialog.findViewById(R.id.btnCancelar);


        String a = fit.getAcompaniante();
        List<Contacto> lContactos;
        if (!"".equals(a)) {
            lContactos = new ArrayList<>();
            AcompaniantesViewModel viewModelAcompaniantes = new ViewModelProvider(this).get(AcompaniantesViewModel.class);

            String[] a_ids = a.split("\n");
            List<String> list_ids = new ArrayList<>(Arrays.asList(a_ids));

            for (String numero : list_ids) {
                if (!"".equals(numero)) {
                    LiveData<Contacto> contacto;
                    contacto = viewModelAcompaniantes.getDataContactoId(Integer.valueOf(numero));
                    if (contacto != null) {
                        contacto.observe(getViewLifecycleOwner(), contactoId -> {
                            if (contactoId != null) {
                                if (!lContactos.contains(contactoId)) {
                                    lContactos.add(contactoId);
                                    RadioButton r = new RadioButton(getContext());
                                    r.setTag(contactoId);
                                    r.setText(contactoId.getName());

                                    r.setOnClickListener(v -> {
                                        if (v.getTag() != null) {
                                            Contacto cTag = (Contacto) v.getTag();
                                            mbtg.clearChecked();
                                            btnCompartir.setEnabled(false);

                                            btnMsm.setVisibility(View.GONE);
                                            btnWatsapp.setVisibility(View.GONE);
                                            btnCorreo.setVisibility(View.GONE);

                                            if (!"".equals(cTag.getPhone())) {
                                                btnMsm.setVisibility(View.VISIBLE);
                                                btnWatsapp.setVisibility(View.VISIBLE);
                                            }

                                            if (!"".equals(cTag.getEmail())) {
                                                btnCorreo.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });

                                    rG.addView(r);
                                }
                            }
                        });
                    }
                }
            }
        }

        mbtg.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            btnCompartir.setEnabled(true);
        });

        btnCompartir.setOnClickListener(v -> {
            int bSelect = mbtg.getCheckedButtonId();
            RadioButton rb = dialog.findViewById(rG.getCheckedRadioButtonId());
            Contacto con = (Contacto) rb.getTag();

            if (dialog.findViewById(bSelect) == btnMsm) {
                sharedSms(fit, con.getPhone());

            } else if (dialog.findViewById(bSelect) == btnWatsapp) {
                sharedWhatsapp(fit, con.getPhone());
            } else {
                sharedEmail(fit, con.getEmail());
            }

            dialog.dismiss();
        });
        btnCancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();

    }

    private void sharedWhatsapp(Fitnes fit, String numero) {
        numero = numero.replace("+", "");
        String mensaje =
                "\t" + "Te Invito A Ponernos Fit" + "\n" +
                "Lugar: " + fit.getLugar() + "\n" +
                "Coordinador: " + fit.getCoordinador() + "\n" +
                "Ejercicios A Realizar: " + fit.getEjercicio() + "\n" +
                "Fecha: " + fit.getFecha() + "\n" +
                "Ubicacion:"+"\n"+
                "https://maps.google.com/maps?q=" + fit.getLatitud() + "," + fit.getLongitud();
                //"Ubicacion: " + fit.getLatitud() + "," + fit.getLongitud();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            String url = "https://api.whatsapp.com/send?phone=" + numero + "&text=" + URLEncoder.encode(mensaje, "UTF-8");
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse(url));
            startActivity(intent);

        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocurrio un error al compartir con la app de whatsapp", Toast.LENGTH_SHORT).show();
        }
    }

    public void sharedSms(Fitnes fit, String numero) {
        numero = numero.replace("+", "");

        String mensaje =
                "\t" + "Te Invito A Ponernos Fit" + "\n" +
                "Lugar: " + fit.getLugar() + "\n" +
                "Coordinador: " + fit.getCoordinador() + "\n" +
                "Ejercicios A Realizar: " + fit.getEjercicio() + "\n" +
                "Fecha: " + fit.getFecha() + "\n" +
                "Ubicacion:"+"\n"+
                "https://maps.google.com/maps?q=" + fit.getLatitud() + "," + fit.getLongitud();
                //"Ubicacion: " + fit.getLatitud() + ", " + fit.getLongitud();
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("smsto:" + numero));

            intent.putExtra("sms_body", mensaje);//Agrego el mensaje

            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocurrio un error al compartir con la app de SMS", Toast.LENGTH_SHORT).show();
        }
    }


    public void sharedEmail(Fitnes fit, String destinatario) {
        String asunto= "Compartir Evento";
        String mensaje =
                "\t" + "Te Invito A Ponernos Fit" + "\n" +
                "Lugar: " + fit.getLugar() + "\n" +
                "Coordinador: " + fit.getCoordinador() + "\n" +
                "Ejercicios A Realizar: " + fit.getEjercicio() + "\n" +
                "Fecha: " + fit.getFecha() + "\n" +
                "Ubicacion:"+"\n"+
                "https://maps.google.com/maps?q=" + fit.getLatitud() + "," + fit.getLongitud();
                //"Ubicacion: " + fit.getLatitud() + ", " + fit.getLongitud();

        try {
            String uriText =
                    "mailto:"+destinatario +
                            "?subject=" + Uri.encode(asunto) +
                            "&body=" + Uri.encode(mensaje);
            Uri uri = Uri.parse(uriText);

            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            sendIntent.setData(uri);
            startActivity(Intent.createChooser(sendIntent, "Compartir Evento"));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Ocurrio un error al compartir con la app de correo", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(Fitnes data) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("Evento", data);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
        navController.navigate(R.id.navigation_dashboard, bundle);
    }

    @Override
    public void onItemClickDelete(Fitnes data) {
        DialogConfirm(data);
    }

    @Override
    public void onItemClickShared(Fitnes data) {
        DialogShare(data);
    }

}