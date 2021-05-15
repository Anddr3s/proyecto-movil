package com.example.gatobar.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.gatobar.models.CnnSQLite;
import com.example.gatobar.R;
import com.example.gatobar.models.Plato;
import com.example.gatobar.ui.gallery.GalleryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ListView lvList;
    private SearchView search;
    private CnnSQLite cnn;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        init();

        return root;
    }

    public void init() {

        lvList = root.findViewById(R.id.lvList);
        search = root.findViewById(R.id.search);
        cnn = new CnnSQLite(getContext());

        //FloatingActionButton fbtnAdd = root.findViewById(R.id.fbtnAdd);

        setAdapter(cnn.selectPlatos());
        registerForContextMenu(lvList);

        /*
        fbtnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent newView = new Intent(getApplication(), AddPlatos.class);
                startActivity(newView);
            }
        });*/

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setAdapter(cnn.selectPlato(newText));
                return true;
            }
        });
    }

    public void setAdapter(Cursor dates) {
        String[] columns = new String[]{"plato_id","nombre","categoria_id","precio"};
        int[] components = new int[]{R.id.codigo, R.id.nombre, R.id.tipo, R.id.costo};

        CursorAdapter lvAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_plato, dates, columns, components, 0);
        lvList.setAdapter(lvAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = this.getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Cursor dates = (Cursor) lvList.getItemAtPosition(info.position);
        int codigo = dates.getInt(1);
        String nombre = dates.getString(2);

        if (item.getItemId() == R.id.menuModificar) {

            Plato plato = new Plato(codigo, nombre, dates.getString(3), dates.getDouble(4), dates.getInt(5));
            Plato.plato = plato;

            Fragment nuevoFragmento = new GalleryFragment();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(root.getId(), nuevoFragmento);
            transaction.addToBackStack(null);
            transaction.commit();

            return true;
        } else if (item.getItemId() == R.id.menuEliminar) {

            if (cnn.deletePlato(codigo)) {
                message(nombre, true);
                setAdapter(cnn.selectPlatos());
            } else message(null, false);

            return true;
        }

        return super.onContextItemSelected(item);
    }

    public void message(String name, boolean status) {
        if (status) {
            String message = name + " was deleted!";
            Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
        } else {
            String message =  "Delete failed!";
            Toast.makeText(this.getContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}