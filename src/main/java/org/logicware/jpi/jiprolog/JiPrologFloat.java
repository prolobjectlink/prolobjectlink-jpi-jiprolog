package org.logicware.jpi.jiprolog;

import org.logicware.jpi.ArityError;
import org.logicware.jpi.FunctorError;
import org.logicware.jpi.IndicatorError;
import org.logicware.jpi.PrologDouble;
import org.logicware.jpi.PrologFloat;
import org.logicware.jpi.PrologInteger;
import org.logicware.jpi.PrologLong;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPNumber;

public final class JiPrologFloat extends JiPrologTerm implements PrologFloat {

	public JiPrologFloat(PrologProvider provider) {
		super(FLOAT_TYPE, provider, JIPNumber.create(0));
	}

	public JiPrologFloat(PrologProvider provider, Number value) {
		super(FLOAT_TYPE, provider, JIPNumber.create(value.floatValue()));
	}

	public PrologInteger getPrologInteger() {
		return new JiPrologInteger(provider, getIntValue());
	}

	public PrologFloat getPrologFloat() {
		return new JiPrologFloat(provider, getFloatValue());
	}

	public PrologDouble getPrologDouble() {
		return new JiPrologDouble(provider, getDoubleValue());
	}

	public PrologLong getPrologLong() {
		return new JiPrologLong(provider, getLongValue());
	}

	public long getLongValue() {
		return (long) ((JIPNumber) value).getDoubleValue();
	}

	public double getDoubleValue() {
		return ((JIPNumber) value).getDoubleValue();
	}

	public int getIntValue() {
		return (int) ((JIPNumber) value).getDoubleValue();
	}

	public float getFloatValue() {
		return (float) ((JIPNumber) value).getDoubleValue();
	}

	@Override
	public PrologTerm[] getArguments() {
		return new JiPrologFloat[0];
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
		float f = getFloatValue();
		return new JiPrologFloat(provider, f);
	}

}
