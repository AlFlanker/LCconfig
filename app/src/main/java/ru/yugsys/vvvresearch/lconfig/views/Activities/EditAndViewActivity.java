package ru.yugsys.vvvresearch.lconfig.views.Activities;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.*;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.BaseModel;
import ru.yugsys.vvvresearch.lconfig.presenters.Presentable.DataActivityPresenter;
import ru.yugsys.vvvresearch.lconfig.presenters.Presentable.ListPresenter;
import ru.yugsys.vvvresearch.lconfig.views.Interfaces.IEditView;
import ru.yugsys.vvvresearch.lconfig.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class EditAndViewActivity extends AppCompatActivity implements IEditView {
    @Override
    public void ShowError() {
        Toast.makeText(EditAndViewActivity.this, "Нет данных", Toast.LENGTH_SHORT).show();
        return;
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
//        baseModel = new DataModel();
//        baseModel.setSession(((App)getApplication()).getDaoSession());
        listPresenter.bindView(this);
        listPresenter.setModel(new DataModel(((App) getApplication()).getDaoSession()));
        listPresenter.getSession(((App) getApplication()).getDaoSession());
        listPresenter.loadData();
//        DaoSession daoSession = ((App)getApplication()).getDaoSession();
//        mDevDao = daoSession.getDev_DataDao();
//        devsQuery = mDevDao.queryBuilder().orderAsc(Dev_DataDao.Properties.Name).build();
//        updateDevs();

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

