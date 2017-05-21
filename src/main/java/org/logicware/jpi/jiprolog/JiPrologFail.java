package org.logicware.jpi.jiprolog;

import org.logicware.jpi.IPrologTerm;

import com.ugos.jiprolog.engine.JIPAtom;

public final class JiPrologFail extends JiPrologTerm implements IPrologTerm {

	protected JiPrologFail() {
		super(FAIL_TYPE, JIPAtom.create("fail"));
	}

	@Override
	public IPrologTerm[] getArguments() {
		return new IPrologTerm[0];
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
	public IPrologTerm clone() {
		return new JiPrologFail();
	}

}
