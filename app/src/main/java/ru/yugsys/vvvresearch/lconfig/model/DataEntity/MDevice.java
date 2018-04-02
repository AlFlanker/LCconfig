package ru.yugsys.vvvresearch.lconfig.model.DataEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.MDeviceDao;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Entity(

        active = true,

        nameInDb = "MDevicesTable",

        createInDb = true,

        generateConstructors = true,

        generateGettersSetters = true
)
public class MDevice {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String type;
    @NotNull
    private boolean isOTTA;
    @Unique
    private String eui;
    @NotNull
    private String appeui;
    @NotNull
    private String appkey;
    @NotNull
    private String nwkid;
    @NotNull
    private String devadr;
    @NotNull
    private String nwkskey;
    @NotNull
    private String appskey;

    private double Latitude;
    private double Longitude;
    @NotNull
    private String outType;
    @NotNull
    private String kV;
    @NotNull
    private String kI;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 455904923)
    private transient MDeviceDao myDao;

    @Generated(hash = 761934981)
    public MDevice(Long id, @NotNull String type, boolean isOTTA, String eui,
                   @NotNull String appeui, @NotNull String appkey, @NotNull String nwkid,
                   @NotNull String devadr, @NotNull String nwkskey,
                   @NotNull String appskey, double Latitude, double Longitude,
                   @NotNull String outType, @NotNull String kV, @NotNull String kI) {
        this.id = id;
        this.type = type;
        this.isOTTA = isOTTA;
        this.eui = eui;
        this.appeui = appeui;
        this.appkey = appkey;
        this.nwkid = nwkid;
        this.devadr = devadr;
        this.nwkskey = nwkskey;
        this.appskey = appskey;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.outType = outType;
        this.kV = kV;
        this.kI = kI;
    }

    @Generated(hash = 267591362)
    public MDevice() {
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Device other = (Device) obj;
        if (!(type.equals(other.type)))
            return false;
        if (!(eui.equals(other.eui)))
            return false;
        if (!(appeui.equals(other.appeui)))
            return false;
        if (!(appkey.equals(other.appkey)))
            return false;
        if (!(nwkid.equals(other.nwkid)))
            return false;
        if (!(devadr.equals(other.devadr)))
            return false;
        if (!(nwkskey.equals(other.nwkskey)))
            return false;
        if (!(appskey.equals(other.appskey)))
            return false;
        if (!(outType.equals(other.outType)))
            return false;
        if (!(kV.equals(other.kV)))
            return false;
        if (!(kI.equals(other.kI)))
            return false;
        return true;

        /*lat and lon don't use*/
    }

    @Override
    public int hashCode() {
        final int tmp = 37;
        int result = 1;
        result = result * tmp + this.type.hashCode();
        result = result * tmp + this.eui.hashCode();
        result = result * tmp + this.appeui.hashCode();
        result = result * tmp + this.appkey.hashCode();
        result = result * tmp + this.nwkid.hashCode();
        result = result * tmp + this.devadr.hashCode();
        result = result * tmp + this.nwkskey.hashCode();
        result = result * tmp + this.appskey.hashCode();
        result = result * tmp + this.outType.hashCode();
        result = result * tmp + +this.kV.hashCode();
        result = result * tmp + this.kI.hashCode();
        return result;
    }

    public void setDevadrMSBtoLSB(String devadr) {
        if (devadr != null) {
            StringBuilder devText = new StringBuilder();
            for (int i = 0; i < devadr.length(); i += 2) {
                devText.insert(0, devadr.substring(i, i + 2));
                this.devadr = devText.toString();
            }
        } else this.devadr = null;
    }

    public String getDevadrMSBtoLSB() {
        if (devadr != null) {
            StringBuilder devText = new StringBuilder();
            int length = devadr.length();
            for (int i = 0; i < length; i += 2) {
                devText = devText.insert(0, devadr.substring(i, i + 2));
            }
            return devText.toString();
        } else return null;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getIsOTTA() {
        return this.isOTTA;
    }

    public void setIsOTTA(boolean isOTTA) {
        this.isOTTA = isOTTA;
    }

    public String getEui() {
        return this.eui;
    }

    public void setEui(String eui) {
        this.eui = eui;
    }

    public String getAppeui() {
        return this.appeui;
    }

    public void setAppeui(String appeui) {
        this.appeui = appeui;
    }

    public String getAppkey() {
        return this.appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getNwkid() {
        return this.nwkid;
    }

    public void setNwkid(String nwkid) {
        this.nwkid = nwkid;
    }

    public String getDevadr() {
        return this.devadr;
    }

    public void setDevadr(String devadr) {
        this.devadr = devadr;
    }

    public String getNwkskey() {
        return this.nwkskey;
    }

    public void setNwkskey(String nwkskey) {
        this.nwkskey = nwkskey;
    }

    public String getAppskey() {
        return this.appskey;
    }

    public void setAppskey(String appskey) {
        this.appskey = appskey;
    }

    public double getLatitude() {
        return this.Latitude;
    }

    public void setLatitude(double Latitude) {
        this.Latitude = Latitude;
    }

    public double getLongitude() {
        return this.Longitude;
    }

    public void setLongitude(double Longitude) {
        this.Longitude = Longitude;
    }

    public String getOutType() {
        return this.outType;
    }

    public void setOutType(String outType) {
        this.outType = outType;
    }

    public String getKV() {
        return this.kV;
    }

    public void setKV(String kV) {
        this.kV = kV;
    }

    public String getKI() {
        return this.kI;
    }

    public void setKI(String kI) {
        this.kI = kI;
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
    @Generated(hash = 1452448850)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMDeviceDao() : null;
    }

    public void copyFields(MDevice mdev) {
        Field[] fields = MDevice.class.getFields();
        for (Field field : fields) {
            if (!Modifier.isTransient(field.getModifiers())) {
                try {
                    field.set(this, field.get(mdev));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
