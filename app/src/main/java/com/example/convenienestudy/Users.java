package com.example.convenienestudy;

import java.util.UUID;

public abstract class Users {

    private static int idCounter;
    private String name, email, userId;
    private int schoolId;

    public Users() {
    }

    public Users(String name, String email, int schoolID, String userId){
        this.name = name;
        this.email = email;
        this.schoolId = schoolID;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }

    public int getSchoolId() {
        return schoolId;
    }
}
