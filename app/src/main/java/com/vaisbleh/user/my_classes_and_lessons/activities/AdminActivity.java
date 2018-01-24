package com.vaisbleh.user.my_classes_and_lessons.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Group;
import com.vaisbleh.user.my_classes_and_lessons.model.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editJson;
    private ClassesLessonsDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setTitle("Administrator");

        editJson = findViewById(R.id.editJson);
        findViewById(R.id.shareStudents).setOnClickListener(this);
        findViewById(R.id.uploadBtn).setOnClickListener(this);
        helper = new ClassesLessonsDBHelper(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.shareStudents:

                ArrayList<Student> students = helper.getStudents();
                Gson gson = new Gson();
                String json = gson.toJson(students);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, json);
                shareIntent.setType("text/plain");
                startActivity(shareIntent);

                break;

            case R.id.uploadBtn:

                String jsonStudents = editJson.getText().toString();
                try {
                    JSONArray root = new JSONArray(jsonStudents);
                    for (int i = 0; i <root.length() ; i++) {
                        JSONObject object = root.getJSONObject(i);
                        String studentName = object.getString("studentName");
                        String photoAddress = object.getString("photoAddress");
                        helper.insertStudent(new Student(studentName, photoAddress, new Group(MainActivity.NONE, MainActivity.NONE)));
                    }
                    Toast.makeText(this, "loading is complete", Toast.LENGTH_SHORT).show();
                    editJson.setText("");


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "JSON is incorrect", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }
}
