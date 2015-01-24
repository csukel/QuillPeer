package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import core.AESCrypt;

/**
 * Created by loucas on 24/01/2015.
 */
public class UserInfoProvider {
    private DBHelper dbHelper = null;
    private static final String LOG_TAG = "UserInfoProvider";
    public static final String TABLE_NAME = "USER_INFO";
    public static final String KEY_USERNAME = "USERNAME";
    public static final String KEY_PASSWORD = "PASSWORD";

    /** Constructor */
    public UserInfoProvider(Context context) {
        Log.v(LOG_TAG, "context=" + context.toString());
        dbHelper = new DBHelper(context);
    }

    /**
     * Get the database connection.
     */
    public SQLiteDatabase getConnection() {
        return dbHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void closeConnection() {
        dbHelper.close();
    }

    /**
     * Insert data to player info table when a new player registers
     */
    public void addUser(String username,String password) {
        if (null != username && null != password) {
            AESCrypt crypt;
            String encryptedPass="";
            try {
                crypt = new AESCrypt("");
                encryptedPass= crypt.encrypt(password);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String sql = "INSERT INTO " + TABLE_NAME + " VALUES" + "(" + "'" + username + "'"
                    + ", " + "'" + encryptedPass + "');";
            dbHelper.insert(sql);
        } else {
            Log.v(LOG_TAG, "Cannot add user information, "
                    + "because user has null value!");
        }
    }

    public boolean usernameExists(String username){
        String[] whereArgs = new String[]{KEY_USERNAME +" = "+ "'"+username+"'"+";"};
        return dbHelper.isDataExist(TABLE_NAME,whereArgs);
    }

    public boolean userExists(String username, String pass){
        AESCrypt crypt;
        String encryptedPass="";
        try {
            crypt = new AESCrypt("");
            encryptedPass= crypt.encrypt(pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] whereArgs = new String[]{KEY_USERNAME +" = "+ "'"+username+"'", KEY_PASSWORD + "="+"'"+encryptedPass+"';"};
        return dbHelper.isDataExist(TABLE_NAME,whereArgs);
    }

}