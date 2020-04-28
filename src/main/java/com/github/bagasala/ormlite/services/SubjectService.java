package com.github.bagasala.ormlite.services;

import com.github.bagasala.Bot;
import com.github.bagasala.ormlite.models.Subject;

import java.sql.SQLException;


public class SubjectService {
    public static Subject getSubjectByName(String name) throws SQLException {
        for(Subject s: Bot.subjectDao.queryForAll()){
            if(s.getName().equalsIgnoreCase(name)){
                return s;
            }
        }
        return null;
    }
}
