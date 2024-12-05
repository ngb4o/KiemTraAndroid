package cr424ak.nguyengiabao.kiemtra2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;
    private List<Student> studentListFull;
    private SearchView searchView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        
        recyclerView = findViewById(R.id.recyclerView);
        
        studentList = new ArrayList<>();
        studentListFull = new ArrayList<>();
        loadStudentsFromDatabase();
        
        studentAdapter = new StudentAdapter(studentList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(studentAdapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Cập nhật cấu hình SearchView
        searchView = findViewById(R.id.searchView);
        searchView.setQueryHint("Tìm theo tên, mã SV, SĐT hoặc khoa");
        searchView.setIconifiedByDefault(false); // Luôn hiển thị thanh tìm kiếm
        
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 1) {
                    performSearch(newText);
                } else {
                    // Nếu ô tìm kiếm trống, hiển thị lại toàn bộ danh sách
                    studentList.clear();
                    studentList.addAll(dbHelper.getAllStudents());
                    studentAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        // Thêm xử lý cho FABs
        findViewById(R.id.fabAddStudent).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
            startActivityForResult(intent, 1);
        });

        findViewById(R.id.fabDepartment).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DepartmentActivity.class);
            startActivity(intent);
        });
    }

    private void loadStudentsFromDatabase() {
        studentList.clear();
        studentListFull.clear();
        
        List<Student> students = dbHelper.getAllStudents();
        studentList.addAll(students);
        studentListFull.addAll(students);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudentsFromDatabase();
        studentAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Student student = (Student) data.getSerializableExtra("student");
            if (requestCode == 1) { // Thêm mới
                long result = dbHelper.addStudent(student);
                if (result != -1) {
                    loadStudentsFromDatabase();
                    Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi thêm sinh viên", Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == 2) { // Cập nhật
                if (dbHelper.updateStudent(student)) {
                    loadStudentsFromDatabase();
                    Toast.makeText(this, "Cập nhật sinh viên thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi khi cập nhật sinh viên", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void performSearch(String query) {
        if (query.isEmpty()) {
            studentList.clear();
            studentList.addAll(dbHelper.getAllStudents());
        } else {
            List<Student> searchResults = dbHelper.searchStudents(query);
            studentList.clear();
            studentList.addAll(searchResults);
            
            // Hiển thị thông báo kết quả tìm kiếm
            if (searchResults.isEmpty()) {
                Toast.makeText(this, "Không tìm thấy kết quả phù hợp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Tìm thấy " + searchResults.size() + " kết quả", Toast.LENGTH_SHORT).show();
            }
        }
        studentAdapter.notifyDataSetChanged();
    }
}
