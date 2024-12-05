package cr424ak.nguyengiabao.kiemtra2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_STUDENT = "students";
    private static final String TABLE_DEPARTMENT = "departments";

    // Common column names
    private static final String KEY_ID = "id";
    
    // Student Table columns
    private static final String KEY_NAME = "name";
    private static final String KEY_STUDENT_ID = "student_id";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_BIRTH_DATE = "birth_date";
    private static final String KEY_DEPARTMENT = "department";
    private static final String KEY_GENDER = "gender";

    // Department Table columns
    private static final String KEY_DEPARTMENT_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Department table
        String CREATE_DEPARTMENT_TABLE = "CREATE TABLE " + TABLE_DEPARTMENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_DEPARTMENT_NAME + " TEXT UNIQUE"
                + ")";
        db.execSQL(CREATE_DEPARTMENT_TABLE);

        // Create Student table
        String CREATE_STUDENT_TABLE = "CREATE TABLE " + TABLE_STUDENT + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_STUDENT_ID + " TEXT UNIQUE,"
                + KEY_PHONE + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_BIRTH_DATE + " TEXT,"
                + KEY_DEPARTMENT + " TEXT,"
                + KEY_GENDER + " TEXT"
                + ")";
        db.execSQL(CREATE_STUDENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENT);
        onCreate(db);
    }

    // Add new student
    public long addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, student.getName());
        values.put(KEY_STUDENT_ID, student.getStudentId());
        values.put(KEY_PHONE, student.getPhoneNumber());
        values.put(KEY_EMAIL, student.getEmail());
        values.put(KEY_BIRTH_DATE, student.getBirthDate());
        values.put(KEY_DEPARTMENT, student.getDepartment());
        values.put(KEY_GENDER, student.getGender());

        long id = db.insert(TABLE_STUDENT, null, values);
        db.close();
        return id;
    }

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student(
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                    cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(KEY_BIRTH_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_DEPARTMENT)),
                    cursor.getString(cursor.getColumnIndex(KEY_GENDER))
                );
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;
    }

    // Search students by name or ID
    public List<Student> searchStudents(String query) {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Tìm kiếm theo tên, mã SV, SĐT hoặc khoa
        String selectQuery = "SELECT * FROM " + TABLE_STUDENT + 
                            " WHERE " + KEY_NAME + " LIKE ? OR " + 
                            KEY_STUDENT_ID + " LIKE ? OR " +
                            KEY_PHONE + " LIKE ? OR " +
                            KEY_DEPARTMENT + " LIKE ?";
                            
        String searchPattern = "%" + query + "%";
        String[] selectionArgs = new String[]{
            searchPattern, 
            searchPattern, 
            searchPattern, 
            searchPattern
        };

        Cursor cursor = db.rawQuery(selectQuery, selectionArgs);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Student student = new Student(
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                    cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(KEY_BIRTH_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_DEPARTMENT)),
                    cursor.getString(cursor.getColumnIndex(KEY_GENDER))
                );
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;
    }

    // Get students by department
    public List<Student> getStudentsByDepartment(String department) {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_STUDENT + 
                           " WHERE " + KEY_DEPARTMENT + " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{department});

        if (cursor.moveToFirst()) {
            do {
                Student student = new Student(
                    cursor.getString(cursor.getColumnIndex(KEY_NAME)),
                    cursor.getString(cursor.getColumnIndex(KEY_STUDENT_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_PHONE)),
                    cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(KEY_BIRTH_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_DEPARTMENT)),
                    cursor.getString(cursor.getColumnIndex(KEY_GENDER))
                );
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;
    }

    public long addDepartment(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEPARTMENT_NAME, name);
        long id = db.insert(TABLE_DEPARTMENT, null, values);
        db.close();
        return id;
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT d.*, COUNT(s." + KEY_ID + ") as student_count " +
                            "FROM " + TABLE_DEPARTMENT + " d " +
                            "LEFT JOIN " + TABLE_STUDENT + " s " +
                            "ON d." + KEY_DEPARTMENT_NAME + " = s." + KEY_DEPARTMENT + " " +
                            "GROUP BY d." + KEY_ID;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Department department = new Department(
                    cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(KEY_DEPARTMENT_NAME))
                );
                @SuppressLint("Range") int studentCount = cursor.getInt(cursor.getColumnIndex("student_count"));
                department.setStudentCount(studentCount);
                departments.add(department);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return departments;
    }

    public boolean deleteStudent(String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_STUDENT, KEY_STUDENT_ID + "=?", new String[]{studentId}) > 0;
    }

    public boolean deleteDepartment(int departmentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Kiểm tra xem có sinh viên nào trong khoa không
        Cursor cursor = db.query(TABLE_STUDENT, null, 
            KEY_DEPARTMENT + "=?", 
            new String[]{String.valueOf(departmentId)}, 
            null, null, null);
        
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Không thể xóa vì còn sinh viên trong khoa
        }
        cursor.close();
        return db.delete(TABLE_DEPARTMENT, KEY_ID + "=?", new String[]{String.valueOf(departmentId)}) > 0;
    }

    public boolean isStudentIdExists(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_STUDENT, 
            new String[]{KEY_STUDENT_ID}, 
            KEY_STUDENT_ID + "=?",
            new String[]{studentId}, 
            null, null, null);
        
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, student.getName());
        values.put(KEY_PHONE, student.getPhoneNumber());
        values.put(KEY_EMAIL, student.getEmail());
        values.put(KEY_BIRTH_DATE, student.getBirthDate());
        values.put(KEY_DEPARTMENT, student.getDepartment());
        values.put(KEY_GENDER, student.getGender());

        // Cập nhật theo mã sinh viên
        int result = db.update(TABLE_STUDENT, 
            values, 
            KEY_STUDENT_ID + "=?", 
            new String[]{student.getStudentId()});
        
        db.close();
        return result > 0;
    }
}