package org.logicware.jpi.jiprolog;

import org.logicware.jpi.PrologList;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPTerm;

public class JiPrologEmpty extends JiPrologList implements PrologList {

	protected JiPrologEmpty(PrologProvider<JIPTerm> provider) {
		super(EMPTY_TYPE, provider);
		value = JIPList.create(null, null);
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
	public PrologTerm clone() {
		return new JiPrologEmpty(provider);
	}

}
