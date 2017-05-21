package org.logicware.jpi.jiprolog;

import org.logicware.jpi.IPrologTerm;

import com.ugos.jiprolog.engine.JIPList;

public class JiPrologEmpty extends JiPrologTerm implements IPrologTerm {

	protected JiPrologEmpty() {
		super(EMPTY_TYPE, JIPList.create(null, null));
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
		return "[]";
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
		return new JiPrologEmpty();
	}

}
