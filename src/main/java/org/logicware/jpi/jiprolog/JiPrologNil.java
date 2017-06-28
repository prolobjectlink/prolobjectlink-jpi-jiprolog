package org.logicware.jpi.jiprolog;

import org.logicware.jpi.IPrologIndex;
import org.logicware.jpi.IPrologTerm;

import com.ugos.jiprolog.engine.JIPAtom;

public final class JiPrologNil extends JiPrologTerm implements IPrologTerm {

	protected JiPrologNil() {
		super(NIL_TYPE, JIPAtom.create("nil"));
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
		return new JiPrologNil();
	}

	public IPrologIndex getIndex() {
		throw new UnsupportedOperationException();
	}

}