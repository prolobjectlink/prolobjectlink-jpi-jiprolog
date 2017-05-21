package org.logicware.jpi.jiprolog;

import org.logicware.jpi.ArityError;
import org.logicware.jpi.FunctorError;
import org.logicware.jpi.IPrologDouble;
import org.logicware.jpi.IPrologFloat;
import org.logicware.jpi.IPrologInteger;
import org.logicware.jpi.IPrologLong;
import org.logicware.jpi.IPrologTerm;
import org.logicware.jpi.IndicatorError;

import com.ugos.jiprolog.engine.JIPNumber;

public final class JiPrologDouble extends JiPrologTerm implements IPrologDouble {

	public JiPrologDouble() {
		super(DOUBLE_TYPE, JIPNumber.create(0));
	}

	public JiPrologDouble(Number value) {
		super(DOUBLE_TYPE, JIPNumber.create(value.doubleValue()));
	}

	public IPrologInteger getPrologInteger() {
		return new JiPrologInteger(getIntValue());
	}

	public IPrologFloat getPrologFloat() {
		return new JiPrologFloat(getFloatValue());
	}

	public IPrologDouble getPrologDouble() {
		return new JiPrologDouble(getDoubleValue());
	}

	public IPrologLong getPrologLong() {
		return new JiPrologLong(getLongValue());
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
	public IPrologTerm[] getArguments() {
		return new JiPrologDouble[0];
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
	public IPrologTerm clone() {
		double d = getDoubleValue();
		return new JiPrologDouble(d);
	}

}
