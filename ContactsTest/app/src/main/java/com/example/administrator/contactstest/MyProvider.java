package com.example.administrator.contactstest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/10/31 0031.
 */

public class MyProvider extends ContentProvider {
    public static final int TABLE1_DIR = 0;
    public static final int TABLE1_ITEM = 1;
    public static final int TABLE2_DIR = 2;
    public static final int TABLE2_ITEM = 3;
    public static final String AUTHORITY = "com.example.administrator.contactstest.privider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        uriMatcher.addURI(AUTHORITY, "table1", TABLE1_DIR);
//        uriMatcher.addURI(AUTHORITY, "table1/#", TABLE1_ITEM);
//        uriMatcher.addURI(AUTHORITY, "table2", TABLE2_DIR);
//        uriMatcher.addURI(AUTHORITY, "table2/#", TABLE2_ITEM);
        uriMatcher.addURI(AUTHORITY, "Account", TABLE1_DIR);
        uriMatcher.addURI(AUTHORITY, "Account/#", TABLE1_ITEM);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "Account.db", null, 1);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                cursor = db.query("Account", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case TABLE1_ITEM:
                String id = uri.getPathSegments().get(1);
                cursor = db.query("Account", projection, "id=?", new String[]{id}, null, null, sortOrder);
                break;
//            case TABLE2_DIR:
//                break;
//            case TABLE2_ITEM:
//                break;
            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
            case TABLE1_ITEM:
                long id = db.insert("Account", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/Account/" + id);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int updatedRows = 0;
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                updatedRows = db.update("Account", values, selection, selectionArgs);
                break;
            case TABLE1_ITEM:
                String id = uri.getPathSegments().get(1);
                updatedRows = db.update("Account", values, "id=?", new String[]{id});
            default:
                break;
        }
        return updatedRows;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int deleteRows = 0;
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                deleteRows = db.delete("Account", selection, selectionArgs);
                break;
            case TABLE1_ITEM:
                String id = uri.getPathSegments().get(1);
                deleteRows = db.delete("Account", "id=?", new String[]{id});
            default:
                break;
        }
        return deleteRows;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TABLE1_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.administrator.contactstest.privider.table1";
            case TABLE1_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.administrator.contactstest.privider.table1";
            case TABLE2_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.administrator.contactstest.privider.table2";
            case TABLE2_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.administrator.contactstest.privider.table2";
            default:
                break;
        }
        return null;
    }
}
