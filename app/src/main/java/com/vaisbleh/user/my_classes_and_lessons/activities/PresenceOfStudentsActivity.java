package com.vaisbleh.user.my_classes_and_lessons.activities;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;

import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVWriter;
import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.controller.StudentsPresenceAdapter;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Lesson;
import com.vaisbleh.user.my_classes_and_lessons.model.PresenceToShare;
import com.vaisbleh.user.my_classes_and_lessons.model.StudentPresence;

import java.io.File;
import java.io.FileWriter;
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
        switch (view.getId()) {

            case R.id.shareBtn:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                } else {

                    ArrayList<PresenceToShare> presenceToShares = new ArrayList<>();
                    for (int i = 0; i < studentPresences.size(); i++) {
                        StudentPresence studentPresence = studentPresences.get(i);
                        String studentName = studentPresence.getStudent().getStudentName();
                        String groupName = studentPresence.getLesson().getGroup().getGroupName();
                        String groupSite = studentPresence.getLesson().getGroup().getSite();
                        String lessonName = studentPresence.getLesson().getLessonName();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NewLessonActivity.DATE_FORMAT);
                        String lessonTime = simpleDateFormat.format(new Date(studentPresence.getLesson().getTime()));
                        boolean isPresence = studentPresence.isPresence();
                        presenceToShares.add(new PresenceToShare(studentName, groupName, groupSite, lessonName, lessonTime, isPresence));
                    }

//                    Gson gson = new Gson();
//                    String json = gson.toJson(presenceToShares);


                    String timeStamp = new SimpleDateFormat("_dd_MM_yyyy__HH_mm_ss").format(lesson.getTime());
                    String fileName = lesson.getLessonName() + "_" + lesson.getGroup().getGroupName() + timeStamp + ".csv";


                    File path = Environment.getExternalStorageDirectory();
                    File curDir = new File(path.getPath() + "/" + "eJournal");
                    if (!curDir.exists()) {
                        curDir.mkdirs();
                    }

                    File csvFile = new File(curDir, fileName);


                    try {
                        csvFile.createNewFile();


                        CSVWriter csvWrite = new CSVWriter(new FileWriter(csvFile));
                        String heading[] = {"student name", "presence", "lesson name", "lesson time", "group name", "site"};
                        csvWrite.writeNext(heading);
                        for (int i = 0; i < presenceToShares.size(); i++) {
                            PresenceToShare presenceToShare = presenceToShares.get(i);
                            String arrStr[] = {presenceToShare.getStudentName(), String.valueOf(presenceToShare.isPresence()), presenceToShare.getLessonName(), presenceToShare.getLessonTime(), presenceToShare.getGroupName(), presenceToShare.getGroupSite()};
                            csvWrite.writeNext(arrStr);
                        }
                        csvWrite.close();
                        Toast.makeText(this, "download complete", Toast.LENGTH_SHORT).show();


                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("error", e.getMessage());
                        Toast.makeText(this, "download false", Toast.LENGTH_SHORT).show();
                    }


//                    Intent shareIntent = new Intent();
//                    shareIntent.setAction(Intent.ACTION_SEND);
//                    shareIntent.putExtra(Intent.EXTRA_TEXT, json);
//                    shareIntent.setType("text/plain");
//                    startActivity(shareIntent);


                    break;
                }
        }
    }

}
