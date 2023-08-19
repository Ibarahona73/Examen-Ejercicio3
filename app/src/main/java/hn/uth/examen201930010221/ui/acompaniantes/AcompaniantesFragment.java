package hn.uth.examen201930010221.ui.acompaniantes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.List;

import hn.uth.examen201930010221.R;
import hn.uth.examen201930010221.databinding.FragmentAcompaniantesBinding;
import hn.uth.examen201930010221.entity.Contacto;
import hn.uth.examen201930010221.entity.Contacto2;

public class AcompaniantesFragment extends Fragment{

    private FragmentAcompaniantesBinding binding;

    private ContactosAdapter adapterTodos;
    private ContactosAdapter adapterFrecuentes;

    AcompaniantesViewModel aContactos;

    private int op = 1;

    private static final int PERMISSION_REQUEST_READ_CONTACT = 100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAcompaniantesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        aContactos = new ViewModelProvider(this).get(AcompaniantesViewModel.class);

        adapterTodos = new ContactosAdapter(new ArrayList<>());
        adapterFrecuentes = new ContactosAdapter(new ArrayList<>());

        actualizarInformacion();
        binding.bFabAction.setOnClickListener(v -> {
            if (adapterTodos.getItemCount() > 0) {
                aContactos.deleteAll();
            } else {
                binding.bFabAction.setVisibility(View.GONE);

                binding.tvLoading.setText(getString(R.string.importando_contactos));
                binding.tvLoading.setVisibility(View.VISIBLE);
                solicitudPermisoContactos(this.getContext());
            }
        });


        binding.rGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == binding.btnTodos.getId()) {
                binding.rvAcompaniantes.setVisibility(View.VISIBLE);
                binding.bFabAction.setVisibility(View.VISIBLE);
                if (adapterTodos.getItemCount() != 0) {
                    binding.tvLoading.setVisibility(View.GONE);
                } else {
                    binding.tvLoading.setVisibility(View.VISIBLE);
                }
            } else {
                binding.rvAcompaniantes.setVisibility(View.GONE);
                binding.bFabAction.setVisibility(View.GONE);
                binding.tvLoading.setVisibility(View.GONE);
                if (adapterFrecuentes.getItemCount() != 0) {
                } else {

                }
            }


        });

        setupRecyclerView();
        return root;
    }


    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this.getContext());
        binding.rvAcompaniantes.setLayoutManager(linearLayoutManager);
        binding.rvAcompaniantes.setAdapter(adapterTodos);

    }

    private void solicitudPermisoContactos(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_READ_CONTACT);
        } else {
            getContacts(context);
        }
    }

    @SuppressLint("Range")
    private void getContacts(Context context) {
        List<Contacto2> contactos = new ArrayList<>();

        Contacto2 cOld = new Contacto2("", "", "");
        AcompaniantesViewModel aContactos = new ViewModelProvider(this).get(AcompaniantesViewModel.class);

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " > 0",
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phone = phone.replaceAll(" +|-|\\(|\\)", "");
                String correo = "";

                @SuppressLint("Range") Cursor cursorCorreo = resolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))},
                        null
                );

                if (cursorCorreo != null && cursorCorreo.moveToNext()) {
                    correo = cursorCorreo.getString(cursorCorreo.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
                    cursorCorreo.close();
                }
                correo = correo.trim();

                Contacto2 nuevo2 = new Contacto2(name, phone, correo);

                String n1, n2, p1, p2, e1, e2;
                n1 = cOld.getName();
                n2 = nuevo2.getName();
                p1 = cOld.getPhone();
                p2 = nuevo2.getPhone();
                e1 = cOld.getEmail();
                e2 = nuevo2.getEmail();

                if (!n1.equals(n2) || !p1.equals(p2) || !e1.equals(e2)) {
                    cOld = nuevo2;
                    contactos.add(nuevo2);
                    Contacto nuevo = new Contacto(name, phone, correo, 0);
                    aContactos.insert(nuevo);
                }

            }

            cursor.close();
        }

        binding.bFabAction.setVisibility(View.VISIBLE);

    }

    private void actualizarInformacion() {
          aContactos.getDataSet().observe(getViewLifecycleOwner(), contactos -> {
            adapterTodos.setItems(contactos);
            if (binding.rGroup.getCheckedRadioButtonId() == binding.btnTodos.getId()) {
                if (adapterTodos.getItemCount() > 0) {
                    if (op == 1) {
                        op = 2;
                        binding.tvLoading.setVisibility(View.GONE);
                        Drawable dIcon = getResources().getDrawable(R.drawable.ic_delete_24dp);
                        binding.bFabAction.setIcon(dIcon);
                        binding.bFabAction.setText(R.string.eliminar_contactos);
                    }
                } else {
                    if (op == 2) {
                        op = 1;
                        Drawable dIcon = getResources().getDrawable(R.drawable.ic_contacts);
                        binding.bFabAction.setIcon(dIcon);
                        binding.tvLoading.setText(getString(R.string.sin_contactos));
                        binding.bFabAction.setText(R.string.importar_contactos);

                        binding.tvLoading.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                binding.bFabAction.setVisibility(View.GONE);
            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_READ_CONTACT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContacts(requireContext());
                if (adapterTodos.getItemCount() == 0) {
                    binding.tvLoading.setVisibility(View.GONE);
                }
            } else {
                if (adapterTodos.getItemCount() == 0) {
                    binding.tvLoading.setText(getString(R.string.sin_contactos));
                    binding.tvLoading.setVisibility(View.VISIBLE);
                }
                Snackbar.make(binding.bFabAction, R.string.permiso_denegado, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}