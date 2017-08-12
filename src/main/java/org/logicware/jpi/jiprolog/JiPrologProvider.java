package org.logicware.jpi.jiprolog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;

import org.logicware.jpi.AbstractProvider;
import org.logicware.jpi.PrologAtom;
import org.logicware.jpi.PrologConverter;
import org.logicware.jpi.PrologConverterFactory;
import org.logicware.jpi.PrologDouble;
import org.logicware.jpi.PrologEngine;
import org.logicware.jpi.PrologExpression;
import org.logicware.jpi.PrologFloat;
import org.logicware.jpi.PrologIndicator;
import org.logicware.jpi.PrologInteger;
import org.logicware.jpi.PrologList;
import org.logicware.jpi.PrologLong;
import org.logicware.jpi.PrologOperator;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologStructure;
import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.PrologVariable;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public class JiPrologProvider extends AbstractProvider<JIPTerm> implements PrologProvider<JIPTerm> {

	static final PrologConverter<JIPTerm> converter = PrologConverterFactory.createPrologAdapter(JiPrologConverter.class);
	static final PrologProvider<JIPTerm> provider = converter.createProvider();

	// constants terms
	static final PrologTerm CUT = new JiPrologCut(provider);
	static final PrologTerm NIL = new JiPrologNil(provider);
	static final PrologTerm FAIL = new JiPrologFail(provider);
	static final PrologTerm TRUE = new JiPrologTrue(provider);
	static final PrologTerm FALSE = new JiPrologFalse(provider);
	static final PrologTerm EMPTY = new JiPrologList(provider);

	JiPrologProvider(PrologConverter<JIPTerm> adapter) {
		super(adapter);
	}

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
		return new JiPrologEngine(this);
	}

	// parser helpers

	public PrologTerm parsePrologTerm(String term) {
		JIPTermParser parser = new JIPEngine().getTermParser();
		return converter.toTerm(parser.parseTerm(term));
	}

	public PrologTerm[] parsePrologTerms(String stringTerms) {
		JIPTermParser parser = new JIPEngine().getTermParser();
		InputStream inputStream = new ByteArrayInputStream(stringTerms.getBytes());
		Enumeration<JIPTerm> e = parser.parseStream(inputStream, inputStream.toString());
		ArrayList<PrologTerm> terms = new ArrayList<PrologTerm>();
		while (e.hasMoreElements()) {
			JIPTerm term = e.nextElement();
			if (!(term instanceof JIPCons)) {
				terms.add(converter.toTerm(term));
			} else {

				JIPCons structure = (JIPCons) term;
				int deep = structure.getHeight();
				for (int i = 1; i <= deep; i++) {

					JIPTerm j = structure.getNth(i);

					System.out.println(j + "[ " + j.getClass() + " ]");

					PrologTerm k = converter.toTerm(structure.getNth(i));

					terms.add(k);

				}

			}
		}
		return terms.toArray(new PrologTerm[0]);
	}

	// terms

	public PrologAtom newPrologAtom(String functor) {
		return new JiPrologAtom(this, functor);
	}

	public PrologFloat newPrologFloat(Number value) {
		return new JiPrologFloat(this, value);
	}

	public PrologDouble newPrologDouble(Number value) {
		return new JiPrologDouble(this, value);
	}

	public PrologInteger newPrologInteger(Number value) {
		return new JiPrologInteger(this, value);
	}

	public PrologLong newPrologLong(Number value) {
		return new JiPrologLong(this, value);
	}

	public PrologVariable newPrologVariable() {
		return new JiPrologVariable(this);
	}

	public PrologVariable newPrologVariable(String name) {
		return new JiPrologVariable(this, name);
	}

	public PrologList newPrologList() {
		return new JiPrologList(this);
	}

	public PrologList newPrologList(PrologTerm[] arguments) {
		return new JiPrologList(this, arguments);
	}

	public PrologList newPrologList(PrologTerm head, PrologTerm tail) {
		return new JiPrologList(this, head, tail);
	}

	public PrologList newPrologList(PrologTerm[] arguments, PrologTerm tail) {
		return new JiPrologList(this, arguments, tail);
	}

	public PrologStructure newPrologStructure(String functor, PrologTerm... arguments) {
		return new JiPrologStructure(this, functor, arguments);
	}

	public PrologExpression newPrologExpression(PrologTerm left, String operator, PrologTerm right) {
		return new JiPrologExpression(this, left, operator, right);
	}

	public boolean currentPredicate(String functor, int arity) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean currentOperator(int priority, String specifier, String operator) {
		// TODO Auto-generated method stub
		return false;
	}

	public Set<PrologIndicator> currentPredicates() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<PrologOperator> currentOperators() {
		// TODO Auto-generated method stub
		return null;
	}

}
