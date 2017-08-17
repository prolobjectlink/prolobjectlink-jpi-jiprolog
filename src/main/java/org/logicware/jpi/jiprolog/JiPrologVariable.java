package org.logicware.jpi.jiprolog;

import org.logicware.jpi.ArityError;
import org.logicware.jpi.FunctorError;
import org.logicware.jpi.IndicatorError;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.PrologVariable;

import com.ugos.jiprolog.engine.JIPVariable;

public class JiPrologVariable extends JiPrologTerm implements PrologVariable {

	public JiPrologVariable(PrologProvider provider) {
		super(VARIABLE_TYPE, provider, JIPVariable.create());
	}

	public JiPrologVariable(PrologProvider provider, String name) {
		super(VARIABLE_TYPE, provider, JIPVariable.create(name));
	}

	public boolean isAnonim() {
		return ((JIPVariable) value).isAnonymous();
	}

	public String getName() {
		return ((JIPVariable) value).getName();
	}

	public void setName(String name) {
		this.value = JIPVariable.create(name);
	}

	@Override
	public PrologTerm[] getArguments() {
		return new JiPrologVariable[0];
	}

	@Override
	public int getArity() {
		throw new ArityError(this);
	}

	@Override
	public String getFunctor() {
		throw new FunctorError(this);
	}

	@Override
	public String getIndicator() {
		throw new IndicatorError(this);
	}

	@Override
	public boolean hasIndicator(String functor, int arity) {
		throw new IndicatorError(this);
	}

	@Override
	public PrologTerm clone() {
		String n = getName();
		return new JiPrologVariable(provider, n);
	}

}
