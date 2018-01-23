package com.vaisbleh.user.my_classes_and_lessons.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.activities.StudentsActivity;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Group;

import java.util.ArrayList;

/**
 * Created by User on 13-Jan-18.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.GroupsHolder> {

    private Context context;
    private ArrayList<Group>groups = new ArrayList<>();
    private int position;
    public static final String GROUP_NAME = "groupName";

    public GroupsAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;

    }

    public void refresh (ArrayList<Group> newGroups){
        groups.clear();
        groups.addAll(newGroups);
        notifyDataSetChanged();
    }

    public void remove(Group group){
        groups.remove(group);
        notifyItemRemoved(position);
    }

    @Override
    public GroupsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupsHolder(LayoutInflater.from(context).inflate(R.layout.group_item, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupsHolder holder, int position) {
        holder.bind(groups.get(position));
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }


    //****************************************************************************************

    public class GroupsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DialogInterface.OnClickListener{

        private TextView groupNameView, siteView;
        private Group currentGroup;
        private ClassesLessonsDBHelper helper;
        private AlertDialog deleteDialog;
        private Button deleteBtn;

        public GroupsHolder(View itemView) {
            super(itemView);

            groupNameView = itemView.findViewById(R.id.lessonNameView);
            siteView = itemView.findViewById(R.id.siteView);
            deleteBtn = itemView.findViewById(R.id.deleteGroupBtn);
            deleteBtn.setOnClickListener(this);
            itemView.setOnClickListener(this);
            helper = new ClassesLessonsDBHelper(context);


        }

        public void bind(Group group){

            currentGroup = group;
            groupNameView.setText(group.getGroupName());
            siteView.setText(group.getSite());
            if(group.getGroupName().equals("none")){
                deleteBtn.setEnabled(false);
            }

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.deleteGroupBtn:

                    deleteDialog = new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.delete_group))
                            .setMessage(context.getResources().getString(R.string.dialog_message))
                            .setPositiveButton(context.getResources().getString(R.string.positive_btn),this)
                            .setNegativeButton(context.getResources().getString(R.string.negative_btn), this)
                            .create();
                    deleteDialog.show();



                    break;

                default:
                    Intent studentsIntent = new Intent(context, StudentsActivity.class);
                    studentsIntent.putExtra(GROUP_NAME, currentGroup.getGroupName());
                    context.startActivity(studentsIntent);

            }

        }

        @Override
        public void onClick(DialogInterface dialogInterface, int button) {

            if(dialogInterface == dialogInterface) {

                switch (button){
                    case DialogInterface.BUTTON_POSITIVE:

                        String groupName = currentGroup.getGroupName().toString();

                        helper.updateDeletedGroupStudents(groupName);
                        helper.deletePresenceByGroup(groupName);
                        helper.deleteLessonsByGroup(groupName);
                        
                        helper.deleteGroup(groupName);

                        position = getAdapterPosition();
                        remove(currentGroup);

                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        deleteDialog.dismiss();

                        break;
                }
            }

        }
    }

}
