package ru.yugsys.vvvresearch.lconfig.model;



import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import org.greenrobot.greendao.query.Query;
import ru.yugsys.vvvresearch.lconfig.Services.GPScallback;
import ru.yugsys.vvvresearch.lconfig.Services.Helper;
import ru.yugsys.vvvresearch.lconfig.Services.NFCCommand;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataDevice;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DataRead;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.Device;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;
import ru.yugsys.vvvresearch.lconfig.model.Manager.EventManager;


import java.nio.ByteBuffer;
import java.util.*;


public class DataModel implements Model, GPScallback<Location> {
    @Override
    public void setCurrentDevice(Device dev) {
        this.currentDevice = dev;
    }

    @Override
    public Device getCurrentDevice() {
        return this.currentDevice;
    }

    /*-----------------------------------------------------------------------*/
    public DaoSession daoSession;

    @Override
    public void writeAuthData(String login, String password, String server) {

    }

    @Override
    public boolean testLoginConnection(String login, String password, String server) {
        return true;

    }

    @Override
    public void getGPSLocation() {
        eventManager.notifyOnGPS(mCurrentLocation);
    }

    public EventManager eventManager = new EventManager();
    private Location mCurrentLocation;
    public DataDevice currentDataDevice = new DataDevice();
    public Device currentDevice;

    /*------------------------------------------------------------------------*/
    /* Methods and Classes block*/
    @Override
    public void readNfcDev() {
//        new StartReadTask().execute(new Void[0]);

    }

    @Override
    public void saveDevice(Device device) {
        Log.d("BD","datamodel -> saveDevice ->" + device.type);
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        Log.d("BD","device.type = "+device.type);
        try {
            dataDao.insert(device);
        }
        catch (Exception e){
           Log.d("BD","exception: ",e);
        }
        Log.d("BD","Save into");
        eventManager.notifyOnDevDataChecked(true);
    }


    public DataModel(DaoSession daoSession) {
        this.daoSession = daoSession;

    }


    @Override
    public void setCurrentDataDevice(DataDevice d) {
        this.currentDataDevice = d;
    }

    @Override
    public EventManager getEventManager() {
        return eventManager;
    }


    @Override
    public DataDevice getCurrentDev() {
        return currentDataDevice;
    }

    @Override
    public void setCurrentDev(DataDevice dev) {
        this.currentDataDevice = dev;
    }

    @Override
    public void setSession(DaoSession s) {
        this.daoSession = s;
        Log.d("GPS","inside");
    }

    /**
     * загрузка объектов Device из БД в List
     */
    @Override
    public void loadAllDeviceData() {
        DeviceDao dataDao = this.daoSession.getDeviceDao();
        Query<Device> queue = dataDao.queryBuilder().orderAsc(DeviceDao.Properties.Id).build();
        eventManager.notifyOnDataReceive(queue.list());
    }

    @Override
    public void OnGPSdata(Location location) {
        this.mCurrentLocation = location;
        Log.d("GPS", "In Model: " + mCurrentLocation.toString());
//        eventManager.notifyOnGPS(mCurrentLocation);
    }





    public static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60)
                    | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }

    String getStringAchi(String s) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < s.length(); i += 2) {
            String str = s.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();

    }

    Device decodeByteList(String s) {
        String srt = s.replace(" ", "");
        Device d = new Device();
        d.type = getStringAchi(srt.substring(0, 10));
        Log.d("NFC", String.valueOf(s.length()));
        d.eui = srt.substring(12, 28);
        d.appeui = srt.substring(28, 44);
        d.appkey = srt.substring(44, 75);
        d.nwkid = srt.substring(75, 83);
        d.devadr = srt.substring(84, 92);
        d.nwkskey = srt.substring(92, 124);
        d.appskey = srt.substring(124, 156);
        Log.d("NFC", "TEST:" + srt.substring(156, 164));
        long lo = Long.parseLong(srt.substring(156, 164), 16);
        Float f = Float.intBitsToFloat((int) lo);
        Log.d("NFC", "TEST:" + f.toString() + " ");
        d.setLatitude(f);
        d.setLongitude(Float.intBitsToFloat((int) Long.parseLong(srt.substring(164, 172), 16)));
        // d.outType = srt.substring(86,90);
        //   d.kV = srt.substring(91,118);
        //  d.kI = srt.substring(119,120);
        return d;
    }
}
