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

public class JiPrologInteger extends JiPrologTerm implements IPrologInteger {

	public JiPrologInteger() {
		super(INTEGER_TYPE, JIPNumber.create(0));
	}

	public JiPrologInteger(Number value) {
		super(INTEGER_TYPE, JIPNumber.create(value.intValue()));
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
		return new JiPrologInteger[0];
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
		int i = getIntValue();
		return new JiPrologInteger(i);
	}

}
