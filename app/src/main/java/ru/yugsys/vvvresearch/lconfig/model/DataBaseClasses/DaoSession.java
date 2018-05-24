package ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.DeviceEntry;
import ru.yugsys.vvvresearch.lconfig.model.DataEntity.NetData;

import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.NetDataDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig deviceEntryDaoConfig;
    private final DaoConfig netDataDaoConfig;

    private final DeviceEntryDao deviceEntryDao;
    private final NetDataDao netDataDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        deviceEntryDaoConfig = daoConfigMap.get(DeviceEntryDao.class).clone();
        deviceEntryDaoConfig.initIdentityScope(type);

        netDataDaoConfig = daoConfigMap.get(NetDataDao.class).clone();
        netDataDaoConfig.initIdentityScope(type);

        deviceEntryDao = new DeviceEntryDao(deviceEntryDaoConfig, this);
        netDataDao = new NetDataDao(netDataDaoConfig, this);

        registerDao(DeviceEntry.class, deviceEntryDao);
        registerDao(NetData.class, netDataDao);
    }
    
    public void clear() {
        deviceEntryDaoConfig.clearIdentityScope();
        netDataDaoConfig.clearIdentityScope();
    }

    public DeviceEntryDao getDeviceEntryDao() {
        return deviceEntryDao;
    }

    public NetDataDao getNetDataDao() {
        return netDataDao;
    }

}
