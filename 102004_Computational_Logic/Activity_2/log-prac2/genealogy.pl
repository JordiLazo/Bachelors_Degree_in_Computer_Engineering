%
% genealogy facts
%

%% female facts
female(gabriela).
female(noelle).
female(mary).
female(amber).
female(ada).
female(rachel).
female(zoe).
female(karen).
female(anne).
female(angela).
female(agatha).
female(claudia).
female(vanessa).

%% male facts
male(albert).
male(edward).
male(spencer).
male(george).
male(kevin).
male(philip).
male(jack).
male(andrew).
male(oliver).
male(karl).
male(henry).
male(peter).
male(ross).
male(john).

%% Married facts
married(albert, gabriela).
married(edward, noelle).
married(george, mary).
married(spencer, amber).
married(kevin, ada).
married(john, zoe).
married(philip, rachel).
married(jack, karen).
married(oliver, agatha).

%% Parent facts
parent(albert, edward).
parent(gabriela, edward).
parent(edward, george).
parent(noelle, george).
parent(george, spencer).
parent(mary, spencer). 
parent(george, kevin).
parent(mary, kevin).
parent(kevin, rachel).
parent(ada, rachel).
parent(kevin, zoe).
parent(ada, zoe).
parent(rachel, jack).
parent(philip, jack).
parent(rachel, anne).
parent(philip, anne).
parent(rachel, andrew).
parent(philip, andrew).
parent(rachel, oliver).
parent(philip, oliver).
parent(rachel, angela).
parent(philip, angela).
parent(john, vanessa).
parent(zoe, vanessa).
parent(jack, karl).
parent(karen, karl).
parent(jack, henry).
parent(karen, henry).
parent(jack, peter).
parent(karen, peter).
parent(oliver, claudia).
parent(agatha, claudia).
parent(oliver, ross).
parent(agatha, ross).

%% 1- father(X,Y): evaluará a cierto si X es el padre de Y.
father(X,Y) :- male(X), parent(X, Y).
father(X,Y) :- male(X), married(X, Z), parent(Z, Y).
 
%% 2- mother(X,Y): evaluará a cierto si X es la madre de Y.
mother(X,Y) :- female(X), parent(X, Y).
mother(X,Y) :- female(X), married(X, Z), parent(Z, Y).

%% 3- son(X,Y): evaluará a cierto si X es el hijo de Y.
son(X,Y) :- male(X), parent(Y, X).

%% 4- daughter(X,Y): evaluará a cierto si X es la hija de Y.
daughter(X,Y) :- female(X), parent(Y, X).

%% 5- brother(X,Y): evaluará a cierto si X es hermano de Y.
brother(X,Y) :- male(X), parent(Z, X), parent(Z, Y),X\=Y.

%% 6- sister(X,Y): evaluará a cierto si X es hermana de Y.
sister(X,Y) :- female(X), parent(Z, X), parent(Z, Y),X\=Y.

%% 7- uncle(X,Y): evaluará a cierto si X es tío de Y.
uncle(X,Y) :- brother(X, Z), parent(Z, Y).
uncle(X,Y) :- married(X, Z), brother(Z, A), parent(A, Y).

%% 8- aunt(X,Y): evaluará a cierto si X es tía de Y.
aunt(X,Y) :- sister(X, Z), parent(Z, Y).
aunt(X,Y) :- married(X, Z), sister(Z, A), parent(A, Y).

%% 9- grandparent(X,Y): evaluará a cierto si X es abuelo/a de Y.
grandparent(X,Y) :- parent(X, Z), parent(Z, Y).

%% 10- grandchild(X,Y): evaluará a cierto si X es nieto/a de Y.
grandchild(X,Y) :- parent(Y, Z), parent(Z, X).

%% 11- cousin(X,Y): evaluará a cierto si X es primo/a de Y.
cousin(X,Y) :- son(X, Z), uncle(Z, Y).
cousin(X,Y) :- daughter(X, Z), uncle(Z, Y).
cousin(X,Y) :- son(X, Z), aunt(Z, Y).
cousin(X,Y) :- daughter(X, Z), aunt(Z, Y).

%% 12- siblings(X,Y): evaluará a cierto si X es hermano/a de Y.
siblings(X,Y) :- parent(Z, X), parent(Z, Y),X\=Y.

%% He hecho la comprobación en: https://swish.swi-prolog.org/

%% Instrucciones para instalar prolog en linux:

%% 1- sudo apt-add-repository ppa:swi-prolog/stable
%% 2- sudo apt-get update
%% 3- sudo apt-get install swi-prolog

%% Como ejecutar comprobaciones en prolog:
%% Ejemplo: grandchild(george,albert). (Y asi con todas los predicatos escritos).
%% Si devuelve true es verdad, si es false