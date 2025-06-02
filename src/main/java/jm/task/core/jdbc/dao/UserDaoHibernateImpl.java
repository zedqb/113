package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public static final Logger logger = LoggerFactory.getLogger(UserDaoHibernateImpl.class);

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    last_name VARCHAR(100) UNIQUE NOT NULL,
                    age INTEGER NOT NULL
                )
                """;

        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            NativeQuery<?> query = session.createNativeQuery(createTableSQL);
            query.executeUpdate();

            transaction.commit();
            logger.info("Таблица успешно создана.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при создании таблицы", e);
        }
    }

    @Override
    public void dropUsersTable() {
        String dropTableSql = "DROP TABLE IF EXISTS users";
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            NativeQuery<?> nativeQuery = session.createNativeQuery(dropTableSql);
            nativeQuery.executeUpdate();

            transaction.commit();
            logger.info("Таблица успешно удалена.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при удалении таблицы", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = new User(name, lastName, age);
            session.persist(user);

            transaction.commit();
            logger.info("Пользователь успешно добавлен.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при вставке пользователя", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                logger.info("Пользователь с id {} успешно удален.", id);
            } else {
                logger.warn("Пользователь с id {} не найден.", id);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при удалении пользователя", e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        String hql = "FROM User";
        List<User> users = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            users = session.createQuery(hql, User.class).getResultList();

            transaction.commit();
            logger.info("Пользователи успешно получены. Всего: {}", users.size());
        } catch (Exception e) {
            logger.error("Ошибка при получении пользователей из базы данных", e);
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        String hql = "DELETE FROM User";
        Transaction transaction = null;

        try (Session session = Util.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Query<?> query = (Query<?>) session.createQuery(hql);
            query.executeUpdate();

            transaction.commit();
            logger.info("Таблица успешно очищена.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            logger.error("Ошибка при очистке таблицы", e);
        }
    }
}