package mad.sliit.memo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class dbOpenHelper extends SQLiteOpenHelper {


    public static final String TABLE = "notes";


    public dbOpenHelper(Context context) {

        super(context, "notes_database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes(date INTEGER PRIMARY KEY, note TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}