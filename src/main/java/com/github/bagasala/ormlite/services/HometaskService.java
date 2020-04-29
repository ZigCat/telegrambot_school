package com.github.bagasala.ormlite.services;

import com.github.bagasala.Bot;
import com.github.bagasala.ormlite.models.Hometask;
import com.github.bagasala.ormlite.models.Subject;

import java.sql.SQLException;

public class HometaskService {
    public static boolean isFilePathExist(String filePath) throws SQLException {
        for(Hometask h: Bot.hometaskDao.queryForAll()){
            if(h.getFilePath().equals(filePath)){
                return true;
            }
        }
        return false;
    }

    public static Hometask getByParams(String date, Subject subject) throws SQLException {
        for(Hometask h: Bot.hometaskDao.queryForAll()){
            if(h.getDate().equals(date) && h.getSubject().equals(subject)){
                return h;
            }
        }
        return null;
    }
}
