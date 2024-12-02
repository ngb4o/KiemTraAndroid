package cr424ak.nguyengiabao.kiemtra2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
            itemView.setOnClickListener(v -> listener.onDepartmentClick(department));
        }
    }
}