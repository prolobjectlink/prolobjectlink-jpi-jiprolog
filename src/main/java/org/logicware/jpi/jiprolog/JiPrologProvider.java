package org.logicware.jpi.jiprolog;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import org.logicware.jpi.AbstractProvider;
import org.logicware.jpi.PrologAtom;
import org.logicware.jpi.PrologConverter;
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

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public class JiPrologProvider extends AbstractProvider implements PrologProvider {

    JiPrologProvider() {
	super(new JiPrologConverter());
    }

    JiPrologProvider(PrologConverter<JIPTerm> converter) {
	super(converter);
    }

    public boolean isCompliant() {
	return false;
    }

    public PrologTerm prologNil() {
	return new JiPrologNil(this);
    }

    public PrologTerm prologCut() {
	return new JiPrologCut(this);
    }

    public PrologTerm prologFail() {
	return new JiPrologFail(this);
    }

    public PrologTerm prologTrue() {
	return new JiPrologTrue(this);
    }

    public PrologTerm prologFalse() {
	return new JiPrologFalse(this);
    }

    public PrologTerm prologEmpty() {
	return new JiPrologEmpty(this);
    }

    // engine

    public PrologEngine newEngine() {
	return new JiPrologEngine(this);
    }

    // parser helpers

    public PrologTerm parsePrologTerm(String term) {
	JIPTermParser parser = new JIPEngine().getTermParser();
	return toTerm(parser.parseTerm(term), PrologTerm.class);
    }

    public PrologTerm[] parsePrologTerms(String stringTerms) {
	JIPTermParser parser = new JIPEngine().getTermParser();
	InputStream inputStream = new ByteArrayInputStream(stringTerms.getBytes());
	Enumeration<JIPTerm> e = parser.parseStream(inputStream, inputStream.toString());
	ArrayList<PrologTerm> terms = new ArrayList<PrologTerm>();
	while (e.hasMoreElements()) {
	    JIPTerm term = e.nextElement();
	    if (!(term instanceof JIPCons)) {
		terms.add(toTerm(term, PrologTerm.class));
	    } else {

		JIPCons structure = (JIPCons) term;
		int deep = structure.getHeight();
		for (int i = 1; i <= deep; i++) {

		    JIPTerm j = structure.getNth(i);

		    System.out.println(j + "[ " + j.getClass() + " ]");

		    PrologTerm k = toTerm(structure.getNth(i), PrologTerm.class);

		    terms.add(k);

		}

	    }
	}
	return terms.toArray(new PrologTerm[0]);
    }

    // terms

    public PrologAtom newAtom(String functor) {
	return new JiPrologAtom(this, functor);
    }

    public PrologFloat newFloat(Number value) {
	return new JiPrologFloat(this, value);
    }

    public PrologDouble newDouble(Number value) {
	return new JiPrologDouble(this, value);
    }

    public PrologInteger newInteger(Number value) {
	return new JiPrologInteger(this, value);
    }

    public PrologLong newLong(Number value) {
	return new JiPrologLong(this, value);
    }

    public PrologVariable newVariable() {
	return new JiPrologVariable(this);
    }

    public PrologVariable newVariable(String name) {
	return new JiPrologVariable(this, name);
    }

    public PrologList newList() {
	return new JiPrologList(this);
    }

    public PrologList newList(PrologTerm[] arguments) {
	return new JiPrologList(this, arguments);
    }

    public PrologList newList(PrologTerm head, PrologTerm tail) {
	return new JiPrologList(this, head, tail);
    }

    public PrologList newList(PrologTerm[] arguments, PrologTerm tail) {
	return new JiPrologList(this, arguments, tail);
    }

    public PrologStructure newStructure(String functor, PrologTerm... arguments) {
	return new JiPrologStructure(this, functor, arguments);
    }

    public PrologExpression newExpression(PrologTerm left, String operator, PrologTerm right) {
	return new JiPrologExpression(this, left, operator, right);
    }

    @Override
    public String toString() {
	return "JiPrologProvider [converter=" + converter + "]";
    }

}
