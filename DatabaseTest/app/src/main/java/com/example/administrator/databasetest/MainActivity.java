package com.example.administrator.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnQuery = (Button) findViewById(R.id.btn_query);
        Button btnInsert = (Button) findViewById(R.id.btn_insert);

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.administrator.contactstest.privider/Account");
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                while (cursor.moveToNext()) {
                    String account = cursor.getString(cursor.getColumnIndex("account"));
                    String password = cursor.getString(cursor.getColumnIndex("password"));
                    Toast.makeText(MainActivity.this, "account:" + account, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, "password:" + password, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.administrator.contactstest.privider/Account");
                ContentValues values = new ContentValues();
                values.put("account", "ywf");
                values.put("password", "888888");
                Uri newUri = getContentResolver().insert(uri, values);
                String newId = newUri.getPathSegments().get(1);
            }
        });
    }
}
