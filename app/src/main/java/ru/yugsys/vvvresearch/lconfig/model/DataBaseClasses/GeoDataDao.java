package ru.yugsys.vvvresearch.lconfig.model.DataBaseClasses;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import ru.yugsys.vvvresearch.lconfig.model.DataEntity.GeoData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "GeoTable".
 */
public class GeoDataDao extends AbstractDao<GeoData, Long> {

    public static final String TABLENAME = "GeoTable";

    /**
     * Properties of entity GeoData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Eui = new Property(1, String.class, "eui", false, "EUI");
        public final static Property County = new Property(2, String.class, "county", false, "COUNTY");
        public final static Property City = new Property(3, String.class, "city", false, "CITY");
        public final static Property Address = new Property(4, String.class, "address", false, "ADDRESS");
        public final static Property ChangeDate = new Property(5, java.util.Date.class, "changeDate", false, "CHANGE_DATE");
    }

    private DaoSession daoSession;


    public GeoDataDao(DaoConfig config) {
        super(config);
    }

    public GeoDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GeoTable\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"EUI\" TEXT NOT NULL UNIQUE ," + // 1: eui
                "\"COUNTY\" TEXT," + // 2: county
                "\"CITY\" TEXT," + // 3: city
                "\"ADDRESS\" TEXT," + // 4: address
                "\"CHANGE_DATE\" INTEGER NOT NULL );"); // 5: changeDate
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GeoTable\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GeoData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getEui());
 
        String county = entity.getCounty();
        if (county != null) {
            stmt.bindString(3, county);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(4, city);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(5, address);
        }
        stmt.bindLong(6, entity.getChangeDate().getTime());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GeoData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getEui());
 
        String county = entity.getCounty();
        if (county != null) {
            stmt.bindString(3, county);
        }
 
        String city = entity.getCity();
        if (city != null) {
            stmt.bindString(4, city);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(5, address);
        }
        stmt.bindLong(6, entity.getChangeDate().getTime());
    }

    @Override
    protected final void attachEntity(GeoData entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GeoData readEntity(Cursor cursor, int offset) {
        GeoData entity = new GeoData( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.getString(offset + 1), // eui
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // county
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // city
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // address
                new java.util.Date(cursor.getLong(offset + 5)) // changeDate
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GeoData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEui(cursor.getString(offset + 1));
        entity.setCounty(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCity(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAddress(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setChangeDate(new java.util.Date(cursor.getLong(offset + 5)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GeoData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GeoData entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GeoData entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
