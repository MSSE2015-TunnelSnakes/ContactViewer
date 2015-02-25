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

// TODO - Should this class be a singleton?
public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteOpenHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ContactViewer.db";
    private static final String CONTACTS_TABLE_NAME = "Contacts";

    // Database creation sql statement
    private static final String DATABASE_CREATE_CONTACTS = "CREATE TABLE " + CONTACTS_TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, firstName TEXT, lastName TEXT, title TEXT, phone TEXT, email TEXT, twitterHandle TEXT)";
    private static final String[] CONTACT_TABLE_COLUMNS = new String[] {"id", "firstName", "lastName", "title", "phone", "email", "twitterHandle"};

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        try {
            database.execSQL(DATABASE_CREATE_CONTACTS);
        } catch (SQLiteException se) {
            Log.e(TAG, "Error attempting to create database: " + se.getLocalizedMessage());
        }
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
                Cursor cursor = db.query(CONTACTS_TABLE_NAME, CONTACT_TABLE_COLUMNS, null, null, null, null, "lastName");
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
                            contact.setEmail(cursor.getString(cursor.getColumnIndex("email")));
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

        contactValues.put("firstName", contact.getFirstName());
        contactValues.put("lastName", contact.getLastName());
        contactValues.put("title", contact.getTitle());
        contactValues.put("email", contact.getEmail());
        contactValues.put("phone", contact.getPhone());
        contactValues.put("twitterHandle", contact.getTwitterHandle());

        try {
            if(contact.getId() >= 0) {
                db.update(CONTACTS_TABLE_NAME, contactValues, "id = " + contact.getId(), null);
            } else {
                db.insert(CONTACTS_TABLE_NAME, null, contactValues);
            }
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

    // NOTE: This could be done better.  This works for now.
    public Contact getContact(int id) {
        ArrayList<Contact> contacts = getContacts();
        for(Contact c : contacts) {
            if(c.getId() == id) {
                return c;
            }
        }
        return null; // This is bad
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            db.delete(CONTACTS_TABLE_NAME, "id = " + id, null);
        } catch (SQLiteException se) {
            Log.d(TAG, "Could not delete contact: " + se.getLocalizedMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}