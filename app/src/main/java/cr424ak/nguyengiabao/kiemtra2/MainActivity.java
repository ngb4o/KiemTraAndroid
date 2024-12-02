package cr424ak.nguyengiabao.kiemtra2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        // Thiết lập SearchView bên ngoài Toolbar
        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<Student> searchResults = dbHelper.searchStudents(query);
                studentList.clear();
                studentList.addAll(searchResults);
                studentAdapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    studentList.clear();
                    studentList.addAll(dbHelper.getAllStudents());
                } else {
                    List<Student> searchResults = dbHelper.searchStudents(newText);
                    studentList.clear();
                    studentList.addAll(searchResults);
                }
                studentAdapter.notifyDataSetChanged();
                return false;
            }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
            startActivityForResult(intent, 1);
            return true;
        } else if (id == R.id.action_department) {
            Intent intent = new Intent(MainActivity.this, DepartmentActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Student newStudent = (Student) data.getSerializableExtra("student");
            long result = dbHelper.addStudent(newStudent);
            if (result != -1) {
                studentList.clear();
                studentList.addAll(dbHelper.getAllStudents());
                studentAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Thêm sinh viên thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Lỗi khi thêm sinh viên", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
