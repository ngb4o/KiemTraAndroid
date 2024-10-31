package cr424ak.nguyengiabao.kiemtra2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {

    private EditText edtHoTen, edtMaSV, edtSDT, edtEmail, edtNgaySinh;
    private Spinner spinnerKhoa;
    private RadioGroup radioGroupGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        edtHoTen = findViewById(R.id.edtHoTen);
        edtMaSV = findViewById(R.id.edtMaSV);
        edtSDT = findViewById(R.id.edtSDT);
        edtEmail = findViewById(R.id.edtEmail);
        edtNgaySinh = findViewById(R.id.edtNgaySinh);
        spinnerKhoa = findViewById(R.id.spinnerKhoa);
        radioGroupGender = findViewById(R.id.radioGroupGender);

        List<String> khoaList = new ArrayList<>();
        khoaList.add("Khoa");
        khoaList.add("Khoa CMU");
        khoaList.add("Khoa PSU");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, khoaList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhoa.setAdapter(adapter);

        Button btnNhap = findViewById(R.id.btnNhap);
        btnNhap.setOnClickListener(view -> saveStudentData());
    }

    private void saveStudentData() {
        String name = edtHoTen.getText().toString();
        String studentId = edtMaSV.getText().toString();
        String phoneNumber = edtSDT.getText().toString();
        String email = edtEmail.getText().toString();
        String birthDate = edtNgaySinh.getText().toString();
        String department = spinnerKhoa.getSelectedItem().toString();

        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton selectedGenderButton = findViewById(selectedGenderId);
        String gender = selectedGenderButton != null ? selectedGenderButton.getText().toString() : "Không xác định";

        Student student = new Student(name, studentId, phoneNumber, email, birthDate, department, gender);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("student", student);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
