package ru.yugsys.vvvresearch.lconfig.model.DataEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceDao;

/**
 * @author Alex Flanker
 * после внесения ЛЮБЫХ изменний в класс инкрементировать scheme в build.gradle !!!
 */
@Entity(

        active = true,

        nameInDb = "DevicesTable",

        createInDb = true,

        generateConstructors = true,

        generateGettersSetters = true
)
public class Device {

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
        result = result*tmp + this.type.hashCode();
        result = result*tmp + this.eui.hashCode();
        result = result*tmp + this.appeui.hashCode();
        result = result*tmp + this.appkey.hashCode();
        result = result*tmp + this.nwkid.hashCode();
        result = result*tmp + this.devadr.hashCode();
        result = result*tmp + this.nwkskey.hashCode();
        result = result*tmp + this.appskey.hashCode();
        result = result*tmp + this.outType.hashCode();
        result = result*tmp + + this.kV.hashCode();
        result = result*tmp + this.kI.hashCode();
        return result;
    }

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @Id(autoincrement = true)
    public Long id;
    @NotNull
    public String type;
    public String isOTAA;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public String eui;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public String appeui;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public String appkey;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public String nwkid;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public String devadr;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public String nwkskey;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public String appskey;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public float Latitude;
    @NotNull
    public float Longitude;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public String outType;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     * @ordered
     */
    @NotNull
    public String kV;

    /**
     * <!-- begin-user-doc -->
     * <!--  end-user-doc  -->
     *
     * @generated
     */
    @NotNull
    public String kI;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 371273952)
    private transient DeviceDao myDao;

    @Generated(hash = 1944223767)
    public Device(Long id, @NotNull String type, String isOTAA, @NotNull String eui,
                  @NotNull String appeui, @NotNull String appkey, @NotNull String nwkid,
                  @NotNull String devadr, @NotNull String nwkskey, @NotNull String appskey,
                  float Latitude, float Longitude, @NotNull String outType, @NotNull String kV,
                  @NotNull String kI) {
        this.id = id;
        this.type = type;
        this.isOTAA = isOTAA;
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

    @Generated(hash = 1469582394)
    public Device() {
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

    public void setLatitude(float Latitude) {
        this.Latitude = Latitude;
    }

    public double getLongitude() {
        return this.Longitude;
    }

    public void setLongitude(float Longitude) {
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
    @Generated(hash = 1755220927)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDeviceDao() : null;
    }

    public String getIsOTAA() {
        return this.isOTAA;
    }

    public void setIsOTAA(String isOTAA) {
        this.isOTAA = isOTAA;
    }


}
