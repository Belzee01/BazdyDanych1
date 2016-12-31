package database;

public class Queries {

    public static String INSERT_NEW_USER = "INSERT INTO uzytkownicy (imie, nazwisko) VALUES (?, ?);";

    public static String INSERT_NEW_USERS = "INSERT INTO uzytkownicy (id, imie, nazwisko) VALUES (?, ?, ?);";

    public static String SELECT_NEW_USER_ID = "(SELECT id FROM uzytkownicy where nazwisko=?);";

    public static String INSERT_NEW_HASLA = "INSERT INTO hasla (login, password, uzytkownicy_id) VALUES (?, ?, ?);";

    public static String INSERT_NEW_HASLAS = "INSERT INTO hasla (id, login, password, uzytkownicy_id) VALUES (?, ?, ?, ?);";

    public static String SELECT_GIVEN_CREDENTIALS = "select * from hasla where login=? and password=?;";

    public static String INSERT_NEW_ADMIN = "INSERT INTO admini (uzytkownicy_id) VALUES(?);";

    public static String INSERT_NEW_ADMINS = "INSERT INTO admini (id, uzytkownicy_id) VALUES(?, ?);";

    public static class StandardUser {

    }

    public static class AdminUser {
        public static String SELECT_ALL_COMAPNIES = "select * from firmy;";
        public static String SELECT_ALL_PACIENT = "select * from pacjenci;";
        public static String SELECT_ALL_ADMINS = "select * from admini;";
        public static String SELECT_ALL_EXAMINES = "select * from badania;";
        public static String SELECT_ALL_DOCTORS = "select * from lekarze";
        public static String SELECT_ALL_REPORTS = "select * from raporty;";

        public static String SELECT_USER_NAME_AND_SURNAME_BY_ID = "select imie, nazwisko from uzytkownicy where id = ?;";
        public static String SELECT_HASLA_LOGIN_BY_USER_ID = "select login from hasla where uzytkownicy_id=?;";
        public static String DELETE_FROM_ADMIN_LIST = "delete from admini where id =?;";
    }

    public static class DataLoaderQueries {
        public static final String CLEAN_UP_ALL_TABLES = "delete from admini; delete from hasla; delete from uzytkownicy;";
    }
}
