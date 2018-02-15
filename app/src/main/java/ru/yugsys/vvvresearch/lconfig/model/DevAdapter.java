package ru.yugsys.vvvresearch.lconfig.model;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ru.yugsys.vvvresearch.lconfig.R;
import ru.yugsys.vvvresearch.lconfig.views.Activities.EditAndViewActivity;

import java.util.List;

public class DevAdapter extends RecyclerView.Adapter<DevAdapter.DevHolder> {

    private List<Dev_Data> devs;
    private LayoutInflater mInflater;
    private AppCompatActivity mAppCompatActivity;

    public DevAdapter(List<Dev_Data> devs, LayoutInflater inflater, AppCompatActivity activity) {
        this.devs = devs;
        this.mInflater = inflater;
        this.mAppCompatActivity = activity;
    }

    @Override
    public DevHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.dev_item, parent, false);
        return new DevHolder(view);
    }

    @Override
    public void onBindViewHolder(DevHolder holder, int position) {
        Dev_Data dev = devs.get(position);
        holder.bindDev(dev);
    }

    @Override
    public int getItemCount() {
        return devs.size();
    }
    public void setDevs(List<Dev_Data> devs) {
        this.devs = devs;
        notifyDataSetChanged();
    }

    class DevHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView devTypename;
        private TextView devGeoLoc;
        private Dev_Data dev;


        public DevHolder(View itemView) {
            super(itemView);
            devTypename = (TextView) itemView.findViewById(R.id.dev_type_name);
            devGeoLoc = (TextView) itemView.findViewById(R.id.dev_geoloc);
            itemView.setOnClickListener(this);

        }

        public void bindDev(Dev_Data dev) {
            devTypename.setText(dev.getName());
            devGeoLoc.setText(Float.toString(dev.getGeo()));
        }

        @Override
        public void onClick(View view) {
            Dev_Data dev_data = devs.get(getLayoutPosition());
            Long devid = dev_data.getId();
            ((EditAndViewActivity)mAppCompatActivity).getDevDao().deleteByKey(devid);
            ((EditAndViewActivity)mAppCompatActivity).updateDevs();
        }
    }
}