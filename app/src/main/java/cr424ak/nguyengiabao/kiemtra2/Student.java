package cr424ak.nguyengiabao.kiemtra2;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private String studentId;

    public Student(String name, String studentId, String phoneNumber, String email, String birthDate, String department, String gender) {
        this.name = name;
        this.studentId = studentId;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.birthDate = birthDate;
        this.department = department;
        this.gender = gender;
    }

    private String phoneNumber;
    private String email;
    private String birthDate;
    private String department;
    private String gender;


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }


}

