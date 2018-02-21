package ru.yugsys.vvvresearch.lconfig;

import android.app.Application;
import android.content.Context;
import org.greenrobot.greendao.database.Database;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoMaster;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;

public class App extends Application {
    private DaoSession daoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "devices-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }


}
