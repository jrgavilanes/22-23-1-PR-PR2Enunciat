package uoc.ds.pr.model;


import java.time.LocalDate;

public class Worker {
    private String dni;
    private String name;
    private String surname;
    private LocalDate birthday;
    private String roleId;

    public Worker(String dni, String name, String surname, LocalDate birthday, String roleId) {
        this.dni = dni;
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.roleId = roleId;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


}