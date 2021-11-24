package com.example.nhommobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhommobile.R;
import com.example.nhommobile.adapter.XeOtoAdapter;
import com.example.nhommobile.model.SanPhamMoi;
import com.example.nhommobile.retrofit.ApiBanHang;
import com.example.nhommobile.retrofit.RetrofitClient;
import com.example.nhommobile.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XeOtoActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    XeOtoAdapter adapterDt;
    List<SanPhamMoi> sanPhamMoiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xe_oto);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        loai = getIntent().getIntExtra("loai",1);
        ThamChieu();
        ActionToolBar();
        getData();
    }

    private void getData() {
        compositeDisposable.add(apiBanHang.getSanPham(page,loai)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if (sanPhamMoiModel.isSuccess()){
                        sanPhamMoiList = sanPhamMoiModel.getResult();
                        adapterDt = new XeOtoAdapter(getApplicationContext(),sanPhamMoiList);
                        recyclerView.setAdapter(adapterDt);
                    }

                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),"Khong ket noi sever", Toast.LENGTH_LONG).show();
                }
        ));
    }

    private void ActionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void ThamChieu() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycleview_dt);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoiList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}