package ru.yugsys.vvvresearch.lconfig.model.DataEntity;

import android.location.Location;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import ru.yugsys.vvvresearch.lconfig.Services.CRC16;
import ru.yugsys.vvvresearch.lconfig.Services.Util;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;

@Entity(

        active = true,

        nameInDb = "DeviceTable",

        createInDb = true,

        generateConstructors = true,

        generateGettersSetters = true
)
public class DeviceEntry {
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
    private byte sendFrequency;
    @NotNull
    private Boolean isDeleted;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 502908221)
    private transient DeviceEntryDao myDao;

    @Generated(hash = 495081188)
    public DeviceEntry(Long id, @NotNull String type, boolean isOTTA, String eui,
            @NotNull String appeui, @NotNull String appkey, @NotNull String nwkid,
            @NotNull String devadr, @NotNull String nwkskey, @NotNull String appskey,
            double Latitude, double Longitude, @NotNull String outType, @NotNull String kV,
            @NotNull String kI, @NotNull String comment, @NotNull Date dateOfLastChange,
            byte sendFrequency, @NotNull Boolean isDeleted) {
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
        this.sendFrequency = sendFrequency;
        this.isDeleted = isDeleted;
    }

    @Generated(hash = 105489907)
    public DeviceEntry() {
    }

    //***********************************************************************/
	//* the function Convert Fields of Object to byte array
	// Alex Flanker
	//***********************************************************************/
    public static byte[] DeviceToByteArray(DeviceEntry dev) throws IllegalAccessException, IOException, NoSuchFieldException {
		Field field;
		StringBuilder sb = new StringBuilder();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] raw=new byte[8];
		field = DeviceEntry.class.getDeclaredField("type");
		field.setAccessible(true);
		String s = field.get(dev).toString();
		sb.append(s);
		while (sb.length() < 5) {
			sb.append((char) 0x00);
		}
		byteArrayOutputStream.write(sb.toString().getBytes());
		field = DeviceEntry.class.getDeclaredField("isOTTA");
		field.setAccessible(true);
		byteArrayOutputStream.write((field.get(dev).equals(Boolean.TRUE) ? 1 : 0));
		field = DeviceEntry.class.getDeclaredField("eui");
		field.setAccessible(true);
		byteArrayOutputStream.write(Util.hexToBytes(field.get(dev).toString()));
		field = DeviceEntry.class.getDeclaredField("appeui");
		field.setAccessible(true);
		//raw = new BigInteger(field.get(dev).toString(),16).toByteArray();
		byteArrayOutputStream.write(Util.hexToBytes(field.get(dev).toString()));
		field = DeviceEntry.class.getDeclaredField("appkey");
		field.setAccessible(true);
		byteArrayOutputStream.write(Util.hexToBytes(field.get(dev).toString()));
		field = DeviceEntry.class.getDeclaredField("nwkid");
		field.setAccessible(true);
		byteArrayOutputStream.write(Util.hexToBytes(field.get(dev).toString()));

		field = DeviceEntry.class.getDeclaredField("devadr");
		field.setAccessible(true);
		byteArrayOutputStream.write(Util.hexToBytes(field.get(dev).toString()));


        field = DeviceEntry.class.getDeclaredField("nwkskey");
		field.setAccessible(true);
		byteArrayOutputStream.write(Util.hexToBytes(field.get(dev).toString()));
		field = DeviceEntry.class.getDeclaredField("appskey");
		field.setAccessible(true);
		byteArrayOutputStream.write(Util.hexToBytes(field.get(dev).toString()));

		field = DeviceEntry.class.getDeclaredField("Latitude");
		field.setAccessible(true);
		float f = Float.parseFloat(String.valueOf(field.get(dev)));
		byteArrayOutputStream.write(ByteBuffer.allocate(4).order((ByteOrder.LITTLE_ENDIAN)).putFloat(f).array());


		field = DeviceEntry.class.getDeclaredField("Longitude");
		field.setAccessible(true);
		f = Float.parseFloat(String.valueOf(field.get(dev)));

		byteArrayOutputStream.write(ByteBuffer.allocate(4).order((ByteOrder.LITTLE_ENDIAN)).putFloat(f).array());

		field = DeviceEntry.class.getDeclaredField("outType");
		field.setAccessible(true);
		s = field.get(dev).toString();
		sb = new StringBuilder();
		sb.append(s);
		while (sb.length() < 5) {
			sb.append((char) 0x00);
		}
		byteArrayOutputStream.write(sb.toString().getBytes());
		field = DeviceEntry.class.getDeclaredField("kV");
		field.setAccessible(true);
		byteArrayOutputStream.write(Util.hexToBytes(field.get(dev).toString()));
		field = DeviceEntry.class.getDeclaredField("kI");
		field.setAccessible(true);
		byteArrayOutputStream.write(Util.hexToBytes(field.get(dev).toString()));
		CRC16 c = new CRC16();
		int cr = c.CRC16ArrayGet(0, byteArrayOutputStream.toByteArray());
		byte[] crb = ByteBuffer.allocate(4).putInt(cr).array();
		StringBuilder stb = new StringBuilder();
		for (Byte b : crb) {
			stb.append(String.format("%02x; ", b));
		}
		return byteArrayOutputStream.toByteArray();
	}

    //***********************************************************************/
    //* the function Convert raw byte[] to Device
    // Alex Flanker
    //***********************************************************************/
public static DeviceEntry decodeByteArrayToDevice(byte[] raw) throws IllegalAccessException, IOException {
        DeviceEntry device = new DeviceEntry();
        byte[] buf;
        StringBuilder stringBuilder = new StringBuilder();
        Field[] fields = DeviceEntry.class.getDeclaredFields();
        String[] names = new String[fields.length];
        int i = 0;
        for (Field field : fields) {
            names[i++] = field.getName();
        }

        for (Field field : fields) {
            //name = field.getName();
            if (field.getName().equals("type")) {
                buf = new byte[5];
                System.arraycopy(raw, 0, buf, 0, 5);
                device.setType(new String(buf, StandardCharsets.UTF_8).toUpperCase().trim());
//				field.set(device, new String(buf, StandardCharsets.UTF_8));
            }
            if (field.getName().equals("isOTTA")) {
                buf = new byte[1];
                boolean isotta;
                System.arraycopy(raw, 5, buf, 0, 1);
                if (buf[0] > 0) {
                    isotta = true;
                } else isotta = false;
                device.setIsOTTA(isotta);
                //field.set(device, isotta);

            }
            if (field.getName().equals("eui")) {
                buf = new byte[8];
                stringBuilder = new StringBuilder();
                System.arraycopy(raw, 6, buf, 0, 8);
                for (Byte b : buf) {
                    stringBuilder.append(String.format("%02x", b));
                }
                device.setEui(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
            if (field.getName().equals("appeui")) {
                buf = new byte[8];
                stringBuilder = new StringBuilder();
                System.arraycopy(raw, 14, buf, 0, 8);
                for (Byte b : buf) {
                    stringBuilder.append(String.format("%02x", b));
                }
                device.setAppeui(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
            if (field.getName().equals("appkey")) {
                buf = new byte[16];
                stringBuilder = new StringBuilder();
                System.arraycopy(raw, 22, buf, 0, 16);
                for (Byte b : buf) {
                    stringBuilder.append(String.format("%02x", b));
                }
                device.setAppkey(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
            if (field.getName().equals("nwkid")) {
                buf = new byte[4];
                stringBuilder = new StringBuilder();
                System.arraycopy(raw, 38, buf, 0, 4);
                for (Byte b : buf) {
                    stringBuilder.append(String.format("%02x", b));
                }
                device.setNwkid(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
            if (field.getName().equals("devadr")) {
                buf = new byte[4];
                System.arraycopy(raw, 42, buf, 0, 4);
                stringBuilder = new StringBuilder();
                for (Byte b : buf) {
                    stringBuilder.append(String.format("%02x", b));
                }
                device.setDevadr(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
            if (field.getName().equals("nwkskey")) {
                buf = new byte[16];
                System.arraycopy(raw, 46, buf, 0, 16);
                stringBuilder = new StringBuilder();
                for (Byte b : buf) {
                    stringBuilder.append(String.format("%02x", b));
                }
                device.setNwkskey(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
            if (field.getName().equals("appskey")) {
                buf = new byte[16];
                System.arraycopy(raw, 62, buf, 0, 16);
                stringBuilder = new StringBuilder();
                for (Byte b : buf) {
                    stringBuilder.append(String.format("%02x", b));
                }
                device.setAppskey(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
            if (field.getName().equals("Latitude")) {
                buf = new byte[4];
                System.arraycopy(raw, 78, buf, 0, 4);
//				field.set(device, ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getFloat());
                device.setLatitude(ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getFloat());
                //order(ByteOrder.LITTLE_ENDIAN)
            }
            if (field.getName().equals("Longitude")) {
                buf = new byte[4];
                System.arraycopy(raw, 82, buf, 0, 4);
                device.setLongitude(ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getFloat());
//				field.set(device, ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN).getFloat());
            }
            if (field.getName().equals("outType")) {    //refactor!
                buf = new byte[5];
                System.arraycopy(raw, 86, buf, 0, 5);
                stringBuilder = new StringBuilder();
                for (Byte b : buf) {
                    if (b == 0x00) {
                    } else
                        stringBuilder.append(new String(new byte[]{b}, StandardCharsets.UTF_8));
                }
                device.setOutType(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
            if (field.getName().equals("kV")) {
                buf = new byte[28];
                stringBuilder = new StringBuilder();
                System.arraycopy(raw, 91, buf, 0, 28);
                stringBuilder = new StringBuilder();
                for (Byte b : buf) {
                    stringBuilder.append(String.format("%02x", b));
                }
                device.setKV(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
            if (field.getName().equals("kI")) {
                buf = new byte[2];
                stringBuilder = new StringBuilder();
                System.arraycopy(raw, 119, buf, 0, 2);
                for (Byte b : buf) {
                    stringBuilder.append(String.format("%02x", b));
                }
                device.setKI(stringBuilder.toString().toUpperCase());
//				field.set(device, stringBuilder.toString());
            }
        }
        return device;

    }

    public static DeviceEntry generate(String EUI, Location location) {
		String mEUI;
		mEUI = EUI.replace(" ", "");
		mEUI = mEUI.substring(8);
		DeviceEntry newDev = new DeviceEntry();
        newDev.setType("LC503");
        newDev.setIsOTTA(Boolean.FALSE);
        newDev.setEui(EUI.replace(" ", "").trim());
        newDev.setAppeui("0000000000000001");
        newDev.setAppkey("2B7E151628AED2A6ABF7158809CF4F3C");
        newDev.setNwkid("00000000");

		//ConvertStringToHexBytesArray(String.valueOf(Integer.reverseBytes(Integer.parseInt(mEUI,16))));
        newDev.setDevadr(mEUI);
        newDev.setNwkskey("2B7E151628AED2A6ABF7158809CF4F3C");
        newDev.setAppskey("2B7E151628AED2A6ABF7158809CF4F3C");
        newDev.setLatitude(location != null ? location.getLatitude() : 0.0d);
        newDev.setLongitude(location != null ? location.getLongitude() : 0.0d);
        newDev.setOutType("PMW");
        newDev.setKV("EC03CE03D003E103E30304040E04B9096C09CE080F087407A6060506");
        newDev.setKI("991C");
        newDev.setDateOfLastChange(new Date());
        newDev.setComment("");
        newDev.setIsDeleted(false);
		return newDev;
//		newDev.setEui();
	}

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DeviceEntry other = (DeviceEntry) obj;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1970784809)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDeviceEntryDao() : null;
    }
    public void setDevadrMSBtoLSB(String devadr) {
        if (devadr != null) {
            StringBuilder devText = new StringBuilder();
            for (int i = 0; i < devadr.length(); i += 2) {
                devText.insert(0, devadr.substring(i, i + 2));
                this.devadr = devText.toString().toUpperCase();
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
            return devText.toString().toUpperCase();
        } else return null;
    }

    public byte getSendFrequency() {
        return this.sendFrequency;
    }

    public void setSendFrequency(byte sendFrequency) {
        this.sendFrequency = sendFrequency;
    }


}
