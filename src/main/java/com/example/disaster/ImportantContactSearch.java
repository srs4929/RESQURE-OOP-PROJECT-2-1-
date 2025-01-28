package com.example.disaster;

public class ImportantContactSearch {

    Integer id;
    String name,role,phone,location;
    public ImportantContactSearch(Integer id,String name,String role,String phone,String location)
    {
        this.id=id;
        this.name=name;
        this.role=role;
        this.phone=phone;
        this.location=location;
    }

    public Integer getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getLocation() {
        return location;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
       this. role = role;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
