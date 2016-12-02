package database;

public class Queries {

    public static String INSERT_NEW_USER = "INSERT INTO uzytkownicy (imie, nazwisko) VALUES (?, ?);";

    public static String SELECT_NEW_USER_ID = "(SELECT id FROM uzytkownicy where nazwisko=?);";

    public static String INSERT_NEW_HASLA = "INSERT INTO hasla (login, password, uzytkownicy_id) VALUES (?, ?, ?);";

}
