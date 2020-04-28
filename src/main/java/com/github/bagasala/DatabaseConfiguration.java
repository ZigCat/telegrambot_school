package com.github.bagasala;


import com.github.bagasala.ormlite.models.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseConfiguration {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/telegram_bot?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static final String DB_LOGIN = "root";
    private static final String DB_PASSWORD = "";
    public static ConnectionSource connectionSource;
    static {
        try{
            connectionSource = new JdbcConnectionSource(DB_URL, DB_LOGIN, DB_PASSWORD);
            TableUtils.createTableIfNotExists(connectionSource, UserDb.class);
            TableUtils.createTableIfNotExists(connectionSource, Subject.class);
            TableUtils.createTableIfNotExists(connectionSource, Group.class);
            TableUtils.createTableIfNotExists(connectionSource, Schedule.class);
            TableUtils.createTableIfNotExists(connectionSource, TeacherSubject.class);
            TableUtils.createTableIfNotExists(connectionSource, Hometask.class);
           //TableUtils.createTableIfNotExists(connectionSource, UserGroup.class);
            TableUtils.createTableIfNotExists(connectionSource, Attendance.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
