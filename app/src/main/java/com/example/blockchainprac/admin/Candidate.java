package com.example.blockchainprac.admin;

public class Candidate {

    private String Id;
    private String name;
    private String email;
    private String age;
    private String phone;

    public Candidate() {
    }

    public Candidate(String id, String name, String email, String age, String phone) {
        Id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.phone = phone;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
