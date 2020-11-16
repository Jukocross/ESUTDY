package com.example.convenienestudy;

import java.util.ArrayList;

public class School{

    private int schoolId;
    private String name;
    private ArrayList<Integer> listOfUsers;

    public School() {
    }

    public School(int schoolId, String name){
        this.name = name;
        this.schoolId = schoolId;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getListOfUsers() {
        return listOfUsers;
    }

    public boolean addUser(int id) {
        boolean added = false;
        if (!listOfUsers.contains(id)) {
            this.listOfUsers.add(id);
            added = true;
        }
        return added;
    }

    public boolean removeUser(int id){
        boolean removed = false;
        if (listOfUsers.contains(id)){
            this.listOfUsers.remove(id);
            removed = true;
        }
        return removed;
    }

}
