package com.example.gatobar.ui.slideshow;

import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.gatobar.R;
import com.example.gatobar.models.CnnSQLite;
import com.example.gatobar.models.Images;
import com.example.gatobar.services.Adapter;
import com.example.gatobar.services.ImagesService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SlideshowFragment extends Fragment {

    private ListView lvImgs;
    private ArrayList<Images> images;
    private ArrayAdapter arrayAdapter;
    private SlideshowViewModel slideshowViewModel;
    private View root;
    private CnnSQLite cnn;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel = ViewModelProviders.of(this).get(SlideshowViewModel.class);
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        init();

        return root;
    }

    public void init() {

        cnn = new CnnSQLite(getContext());
        images = new ArrayList<>();
        lvImgs = root.findViewById(R.id.lvImgs);
        arrayAdapter = new Adapter(getActivity(), images);
        lvImgs.setAdapter(arrayAdapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(getContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            getImgs();

        } else {

            Cursor data = cnn.selectImgs();
            data.moveToFirst();

            while(!data.isAfterLast()) {

                images.add(new Images(data.getString(1),
                        data.getString(2),
                        data.getInt(3),
                        data.getInt(4),
                        data.getString(5),
                        data.getString(6)));

                data.moveToNext();
            }
            arrayAdapter.notifyDataSetChanged();
        }
    }

    public void getImgs() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://picsum.photos").addConverterFactory(GsonConverterFactory.create()).build();
        ImagesService service = retrofit.create(ImagesService.class);
        Call<List<Images>> call = service.getImages();

        call.enqueue(new Callback<List<Images>>() {
            @Override
            public void onResponse(Call<List<Images>> call, Response<List<Images>> response) {

                assert response.body() != null;
                for (Images i:response.body()) {
                    images.add(new Images(i.getId(), i.getAuthor(), i.getWidth(), i.getHeight(), i.getUrl(), i.getDownload_url()));
                    loadToDataBase(i);
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onFailure(Call<List<Images>> call, Throwable t) {}
        });
    }

    public void loadToDataBase(Images images) {

        if (cnn.selectImg(images.getId()).getCount() > 0) {
            cnn.updateImg(images);
        } else {
            cnn.insertImages(images);
        }
    }
}