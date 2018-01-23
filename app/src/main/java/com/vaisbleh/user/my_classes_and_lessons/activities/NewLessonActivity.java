package com.vaisbleh.user.my_classes_and_lessons.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Group;
import com.vaisbleh.user.my_classes_and_lessons.model.Lesson;
import com.vaisbleh.user.my_classes_and_lessons.model.Student;
import com.vaisbleh.user.my_classes_and_lessons.model.StudentPresence;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewLessonActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView timeView;
    private EditText editLessonName;
    private Spinner groupsNewLessonSpinner;
    private ClassesLessonsDBHelper helper;
    private ArrayAdapter<String> spinnerAdapter;
    private long time;
    public static final String DATE_FORMAT = "dd/MM/yyyy   HH:mm";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_lesson);

        getSupportActionBar().setTitle("New Lesson");

        helper = new ClassesLessonsDBHelper(this);

        timeView = findViewById(R.id.timeView);
        editLessonName = findViewById(R.id.editLessonName);
        groupsNewLessonSpinner = findViewById(R.id.groupsNewLessonSpinner);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        ArrayList<String> groupsName = helper.getGroupsNames();
        groupsName.remove(MainActivity.NONE);
        spinnerAdapter.addAll(groupsName);
        groupsNewLessonSpinner.setAdapter(spinnerAdapter);

        time = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        String lessonTime = simpleDateFormat.format(new Date(time));
        timeView.setText(lessonTime);

        findViewById(R.id.saveNewLessonBtn).setOnClickListener(this);




    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.saveNewLessonBtn:

                if(editLessonName.getText().toString().equals("")||groupsNewLessonSpinner.getSelectedItem().toString().equals("All groups")){
                    Toast.makeText(this, "Name mast be filled and group mast be chosen", Toast.LENGTH_SHORT).show();
                }else {
                    String lessonName = editLessonName.getText().toString();
                    String groupName = groupsNewLessonSpinner.getSelectedItem().toString();
                    Group group =  helper.getGroup(groupName);
                    Lesson lesson = new Lesson(time, lessonName, group);
                    helper.insertLesson(lesson);

                    ArrayList<Student>students = new ArrayList<>();
                    students = helper.getStudentsByGroup(groupName);

                    for (int i = 0; i <students.size() ; i++) {
                        StudentPresence studentPresence = new StudentPresence(students.get(i), lesson, false);
                        helper.insertStudentPresence(studentPresence);
                    }
                    finish();

                }

                break;
        }
    }
}
