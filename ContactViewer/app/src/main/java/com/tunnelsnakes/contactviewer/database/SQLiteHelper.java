package com.tunnelsnakes.contactviewer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import com.tunnelsnakes.contactviewer.models.Contact;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteOpenHelper";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_CONTACTS = "CREATE TABLE Contacts (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, firstName TEXT, lastName TEXT, title TEXT, phone TEXT, email TEXT, twitterHandle TEXT)";
    private static final String[] contactTableColumns = new String[] {"id", "firstName", "lastName", "title", "phone", "email", "twitterHandle"};

    public SQLiteHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
     //   SQLiteDatabase db = this.getReadableDatabase();
        database.execSQL(DATABASE_CREATE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,"Upgrading database from version " + oldVersion + " to "+ newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS ContactsActivity");
        onCreate(db);
    }

    public ArrayList<Contact> getContacts() {
        ArrayList<Contact> results = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            try {
                Cursor cursor = db.query("ContactsActivity", contactTableColumns, null, null, null, null, "lastName");
                Log.v(TAG, "getContacts found " + cursor.getCount() + " records");

                if (cursor != null) {
                    // move cursor to first row
                    if (cursor.moveToFirst()) {
                        do {
                            Contact contact = new Contact();
                            contact.setId(cursor.getInt(cursor.getColumnIndex("id")));
                            contact.setFirstName(cursor.getString(cursor.getColumnIndex("firstName")));
                            contact.setLastName(cursor.getString(cursor.getColumnIndex("lastName")));
                            contact.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                            contact.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                            contact.setTwitterHandle(cursor.getString(cursor.getColumnIndex("twitterHandle")));
                            results.add(contact);
                            // move to next row
                        } while (cursor.moveToNext());
                    }
                }
            } catch (SQLiteException se) {
                Log.e(TAG, "SQLiteException: " + se.getLocalizedMessage());
            } finally {
                if (db != null) {
                    db.close();
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, "Error getting writable database: " + ex.getLocalizedMessage());
        }

        return results;
    }

    public boolean addUpdateContact(Contact contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contactValues = new ContentValues();
        if(contact.getId() >= 0) {
            contactValues.put("id", contact.getId());
        }

        contactValues.put("firstName", contact.getFirstName());
        contactValues.put("lastName", contact.getLastName());
        contactValues.put("title", contact.getTitle());
        contactValues.put("phone", contact.getPhone());
        contactValues.put("twitterHandle", contact.getTwitterHandle());

        try {
            db.insertWithOnConflict("ContactsActivity", null, contactValues, SQLiteDatabase.CONFLICT_IGNORE);
        } catch (SQLiteException se) {
            Log.e(TAG, "SQLiteException: " + se.getLocalizedMessage());
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
            return true;
        }
    }
}