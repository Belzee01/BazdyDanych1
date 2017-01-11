package database;

public class Queries {

    public static String INSERT_NEW_USER = "INSERT INTO uzytkownicy (imie, nazwisko) VALUES (?, ?);";

    public static String INSERT_NEW_USERS = "INSERT INTO uzytkownicy (imie, nazwisko) VALUES (?, ?);";

    public static String INSERT_NEW_USER_NEXT_SEQ = "insert into uzytkownicy (id, imie, nazwisko) VALUES (nextval('uzytkownicy_id_seq'),?,?);";

    public static String SELECT_NEW_USER_ID = "(SELECT id FROM uzytkownicy where nazwisko=?);";

    public static String SELECT_USER_ID = "(SELECT id FROM uzytkownicy where imie = ? and nazwisko=?);";

    public static String INSERT_NEW_HASLA = "INSERT INTO hasla (login, password, uzytkownicy_id) VALUES (?, ?, ?);";

    public static String INSERT_NEW_HASLA_NEXT_SEQ = "INSERT INTO hasla (id, login, password, uzytkownicy_id) VALUES (nextval('hasla_id_seq'), ?, ?, ?);";

    public static String INSERT_NEW_HASLAS = "INSERT INTO hasla (login, password, uzytkownicy_id) VALUES (?, ?, ?);";

    public static String SELECT_GIVEN_CREDENTIALS = "select * from hasla where login=? and password=?;";

    public static String SELECT_ADMIN = "select * from admini where uzytkownicy_id=?;";

    public static String INSERT_NEW_ADMIN = "INSERT INTO admini (uzytkownicy_id) VALUES(?);";

    public static String UPDATE_ADMIN = "update admini set (uzytkownicy_id) VALUES(?);";

    public static String INSERT_NEW_ADMINS = "INSERT INTO admini (uzytkownicy_id) VALUES(?);";

    public static String INSERT_NEW_EXAMINES = "INSERT INTO badania (nazwa, cena, czas) VALUES(?, ?, ?);";

    public static String INSERT_NEW_BADANIE = "INSERT INTO badanie (pacjent_id, badania_id, lekarze_id, data) VALUES(?, ?, ?, ?);";

    public static String INSERT_NEW_DOCTORS = "INSERT INTO lekarze (imie, nazwisko) VALUES(?, ?);";

    public static String INSERT_NEW_COMPANY = "INSERT INTO firmy (nazwa, nip, adres) VALUES(?, ?, ?);";

    public static String INSERT_NEW_PATIENT = "INSERT INTO pacjent (imie, nazwisko, firmy_id) VALUES(?, ?, ?);";

    public static String SELECT_PATIENT = "select * from patient_list_view where imie=? and nazwisko=? and nazwa=?;";

    public static String SELECT_COMAPNY_ID = "select id from firmy where nazwa=?;";

    public static class StandardUser {

    }

    public static class AdminUser {
        public static String SELECT_ALL_COMAPNIES = "select * from firmy;";
        public static String SELECT_ALL_PACIENT = "select * from pacjent;";
        public static String SELECT_ALL_ADMINS = "select * from admini;";
        public static String SELECT_ALL_EXAMINES = "select * from badania;";
        public static String SELECT_ALL_DOCTORS = "select * from lekarze";
        public static String SELECT_DOCTOR_ID = "select id from lekarze where imie=? and nazwisko=?";
        public static String SELECT_ALL_REPORTS = "select * from raporty;";
        public static String SELECT_ALL_USERS = "select * from uzytkownicy;";


        public static String SELECT_ALL_ADMINS_FROM_VIEW = "SELECT * from admin_list_view;";

        public static String SELECT_ALL_PACIENT_VIEW = "select * from patient_list_view;";

        public static String SELECT_ALL_USERS_VIEW = "select * from user_view;";


        public static String SELECT_USER_NAME_AND_SURNAME_BY_ID = "select imie, nazwisko from uzytkownicy where id = ?;";
        public static String SELECT_HASLA_LOGIN_BY_USER_ID = "select login from hasla where uzytkownicy_id=?;";
        public static String DELETE_FROM_ADMIN_LIST_BY_USER_ID = "delete from admini where uzytkownicy_id =?;";
        public static String DELETE_FROM_EXAMINE_LIST = "delete from badania where id =?;";
        public static String DELETE_FROM_DOCTORS_LIST = "delete from lekarze where id =?;";
        public static String DELETE_FROM_COMPANY_LIST = "delete from firmy where id =?;";
        public static String DELETE_FROM_PATIENT_LIST = "delete from pacjent where id =?;";
        public static String DELETE_FROM_USER_LIST = "delete from uzytkownicy where id =?;";


    }

    public static class DataLoaderQueries {
        public static final String CLEAN_UP_ALL_TABLES = "delete from admini; delete from hasla; delete from uzytkownicy; delete from badania; delete from lekarze;delete from pacjent;delete from firmy;";
    }
}
