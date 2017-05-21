package org.logicware.jpi.jiprolog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import org.logicware.jpi.IPrologAtom;
import org.logicware.jpi.IPrologDouble;
import org.logicware.jpi.IPrologEngine;
import org.logicware.jpi.IPrologExpression;
import org.logicware.jpi.IPrologFloat;
import org.logicware.jpi.IPrologInteger;
import org.logicware.jpi.IPrologList;
import org.logicware.jpi.IPrologLong;
import org.logicware.jpi.IPrologProvider;
import org.logicware.jpi.IPrologStructure;
import org.logicware.jpi.IPrologTerm;
import org.logicware.jpi.IPrologVariable;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public class JiPrologProvider extends JiPrologAbstract implements IPrologProvider {

	// constants terms
	static final IPrologTerm CUT = new JiPrologCut();
	static final IPrologTerm NIL = new JiPrologNil();
	static final IPrologTerm FAIL = new JiPrologFail();
	static final IPrologTerm TRUE = new JiPrologTrue();
	static final IPrologTerm FALSE = new JiPrologFalse();
	static final IPrologTerm EMPTY = new JiPrologList();

	public IPrologTerm prologNil() {
		return NIL;
	}

	public IPrologTerm prologCut() {
		return CUT;
	}

	public IPrologTerm prologFail() {
		return FAIL;
	}

	public IPrologTerm prologTrue() {
		return TRUE;
	}

	public IPrologTerm prologFalse() {
		return FALSE;
	}

	public IPrologTerm prologEmpty() {
		return EMPTY;
	}

	// engine

	public IPrologEngine newPrologEngine() {
		return new JiPrologEngine();
	}

	// parser helpers

	public IPrologTerm parsePrologTerm(String term) {
		JIPTermParser parser = new JIPEngine().getTermParser();
		return adapt(parser.parseTerm(term));
	}

	public IPrologList parsePrologList(String stringList) {
		IPrologTerm term = parsePrologTerm(stringList);
		checkListType(term);
		return (IPrologList) term;
	}

	public IPrologTerm[] parsePrologTerms(String stringTerms) {
		JIPTermParser parser = new JIPEngine().getTermParser();
		InputStream inputStream = new ByteArrayInputStream(stringTerms.getBytes());
		Enumeration<JIPTerm> e = parser.parseStream(inputStream, inputStream.toString());
		ArrayList<IPrologTerm> terms = new ArrayList<IPrologTerm>();
		while (e.hasMoreElements()) {
			JIPTerm term = e.nextElement();
			if (!(term instanceof JIPCons)) {
				terms.add(adapt(term));
			} else {

				JIPCons structure = (JIPCons) term;
				int deep = structure.getHeight();
				for (int i = 1; i <= deep; i++) {

					JIPTerm j = structure.getNth(i);

					System.out.println(j + "[ " + j.getClass() + " ]");

					IPrologTerm k = adapt(structure.getNth(i));

					terms.add(k);

				}

			}
		}
		return terms.toArray(new IPrologTerm[0]);
	}

	public IPrologStructure parsePrologStructure(String stringStructure) {
		IPrologTerm term = parsePrologTerm(stringStructure);
		checkStructureType(term);
		return (IPrologStructure) term;
	}

	public IPrologExpression parsePrologExpression(String stringExpression) {
		IPrologTerm term = parsePrologTerm(stringExpression);
		checkExpressionType(term);
		return (IPrologExpression) term;
	}

	// terms

	public IPrologAtom newPrologAtom(String functor) {
		return new JiPrologAtom(functor);
	}

	public IPrologFloat newPrologFloat() {
		return new JiPrologFloat();
	}

	public IPrologFloat newPrologFloat(Number value) {
		return new JiPrologFloat(value);
	}

	public IPrologDouble newPrologDouble() {
		return new JiPrologDouble();
	}

	public IPrologDouble newPrologDouble(Number value) {
		return new JiPrologDouble(value);
	}

	public IPrologInteger newPrologInteger() {
		return new JiPrologInteger();
	}

	public IPrologInteger newPrologInteger(Number value) {
		return new JiPrologInteger(value);
	}

	public IPrologLong newPrologLong() {
		return new JiPrologLong();
	}

	public IPrologLong newPrologLong(Number value) {
		return new JiPrologLong(value);
	}

	public IPrologVariable newPrologVariable() {
		return new JiPrologVariable();
	}

	public IPrologVariable newPrologVariable(String name) {
		return new JiPrologVariable(name);
	}

	public IPrologList newPrologList() {
		return new JiPrologList();
	}

	public IPrologList newPrologList(IPrologTerm[] arguments) {
		return new JiPrologList(adapt(arguments));
	}

	public IPrologList newPrologList(IPrologTerm head, IPrologTerm tail) {
		return new JiPrologList(head, tail);
	}

	public IPrologList newPrologList(IPrologTerm[] arguments, IPrologTerm tail) {
		return new JiPrologList(adapt(arguments), adapt(tail));
	}

	public IPrologStructure newPrologStructure(String functor, IPrologTerm... arguments) {
		return new JiPrologStructure(functor, adapt(arguments));
	}

	public IPrologExpression newPrologExpression(IPrologTerm left, String operator, IPrologTerm right) {
		return new JiPrologExpression(left, operator, right);
	}

}
