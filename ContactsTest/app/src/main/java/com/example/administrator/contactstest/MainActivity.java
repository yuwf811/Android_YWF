package com.example.administrator.contactstest;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView contactsView;
    ArrayAdapter<String> adapter;
    List<String> contactsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contactsView = (ListView) findViewById(R.id.contacts_view);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contactsList);
        contactsView.setAdapter(adapter);
        readContacts();
    }

    private void readContacts() {
        Cursor cursor = null;
        try {
//            String[] mPermission = {"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS"};
            String[] mPermission = {"android.permission.READ_CONTACTS", "android.permission.READ_CONTACTS"};
            if (ActivityCompat.checkSelfPermission(this, mPermission[0]) != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1]) != MockPackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, mPermission, 1);
            }

            while((ActivityCompat.checkSelfPermission(this, mPermission[0]) != MockPackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, mPermission[1]) != MockPackageManager.PERMISSION_GRANTED)){
                Thread.currentThread();
                Thread.sleep(500);
            }
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (cursor.moveToNext()) {
                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactsList.add(displayName + "\n" + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
