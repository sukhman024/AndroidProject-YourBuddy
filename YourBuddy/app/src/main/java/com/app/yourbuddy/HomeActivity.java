package com.app.yourbuddy;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import database.DatabaseHelper;

public class HomeActivity extends AppCompatActivity {


    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    int foreignStudyTaskStatus;
    int permanentResidenceTaskStatus;
    int personaTravelTaskStatus;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CardView foreignStudyCardView = (CardView) findViewById(R.id.card_fs);
        CardView permanenetResidenceCardView = (CardView) findViewById(R.id.card_pr);

        TextView fsStatus = findViewById(R.id.status_fs);
        TextView prStatus = findViewById(R.id.status_pr);
        TextView ptStatus = findViewById(R.id.status_pt);

        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        username = pref.getString("username", null);
        foreignStudyTaskStatus = checkTaskStatusForUser(username, AppConstants.FOREIGN_STUDY_TASK_ID);
        permanentResidenceTaskStatus = checkTaskStatusForUser(username, AppConstants.PERMANENT_RESIDENCE_TASK_ID);
        personaTravelTaskStatus = checkTaskStatusForUser(username, AppConstants.PERSONAL_TRAVEL_TASK_ID);

        statusUpdate(fsStatus, foreignStudyTaskStatus);
        statusUpdate(prStatus, permanentResidenceTaskStatus);
        statusUpdate(ptStatus, personaTravelTaskStatus);

        foreignStudyCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(AppConstants.FOREIGN_STUDY_TASK_ID, foreignStudyTaskStatus, ForeignStepActivity.class);
            }
        });
        permanenetResidenceCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity(AppConstants.PERMANENT_RESIDENCE_TASK_ID, permanentResidenceTaskStatus, PermanentResidenceActivity.class);
            }
        });
    }

    private void startNextActivity(int taskId, int taskStatus, Class nextActivity) {
        if (username == null || username == "") {
            Toast.makeText(HomeActivity.this, "Username missing", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("curr_task_id", taskId);
        editor.commit();
        if (taskStatus == AppConstants.TASK_IN_PROGRESS) {
            Toast.makeText(HomeActivity.this, "In Progress Task", Toast.LENGTH_LONG).show();
            startActivity(new Intent(HomeActivity.this, nextActivity));
            return;
        } else if (taskStatus == AppConstants.TASK_COMPLETED) {
            Toast.makeText(HomeActivity.this, "Completed Task!! Starting Read only Mode", Toast.LENGTH_LONG).show();
            startActivity(new Intent(HomeActivity.this, nextActivity));
            return;
        }
        boolean isAdded = databaseHelper.addUserTask(username, taskId, AppConstants.TASK_IN_PROGRESS);
        if (isAdded) {
            Toast.makeText(HomeActivity.this, "Task Started", Toast.LENGTH_LONG).show();
            startActivity(new Intent(HomeActivity.this, nextActivity));
        } else {
            Toast.makeText(HomeActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }

    }

    private void statusUpdate(TextView textView, int status) {
        if (status == AppConstants.TASK_IN_PROGRESS) {
            textView.setBackgroundColor(Color.parseColor("#1976D2"));
            textView.setText("In Progress");
        } else if (status == AppConstants.TASK_COMPLETED) {
            textView.setBackgroundColor(Color.parseColor("#757575"));
            textView.setText("Completed");
        }
    }

    private int checkTaskStatusForUser(String username, int taskId) {
        return databaseHelper.checkTaskStatusForUser(username, taskId);
    }
}
