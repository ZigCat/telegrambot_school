package com.github.bagasala.ormlite.models;

import com.j256.ormlite.field.DatabaseField;

import java.util.Objects;

public class Group {
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(unique = true)
    private String groupName;


    public Group() {
    }

    public Group(String groupName) {
        this.groupName = groupName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", groupName='" + groupName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id &&
                Objects.equals(groupName, group.groupName) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName);
    }
}
