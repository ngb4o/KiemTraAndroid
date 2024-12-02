package cr424ak.nguyengiabao.kiemtra2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DepartmentActivity extends AppCompatActivity {
    private EditText edtDepartmentName;
    private RecyclerView recyclerView;
    private DepartmentAdapter departmentAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department);

        dbHelper = new DatabaseHelper(this);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Quản lý Khoa");

        // Initialize views
        edtDepartmentName = findViewById(R.id.edtDepartmentName);
        Button btnAddDepartment = findViewById(R.id.btnAddDepartment);
        recyclerView = findViewById(R.id.recyclerViewDepartments);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Department> departments = dbHelper.getAllDepartments();
        departmentAdapter = new DepartmentAdapter(departments, this::onDepartmentClick);
        recyclerView.setAdapter(departmentAdapter);

        // Add department button click listener
        btnAddDepartment.setOnClickListener(v -> {
            String departmentName = edtDepartmentName.getText().toString().trim();
            if (!departmentName.isEmpty()) {
                long result = dbHelper.addDepartment(departmentName);
                if (result != -1) {
                    departmentAdapter.updateDepartments(dbHelper.getAllDepartments());
                    edtDepartmentName.setText("");
                    Toast.makeText(this, "Thêm khoa thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi thêm khoa", Toast.LENGTH_SHORT).show();
                }
            } else {
                edtDepartmentName.setError("Vui lòng nhập tên khoa");
            }
        });
    }

    private void onDepartmentClick(Department department) {
        // Handle department click - show students in this department
        List<Student> studentsInDepartment = dbHelper.getStudentsByDepartment(department.getName());
        // You could start a new activity or show a dialog with the list of students
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