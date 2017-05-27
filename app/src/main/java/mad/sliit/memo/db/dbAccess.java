package mad.sliit.memo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mad.sliit.memo.Notes;

public class dbAccess {

    private SQLiteDatabase database;

    private dbOpenHelper openHelper;

    private static volatile dbAccess instance;


    //------------------------------------------------------------------------------/
    private dbAccess(Context context) {

        this.openHelper = new dbOpenHelper(context);
    }


    //------------------------------------------------------------------------------/
    public static synchronized dbAccess getInstance(Context context) {
        if (instance == null) {
            instance = new dbAccess(context);
        }
        return instance;
    }


    //------------------------------------------------------------------------------/
    public void open() {

        this.database = openHelper.getWritableDatabase();
    }


    //------------------------------------------------------------------------------/
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }



    //------------------------------------------------------------------------------/
    public void save(Notes memo) {
        ContentValues values = new ContentValues();
        values.put("date", memo.getTime());
        values.put("note", memo.getText());
        database.insert(dbOpenHelper.TABLE, null, values);
    }



    //------------------------------------------------------------------------------/
    public void update(Notes memo) {
        ContentValues values = new ContentValues();
        values.put("date", new Date().getTime());
        values.put("note", memo.getText());
        String date = Long.toString(memo.getTime());
        database.update(dbOpenHelper.TABLE, values, "date = ?", new String[]{date});
    }



    //------------------------------------------------------------------------------/
    public void delete(Notes memo) {
        String date = Long.toString(memo.getTime());
        database.delete(dbOpenHelper.TABLE, "date = ?", new String[]{date});
    }



    //------------------------------------------------------------------------------/
    public List getAllNotes() {
        List notes = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * From notes ORDER BY date DESC", null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            long time = cursor.getLong(0);
            String text = cursor.getString(1);
            notes.add(new Notes(time, text));
            cursor.moveToNext();
        }

        cursor.close();
        return notes;
    }




}