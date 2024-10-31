package cr424ak.nguyengiabao.kiemtra2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;

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

        holder.name.setText(student.getName());
        holder.phoneNumber.setText("Số điện thoại: " + student.getPhoneNumber());
        holder.studentIdAndDepartment.setText("Mã SV: " + student.getStudentId() + " - " + student.getDepartment());
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
    }
}
