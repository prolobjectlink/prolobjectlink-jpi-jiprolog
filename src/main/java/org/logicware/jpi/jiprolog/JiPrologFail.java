package org.logicware.jpi.jiprolog;

import org.logicware.jpi.PrologIndex;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPAtom;

public final class JiPrologFail extends JiPrologTerm implements PrologTerm {

	protected JiPrologFail() {
		super(FAIL_TYPE, JIPAtom.create("fail"));
	}

	@Override
	public PrologTerm[] getArguments() {
		return new PrologTerm[0];
	}

	@Override
	public int getArity() {
		return 0;
	}

	@Override
	public String getFunctor() {
		return "" + value + "";
	}

	@Override
	public String getIndicator() {
		return getFunctor() + "/" + getArity();
	}

	@Override
	public boolean hasIndicator(String functor, int arity) {
		return getFunctor().equals(functor) && getArity() == arity;
	}

	@Override
	public PrologTerm clone() {
		return new JiPrologFail();
	}

	public PrologIndex getIndex() {
		throw new UnsupportedOperationException();
	}

}
