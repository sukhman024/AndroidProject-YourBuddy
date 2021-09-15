package com.app.yourbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import database.DatabaseHelper;

public class PermanentResidenceStep1ApplyVisaActivity extends AppCompatActivity {

    
    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PermanentResidenceStep1ApplyVisaActivity.this, ForeignStepActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permanent_residence_step1_apply_visa);
        final CheckBox cbPRContact = findViewById(R.id.cb_pr_contact);
        final CheckBox cbPRSubmit = findViewById(R.id.cb_pr_submit);
        final CheckBox cbPRAppear = findViewById(R.id.cb_pr_appear);

        final TextView tvPRContactStatus = findViewById(R.id.status_pr_contact);
        final TextView tvPRSubmitStatus = findViewById(R.id.status_pr_submit);
        final TextView tvPRAppearStatus = findViewById(R.id.status_pr_appear);

        final Button btnDone = findViewById(R.id.btn_done_pr1);

        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        final String username = pref.getString("username", null);
        final int taskId = pref.getInt("curr_task_id", -1);
        final int subtaskId = pref.getInt("curr_subtask_id", -1);
        final int subtaskStatus = pref.getInt("subtask_status", -1);

        final int contactTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_CONTACT_ID);
        final int submitTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_SUBMIT_APPLICATION_ID);
        final int appearTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_APPEAR_INTERVIEW_ID);

        updateStatus(cbPRContact, tvPRContactStatus, contactTaskStatus);
        updateStatus(cbPRSubmit, tvPRSubmitStatus, submitTaskStatus);
        updateStatus(cbPRAppear, tvPRAppearStatus, appearTaskStatus);

        cbPRContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = UpdateTaskDetails(cbPRContact, tvPRContactStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_CONTACT_ID);
                if (isAdded) {
                    cbPRSubmit.setEnabled(true);
                    cbPRSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isAdded = UpdateTaskDetails(cbPRSubmit, tvPRSubmitStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_SUBMIT_APPLICATION_ID);
                            if (isAdded) {
                                cbPRAppear.setEnabled(true);
                                cbPRAppear.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UpdateTaskDetails(cbPRAppear, tvPRAppearStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_APPEAR_INTERVIEW_ID);
                                    }
                                });
                                btnDone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_APPEAR_INTERVIEW_ID);
                                        if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                                            markSubtaskCompleted(username, taskId, subtaskId);
                                        } else {
                                            Toast.makeText(PermanentResidenceStep1ApplyVisaActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
        if (contactTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cbPRSubmit);
            disableTask(cbPRAppear);
            disableButton(btnDone);
            return;
        }
        cbPRSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = UpdateTaskDetails(cbPRSubmit, tvPRSubmitStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_SUBMIT_APPLICATION_ID);
                if (isAdded) {
                    cbPRAppear.setEnabled(true);
                    cbPRAppear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UpdateTaskDetails(cbPRAppear, tvPRAppearStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_APPEAR_INTERVIEW_ID);
                        }
                    });
                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_APPEAR_INTERVIEW_ID);
                            if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                                markSubtaskCompleted(username, taskId, subtaskId);
                            } else {
                                Toast.makeText(PermanentResidenceStep1ApplyVisaActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        if (submitTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cbPRAppear);
            disableButton(btnDone);
            return;
        }
        cbPRAppear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTaskDetails(cbPRAppear, tvPRAppearStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_APPEAR_INTERVIEW_ID);
            }
        });
        if (subtaskStatus == AppConstants.TASK_COMPLETED) {
            btnDone.setEnabled(false);
        } else {
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_APPLY_VISA_DETAILED_TASK_APPEAR_INTERVIEW_ID);
                    if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                        markSubtaskCompleted(username, taskId, subtaskId);
                    } else {
                        Toast.makeText(PermanentResidenceStep1ApplyVisaActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void markSubtaskCompleted(String username, int taskId, int subtaskId) {
        boolean isUpdated = databaseHelper.updateSubTaskStatus(username, taskId, subtaskId, AppConstants.TASK_COMPLETED);
        if (isUpdated) {
            Toast.makeText(PermanentResidenceStep1ApplyVisaActivity.this, "Task completed", Toast.LENGTH_LONG).show();
            startActivity(new Intent(PermanentResidenceStep1ApplyVisaActivity.this, ForeignStepActivity.class));
        } else {
            Toast.makeText(PermanentResidenceStep1ApplyVisaActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }
    }

    private void disableButton(Button btnDone) {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PermanentResidenceStep1ApplyVisaActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean UpdateTaskDetails(CheckBox checkbox, TextView textView, String username, int taskId, int subTaskId, int detailedTaskId) {
        textView.setBackgroundColor(Color.parseColor("#757575"));
        textView.setText("Completed");
        checkbox.setEnabled(false);
        boolean isAdded = databaseHelper.addUserTaskSubTskDetailedTask(username, taskId, subTaskId, detailedTaskId, AppConstants.TASK_COMPLETED);
        if (isAdded) {
            Toast.makeText(PermanentResidenceStep1ApplyVisaActivity.this, "Task Marked as completed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(PermanentResidenceStep1ApplyVisaActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }
        return isAdded;
    }

    private int checkDetailedTaskStatusForUser(String username, int taskId, int subtaskId, int detailedTaskId) {
        return databaseHelper.checkDetailedTaskStatusForUser(username, taskId, subtaskId, detailedTaskId);
    }

    private void disableTask(CheckBox checkBox) {
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PermanentResidenceStep1ApplyVisaActivity.this, AppConstants.TASK_DISBALED_MSG, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateStatus(CheckBox checkBox, TextView textView, int status) {
        if (status == AppConstants.TASK_COMPLETED) {
            textView.setBackgroundColor(Color.parseColor("#757575"));
            textView.setText("Completed");
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }
    }

}
