package cr424ak.nguyengiabao.kiemtra2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private Context context;
    private DatabaseHelper dbHelper;

    public StudentAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.bindStudent(student);
        
        holder.itemView.findViewById(R.id.btnDelete).setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa sinh viên này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    DatabaseHelper dbHelper = new DatabaseHelper(v.getContext());
                    if (dbHelper.deleteStudent(student.getStudentId())) {
                        studentList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(v.getContext(), "Đã xóa sinh viên", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView name, phoneNumber, studentIdAndDepartment;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txtName);
            phoneNumber = itemView.findViewById(R.id.txtPhoneNumber);
            studentIdAndDepartment = itemView.findViewById(R.id.txtStudentIdAndDepartment);
        }

        public void bindStudent(Student student) {
            name.setText(student.getName());
            phoneNumber.setText("Số điện thoại: " + student.getPhoneNumber());
            studentIdAndDepartment.setText("Mã SV: " + student.getStudentId() + " - " + student.getDepartment());
            
            itemView.findViewById(R.id.btnCall).setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + student.getPhoneNumber()));
                itemView.getContext().startActivity(intent);
            });

            itemView.findViewById(R.id.btnEmail).setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + student.getEmail()));
                itemView.getContext().startActivity(intent);
            });

            itemView.findViewById(R.id.btnShare).setOnClickListener(v -> {
                String shareText = "Thông tin sinh viên:\n" +
                        "Tên: " + student.getName() + "\n" +
                        "MSSV: " + student.getStudentId() + "\n" +
                        "Khoa: " + student.getDepartment();
                
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                itemView.getContext().startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
            });
        }
    }
}
