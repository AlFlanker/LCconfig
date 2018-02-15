package ru.yugsys.vvvresearch.lconfig;

import android.app.Application;
import org.greenrobot.greendao.database.Database;
import ru.yugsys.vvvresearch.lconfig.model.DaoMaster;
import ru.yugsys.vvvresearch.lconfig.model.DaoSession;

public class App extends Application{
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "dev-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
