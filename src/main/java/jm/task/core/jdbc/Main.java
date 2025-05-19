package jm.task.core.jdbc;
import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoJDBCImpl();
        UserService userService = new UserServiceImpl(userDao);

        userService.saveUser("Миша", "Журиков", (byte) 25);
        userService.saveUser("Вадим", "Старостин", (byte) 33);
        userService.saveUser("Миша", "Иванков", (byte) 32);
        userService.saveUser("Вин", "Дизель", (byte) 63);
    }
}









