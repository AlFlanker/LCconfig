package ru.yugsys.vvvresearch.lconfig.model.DataEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.NetDataDao;

@Entity(

        active = true,

        nameInDb = "NetData",

        createInDb = true,

        generateConstructors = true,

        generateGettersSetters = true
)
public class NetData {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String serviceName;
    @NotNull
    private String login;
    @NotNull
    private String password;
    @NotNull
    private String token;
    @NotNull
    private String address;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1798268359)
    private transient NetDataDao myDao;

    @Generated(hash = 276784716)
    public NetData(Long id, @NotNull String serviceName, @NotNull String login,
                   @NotNull String password, @NotNull String token,
                   @NotNull String address) {
        this.id = id;
        this.serviceName = serviceName;
        this.login = login;
        this.password = password;
        this.token = token;
        this.address = address;
    }

    @Generated(hash = 22099944)
    public NetData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1189446882)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNetDataDao() : null;
    }
}
