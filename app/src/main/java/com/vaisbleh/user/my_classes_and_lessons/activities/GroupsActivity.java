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

import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.controller.GroupsAdapter;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Group;

import java.util.ArrayList;

public class GroupsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner siteSpinner;
    private RecyclerView listGroups;
    private ClassesLessonsDBHelper helper;
    private ArrayAdapter<String> spinnerAdapter;
    private GroupsAdapter groupsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        getSupportActionBar().setTitle("Groups");

        findViewById(R.id.newGroupBtn).setOnClickListener(this);
        siteSpinner = findViewById(R.id.siteSpinner);
        siteSpinner.setOnItemSelectedListener(this);

        helper = new ClassesLessonsDBHelper(this);

        listGroups = findViewById(R.id.listGroups);
        listGroups.setLayoutManager(new LinearLayoutManager(this));
        groupsAdapter = new GroupsAdapter(this, getGroupsArrayList());
        listGroups.setAdapter(groupsAdapter);




        spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        siteSpinner.setAdapter(spinnerAdapter);
        refreshSpinner();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshSpinner();
        siteSpinner.setSelection(0);
        groupsAdapter.refresh(getGroupsArrayList());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.newGroupBtn:
                startActivity(new Intent(this, NewGroupActivity.class));
                break;
        }
    }



    private void refreshSpinner(){
        spinnerAdapter.clear();
        ArrayList<String>sites = helper.getSites();
        spinnerAdapter.addAll(sites);
    }

    public ArrayList<Group> getGroupsArrayList(){
        return helper.getAllGroups();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String selectedSite = siteSpinner.getSelectedItem().toString();

        ArrayList<Group>groups;
        if(selectedSite.equals("All sites")){
            groups = helper.getAllGroups();
        }else {
            groups = helper.getGroupsBySite(selectedSite);
        }

        groupsAdapter.refresh(groups);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
