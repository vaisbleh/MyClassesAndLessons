package com.vaisbleh.user.my_classes_and_lessons.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Group;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SharedPreferences sp;
    public static final String IS_FIRST = "isFirst";
    public static final String NONE = "none";

    private ClassesLessonsDBHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.groupsBtn).setOnClickListener(this);
        findViewById(R.id.lessonsBtn).setOnClickListener(this);
        findViewById(R.id.studentBtn).setOnClickListener(this);
        findViewById(R.id.adminBtn).setOnClickListener(this);


        helper = new ClassesLessonsDBHelper(this);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirst = sp.getBoolean(IS_FIRST, true);
        if(isFirst){
            runOnFirstTime();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.groupsBtn:
                startActivity(new Intent(this, GroupsActivity.class));
                break;

            case R.id.lessonsBtn:
                startActivity(new Intent(this, LessonsActivity.class));
                break;

            case R.id.studentBtn:
                startActivity(new Intent(this, StudentsActivity.class));
                break;

            case R.id.adminBtn:
                startActivity(new Intent(this, AdminActivity.class));
                break;



        }
    }
    private void runOnFirstTime(){
        helper.insertGroup(new Group(NONE, NONE));
        sp.edit().putBoolean(IS_FIRST, false).apply();

    }

}
