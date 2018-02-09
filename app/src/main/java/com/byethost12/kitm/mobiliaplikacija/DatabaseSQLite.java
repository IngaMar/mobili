package com.byethost12.kitm.mobiliaplikacija;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSQLite extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db";

    private static final String TABLE_USERS = "users";
    private static final String USER_ID = "id";
    private static final String USER_LEVEL = "userlevel";
    private static final String USER_NAME = "name";
    private static final String USER_PASSWORD = "password";
    private static final String USER_EMAIL = "email";

    public DatabaseSQLite (Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate (SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE" + TABLE_USERS + "("
                + USER_ID + "INTEGER PRIMARY KEY,"
                + USER_LEVEL + "TEXT,"
                + USER_NAME + "TEXT,"
                + USER_PASSWORD + "TEXT,"
                + USER_EMAIL + "TEXT," + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }
    @Override
    public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        //Sukuria lantele naujai
        onCreate(db);
    }
void addUser (User user){
        SQLiteDatabase db = this.getWritableDatabase();

    ContentValues values = new ContentValues();
    values.put(USER_LEVEL,      user.getUserlevel());
    values.put(USER_NAME,       user.getUsernameForRegister());
    values.put(USER_PASSWORD,   user.getPasswordForRegister());
    values.put(USER_EMAIL,      user.getEmailForRegister());

    //Iterpia eilute
    db.insert(TABLE_USERS, null, values);

    //Uzdaro porta
    db.close();
}
    User getUser (int id){
    SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_USERS,
                new String[]{
                        USER_ID,
                        USER_LEVEL,
                        USER_NAME,
                        USER_PASSWORD,
                        USER_EMAIL
                },
                USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor !=null)
                cursor.moveToFirst();
        User user = new User(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );
        return user;
    }
        public List<User> getALLUsers(){
        List<User> users = new ArrayList<User>();

            //Select All Query
            String selectQuery = "SELECT * FROM" + TABLE_USERS;

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            //looping throught all rows and adding to list

            if (cursor.moveToFirst()){
                do {
                    User user = new User ();

                    user.setId(Integer.parseInt(cursor.getString(0)));
                    user.setUserlevel(cursor.getString(1));
                    user.setUsernameForRegister(cursor.getString(2));
                    user.setPasswordForRegister(cursor.getString(3));
                    user.setEmailForRegister(cursor.getString(4));

                    //ading user to list
                    users.add(user);
                } while (cursor.moveToNext());
            }
            //return users list
            return users;
        }

        public boolean isValidUser (String username, String password){
            Cursor c = getReadableDatabase().rawQuery(
                    "SELECT * FROM" + TABLE_USERS + "WHERE"
                    + USER_NAME + "='" + username + "'AND" +
                            USER_PASSWORD + "='" + password + "'", null);
            if(c.getCount()>0)
                return true;
            return false;

        }
    }

