package com.vaisbleh.user.my_classes_and_lessons.controller;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vaisbleh.user.my_classes_and_lessons.R;
import com.vaisbleh.user.my_classes_and_lessons.activities.LessonsActivity;
import com.vaisbleh.user.my_classes_and_lessons.activities.NewLessonActivity;
import com.vaisbleh.user.my_classes_and_lessons.activities.PresenceOfStudentsActivity;
import com.vaisbleh.user.my_classes_and_lessons.db.ClassesLessonsDBHelper;
import com.vaisbleh.user.my_classes_and_lessons.model.Lesson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by User on 21-Jan-18.
 */

public class LessonsAdapter extends RecyclerView.Adapter<LessonsAdapter.LessonsHolder> {

    private Context context;
    private ArrayList<Lesson> lessons = new ArrayList<>();
    private int position;

    public LessonsAdapter(Context context, ArrayList<Lesson> lessons) {
        this.context = context;
        this.lessons = lessons;
    }

    public void refresh(ArrayList<Lesson> newLessons) {
        lessons.clear();
        lessons.addAll(newLessons);
        notifyDataSetChanged();
    }

    public void remove(Lesson lesson) {
        lessons.remove(lesson);
        notifyItemRemoved(position);
    }

    @Override
    public LessonsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LessonsHolder(LayoutInflater.from(context).inflate(R.layout.lesson_item, parent, false));
    }

    @Override
    public void onBindViewHolder(LessonsHolder holder, int position) {
        holder.bind(lessons.get(position));
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    //*************************************************************************************************
    public class LessonsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, DialogInterface.OnClickListener {

        private TextView timeView, lessonNameView, groupLessonView;
        private ClassesLessonsDBHelper helper;
        private AlertDialog deleteDialog;
        private Lesson currentLesson;


        public LessonsHolder(View itemView) {
            super(itemView);
            helper = new ClassesLessonsDBHelper(context);
            timeView = itemView.findViewById(R.id.timeView);
            lessonNameView = itemView.findViewById(R.id.lessonNameView);
            groupLessonView = itemView.findViewById(R.id.groupLessonView);
            itemView.findViewById(R.id.deleteLessonBtn).setOnClickListener(this);
            itemView.setOnClickListener(this);

        }

        public void bind(Lesson lesson) {
            long time = lesson.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(NewLessonActivity.DATE_FORMAT);
            String lessonTime = simpleDateFormat.format(new Date(time));
            timeView.setText(lessonTime);
            lessonNameView.setText(lesson.getLessonName());
            currentLesson = lesson;
            groupLessonView.setText(lesson.getGroup().getGroupName());
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.deleteLessonBtn:

                    deleteDialog = new AlertDialog.Builder(context)
                            .setTitle(context.getResources().getString(R.string.delete_lesson))
                            .setMessage(context.getResources().getString(R.string.dialog_message))
                            .setPositiveButton(context.getResources().getString(R.string.positive_btn), this)
                            .setNegativeButton(context.getResources().getString(R.string.negative_btn), this)
                            .create();
                    deleteDialog.show();

                    break;

                default:

                    Intent intent = new Intent(context, PresenceOfStudentsActivity.class);
                    intent.putExtra(LessonsActivity.LESSON_TO_SHOW, currentLesson);
                    context.startActivity(intent);

            }

        }

        @Override
        public void onClick(DialogInterface dialogInterface, int button) {
            switch (button) {
                case DialogInterface.BUTTON_POSITIVE:
                    helper.deletePresenceByLesson(currentLesson);
                    helper.deleteLesson(currentLesson.getTime());
                    position = getAdapterPosition();
                    remove(currentLesson);

                    break;

                case DialogInterface.BUTTON_NEGATIVE:

                    deleteDialog.dismiss();

                    break;
            }
        }
    }
}

