grandparent(X, Z):-','(parent(X, Y), parent(Y, Z)).
parent(pam, bob).
parent(tom, bob).
parent(tom, liz).
parent(bob, ann).
parent(bob, pat).
parent(pat, m).
female(pam).
female(liz).
female(ann).
female(pat).
predecessor(X, Z):-parent(X, Z).
predecessor(X, Z):-','(parent(X, Y), predecessor(Y, Z)).
male(tom).
male(bob).
male(m).
sister(X, Y):-','(parent(Z, X), ','(parent(Z, Y), ','(female(X), different(X, Y)))).
different(X, X):-','(!, fail).
different(X, Y).
offspring(X, Y):-parent(X, Y).
mother(X, Y):-','(parent(X, Y), female(X)).
