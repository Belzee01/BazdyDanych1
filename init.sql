-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2016-12-01 19:44:26.809

-- tables
-- Table: Admini
CREATE TABLE Admini (
  id serial  NOT NULL,
  uzytkownicy_id int  NOT NULL,
  CONSTRAINT Admini_pk PRIMARY KEY (id)
);

-- Table: hasla
CREATE TABLE hasla (
  id serial  NOT NULL,
  login varchar(20)  NOT NULL,
  password varchar(20)  NOT NULL,
  uzytkownicy_id int  NOT NULL,
  CONSTRAINT hasla_pk PRIMARY KEY (id)
);

-- Table: uzytkownicy
CREATE TABLE uzytkownicy (
  id serial  NOT NULL,
  imie varchar(20)  NOT NULL,
  nazwisko varchar(20)  NOT NULL,
  CONSTRAINT uzytkownicy_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: Admini_uzytkownicy (table: Admini)
ALTER TABLE Admini ADD CONSTRAINT Admini_uzytkownicy
FOREIGN KEY (uzytkownicy_id)
REFERENCES uzytkownicy (id)
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

-- End of file.



INSERT INTO uzytkownicy (imie, nazwisko) VALUES ('Kajetan', 'Lipensky');

INSERT INTO hasla (login, password, uzytkownicy_id) VALUES ('testUser1', 'testUserPass', (SELECT id FROM uzytkownicy where nazwisko='Lipensky'));

