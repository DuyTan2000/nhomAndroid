package com.example.nhommobile.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nhommobile.R;
import com.example.nhommobile.adapter.LoaiXeAdapter;
import com.example.nhommobile.adapter.SanPhamMoiAdapter;
import com.example.nhommobile.model.LoaiXe;
import com.example.nhommobile.model.SanPhamMoi;
import com.example.nhommobile.retrofit.ApiBanHang;
import com.example.nhommobile.retrofit.RetrofitClient;
import com.example.nhommobile.utils.Utils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerViewManhinhchinh;
    NavigationView navigationView;
    ListView listViewManhinhchinh;
    DrawerLayout drawerLayout;
    LoaiXeAdapter loaiXeAdapter;
    List<LoaiXe> mangloaixe;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);

        Thamchieumain();

        ActionBar();
        if(isConnected(this)){

            ActionViewFlipper();
            getLoaiPtXe();
            getSpMoi();
            getEventClick();

        }else{
            Toast.makeText(getApplicationContext(),"khong co internet",Toast.LENGTH_LONG).show();
        }

    }

    private void getEventClick() {
        listViewManhinhchinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent xeOto = new Intent(getApplicationContext(),XeOtoActivity.class);
                        xeOto.putExtra("loai",1);
                        startActivity(xeOto);
                        break;
                    case 2:
                        Intent xeMay = new Intent(getApplicationContext(),XeOtoActivity.class);
                        xeMay.putExtra("loai",2);
                        startActivity(xeMay);
                        break;

                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                sanPhamMoiModel -> {
                    if (sanPhamMoiModel.isSuccess()){
                        mangSpMoi = sanPhamMoiModel.getResult();
                        spAdapter = new SanPhamMoiAdapter(getApplicationContext(),mangSpMoi);
                        recyclerViewManhinhchinh.setAdapter(spAdapter);


                    }
                },
                throwable -> {
                    Toast.makeText(getApplicationContext(),"Khong ket noi duoc voi server"+ throwable.getMessage(),Toast.LENGTH_LONG).show();
                }
        ));
    }

    private void getLoaiPtXe() {

        compositeDisposable.add(apiBanHang.getLoaiSp()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                loaiXeModel -> {
                    if (loaiXeModel.isSuccess()){
                        mangloaixe = loaiXeModel.getResult();
                        loaiXeAdapter = new LoaiXeAdapter(getApplicationContext(),mangloaixe);
                        listViewManhinhchinh.setAdapter(loaiXeAdapter);
                    }
                }
        ));

    }

    private void ActionViewFlipper() {
        List<String> bannerquangcao = new ArrayList<>();
        bannerquangcao.add("https://static1.cafeauto.vn/cafeautoData/upload/tintuc/thitruong/2014/05/tuan-02/1-ceog-1399536221.jpg");
        bannerquangcao.add("https://www.mitsubishi-satsco.com.vn/w/wp-content/uploads/2017/10/Mitsubishi-tan-binh-khuyen-mai.jpg");
        bannerquangcao.add("https://www.homecredit.vn/Vendor/kcfinder/upload/images/Banner-Yamaha-HonDa-VinFast-1--986-x-450-px-(Final).png");
        for(int i=0;i<bannerquangcao.size();i++){
            ImageView imgView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(bannerquangcao.get(i)).into(imgView);
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imgView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(slide_in);
        viewFlipper.setInAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }

    private void Thamchieumain() {
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewlippper);
        recyclerViewManhinhchinh = findViewById(R.id.recycleview);

        navigationView = findViewById(R.id.navigationview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewManhinhchinh.setLayoutManager(layoutManager);
        recyclerViewManhinhchinh.setHasFixedSize(true);

        listViewManhinhchinh = findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = findViewById(R.id.drawerlayout);

        mangloaixe = new ArrayList<>();
        mangSpMoi = new ArrayList<>();

        if (Utils.manggiohang == null){
            Utils.manggiohang = new ArrayList<>();

        }


    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected()) ){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}