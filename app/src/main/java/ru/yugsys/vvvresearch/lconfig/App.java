package ru.yugsys.vvvresearch.lconfig;

import android.app.Application;
import org.greenrobot.greendao.database.Database;
import ru.yugsys.vvvresearch.lconfig.Services.GPSTracker;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoMaster;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataModel;
import ru.yugsys.vvvresearch.lconfig.model.Interfaces.Model;

public class App extends Application {
    private Model model;
    public String out;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devices-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        DataModel dataModel = new DataModel(daoSession);
        GPSTracker gpsTracker = GPSTracker.instance();
        gpsTracker.onChange(dataModel);
        model = dataModel;

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public Model getModel() {
        return model;
    }
}
