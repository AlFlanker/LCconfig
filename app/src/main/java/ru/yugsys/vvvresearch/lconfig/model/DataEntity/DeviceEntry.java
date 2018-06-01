package ru.yugsys.vvvresearch.lconfig.model.DataEntity;

import android.location.Location;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.*;
import ru.yugsys.vvvresearch.lconfig.Services.Util;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DaoSession;
import ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses.DeviceEntryDao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.lang.Object;
import java.util.Objects;


@Entity(

        active = true,

        nameInDb = "MainTable",

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
    private Boolean isDeleted;
    @NotNull
    private Boolean isSyncServer;


    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 502908221)
    private transient DeviceEntryDao myDao;

    @Generated(hash = 1743064902)
    public DeviceEntry(Long id, @NotNull String type, boolean isOTTA, String eui, @NotNull String appeui, @NotNull String appkey,
                       @NotNull String nwkid, @NotNull String devadr, @NotNull String nwkskey, @NotNull String appskey, double Latitude,
                       double Longitude, @NotNull String outType, @NotNull String kV, @NotNull String kI, @NotNull String comment,
                       @NotNull Date dateOfLastChange, @NotNull Boolean isDeleted, @NotNull Boolean isSyncServer) {
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
        this.isSyncServer = isSyncServer;
    }

    @Generated(hash = 105489907)
    public DeviceEntry() {
    }

    //***********************************************************************/
    //* the function Convert Fields of Object to byte array
    // Alex Flanker
    //***********************************************************************/
    public static byte[] Object2ByteArray(DeviceEntry dev) throws IllegalAccessException, IOException, NoSuchFieldException {
        Field field;
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] raw = new byte[8];
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
        int cr = DeviceEntry.CRC16.CRC16ArrayGet(0, byteArrayOutputStream.toByteArray());
        byte[] crb = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(cr).array(), 2, 4);
        byteArrayOutputStream.write(new byte[]{crb[1], crb[0]});
        //new block
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        field = DeviceEntry.class.getDeclaredField("comment");
        field.setAccessible(true);
        s = field.get(dev).toString();
        byte[] tmp = s.getBytes();
        baos.write(Arrays.copyOf(tmp, 25));
        field = DeviceEntry.class.getDeclaredField("dateOfLastChange");
        field.setAccessible(true);
        long ms = ((Date) field.get(dev)).getTime();
        byte[] t = ByteBuffer.allocate(8).putLong(ms).array();
        baos.write(t);

        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int amountOfByte = baos.size() + 2; // Without CRC bytes, but with check length!

        byte[] bytes = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(amountOfByte).array(), 2, 4);
        buf.write(new byte[]{bytes[1], bytes[0]});
        buf.write(baos.toByteArray());
        baos.close();
        int cr2 = DeviceEntry.CRC16.CRC16ArrayGet(0, buf.toByteArray());
        byte[] crb2 = Arrays.copyOfRange(ByteBuffer.allocate(4).putInt(cr2).array(), 2, 4);
        buf.write(new byte[]{crb2[1], crb2[0]});

        byteArrayOutputStream.write(buf.toByteArray());
        buf.close();

        return byteArrayOutputStream.toByteArray();
    }

    //***********************************************************************/
    //* the function Convert raw byte[] to Device
    // Alex Flanker
    //***********************************************************************/
    public static DeviceEntry decodeByteArrayToDevice(byte[] raw) throws IllegalAccessException, IOException, NoSuchFieldException {
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
        if (raw.length > 121) {
            //int amount = ByteBuffer.wrap(Arrays.copyOf(new byte[]{raw[122],raw[121]},4)).order(ByteOrder.LITTLE_ENDIAN).getInt();
            StringBuilder sb = new StringBuilder();
            for (Byte ch : Arrays.copyOfRange(raw, 123, (123 + 25))) {
                if (ch != 0x00) {
                    sb.append(new String(new byte[]{ch}, StandardCharsets.UTF_8));
                }
            }
            device.setComment(sb.toString());
            device.setDateOfLastChange(new Date(ByteBuffer.wrap(Arrays.copyOfRange(raw, (123 + 25), (123 + 25 + 8))).getLong()));
            //device.setIsSyncServer(false);
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
        newDev.setComment("test");
        newDev.setIsDeleted(false);
        newDev.setIsSyncServer(false);
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
        if (!Objects.equals(this.isSyncServer, other.isSyncServer))
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
                devText.insert(0, devadr.substring(i, i + 2));
            }
            return devText.toString().toUpperCase();
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

    public Boolean getIsSyncServer() {
        return this.isSyncServer;
    }

    public void setIsSyncServer(Boolean isSyncServer) {
        this.isSyncServer = isSyncServer;
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
    @Generated(hash = 1970784809)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDeviceEntryDao() : null;
    }


    public static class CRC16 {
        static final char[] CRCTablHigh = {
                0x00, 0x3D, 0x7A, 0x47, 0xF5, 0xC8, 0x8F, 0xB2,
                0xD6, 0xEB, 0xAC, 0x91, 0x23, 0x1E, 0x59, 0x64,
                0x91, 0xAC, 0xEB, 0xD6, 0x64, 0x59, 0x1E, 0x23,
                0x47, 0x7A, 0x3D, 0x00, 0xB2, 0x8F, 0xC8, 0xF5,
                0x1E, 0x23, 0x64, 0x59, 0xEB, 0xD6, 0x91, 0xAC,
                0xC8, 0xF5, 0xB2, 0x8F, 0x3D, 0x00, 0x47, 0x7A,
                0x8F, 0xB2, 0xF5, 0xC8, 0x7A, 0x47, 0x00, 0x3D,
                0x59, 0x64, 0x23, 0x1E, 0xAC, 0x91, 0xD6, 0xEB,
                0x3D, 0x00, 0x47, 0x7A, 0xC8, 0xF5, 0xB2, 0x8F,
                0xEB, 0xD6, 0x91, 0xAC, 0x1E, 0x23, 0x64, 0x59,
                0xAC, 0x91, 0xD6, 0xEB, 0x59, 0x64, 0x23, 0x1E,
                0x7A, 0x47, 0x00, 0x3D, 0x8F, 0xB2, 0xF5, 0xC8,
                0x23, 0x1E, 0x59, 0x64, 0xD6, 0xEB, 0xAC, 0x91,
                0xF5, 0xC8, 0x8F, 0xB2, 0x00, 0x3D, 0x7A, 0x47,
                0xB2, 0x8F, 0xC8, 0xF5, 0x47, 0x7A, 0x3D, 0x00,
                0x64, 0x59, 0x1E, 0x23, 0x91, 0xAC, 0xEB, 0xD6,
                0x7A, 0x47, 0x00, 0x3D, 0x8F, 0xB2, 0xF5, 0xC8,
                0xAC, 0x91, 0xD6, 0xEB, 0x59, 0x64, 0x23, 0x1E,
                0xEB, 0xD6, 0x91, 0xAC, 0x1E, 0x23, 0x64, 0x59,
                0x3D, 0x00, 0x47, 0x7A, 0xC8, 0xF5, 0xB2, 0x8F,
                0x64, 0x59, 0x1E, 0x23, 0x91, 0xAC, 0xEB, 0xD6,
                0xB2, 0x8F, 0xC8, 0xF5, 0x47, 0x7A, 0x3D, 0x00,
                0xF5, 0xC8, 0x8F, 0xB2, 0x00, 0x3D, 0x7A, 0x47,
                0x23, 0x1E, 0x59, 0x64, 0xD6, 0xEB, 0xAC, 0x91,
                0x47, 0x7A, 0x3D, 0x00, 0xB2, 0x8F, 0xC8, 0xF5,
                0x91, 0xAC, 0xEB, 0xD6, 0x64, 0x59, 0x1E, 0x23,
                0xD6, 0xEB, 0xAC, 0x91, 0x23, 0x1E, 0x59, 0x64,
                0x00, 0x3D, 0x7A, 0x47, 0xF5, 0xC8, 0x8F, 0xB2,
                0x59, 0x64, 0x23, 0x1E, 0xAC, 0x91, 0xD6, 0xEB,
                0x8F, 0xB2, 0xF5, 0xC8, 0x7A, 0x47, 0x00, 0x3D,
                0xC8, 0xF5, 0xB2, 0x8F, 0x3D, 0x00, 0x47, 0x7A,
                0x1E, 0x23, 0x64, 0x59, 0xEB, 0xD6, 0x91, 0xAC
        };
        static final char[] CRCTablLow = {
                0x00, 0x65, 0xCA, 0xAF, 0x94, 0xF1, 0x5E, 0x3B,
                0x4D, 0x28, 0x87, 0xE2, 0xD9, 0xBC, 0x13, 0x76,
                0xFF, 0x9A, 0x35, 0x50, 0x6B, 0x0E, 0xA1, 0xC4,
                0xB2, 0xD7, 0x78, 0x1D, 0x26, 0x43, 0xEC, 0x89,
                0x9B, 0xFE, 0x51, 0x34, 0x0F, 0x6A, 0xC5, 0xA0,
                0xD6, 0xB3, 0x1C, 0x79, 0x42, 0x27, 0x88, 0xED,
                0x64, 0x01, 0xAE, 0xCB, 0xF0, 0x95, 0x3A, 0x5F,
                0x29, 0x4C, 0xE3, 0x86, 0xBD, 0xD8, 0x77, 0x12,
                0x36, 0x53, 0xFC, 0x99, 0xA2, 0xC7, 0x68, 0x0D,
                0x7B, 0x1E, 0xB1, 0xD4, 0xEF, 0x8A, 0x25, 0x40,
                0xC9, 0xAC, 0x03, 0x66, 0x5D, 0x38, 0x97, 0xF2,
                0x84, 0xE1, 0x4E, 0x2B, 0x10, 0x75, 0xDA, 0xBF,
                0xAD, 0xC8, 0x67, 0x02, 0x39, 0x5C, 0xF3, 0x96,
                0xE0, 0x85, 0x2A, 0x4F, 0x74, 0x11, 0xBE, 0xDB,
                0x52, 0x37, 0x98, 0xFD, 0xC6, 0xA3, 0x0C, 0x69,
                0x1F, 0x7A, 0xD5, 0xB0, 0x8B, 0xEE, 0x41, 0x24,
                0x6C, 0x09, 0xA6, 0xC3, 0xF8, 0x9D, 0x32, 0x57,
                0x21, 0x44, 0xEB, 0x8E, 0xB5, 0xD0, 0x7F, 0x1A,
                0x93, 0xF6, 0x59, 0x3C, 0x07, 0x62, 0xCD, 0xA8,
                0xDE, 0xBB, 0x14, 0x71, 0x4A, 0x2F, 0x80, 0xE5,
                0xF7, 0x92, 0x3D, 0x58, 0x63, 0x06, 0xA9, 0xCC,
                0xBA, 0xDF, 0x70, 0x15, 0x2E, 0x4B, 0xE4, 0x81,
                0x08, 0x6D, 0xC2, 0xA7, 0x9C, 0xF9, 0x56, 0x33,
                0x45, 0x20, 0x8F, 0xEA, 0xD1, 0xB4, 0x1B, 0x7E,
                0x5A, 0x3F, 0x90, 0xF5, 0xCE, 0xAB, 0x04, 0x61,
                0x17, 0x72, 0xDD, 0xB8, 0x83, 0xE6, 0x49, 0x2C,
                0xA5, 0xC0, 0x6F, 0x0A, 0x31, 0x54, 0xFB, 0x9E,
                0xE8, 0x8D, 0x22, 0x47, 0x7C, 0x19, 0xB6, 0xD3,
                0xC1, 0xA4, 0x0B, 0x6E, 0x55, 0x30, 0x9F, 0xFA,
                0x8C, 0xE9, 0x46, 0x23, 0x18, 0x7D, 0xD2, 0xB7,
                0x3E, 0x5B, 0xF4, 0x91, 0xAA, 0xCF, 0x60, 0x05,
                0x73, 0x16, 0xB9, 0xDC, 0xE7, 0x82, 0x2D, 0x48
        };

        private static int CRC16Get(int beginCRC, int bt) {

            int tmp;
            beginCRC &= 0x0000FFFF;
            tmp = ((beginCRC >> 8) ^ (bt & (0x00ff)));
            beginCRC = ((beginCRC & 0x000000ff) | ((CRCTablHigh[tmp] ^ beginCRC & 0x0000FFFF) << 8));
            beginCRC = ((beginCRC & 0x0000ff00) | CRCTablLow[tmp]);
            tmp = beginCRC & 0x0000FFFF;
            return beginCRC;


        }

        public static int CRC16ArrayGet(int beginCRC, byte[] buffer) {
            char j;
            for (byte b : buffer) {
                beginCRC = CRC16Get(beginCRC, b);
            }
            return (~beginCRC) & 0x0000FFFF;
        }
    }
}



