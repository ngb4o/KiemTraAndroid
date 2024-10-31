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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        edtHoTen = findViewById(R.id.edtHoTen);
        edtMaSV = findViewById(R.id.edtMaSV);
        edtSDT = findViewById(R.id.edtSDT);
        edtEmail = findViewById(R.id.edtEmail);
        edtNgaySinh = findViewById(R.id.edtNgaySinh); // Initialize as TextView
        spinnerKhoa = findViewById(R.id.spinnerKhoa);
        radioGroupGender = findViewById(R.id.radioGroupGender);

        List<String> khoaList = new ArrayList<>();
        khoaList.add("Khoa");
        khoaList.add("Khoa CMU");
        khoaList.add("Khoa PSU");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, khoaList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhoa.setAdapter(adapter);

        // Set up the date picker
        edtNgaySinh.setOnClickListener(view -> showDatePicker());

        Button btnNhap = findViewById(R.id.btnNhap);
        btnNhap.setOnClickListener(view -> saveStudentData());
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

    private void saveStudentData() {
        String name = edtHoTen.getText().toString();
        String studentId = edtMaSV.getText().toString();
        String phoneNumber = edtSDT.getText().toString();
        String email = edtEmail.getText().toString();
        String birthDate = edtNgaySinh.getText().toString(); // Get the formatted date
        String department = spinnerKhoa.getSelectedItem().toString();

        // Validation checks...
        if (name.isEmpty()) {
            edtHoTen.setError("Vui lòng nhập họ và tên");
            edtHoTen.requestFocus();
            return;
        }

        if (studentId.isEmpty()) {
            edtMaSV.setError("Vui lòng nhập mã sinh viên");
            edtMaSV.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()) {
            edtSDT.setError("Vui lòng nhập số điện thoại");
            edtSDT.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            edtEmail.setError("Vui lòng nhập email");
            edtEmail.requestFocus();
            return;
        }

        if (birthDate.isEmpty()) {
            edtNgaySinh.setError("Vui lòng nhập ngày sinh");
            edtNgaySinh.requestFocus();
            return;
        }

        if (department.equals("Khoa")) {
            Toast.makeText(this, "Vui lòng chọn khoa", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId == -1) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gender = selectedGenderButton.getText().toString();

        // Create Student object and proceed
        Student student = new Student(name, studentId, phoneNumber, email, birthDate, department, gender);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("student", student);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
