package ru.yugsys.vvvresearch.lconfig.views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.TextView;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import ru.yugsys.vvvresearch.lconfig.App;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresentable;
import ru.yugsys.vvvresearch.lconfig.presenters.MainPresenter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainViewable, View.OnClickListener {
    private ContentAdapter adapter;
    private RecyclerView recyclerView;
    private MainPresentable mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        recyclerView = findViewById(R.id.lc5_recycler_view);
        adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Connect presenter to main view
        mainPresenter = new MainPresenter(((App) getApplication()).getModel());
        mainPresenter.bind(this);
        mainPresenter.fireUpdateDataForView();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mainPresenter.fireUpdateDataForView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setContentForView(List<Device> devices) {
        adapter.setDevices(devices);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            Intent addEditIntent = new Intent(this, AddEditActivity.class);
            startActivity(addEditIntent);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView typeOfLC5;
        public TextView isOTAA;
        public TextView devEUI;
        public TextView appEUI;
        public TextView appKey;
        public TextView nwkID;
        public TextView devAdr;
        public TextView nwkSKey;
        public TextView appSKey;
        public TextView gps;
        public TextView outType;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.lc5_item_list, parent, false));
            typeOfLC5 = itemView.findViewById(R.id.lc5_type);
            isOTAA = itemView.findViewById(R.id.lc5_isOTAA);
            devEUI = itemView.findViewById(R.id.lc5_deveui);
            appEUI = itemView.findViewById(R.id.lc5_appEUI);
            appKey = itemView.findViewById(R.id.lc5_appKey);
            nwkID = itemView.findViewById(R.id.lc5_nwkID);
            devAdr = itemView.findViewById(R.id.lc5_devAdr);
            nwkSKey = itemView.findViewById(R.id.lc5_nwkSKey);
            appSKey = itemView.findViewById(R.id.lc5_appSKey);
            gps = itemView.findViewById(R.id.lc5_gps);
            outType = itemView.findViewById(R.id.lc5_out_type);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        List<Device> devices;

        public ContentAdapter(Context context) {

        }

        public void setDevices(List<Device> devices) {
            this.devices = devices;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.typeOfLC5.setText(devices.get(position).type);
            //holder.isOTAA.setText(devices.get(position).);
            holder.devEUI.setText(devices.get(position).getEui());
            holder.appEUI.setText(devices.get(position).appeui);
            holder.appKey.setText(devices.get(position).appkey);
            holder.nwkID.setText(devices.get(position).nwkid);
            holder.devAdr.setText(devices.get(position).devadr);
            holder.nwkSKey.setText(devices.get(position).nwkskey);
            holder.appSKey.setText(devices.get(position).appskey);
            holder.gps.setText(devices.get(position).Latitude + ", " + devices.get(position).Longitude);
            holder.outType.setText(devices.get(position).outType);

        }

        @Override
        public int getItemCount() {
            return devices.size();
        }
    }

}
