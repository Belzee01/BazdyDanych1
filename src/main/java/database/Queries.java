package database;

public class Queries {

    public static String INSERT_NEW_USER = "INSERT INTO uzytkownicy (imie, nazwisko) VALUES (?, ?);";

    public static String SELECT_NEW_USER_ID = "(SELECT id FROM uzytkownicy where nazwisko=?);";

    public static String INSERT_NEW_HASLA = "INSERT INTO hasla (login, password, uzytkownicy_id) VALUES (?, ?, ?);";

    public static String SELECT_GIVEN_CREDENTIALS = "select * from hasla where login=? and password=?;";

    public static class StandardUser {

    }

    public static class AdminUser {
        public static String SELECT_ALL_COMAPNIES = "select * from firmy;";
        public static String SELECT_ALL_PACIENT = "select * from pacjenci;";
        public static String SELECT_ALL_ADMINS = "select * from admini";
        public static String SELECT_ALL_EXAMINES = "select * from badania;";
        public static String SELECT_ALL_DOCTORS = "select * from lekarze";
        public static String SELECT_ALL_REPORTS = "select * from raporty;";
    }
}
