CREATE TABLE Estudiant (
	idalumne	serial primary key,
	DNI		varchar(9),
	nom		varchar(20),
	ciutat		varchar(15),
	data_naixement	date
	);

CREATE TABLE Assignatura (
	idassignatura	serial,
	nom		varchar(20),
	departament	varchar(50),
        PRIMARY KEY (Idassignatura)
	);

CREATE TABLE Text (
    ISBN        varchar(10),
    titol   varchar(50),
	editorial		varchar(30),
	autor			varchar(30),
        PRIMARY KEY (ISBN)
	);
	
CREATE TABLE Matricula (
    idalumne        int,
    idassignatura   int references Assignatura(idassignatura),
	nota		int,
    PRIMARY KEY (idalumne,idassignatura),
    FOREIGN KEY (idalumne) REFERENCES Estudiant(idalumne)
	);
	
CREATE TABLE Utilitzat (
    idassignatura   int references Assignatura(idassignatura),
	ISBN			varchar(10) references Text(ISBN),
    PRIMARY KEY (idassignatura, ISBN)
	);
