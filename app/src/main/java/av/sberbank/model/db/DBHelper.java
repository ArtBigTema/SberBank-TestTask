package av.sberbank.model.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artem on 12.04.2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "sberbank.db";

    private List<Class> tables;

    public DBHelper(Context context, List<Class> tables) {
        super(context, DB_NAME, null, DB_VERSION);
        this.tables = tables;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (Class clazz : tables) {
            String sqlCreateTable = Table.create(clazz);
            db.execSQL(sqlCreateTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public <T> List<T> getAll(Class<T> clazz) {
        SQLiteDatabase db = getWritableDatabase();
        DBTable table = clazz.getAnnotation(DBTable.class);
        List<T> items = new ArrayList<>();
        if (tableExist(db, table.name())) {
            Cursor cursor = db.query(table.name(), null, null, null, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        T item = Table.getFromCursor(clazz, cursor);
                        items.add(item);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
                close();
            }
        }
        return items;
    }

    public void clearTable(Class clazz) {
        try {
            DBTable table = (DBTable) clazz.getAnnotation(DBTable.class);
            SQLiteDatabase database = getWritableDatabase();
            if (tableExist(database, table.name())) {
                database.delete(table.name(), null, null);
            }
        } finally {
            close();
        }
    }

    private boolean tableExist(SQLiteDatabase database, String tableName) {
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select DISTINCT tbl_name" +
                    " from sqlite_master where tbl_name = '"
                    + tableName + '\'', null);
            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}