drop view admin_list_view;
drop view patient_list_view;
drop view user_view;

DROP TABLE admini;
DROP TABLE badanie;
DROP TABLE badania;
DROP TABLE pacjent;
DROP TABLE raporty;
DROP TABLE firmy;
DROP TABLE hasla;
DROP TABLE lekarze;
DROP TABLE uzytkownicy;


CREATE TABLE admini (
  id serial  NOT NULL,
  uzytkownicy_id int  NOT NULL,
  CONSTRAINT admini_pk PRIMARY KEY (id)
);

-- Table: badania
CREATE TABLE badania (
  id serial  NOT NULL,
  nazwa varchar(20)  NOT NULL,
  cena int  NOT NULL,
  czas int  NOT NULL,
  CONSTRAINT badania_pk PRIMARY KEY (id)
);

-- Table: badanie
CREATE TABLE badanie (
  id serial  NOT NULL,
  pacjent_id int  NOT NULL,
  badania_id int  NOT NULL,
  lekarze_id int  NOT NULL,
  data date  NOT NULL,
  CONSTRAINT badanie_pk PRIMARY KEY (id)
);

-- Table: firmy
CREATE TABLE firmy (
  id serial  NOT NULL,
  nazwa varchar(20)  NOT NULL,
  NIP varchar(20)  NOT NULL,
  adres varchar(20)  NOT NULL,
  CONSTRAINT firmy_pk PRIMARY KEY (id)
);

-- Table: hasla
CREATE TABLE hasla (
  id serial  NOT NULL,
  login varchar(20)  NOT NULL,
  password varchar(20)  NOT NULL,
  uzytkownicy_id int  NOT NULL,
  CONSTRAINT hasla_pk PRIMARY KEY (id)
);

-- Table: lekarze
CREATE TABLE lekarze (
  id serial  NOT NULL,
  imie varchar(20)  NOT NULL,
  nazwisko varchar(20)  NOT NULL,
  CONSTRAINT lekarze_pk PRIMARY KEY (id)
);

-- Table: pacjent
CREATE TABLE pacjent (
  id serial  NOT NULL,
  imie varchar(20)  NOT NULL,
  nazwisko varchar(20)  NOT NULL,
  firmy_id int  NOT NULL,
  CONSTRAINT pacjent_pk PRIMARY KEY (id)
);

-- Table: raporty
CREATE TABLE raporty (
  id serial  NOT NULL,
  data date  NOT NULL,
  firmy_id int  NOT NULL,
  CONSTRAINT raporty_pk PRIMARY KEY (id)
);

-- Table: uzytkownicy
CREATE TABLE uzytkownicy (
  id serial  NOT NULL,
  imie varchar(20)  NOT NULL,
  nazwisko varchar(20)  NOT NULL,
  typ VARCHAR(20) NOT NULL,
  CONSTRAINT uzytkownicy_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: admini_uzytkownicy (table: admini)
ALTER TABLE admini ADD CONSTRAINT admini_uzytkownicy
FOREIGN KEY (uzytkownicy_id)
REFERENCES uzytkownicy (id)
ON DELETE CASCADE
NOT DEFERRABLE
INITIALLY IMMEDIATE
;

-- Reference: badania_pacjent (table: badanie)
ALTER TABLE badanie ADD CONSTRAINT badania_pacjent
FOREIGN KEY (pacjent_id)
REFERENCES pacjent (id)
NOT DEFERRABLE
INITIALLY IMMEDIATE
;

-- Reference: badanie_badania (table: badanie)
ALTER TABLE badanie ADD CONSTRAINT badanie_badania
FOREIGN KEY (badania_id)
REFERENCES badania (id)
NOT DEFERRABLE
INITIALLY IMMEDIATE
;

-- Reference: badanie_lekarze (table: badanie)
ALTER TABLE badanie ADD CONSTRAINT badanie_lekarze
FOREIGN KEY (lekarze_id)
REFERENCES lekarze (id)
NOT DEFERRABLE
INITIALLY IMMEDIATE
;

-- Reference: hasla_uzytkownicy (table: hasla)
ALTER TABLE hasla ADD CONSTRAINT hasla_uzytkownicy
FOREIGN KEY (uzytkownicy_id)
REFERENCES uzytkownicy (id)
ON DELETE CASCADE
NOT DEFERRABLE
INITIALLY IMMEDIATE
;

-- Reference: pacjent_firmy (table: pacjent)
ALTER TABLE pacjent ADD CONSTRAINT pacjent_firmy
FOREIGN KEY (firmy_id)
REFERENCES firmy (id)
NOT DEFERRABLE
INITIALLY IMMEDIATE
;

-- Reference: raporty_firmy (table: raporty)
ALTER TABLE raporty ADD CONSTRAINT raporty_firmy
FOREIGN KEY (firmy_id)
REFERENCES firmy (id)
NOT DEFERRABLE
INITIALLY IMMEDIATE
;

-- End of file.
create view admin_list_view as select uzytkownicy.id,hasla.login, uzytkownicy.imie, uzytkownicy.nazwisko from uzytkownicy FULL JOIN hasla ON uzytkownicy.id = hasla.uzytkownicy_id where uzytkownicy.id in (select uzytkownicy_id from admini);

CREATE VIEW patient_list_view as select pacjent.id, pacjent.imie, pacjent.nazwisko, firmy.nazwa from pacjent LEFT JOIN firmy ON pacjent.firmy_id = firmy.id;

CREATE VIEW user_view AS select uzytkownicy.id, uzytkownicy.imie, uzytkownicy.nazwisko, hasla.login from uzytkownicy FULL JOIN hasla ON uzytkownicy.id = hasla.uzytkownicy_id;

