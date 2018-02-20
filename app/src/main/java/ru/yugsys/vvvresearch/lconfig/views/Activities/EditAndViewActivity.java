package ru.yugsys.vvvresearch.lconfig.views.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.Services.ConnectivityReceiver;
import ru.yugsys.vvvresearch.lconfig.model.*;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.BaseModel;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;
import ru.yugsys.vvvresearch.lconfig.presenters.Presentable.DataActivityPresenter;
import ru.yugsys.vvvresearch.lconfig.presenters.Presentable.ListPresenter;
import ru.yugsys.vvvresearch.lconfig.views.Interfaces.IEditView;

import java.util.ArrayList;
import java.util.List;

public class EditAndViewActivity extends AppCompatActivity implements IEditView, ConnectivityReceiver.ConnectivityReceiverListener {
    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        final Context context = getApplicationContext();
        if (isConnected) {
            Toast.makeText(getApplicationContext(),
                    "Соединение есть", Toast.LENGTH_LONG).show();
        } else {
//            Toast.makeText(getApplicationContext(),
//                    "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
            AlertDialog.Builder aDialog = new AlertDialog.Builder(this);
            aDialog.setTitle("Нет сети!");
            aDialog.setMessage("Необходимо включить WiFi или GPRS");
            aDialog.setPositiveButton("настройка", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS));
                }
            });
            aDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            aDialog.show();
        }
    }

    @Override
    public void ShowError() {
        Toast.makeText(EditAndViewActivity.this, "Нет данных", Toast.LENGTH_SHORT).show();
        //return;
    }

    @Override
    public void addButtonClick() {

    }

    @Override
    public void bindPresenter(DataActivityPresenter presenter) {
        this.listPresenter = presenter;
    }

    @Override
    public void unbindPresenter() {
        this.listPresenter = null;
    }

    @Override
    public void update(List<Dev_Data> list) {
        this.devs = list;
        adapter.setDevs(devs);
    }

    protected ListPresenter listPresenter = new DataActivityPresenter();
    protected BaseModel baseModel;
    private FloatingActionButton mFab;
    private EditText devNameEditText;
    private EditText devgeoEditText;
    private RecyclerView mRecyclerView;
    private DevAdapter adapter;
    private List<Dev_Data> devs;

    public Dev_DataDao getDevDao() {
        return mDevDao;
    }

    private Dev_DataDao mDevDao;
    private Query<Dev_Data> devsQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_and_view);
        setupViews();
        DataModel dataModel = new DataModel(((App) getApplication()).getDaoSession());
        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnDataReceive, listPresenter);
        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnDevDataChecked, listPresenter);
        dataModel.eventManager.subscribe(EventManager.TypeEvent.OnNFCconnected, listPresenter);
        listPresenter.bindView(this);
        listPresenter.setModel(dataModel);
        listPresenter.loadData();
        App.getInstance().setConnectivityListener(this);
        Log.d("MyTag", "App.getInstance(set(this))from Oncreate");

    }

    public void updateDevs() {
        devs = devsQuery.list();
        adapter.setDevs(devs);
    }

    private void setupViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        devNameEditText = (EditText) findViewById(R.id.dev_type);
        devgeoEditText = (EditText) findViewById(R.id.dev_geo);

        devs = new ArrayList<>();
        adapter = new DevAdapter(devs, getLayoutInflater(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = devNameEditText.getText().toString();
                String devgeo = devgeoEditText.getText().toString();
                listPresenter.AddDev(name, devgeo);
                devNameEditText.setText("");
                devgeoEditText.setText("");
                devNameEditText.clearFocus();
                devgeoEditText.clearFocus();

            }
        });
    }

    }

