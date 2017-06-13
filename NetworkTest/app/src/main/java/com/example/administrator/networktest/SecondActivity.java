package com.example.administrator.networktest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/11/21 0021.
 */

public class SecondActivity extends AppCompatActivity {
    private TextView textView_name;
    private TextView textView_age;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        textView_name = (TextView) findViewById(R.id.name);
        textView_age = (TextView) findViewById(R.id.age);

        Person person  =(Person)getIntent().getParcelableExtra("person_data");
        textView_name.setText(person.getName());
        textView_age.setText( String.valueOf(person.getAge()) );
    }
}
