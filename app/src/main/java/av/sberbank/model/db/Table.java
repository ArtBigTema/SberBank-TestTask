package av.sberbank.model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by Artem on 12.04.2017.
 */

public class Table {
    private static final String TAG = Table.class.getName();

    static String create(Class clazz) {
        DBTable table = (DBTable) clazz.getAnnotation(DBTable.class);
        String tableName = table.name();
        String key = getKey(clazz);
        String fields = appendAllFields(clazz);

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE ");
        sb.append(tableName);
        sb.append(" (");
        sb.append(key);
        sb.append(" INTEGER PRIMARY KEY");
        sb.append(fields);
        sb.append(')');
        return sb.toString();
    }

    private static String getKey(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Key.class)) {
                return field.getAnnotation(DBColumn.class).name();
            }
        }
        return null;
    }

    private static String appendAllFields(Class clazz) {
        StringBuilder result = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Key.class) &&
                    field.isAnnotationPresent(DBColumn.class)) {
                DBColumn annotation = field.getAnnotation(DBColumn.class);
                result.append(',')
                        .append(annotation.name())
                        .append(' ')
                        .append(annotation.type().getValue());
                // such as ",ID TEXT"
            }
        }
        return result.toString();
    }

    static String drop(Class clazz) {
        DBTable table = DBTable.class.cast(clazz.getAnnotation(DBTable.class));
        String tableName = table.name();
        return "DROP TABLE IF EXISTS " + tableName;
    }

    static <T> T getFromCursor(Class<T> clazz, Cursor cursor) {
        try {
            T item = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(DBColumn.class)) {
                    field.setAccessible(true);
                    DBColumn annotation = field.getAnnotation(DBColumn.class);
                    int columnIndex = cursor.getColumnIndex(annotation.name());
                    int type = cursor.getType(columnIndex);

                    if (type == Cursor.FIELD_TYPE_INTEGER) {
                        field.set(item, cursor.getInt(columnIndex));
                    }
                    if (type == Cursor.FIELD_TYPE_STRING) {
                        field.set(item, cursor.getString(columnIndex));
                    }
                }
            }
            return item;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    static <T> ContentValues insert(Class clazz, T object) {
        try {
            ContentValues values = new ContentValues();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(DBColumn.class)) {
                    field.setAccessible(true);
                    String fieldName = field.getAnnotation(DBColumn.class).name();
                    Class<?> fieldType = field.getType();

                    if (fieldType == Integer.class) {
                        values.put(fieldName, Integer.class.cast(field.get(object)));
                    }
                    if (fieldType == String.class) {
                        values.put(fieldName, String.class.cast(field.get(object)));
                    }
                }
            }
            return values;
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return new ContentValues();
    }
}