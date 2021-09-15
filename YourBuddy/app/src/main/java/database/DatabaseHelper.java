package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.database.Cursor;

public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_MOBILE = "mobile";
    public static final String COLUMN_PASSWORD = "password";

    private static final String DATABASE_NAME = "yourbuddy.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private String sqlCreateUserTable = "CREATE TABLE IF NOT EXISTS " +
            TABLE_USERS + "(" + COLUMN_EMAIL + " text primary key , " +
            COLUMN_NAME + " text not null, " + COLUMN_MOBILE + " text not null, " +
            COLUMN_PASSWORD + " text not null);";

    private String insertUser = "INSERT INTO users(email, password, name, mobile) VALUES (\"test@test.com\", \"test\",\"test user\",\"1234567891\")";

    private String sqlCreateTaskTable = "CREATE TABLE IF NOT EXISTS task (task_id integer primary key, task_name text not null,"+
            "task_desc text not null)";

    private String sqlDropTaskTable = "DROP TABLE  IF EXISTS task";

    private String sqlCreateUserTaskTable = "CREATE TABLE IF NOT EXISTS user_task (task_id integer not null, email text not null,"+
            "status integer default 0)";
    private String sqlCreateTaskSubtaskTable = "CREATE TABLE IF NOT EXISTS user_task_subtask (task_id integer not null, email text not null,"+
            "subtask_id integer not null, status integer default 0)";
    private String sqlCreateDetailedTaskTable = "CREATE TABLE IF NOT EXISTS user_detailedtask (task_id integer not null, email text not null,"+
            "subtask_id integer not null, detailedtask_id integer not null, status integer default 0)";
    private String insertTaskTableRow1 = "INSERT INTO task(task_id, task_name,task_desc) VALUES (1, \"Foreign Study\", \"All the required steps if you are applying in a Foreign University\")";
    private String insertTaskTableRow2 = "INSERT INTO task(task_id, task_name,task_desc) VALUES (2, \"Permanent Residence\", \"All the required steps if you are applying for PR in a Foreign Country\")";
    private String insertTaskTableRow3 = "INSERT INTO task(task_id, task_name,task_desc) VALUES (3, \"Personal Travel\", \"All the required steps if you are planning a foreign trip\")";
    // Initialize the database object.

    /**
     * Initialises the database object.
     * @param context the context passed on to the super.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Returns the database name.
     * @return the database name.
     */
    public static String getDatabaseFileName() {
        return DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Create the database with the database creation statement.
        database.execSQL(sqlCreateUserTable);
        //database.execSQL(sqlDropTaskTable);
        database.execSQL(insertUser);
        database.execSQL(sqlCreateTaskTable);
        database.execSQL(insertTaskTableRow1);
        database.execSQL(insertTaskTableRow2);
        database.execSQL(insertTaskTableRow3);
        database.execSQL(sqlCreateUserTaskTable);
        database.execSQL(sqlCreateTaskSubtaskTable);
        database.execSQL(sqlCreateDetailedTaskTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if (oldVersion > 1) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
        //}
    }
    public boolean addUser(String name, String email, String mobile, String password){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put(COLUMN_NAME,name);
        c.put(COLUMN_EMAIL,email);
        c.put(COLUMN_MOBILE,mobile);
        c.put(COLUMN_PASSWORD,password);
        long result=db.insert(TABLE_USERS,null,c);
        if (result==-1){
            return false;
        } else {
            return true;
        }
    }

    public boolean addUserTask(String email, int taskId, int status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("task_id",taskId);
        c.put(COLUMN_EMAIL,email);
        c.put("status", status);
        long result=db.insert("user_task",null,c);
        if (result==-1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateTaskStatus(String email,int taskId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("task_id", taskId);
        contentValues.put("status", status);
        String whereClause = "email='"+email+"' and task_id="+taskId;
        int noOfRowsAffected = db.update("user_task", contentValues, whereClause, null);
        db.close();
        return noOfRowsAffected == 1? true: false;
    }

    public boolean updateSubTaskStatus(String email,int taskId, int subtaskId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("task_id", taskId);
        contentValues.put("subtask_id", subtaskId);
        contentValues.put("status", status);
        String whereClause = "email='"+email+"' and task_id="+taskId+" and subtask_id=" + subtaskId;
        System.out.println("where clause:"+whereClause);

        int noOfRowsAffected = db.update("user_task_subtask", contentValues, whereClause, null);
        System.out.println("noOfRowsAffected:"+noOfRowsAffected);
        db.close();
        return noOfRowsAffected == 1? true: false;
    }

    public boolean updateDetailedTaskStatus(String email,int taskId, int subtaskId, int detailedTaskId, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("task_id", taskId);
        contentValues.put("subtask_id", subtaskId);
        contentValues.put("detailedtask_id", detailedTaskId);
        contentValues.put("status", status);
        String whereClause = "email=? and task_id=? and subtask_id=? and detailedtask_id = ?";
        String whereArgs[] = {email.toString(), String.valueOf(taskId), String.valueOf(subtaskId), String.valueOf(detailedTaskId)};
        int noOfRowsAffected = db.update("user_task", contentValues, whereClause, whereArgs);
        return noOfRowsAffected == 1? true: false;
    }

    public boolean addUserTaskSubTask(String email, int taskId, int subtaskId,int status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("task_id",taskId);
        c.put(COLUMN_EMAIL,email);
        c.put("status", status);
        c.put("subtask_id",subtaskId);
        long result=db.insert("user_task_subtask",null,c);
        if (result==-1){
            return false;
        } else {
            return true;
        }
    }

    public boolean addUserTaskSubTskDetailedTask(String email, int taskId, int subtaskId, int detailedTaskId, int status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues c=new ContentValues();
        c.put("task_id",taskId);
        c.put(COLUMN_EMAIL,email);
        c.put("status", status);
        c.put("subtask_id",subtaskId);
        c.put("detailedtask_id",detailedTaskId);
        long result=db.insert("user_detailedtask",null,c);
        if (result==-1){
            return false;
        } else {
            return true;
        }
    }

    public boolean validateUser(String email,String password)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String query=" select email from "+TABLE_USERS+" where email ='"+email+"' and password='"+password+"'";
        Cursor cursor =db.rawQuery(query,null);
        boolean result = cursor.getCount() == 1 ? true : false;
        cursor.close();
        //SQLiteDatabase database = getWritableDatabase();
        //database.execSQL(sqlDropTaskTable);
        return result;
    }

    public int checkTaskStatusForUser(String email,int taskId)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        int taskStatus = 0;
        String query=" select status from user_task where email ='"+email+"' and task_id='"+taskId+"'";
        Cursor cursor =db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount() <= 0) {
            return taskStatus;
        }
        taskStatus= cursor.getInt(cursor.getColumnIndex("status"));
        cursor.close();
        return taskStatus;
    }

    public int checkSubTaskStatusForUser(String email,int taskId, int subTaskId)
    {
        SQLiteDatabase db=this.getReadableDatabase();
        int taskStatus = 0;
        String query=" select status from user_task_subtask where email ='"+email+"' and task_id='"+taskId+"'" +" and subtask_id='"+subTaskId+"'";
        Cursor cursor =db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount() <= 0) {
            return taskStatus;
        }
        taskStatus= cursor.getInt(cursor.getColumnIndex("status"));
        cursor.close();
        return taskStatus;
    }

    public int checkDetailedTaskStatusForUser(String email,int taskId, int subtaskId, int detailedTaskId )
    {
        SQLiteDatabase db=this.getReadableDatabase();
        int taskStatus = 0;
        String query=" select status from user_detailedtask where email ='"+email+"' and task_id='"+taskId+"'"+ "and subtask_id='"+subtaskId+"'"+
                "and detailedtask_id='"+detailedTaskId+"'";
        Cursor cursor =db.rawQuery(query,null);
        cursor.moveToFirst();
        if(cursor.getCount() <= 0) {
            return taskStatus;
        }
        taskStatus= cursor.getInt(cursor.getColumnIndex("status"));
        cursor.close();
        return taskStatus;
    }
}
