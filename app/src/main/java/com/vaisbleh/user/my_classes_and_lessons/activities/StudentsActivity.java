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
import android.widget.Toast;

import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.controller.GroupsAdapter;
import com.vaisbleh.user.my_classes_and_lessons.controller.StudentsAdapter;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Student;

import java.util.ArrayList;

public class StudentsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String groupName;

    private Spinner groupStudentsSpinner;
    private RecyclerView listStudents;
    private ClassesLessonsDBHelper helper;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> groupsName;
    private StudentsAdapter adapter;

    public static final String STUDENT_TO_EDIT = "studentToEdit";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);

        getSupportActionBar().setTitle("Students");


        groupsName = new ArrayList<>();
        groupName = getIntent().getStringExtra(GroupsAdapter.GROUP_NAME);
        helper = new ClassesLessonsDBHelper(this);
        listStudents = findViewById(R.id.listStudents);
        groupStudentsSpinner = findViewById(R.id.groupStudentsSpinner);
        groupStudentsSpinner.setOnItemSelectedListener(this);

        findViewById(R.id.newStudentBtn).setOnClickListener(this);

        listStudents.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentsAdapter(this, helper.getStudents());
        listStudents.setAdapter(adapter);

        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        groupStudentsSpinner.setAdapter(spinnerAdapter);
        refreshSpinner();

        int position = 0;
        if(groupName!=null){
            for (int i = 0; i < groupsName.size(); i++) {
                if(groupName.equals(groupsName.get(i))){
                    position = i;
                }
            }
        }

        groupStudentsSpinner.setSelection(position);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newStudentBtn:

                startActivity(new Intent(this, EditStudentActivity.class));

                break;
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.refresh(getStudentsList());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        adapter.refresh(getStudentsList());

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void refreshSpinner(){
        spinnerAdapter.clear();
        groupsName = helper.getGroupsNames();
        spinnerAdapter.addAll(groupsName);
    }

    public ArrayList<Student> getStudentsList(){
        String selected = groupStudentsSpinner.getSelectedItem().toString();
        ArrayList<Student> students = new ArrayList<>();
        if(selected.equals(helper.ALL_GROUPS)){
            students = helper.getStudents();
        }else {
            students = helper.getStudentsByGroup(selected);
        }

        return students;
    }
}
