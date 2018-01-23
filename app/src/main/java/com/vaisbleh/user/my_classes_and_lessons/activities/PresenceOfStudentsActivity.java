package com.vaisbleh.user.my_classes_and_lessons.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.controller.StudentsPresenceAdapter;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Lesson;
import com.vaisbleh.user.my_classes_and_lessons.model.PresenceToShare;
import com.vaisbleh.user.my_classes_and_lessons.model.StudentPresence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class PresenceOfStudentsActivity extends AppCompatActivity implements StudentsPresenceAdapter.OnPresenceChangedListener, View.OnClickListener {

    private Lesson lesson;
    private TextView lessonNameTimeView;
    private RecyclerView presenceList;
    private ClassesLessonsDBHelper helper;
    private ArrayList<StudentPresence> studentPresences;
    private StudentsPresenceAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presence_of_students);

        getSupportActionBar().setTitle("Presence");

        lesson = getIntent().getParcelableExtra(LessonsActivity.LESSON_TO_SHOW);


        lessonNameTimeView = findViewById(R.id.lessonNameTimeView);
        presenceList = findViewById(R.id.presenceList);
        helper = new ClassesLessonsDBHelper(this);
        studentPresences = helper.getStudentPresences(lesson);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NewLessonActivity.DATE_FORMAT);
        String lessonTime = simpleDateFormat.format(new Date(lesson.getTime()));

        lessonNameTimeView.setText(lesson.getLessonName() + "\n" + lesson.getGroup().getGroupName() + "\n" + lessonTime);


        presenceList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentsPresenceAdapter(this, studentPresences);
        presenceList.setAdapter(adapter);


        findViewById(R.id.shareBtn).setOnClickListener(this);




    }


    @Override
    public void onPresenceChanged(int position, boolean isPresence) {
        studentPresences.get(position).setPresence(isPresence);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.shareBtn:

                ArrayList<PresenceToShare>presenceToShares = new ArrayList<>();
                for (int i = 0; i <studentPresences.size() ; i++) {
                    StudentPresence studentPresence = studentPresences.get(i);
                    String studentName = studentPresence.getStudent().getStudentName();
                    String groupName = studentPresence.getLesson().getGroup().getGroupName();
                    String groupSite = studentPresence.getLesson().getGroup().getSite();
                    String lessonName = studentPresence.getLesson().getLessonName();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NewLessonActivity.DATE_FORMAT);
                    String lessonTime = simpleDateFormat.format(new Date(studentPresence.getLesson().getTime()));
                    boolean isPresence = studentPresence.isPresence();
                    presenceToShares.add(new PresenceToShare(studentName,groupName,groupSite,lessonName, lessonTime, isPresence));
                }

                Gson gson = new Gson();
                String json = gson.toJson(presenceToShares);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, json);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);


                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            writeFile();
        } else {
            Toast.makeText(this, "Can't to download file", Toast.LENGTH_SHORT).show();
        }
    }

    public void writeFile(){
        Gson gson = new Gson();
        String json = gson.toJson(studentPresences);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String lessonTime = simpleDateFormat.format(new Date(lesson.getTime()));
        String fileName = lesson.getLessonName() + "_" + lessonTime;

        File file;
        FileOutputStream outputStream;
        try {
            file = new File(getExternalFilesDir("MyLessons"), fileName);

            outputStream = new FileOutputStream(file);
            outputStream.write(json.getBytes());
            outputStream.close();
            Toast.makeText(this, "download complete", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "download false", Toast.LENGTH_SHORT).show();
        }

    }
}
