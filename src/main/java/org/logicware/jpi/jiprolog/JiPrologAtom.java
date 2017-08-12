package org.logicware.jpi.jiprolog;

import org.logicware.jpi.PrologAtom;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPAtom;
import com.ugos.jiprolog.engine.JIPTerm;

public final class JiPrologAtom extends JiPrologTerm implements PrologAtom {

	public JiPrologAtom(PrologProvider<JIPTerm> provider, String value) {
		super(ATOM_TYPE, provider, JIPAtom.create(value));
	}

	public String getStringValue() {
		return getFunctor();
	}

	public void setStringValue(String value) {
		this.value = JIPAtom.create(value);
	}

	@Override
	public PrologTerm[] getArguments() {
		return new JiPrologAtom[0];
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
		String s = getFunctor();
		return new JiPrologAtom(provider, s);
	}

}
