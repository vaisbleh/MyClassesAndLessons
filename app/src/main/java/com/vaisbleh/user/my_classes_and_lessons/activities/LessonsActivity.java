package com.vaisbleh.user.my_classes_and_lessons.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.controller.LessonsAdapter;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Lesson;

import java.util.ArrayList;

public class LessonsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner groupsLessonsSpinner;
    private RecyclerView listLessons;
    private ClassesLessonsDBHelper helper;
    private ArrayAdapter<String> spinnerAdapter;
    private LessonsAdapter adapter;
    public static final String LESSON_TO_SHOW = "lessonToShow";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        getSupportActionBar().setTitle("Lessons");

        groupsLessonsSpinner = findViewById(R.id.groupLessonsSpinner);
        listLessons = findViewById(R.id.listLessons);
        helper = new ClassesLessonsDBHelper(this);

        findViewById(R.id.newLessonBtn).setOnClickListener(this);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        groupsLessonsSpinner.setAdapter(spinnerAdapter);
        refreshSpinner();
        groupsLessonsSpinner.setOnItemSelectedListener(this);

        listLessons.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LessonsAdapter(this, helper.getAllLessons());
        listLessons.setAdapter(adapter);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.refresh(getLessonsArray());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.refresh(getLessonsArray());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newLessonBtn:
            startActivity(new Intent(this, NewLessonActivity.class));
                break;
        }
    }

    private void refreshSpinner(){
        spinnerAdapter.clear();
        ArrayList<String> groupsName = helper.getGroupsNames();
        groupsName.remove(MainActivity.NONE);
        spinnerAdapter.addAll(groupsName);
    }

    public ArrayList<Lesson> getLessonsArray(){
        String spinnerSelection = groupsLessonsSpinner.getSelectedItem().toString();
        ArrayList<Lesson>lessons = new ArrayList<>();
        if(spinnerSelection.equals(helper.ALL_GROUPS)){
            lessons = helper.getAllLessons();
        }else {
            lessons = helper.getLessonsByGroup(spinnerSelection);
        }

        return lessons;
    }
}
