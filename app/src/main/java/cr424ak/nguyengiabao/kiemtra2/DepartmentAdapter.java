package cr424ak.nguyengiabao.kiemtra2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {
    private List<Department> departments;
    private final OnDepartmentClickListener listener;

    public interface OnDepartmentClickListener {
        void onDepartmentClick(Department department);
    }

    public DepartmentAdapter(List<Department> departments, OnDepartmentClickListener listener) {
        this.departments = departments;
        this.listener = listener;
    }

    public void updateDepartments(List<Department> newDepartments) {
        this.departments = newDepartments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DepartmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_department, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentViewHolder holder, int position) {
        Department department = departments.get(position);
        holder.bind(department, listener);
        
        holder.itemView.findViewById(R.id.btnDeleteDepartment).setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa khoa này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    DatabaseHelper dbHelper = new DatabaseHelper(v.getContext());
                    if (dbHelper.deleteDepartment(department.getId())) {
                        departments.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(v.getContext(), "Đã xóa khoa", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), 
                            "Không thể xóa khoa vì còn sinh viên", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
        });
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    static class DepartmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtDepartmentName;
        private final TextView txtStudentCount;

        public DepartmentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDepartmentName = itemView.findViewById(R.id.txtDepartmentName);
            txtStudentCount = itemView.findViewById(R.id.txtStudentCount);
        }

        public void bind(Department department, OnDepartmentClickListener listener) {
            txtDepartmentName.setText(department.getName());
            txtStudentCount.setText(String.format("Số sinh viên: %d", department.getStudentCount()));
            
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(itemView.getContext(), DepartmentStudentsActivity.class);
                intent.putExtra("department_name", department.getName());
                itemView.getContext().startActivity(intent);
            });
        }
    }
}