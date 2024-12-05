package cr424ak.nguyengiabao.kiemtra2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DepartmentStudentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private DatabaseHelper dbHelper;
    private String departmentName;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_students);

        departmentName = getIntent().getStringExtra("department_name");
        
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sinh viên khoa " + departmentName);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadStudents();
    }

    private void loadStudents() {
        studentList = dbHelper.getStudentsByDepartment(departmentName);
        studentAdapter = new StudentAdapter(studentList);
        recyclerView.setAdapter(studentAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Student updatedStudent = (Student) data.getSerializableExtra("student");
            if (dbHelper.updateStudent(updatedStudent)) {
                loadStudents(); // Tải lại danh sách sinh viên
                Toast.makeText(this, "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi khi cập nhật sinh viên", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 