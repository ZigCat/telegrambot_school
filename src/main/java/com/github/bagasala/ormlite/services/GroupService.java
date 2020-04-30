package com.github.bagasala.ormlite.services;

import com.github.bagasala.Bot;
import com.github.bagasala.ormlite.models.Group;

import java.sql.SQLException;

public class GroupService {
    public static Group getGroupById(int id) throws SQLException {
        for(Group g: Bot.groupDao.queryForAll()){
            if(g.getId() == id){
                return g;
            }
        }
        return null;
    }
}
