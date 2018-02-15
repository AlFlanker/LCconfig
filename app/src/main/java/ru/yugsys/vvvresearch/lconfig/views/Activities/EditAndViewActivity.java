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
import ru.yugsys.vvvresearch.lconfig.model.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DevAdapter;
import ru.yugsys.vvvresearch.lconfig.model.Dev_Data;
import ru.yugsys.vvvresearch.lconfig.model.Dev_DataDao;
import ru.yugsys.vvvresearch.lconfig.views.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class EditAndViewActivity extends AppCompatActivity {

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

        DaoSession daoSession = ((App)getApplication()).getDaoSession();
        mDevDao = daoSession.getDev_DataDao();

        devsQuery = mDevDao.queryBuilder().orderAsc(Dev_DataDao.Properties.Name).build();
        updateDevs();

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
                addDev();

            }
        });
    }
        private void addDev() {
            String name = devNameEditText.getText().toString();
            String devgeo = devgeoEditText.getText().toString();
            devNameEditText.setText("");
            devgeoEditText.setText("");
            devNameEditText.clearFocus();
            devgeoEditText.clearFocus();

            if (name.trim().equals("") || devgeo.trim().equals("")) {
                Toast.makeText(EditAndViewActivity.this, "Нет данных", Toast.LENGTH_SHORT).show();
                return;
            }
            Dev_Data dev = new Dev_Data();
            dev.setName(name);
            dev.setGeo(Float.parseFloat(devgeo));
            mDevDao.insert(dev);
            updateDevs();
        }
    }

