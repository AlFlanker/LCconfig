package ru.yugsys.vvvresearch.lconfig.model.DataEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import java.util.Objects;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DevEntryDao;

/*25.04.2018*/

@Entity(

        active = true,

        nameInDb = "DeviceEntries",

        createInDb = true,

        generateConstructors = true,

        generateGettersSetters = true
)
public class DevEntry {
    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String type;
    @NotNull
    private boolean isOTTA;
    @NotNull
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
    @NotNull
    private double Latitude;
    private double Longitude;
    @NotNull
    private String outType;
    @NotNull
    private String kV;
    @NotNull
    private String kI;
    @NotNull
    private String comment;
    @NotNull
    private Date dateOfLastChange;
    @NotNull
    private Boolean isDeleted;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2137398891)
    private transient DevEntryDao myDao;

    @Generated(hash = 739654831)
    public DevEntry(Long id, @NotNull String type, boolean isOTTA,
                    @NotNull String eui, @NotNull String appeui, @NotNull String appkey,
                    @NotNull String nwkid, @NotNull String devadr, @NotNull String nwkskey,
                    @NotNull String appskey, double Latitude, double Longitude,
                    @NotNull String outType, @NotNull String kV, @NotNull String kI,
                    @NotNull String comment, @NotNull Date dateOfLastChange,
                    @NotNull Boolean isDeleted) {
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
        this.comment = comment;
        this.dateOfLastChange = dateOfLastChange;
        this.isDeleted = isDeleted;
    }

    @Generated(hash = 1245094749)
    public DevEntry() {
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DevEntry other = (DevEntry) obj;
        if (!Objects.equals(this.type, other.type))
            return false;
        if (!Objects.equals(this.eui, other.eui))
            return false;
        if (!Objects.equals(this.appeui, other.appeui))
            return false;
        if (!Objects.equals(this.appkey, other.appkey))
            return false;
        if (!Objects.equals(this.nwkid, other.nwkid))
            return false;
        if (!Objects.equals(this.devadr, other.devadr))
            return false;
        if (!Objects.equals(this.nwkskey, other.nwkskey))
            return false;
        if (!Objects.equals(this.appskey, other.appskey))
            return false;
        if (!Objects.equals(this.outType, other.outType))
            return false;
        if (!Objects.equals(this.kV, other.kV))
            return false;
        if (!Objects.equals(this.kI, other.kI))
            return false;
        if (!Objects.equals(this.kI, other.kI))
            return false;
        if (!Objects.equals(this.comment, other.comment))
            return false;
        if (!Objects.equals(this.dateOfLastChange, other.dateOfLastChange))
            return false;
        if (!Objects.equals(this.isDeleted, other.isDeleted))
            return false;
        if (!Objects.equals(this.isOTTA, other.isOTTA))
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
        result = result * tmp + this.comment.hashCode();
        result = result * tmp + this.dateOfLastChange.hashCode();
        return (result ^ (result >>> 16));
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

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDateOfLastChange() {
        return this.dateOfLastChange;
    }

    public void setDateOfLastChange(Date dateOfLastChange) {
        this.dateOfLastChange = dateOfLastChange;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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
    @Generated(hash = 668518250)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDevEntryDao() : null;
    }
}
