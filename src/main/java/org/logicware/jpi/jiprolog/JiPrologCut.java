package org.logicware.jpi.jiprolog;

import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPAtom;

public class JiPrologCut extends JiPrologTerm implements PrologTerm {

	protected JiPrologCut(PrologProvider provider) {
		super(CUT_TYPE, provider, JIPAtom.create("!"));
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
		return new JiPrologCut(provider);
	}

}
