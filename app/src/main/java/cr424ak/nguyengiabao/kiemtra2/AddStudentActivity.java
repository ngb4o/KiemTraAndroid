package cr424ak.nguyengiabao.kiemtra2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {

    private EditText edtHoTen, edtMaSV, edtSDT, edtEmail;
    private Spinner spinnerKhoa;
    private RadioGroup radioGroupGender;
    private TextView edtNgaySinh;
    private DatabaseHelper dbHelper;
    private List<Department> departmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        dbHelper = new DatabaseHelper(this);

        edtHoTen = findViewById(R.id.edtHoTen);
        edtMaSV = findViewById(R.id.edtMaSV);
        edtSDT = findViewById(R.id.edtSDT);
        edtEmail = findViewById(R.id.edtEmail);
        edtNgaySinh = findViewById(R.id.edtNgaySinh); // Initialize as TextView
        spinnerKhoa = findViewById(R.id.spinnerKhoa);
        radioGroupGender = findViewById(R.id.radioGroupGender);

        // Setup spinner with departments from database
        setupDepartmentSpinner();

        // Set up the date picker
        edtNgaySinh.setOnClickListener(view -> showDatePicker());

        Button btnNhap = findViewById(R.id.btnNhap);
        btnNhap.setOnClickListener(view -> saveStudentData());
    }

    private void setupDepartmentSpinner() {
        departmentList = dbHelper.getAllDepartments();
        List<String> departmentNames = new ArrayList<>();

        for (Department department : departmentList) {
            departmentNames.add(department.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, departmentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhoa.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh department list when activity resumes
        setupDepartmentSpinner();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    edtNgaySinh.setText(selectedDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Kiểm tra số điện thoại Việt Nam (10 số, bắt đầu bằng 0)
        String phonePattern = "^0\\d{9}$";
        return phoneNumber.matches(phonePattern);
    }

    private boolean isValidEmail(String email) {
        // Kiểm tra định dạng email cơ bản
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        return email.matches(emailPattern);
    }

    private void saveStudentData() {
        String name = edtHoTen.getText().toString().trim();
        String studentId = edtMaSV.getText().toString().trim();
        String phoneNumber = edtSDT.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String birthDate = edtNgaySinh.getText().toString().trim();
        String department = spinnerKhoa.getSelectedItem().toString();

        // Validation checks
        boolean hasError = false;

        if (name.isEmpty()) {
            edtHoTen.setError("Vui lòng nhập họ và tên");
            edtHoTen.requestFocus();
            hasError = true;
        }

        if (studentId.isEmpty()) {
            edtMaSV.setError("Vui lòng nhập mã sinh viên");
            if (!hasError) {
                edtMaSV.requestFocus();
                hasError = true;
            }
        }

        if (phoneNumber.isEmpty()) {
            edtSDT.setError("Vui lòng nhập số điện thoại");
            if (!hasError) {
                edtSDT.requestFocus();
                hasError = true;
            }
        } else if (!isValidPhoneNumber(phoneNumber)) {
            edtSDT.setError("Số điện thoại không hợp lệ (phải có 10 số và bắt đầu bằng số 0)");
            if (!hasError) {
                edtSDT.requestFocus();
                hasError = true;
            }
        }

        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            if (!hasError) {
                edtEmail.requestFocus();
                hasError = true;
            }
        } else if (!isValidEmail(email)) {
            edtEmail.setError("Email không hợp lệ");
            if (!hasError) {
                edtEmail.requestFocus();
                hasError = true;
            }
        }

        if (birthDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        if (spinnerKhoa.getSelectedItemPosition() == -1) {
            Toast.makeText(this, "Vui lòng chọn khoa", Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        if (hasError) {
            return;
        }

        // Nếu tất cả validation đều pass
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gender = selectedGenderButton.getText().toString();

        try {
            // Kiểm tra xem mã sinh viên đã tồn tại chưa
            if (dbHelper.isStudentIdExists(studentId)) {
                edtMaSV.setError("Mã sinh viên đã tồn tại");
                edtMaSV.requestFocus();
                return;
            }

            Student student = new Student(name, studentId, phoneNumber, email, birthDate, department, gender);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("student", student);
            setResult(RESULT_OK, resultIntent);
            finish();
        } catch (Exception e) {
            Toast.makeText(this, "Có lỗi xảy ra: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
