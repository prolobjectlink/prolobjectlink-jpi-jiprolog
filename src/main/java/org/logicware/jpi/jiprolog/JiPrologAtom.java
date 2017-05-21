package org.logicware.jpi.jiprolog;

import org.logicware.jpi.IPrologAtom;
import org.logicware.jpi.IPrologTerm;

import com.ugos.jiprolog.engine.JIPAtom;

public final class JiPrologAtom extends JiPrologTerm implements IPrologAtom {

	public JiPrologAtom(String value) {
		super(ATOM_TYPE, JIPAtom.create(value));
	}

	public String getStringValue() {
		return getFunctor();
	}

	public void setStringValue(String value) {
		this.value = JIPAtom.create(value);
	}

	@Override
	public IPrologTerm[] getArguments() {
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
	public IPrologTerm clone() {
		String s = getFunctor();
		return new JiPrologAtom(s);
	}

}
