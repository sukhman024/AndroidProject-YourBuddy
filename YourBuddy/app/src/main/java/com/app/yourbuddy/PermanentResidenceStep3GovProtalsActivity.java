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

public class PermanentResidenceStep3GovProtalsActivity extends AppCompatActivity {

    

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    public void onBackPressed() {
        startActivity(new Intent(PermanentResidenceStep3GovProtalsActivity.this, PermanentResidenceActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permanent_residence_step3_gov_protals);
        final CheckBox cbApplyMedicare = findViewById(R.id.cb_apply_medicare);
        final CheckBox cbApplyTfn = findViewById(R.id.cb_apply_tfn);
        final CheckBox cbApplyLicense = findViewById(R.id.cb_apply_license);

        final TextView tvApplyMedicareStatus = findViewById(R.id.status_apply_medicare);
        final TextView tvApplyTfnStatus = findViewById(R.id.status_apply_tfn);
        final TextView tvApplyLicenseStatus = findViewById(R.id.status_apply_license);

        final Button btnDone = findViewById(R.id.btn_done_pr3);

        SharedPreferences pref = getSharedPreferences("YourBuddy",
                Context.MODE_PRIVATE);
        final String username = pref.getString("username", null);
        final int taskId = pref.getInt("curr_task_id", -1);
        final int subtaskId = pref.getInt("curr_subtask_id", -1);
        final int subtaskStatus = pref.getInt("subtask_status", -1);

        final int applyMedicareTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_MEDICARE_ID);
        final int applyTfnTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_TFN_ID);
        final int applyLicenseTaskStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_LICENSE_ID);

        updateStatus(cbApplyMedicare, tvApplyMedicareStatus, applyMedicareTaskStatus);
        updateStatus(cbApplyTfn, tvApplyTfnStatus, applyTfnTaskStatus);
        updateStatus(cbApplyLicense, tvApplyLicenseStatus, applyLicenseTaskStatus);

        cbApplyMedicare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = UpdateTaskDetails(cbApplyMedicare, tvApplyMedicareStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_MEDICARE_ID);
                if (isAdded) {
                    cbApplyTfn.setEnabled(true);
                    cbApplyTfn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean isAdded = UpdateTaskDetails(cbApplyTfn, tvApplyTfnStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_TFN_ID);
                            if (isAdded) {
                                cbApplyLicense.setEnabled(true);
                                cbApplyLicense.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        UpdateTaskDetails(cbApplyLicense, tvApplyLicenseStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_LICENSE_ID);
                                    }
                                });
                                btnDone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_LICENSE_ID);
                                        if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                                            markSubtaskCompleted(username, taskId, subtaskId);
                                            markTaskCompleted(username, taskId);
                                        } else {
                                            Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
        if (applyMedicareTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cbApplyTfn);
            disableTask(cbApplyLicense);
            disableButton(btnDone);
            return;
        }
        cbApplyTfn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAdded = UpdateTaskDetails(cbApplyTfn, tvApplyTfnStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_TFN_ID);
                if (isAdded) {
                    cbApplyLicense.setEnabled(true);
                    cbApplyLicense.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UpdateTaskDetails(cbApplyLicense, tvApplyLicenseStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_TFN_ID);
                        }
                    });
                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int applyLicenseTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_LICENSE_ID);
                            if (applyLicenseTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                                markSubtaskCompleted(username, taskId, subtaskId);
                                markTaskCompleted(username, taskId);
                            } else {
                                Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
        if (applyTfnTaskStatus == AppConstants.TASK_NOT_STARTED) {
            disableTask(cbApplyLicense);
            disableButton(btnDone);
            return;
        }
        cbApplyLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateTaskDetails(cbApplyLicense, tvApplyLicenseStatus, username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_LICENSE_ID);
            }
        });
        if (subtaskStatus == AppConstants.TASK_COMPLETED) {
            btnDone.setEnabled(false);
        } else {
            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int appearTaskCurrentStatus = checkDetailedTaskStatusForUser(username, taskId, subtaskId, AppConstants.PERMANENT_RESIDENCE_GOV_PORTAL_DETAILED_TASK_APPLY_LICENSE_ID);
                    if (appearTaskCurrentStatus == AppConstants.TASK_COMPLETED) {
                        markSubtaskCompleted(username, taskId, subtaskId);
                        markTaskCompleted(username, taskId);
                    } else {
                        Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void markTaskCompleted(String username, int taskId) {
        boolean isUpdated = databaseHelper.updateTaskStatus(username, taskId, AppConstants.TASK_COMPLETED);
        if (isUpdated) {
            Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Main Task completed", Toast.LENGTH_LONG).show();
            startActivity(new Intent(PermanentResidenceStep3GovProtalsActivity.this, PermanentResidenceActivity.class));
        } else {
            Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }
    }

    private void markSubtaskCompleted(String username, int taskId, int subtaskId) {
        boolean isUpdated = databaseHelper.updateSubTaskStatus(username, taskId, subtaskId, AppConstants.TASK_COMPLETED);
        if (isUpdated) {
            Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Task completed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Error!!", Toast.LENGTH_LONG).show();
        }
    }

    private void disableButton(Button btnDone) {
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Task can't be marked completed until all the activities are completed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean UpdateTaskDetails(CheckBox checkbox, TextView textView, String username, int taskId, int subTaskId, int detailedTaskId) {
        textView.setBackgroundColor(Color.parseColor("#757575"));
        textView.setText("Completed");
        checkbox.setEnabled(false);
        boolean isAdded = databaseHelper.addUserTaskSubTskDetailedTask(username, taskId, subTaskId, detailedTaskId, AppConstants.TASK_COMPLETED);
        if (isAdded) {
            Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Task Marked as completed", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, "Error!!", Toast.LENGTH_LONG).show();
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
                Toast.makeText(PermanentResidenceStep3GovProtalsActivity.this, AppConstants.TASK_DISBALED_MSG, Toast.LENGTH_LONG).show();
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
