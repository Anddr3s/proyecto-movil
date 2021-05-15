package com.example.gatobar.ui.gallery;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gatobar.R;
import com.example.gatobar.models.CnnSQLite;
import com.example.gatobar.models.Plato;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GalleryFragment extends Fragment {

    private EditText txtNombre;
    private EditText txtIngredientes;
    private EditText txtCosto;
    private Spinner spnTipos;
    private CnnSQLite cnn;
    private Button btnAdd;
    private Plato plato = null;
    private GalleryViewModel galleryViewModel;
    private View root;

    /*public  GalleryFragment(Plato plato) {
        this.plato = plato;
    }*/

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel = ViewModelProviders.of(this).get(GalleryViewModel.class);
        root = inflater.inflate(R.layout.fragment_gallery, container, false);

        initAll();

        if (Plato.plato != null) {
            this.plato = Plato.plato;
            Plato.plato = null;
            initUpdate();
        } else {
            initInsert();
        }
        return root;
    }

    public void initAll() {
        txtNombre = root.findViewById(R.id.txtNombre);
        txtIngredientes = root.findViewById(R.id.txtIngredientes);
        txtCosto = root.findViewById(R.id.txtCosto);
        spnTipos = root.findViewById(R.id.spnTipos);

        String[] options = {"-- Tipo --","Entrada","Postre", "Bebida", "Principal", "Otro"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_model_item, options);
        spnTipos.setAdapter(adapter);

        btnAdd = root.findViewById(R.id.btnAddPlato);
        cnn = new CnnSQLite(getContext());
    }

    public void initUpdate() {

        txtNombre.setText(plato.getNombre());
        txtIngredientes.setText(plato.getIngredientes());
        txtCosto.setText(String.valueOf(plato.getPrecio()));

        int index = this.plato.getCategoria_id();

        spnTipos.setSelection(index, true);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    if (cnn.updatePlato(new Plato(plato.getPlato_id(),
                            txtNombre.getText().toString(),
                            txtIngredientes.getText().toString(),
                            Double.parseDouble(txtCosto.getText().toString()),
                            spnTipos.getSelectedItemPosition()))) {

                        message(txtNombre.getText().toString(), true);
                    } else {
                        message(txtNombre.getText().toString(), false);
                    }
                }
            }
        });
    }

    public void initInsert() {

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validation()) {
                    if(cnn.insertPlato(new Plato(0,
                            txtNombre.getText().toString(),
                            txtIngredientes.getText().toString(),
                            Double.parseDouble(txtCosto.getText().toString()),
                            spnTipos.getSelectedItemPosition() ))) {

                        message(txtNombre.getText().toString(), true);
                    }else {
                        message(txtNombre.getText().toString(), false);
                    }
                    reset();
                }
            }
        });
    }

    public void reset() {
        txtNombre.setText("");
        spnTipos.setSelection(0);
        txtIngredientes.setText("");
        txtCosto.setText("");
    }

    public void message(String name, boolean status) {
        if (status) {
            String message = "!" + name + " insertado!";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        } else {
            String message = "!No insertado!";
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    public boolean validation() {
        if (spnTipos.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "¡Type not selected!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (txtNombre.getText().toString().trim().isEmpty() || txtIngredientes.getText().toString().trim().isEmpty()
                || txtCosto.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), "¡Fields Empty!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}