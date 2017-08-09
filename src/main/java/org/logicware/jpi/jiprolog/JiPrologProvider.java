package org.logicware.jpi.jiprolog;

import static org.logicware.jpi.PrologAdapterFactory.createPrologAdapter;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import org.logicware.jpi.AbstractProvider;
import org.logicware.jpi.PrologAtom;
import org.logicware.jpi.PrologDouble;
import org.logicware.jpi.PrologEngine;
import org.logicware.jpi.PrologExpression;
import org.logicware.jpi.PrologFloat;
import org.logicware.jpi.PrologInteger;
import org.logicware.jpi.PrologList;
import org.logicware.jpi.PrologLong;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologStructure;
import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.PrologVariable;
import org.logicware.jpi.PrologAdapter;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public class JiPrologProvider extends AbstractProvider implements PrologProvider {

	// constants terms
	static final PrologTerm CUT = new JiPrologCut();
	static final PrologTerm NIL = new JiPrologNil();
	static final PrologTerm FAIL = new JiPrologFail();
	static final PrologTerm TRUE = new JiPrologTrue();
	static final PrologTerm FALSE = new JiPrologFalse();
	static final PrologTerm EMPTY = new JiPrologList();

	final PrologAdapter<JIPTerm> adapter = createPrologAdapter(JiPrologAdapter.class);

	public boolean isCompliant() {
		return false;
	}

	public PrologTerm prologNil() {
		return NIL;
	}

	public PrologTerm prologCut() {
		return CUT;
	}

	public PrologTerm prologFail() {
		return FAIL;
	}

	public PrologTerm prologTrue() {
		return TRUE;
	}

	public PrologTerm prologFalse() {
		return FALSE;
	}

	public PrologTerm prologEmpty() {
		return EMPTY;
	}

	// engine

	public PrologEngine newPrologEngine() {
		return new JiPrologEngine();
	}

	// parser helpers

	public PrologTerm parsePrologTerm(String term) {
		JIPTermParser parser = new JIPEngine().getTermParser();
		return adapter.toTerm(parser.parseTerm(term));
	}

	public PrologTerm[] parsePrologTerms(String stringTerms) {
		JIPTermParser parser = new JIPEngine().getTermParser();
		InputStream inputStream = new ByteArrayInputStream(stringTerms.getBytes());
		Enumeration<JIPTerm> e = parser.parseStream(inputStream, inputStream.toString());
		ArrayList<PrologTerm> terms = new ArrayList<PrologTerm>();
		while (e.hasMoreElements()) {
			JIPTerm term = e.nextElement();
			if (!(term instanceof JIPCons)) {
				terms.add(adapter.toTerm(term));
			} else {

				JIPCons structure = (JIPCons) term;
				int deep = structure.getHeight();
				for (int i = 1; i <= deep; i++) {

					JIPTerm j = structure.getNth(i);

					System.out.println(j + "[ " + j.getClass() + " ]");

					PrologTerm k = adapter.toTerm(structure.getNth(i));

					terms.add(k);

				}

			}
		}
		return terms.toArray(new PrologTerm[0]);
	}

	// terms

	public PrologAtom newPrologAtom(String functor) {
		return new JiPrologAtom(functor);
	}

	public PrologFloat newPrologFloat() {
		return new JiPrologFloat();
	}

	public PrologFloat newPrologFloat(Number value) {
		return new JiPrologFloat(value);
	}

	public PrologDouble newPrologDouble() {
		return new JiPrologDouble();
	}

	public PrologDouble newPrologDouble(Number value) {
		return new JiPrologDouble(value);
	}

	public PrologInteger newPrologInteger() {
		return new JiPrologInteger();
	}

	public PrologInteger newPrologInteger(Number value) {
		return new JiPrologInteger(value);
	}

	public PrologLong newPrologLong() {
		return new JiPrologLong();
	}

	public PrologLong newPrologLong(Number value) {
		return new JiPrologLong(value);
	}

	public PrologVariable newPrologVariable() {
		return new JiPrologVariable();
	}

	public PrologVariable newPrologVariable(String name) {
		return new JiPrologVariable(name);
	}

	public PrologList newPrologList() {
		return new JiPrologList();
	}

	public PrologList newPrologList(PrologTerm[] arguments) {
		return new JiPrologList(arguments);
	}

	public PrologList newPrologList(PrologTerm head, PrologTerm tail) {
		return new JiPrologList(head, tail);
	}

	public PrologList newPrologList(PrologTerm[] arguments, PrologTerm tail) {
		return new JiPrologList(arguments, tail);
	}

	public PrologStructure newPrologStructure(String functor, PrologTerm... arguments) {
		return new JiPrologStructure(functor, arguments);
	}

	public PrologExpression newPrologExpression(PrologTerm left, String operator, PrologTerm right) {
		return new JiPrologExpression(left, operator, right);
	}

}
