/******************************************************************
 * Terms extension package
 *
 * Copyright (C) 2002-2004 Ugo Chirico
 *
 * This is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *******************************************************************/

:-module(jipxterms, [numbervars/3, free_variables/2, term_variables/2, copy_term/2, name/2,
                      char_code/2, atom_codes/2, atom_chars/2, number_codes/2,
                      atom_length/2, number_chars/2, atom_number/2, atom_concat/3,
                      concat_atom/2, concat_atom/3, upcase/1, downcase/1,
                      upcase_char/2, upcase_chars/2, downcase_char/1, downcase_chars/2,
                      upcase_atom/2, downcase_atom/2, string_to_atom/2, string_to_list/2,
                      string_length/2, string_concat/3, vars/2, sub_atom/5, subsumes_term/2, unify_with_occurs_check/2]).

:-'$custom_built_in'([numbervars/3, free_variables/2, term_variables/2, copy_term/2, name/2,
                      char_code/2, atom_codes/2, atom_chars/2, number_codes/2,
                      atom_length/2, number_chars/2, atom_number/2, atom_concat/3,
                      concat_atom/2, concat_atom/3, upcase/1, downcase/1,
                      upcase_char/2, upcase_chars/2, downcase_char/1, downcase_chars/2,
                      upcase_atom/2, downcase_atom/2, string_to_atom/2, string_to_list/2,
                      string_length/2, string_concat/3, vars/2, sub_atom/5, subsumes_term/2, unify_with_occurs_check/2]).

%:-assert(ver(jipxterms, '4.0.1')).

vars(Term, Vars):-
    xcall('com.ugos.jiprolog.extensions.terms.Vars2', [Term, Vars]).

numbervars(Term, Start, End):-
    xcall('com.ugos.jiprolog.extensions.terms.Numbervars3', [Term, Start, End]).

free_variables(Term, VarList):-
    xcall('com.ugos.jiprolog.extensions.terms.FreeVariables2', [Term, VarList]).

term_variables(Term, VarList):-
    xcall('com.ugos.jiprolog.extensions.terms.FreeVariables2', [Term, VarList]).

copy_term(Term, Copy):-
    xcall('com.ugos.jiprolog.extensions.terms.CopyTerm2', [Term, Copy]).

name(Atom, CharList):-
    xcall('com.ugos.jiprolog.extensions.terms.Name2', [Atom, CharList]).

char_code(Atom, Code):-
    name(Atom, [Code]).

atom_codes(Atom, Codes):-
    name(Atom, Codes).

atom_chars(Atom, Chars) :-
	xcall('com.ugos.jiprolog.extensions.terms.AtomChars2', [Atom, Chars]).


number_codes(Num, List):-
    number(Num),
    !,
    name(Num, List).

number_codes(Num, List):-
    var(Num),
    !,
    name(Num, List),
    number(Num).

number_codes(_,_):-
 	error(type_error(number, 1)).

number_chars(Num, List):-
    number(Num),
    !,
    atom_chars(Num, List).

number_chars(Num, List):-
    var(Num),
    !,
    atom_chars(Num, List),
    number(Num).

number_chars(_,_):-
	error(type_error(number, 1)).

atom_number(Atom, Number):-
    atom_codes(Atom, Codes),
    catch(number_codes(Number, Codes), _, fail).

atom_concat(Atom1, Atom2, Concat):-
    (atom(Atom1) ; number(Atom1)),
    (atom(Atom2) ; number(Atom2)),
    !,
    atom_chars(Atom1, CAtom1),
    atom_chars(Atom2, CAtom2),
    append(CAtom1, CAtom2, CConcat),
    atom_chars(Concat, CConcat),
    !.

atom_concat(Atom1, Atom2, Concat):-
    (atom(Concat) ; number(Concat)),
    !,
    atom_chars(Concat, CConcat),
    append(CAtom1, CAtom2, CConcat),
    atom_chars(Atom1, CAtom1),
    atom_chars(Atom2, CAtom2).

sub_atom(Atom, Before, Length, After, SubAtom):-
	atom_concat(Prefix, Suffix, Atom),
	atom_concat(BeforeAtom, SubAtom, Prefix),
	atom_length(SubAtom, Length),
	atom_length(BeforeAtom, Before),
	atom_length(Suffix, After).


concat_atom([A1], A1).
concat_atom([A1, A2|List], Atom):-
    atom_concat(A1, A2, C),
    concat_atom([C|List], Atom).

concat_atom([A1], Sep, A1).
concat_atom([A1, A2|List], Sep, Atom):-
    atom_concat(A1, Sep, C1),
    atom_concat(C1, A2, C),
    concat_atom([C|List], Sep, Atom).

atom_length(Atom, Len):-
    length(Atom, Len).

atom_prefix(Atom, Prefix):-
    atom_concat(Prefix, _, Atom).

upcase(Char):-
    Char >= 64,
    Char =< 95.

downcase(Char):-
    Char >= 96,
    Char =< 128.

upcase_char(Char, UChar):-
    upcase(Char),
    UChar = Char.

upcase_char(Char, UChar):-
    downcase(Char),
    UChar is Char - 32.

downcase_char(Char, LChar):-
    downcase(Char),
    UChar = Char.

downcase_char(Char, LChar):-
    upcase(Char),
    LChar is Char + 32.

upcase_chars([], []).

upcase_chars([Char|Rest], [UChars|URest]):-
    upcase_char(Char, UChars),
    upcase_chars(Rest, URest).

downcase_chars([], []).

downcase_chars([Char|Rest], [LChars|LRest]):-
    downcase_char(Char, LChars),
    downcase_chars(Rest, LRest).

downcase_atom(Atom, LAtom):-
    name(Atom ,CAtom),
    downcase_chars(CAtom, CLAtom),
    name(LAtom, CLAtom).

upcase_atom(Atom, UAtom):-
    name(Atom ,CAtom),
    upcase_chars(CAtom, CUAtom),
    name(UAtom, CUAtom).

string_to_atom(String, Atom):-
    name(Atom, String).

string_to_list(String, String):-
    string(String),
    !.

string_to_list(_, _):-
	error(type_error(atom, 1)).

string_length(String, Len):-
    length(String, Len).

string_concat(String1, String2, Concat):-
    append(String1, String2, Concat).


subsumes_term(General, Specific) :-
	\+ \+ '$subsumes'(General, Specific).

'$subsumes'(General, Specific) :-
	term_variables(Specific, Vars1),
	General = Specific,
	term_variables(Vars1, Vars2),
	Vars1 == Vars2.

/*
subsumes_term(General, Specific) :-
	term_variables(Specific, Vars),
	subsumes_term(General, Specific, Vars).

subsumes_term(General, Specific, Vars) :-
	var(General),
	!,
	(	var_member_chk(General, Vars) ->
		General == Specific
	;	\+ General \= Specific
	).

subsumes_term(General, Specific, Vars) :-
	nonvar(Specific),
	functor(General, Functor, Arity),
	functor(Specific, Functor, Arity),
	subsumes_term(Arity, General, Specific, Vars).

subsumes_term(0, _, _, _) :-
	!.

subsumes_term(N, General, Specific, Vars) :-
	arg(N, General,  GenArg),
	arg(N, Specific, SpeArg),
	subsumes_term(GenArg, SpeArg, Vars),
	M is N-1, !,
	subsumes_term(M, General, Specific, Vars).
*/

var_member_chk(Var, [Head| Tail]) :-
	(	Var == Head ->
		true
	;	var_member_chk(Var, Tail)
	).

unify_with_occurs_check(Term1, Term2) :-
	Term1 = Term2,
	acyclic_term(Term1).


%*************************************
convert_chars([], []).

convert_chars([C|CharList], [A|AtomList]):-
    name(A, [C]),
    convert_chars(CharList, AtomList).

