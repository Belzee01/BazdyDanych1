drop view admin_list_view;
drop view patient_list_view;
drop view user_view;

drop TRIGGER raporty_trigger ON raporty;
drop TRIGGER credentials_trigger ON hasla;
DROP TRIGGER company_check_trigger ON firmy;
DROP TABLE kontener_raportow;
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
  data timestamp  NOT NULL,
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

-- Table: kontener_raportow
CREATE TABLE kontener_raportow (
  id serial  NOT NULL,
  raporty_id int  NOT NULL,
  badanie_id int  NOT NULL,
  CONSTRAINT kontener_raportow_pk PRIMARY KEY (id)
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
  data timestamp  NOT NULL,
  firmy_id int  NOT NULL,
  CONSTRAINT raporty_pk PRIMARY KEY (id)
);

-- Table: uzytkownicy
CREATE TABLE uzytkownicy (
  id serial  NOT NULL,
  imie varchar(20)  NOT NULL,
  nazwisko varchar(20)  NOT NULL,
  typ varchar(20)  NOT NULL,
  CONSTRAINT uzytkownicy_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: admini_uzytkownicy (table: admini)
ALTER TABLE admini ADD CONSTRAINT admini_uzytkownicy
FOREIGN KEY (uzytkownicy_id)
REFERENCES uzytkownicy (id)
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
NOT DEFERRABLE
INITIALLY IMMEDIATE
;

-- Reference: kontener_raportow_badanie (table: kontener_raportow)
ALTER TABLE kontener_raportow ADD CONSTRAINT kontener_raportow_badanie
FOREIGN KEY (badanie_id)
REFERENCES badanie (id)
NOT DEFERRABLE
INITIALLY IMMEDIATE
;

-- Reference: kontener_raportow_raporty (table: kontener_raportow)
ALTER TABLE kontener_raportow ADD CONSTRAINT kontener_raportow_raporty
FOREIGN KEY (raporty_id)
REFERENCES raporty (id)
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

create view admin_list_view as select uzytkownicy.id,hasla.login, uzytkownicy.imie, uzytkownicy.nazwisko from uzytkownicy FULL JOIN hasla ON uzytkownicy.id = hasla.uzytkownicy_id where uzytkownicy.id in (select uzytkownicy_id from admini);

CREATE VIEW patient_list_view as select pacjent.id, pacjent.imie, pacjent.nazwisko, firmy.nazwa from pacjent LEFT JOIN firmy ON pacjent.firmy_id = firmy.id;

CREATE VIEW user_view AS select uzytkownicy.id, uzytkownicy.imie, uzytkownicy.nazwisko, hasla.login, uzytkownicy.typ from uzytkownicy FULL JOIN hasla ON uzytkownicy.id = hasla.uzytkownicy_id;

CREATE OR REPLACE FUNCTION getBadanieId() RETURNS TRIGGER AS $example_table$
DECLARE
  bId RECORD;
  checkDate INTEGER;
BEGIN
  SELECT count(id) into checkDate from raporty where firmy_id = new.firmy_id;
  IF checkDate = 1 THEN
    FOR bId IN (select * from badanie where pacjent_id in (select id from pacjent where firmy_id = new.firmy_id) and badanie.data >  (badanie.data - INTERVAL '1 day')) LOOP
      INSERT into kontener_raportow (raporty_id, badanie_id)  VALUES (new.id, bId.id);
    END LOOP;
  END IF ;
  IF checkDate > 1 THEN
    FOR bId IN (select * from badanie where pacjent_id in (select id from pacjent where firmy_id = new.firmy_id) and badanie.data > (select data from raporty where id = (SELECT id from raporty where firmy_id = new.firmy_id ORDER BY id DESC LIMIT 1 OFFSET 1) and firmy_id = new.firmy_id)) LOOP
      INSERT into kontener_raportow (raporty_id, badanie_id)  VALUES (new.id, bId.id);
    END LOOP;
  END IF;
  RETURN NEW;
END;
$example_table$ LANGUAGE plpgsql;

CREATE TRIGGER raporty_trigger AFTER INSERT ON raporty
FOR ROW EXECUTE PROCEDURE getBadanieId();

CREATE TRIGGER raporty_trigger AFTER INSERT ON raporty
FOR ROW EXECUTE PROCEDURE getBadanieId();

CREATE OR REPLACE FUNCTION checkCredentials() RETURNS TRIGGER AS $example_table$
DECLARE
  bId RECORD;
  checkDate INTEGER;
BEGIN
  SELECT INTO bId * from hasla where login=new.login;
  IF bId.id IS NOT NULL THEN
    RAISE 'Login : % exists', new.login;
  END IF;
  RETURN NEW;
END;
$example_table$ LANGUAGE plpgsql;

CREATE TRIGGER credentials_trigger BEFORE INSERT ON hasla
FOR ROW EXECUTE PROCEDURE checkCredentials();


CREATE OR REPLACE FUNCTION companyCheck() RETURNS TRIGGER AS $example_table$
DECLARE
  bId RECORD;
  checkDate INTEGER;
BEGIN
  SELECT INTO bId * from firmy where nazwa=new.nazwa;
  IF bId.id IS NOT NULL THEN
    RAISE 'Firma o podajnej nazwie juz istnieje : % ', new.nazwa;
  END IF;
  RETURN NEW;
END;
$example_table$ LANGUAGE plpgsql;

CREATE TRIGGER company_check_trigger BEFORE INSERT ON firmy
FOR ROW EXECUTE PROCEDURE companyCheck();
