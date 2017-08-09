package org.logicware.jpi.jiprolog;

import org.logicware.jpi.ArityError;
import org.logicware.jpi.FunctorError;
import org.logicware.jpi.PrologDouble;
import org.logicware.jpi.PrologFloat;
import org.logicware.jpi.PrologInteger;
import org.logicware.jpi.PrologLong;
import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.IndicatorError;

import com.ugos.jiprolog.engine.JIPNumber;

public class JiPrologLong extends JiPrologTerm implements PrologLong {

	public JiPrologLong() {
		super(LONG_TYPE, JIPNumber.create(0));
	}

	public JiPrologLong(Number value) {
		super(LONG_TYPE, JIPNumber.create(value.longValue()));
	}

	public PrologInteger getPrologInteger() {
		return new JiPrologInteger(getIntValue());
	}

	public PrologFloat getPrologFloat() {
		return new JiPrologFloat(getFloatValue());
	}

	public PrologDouble getPrologDouble() {
		return new JiPrologDouble(getDoubleValue());
	}

	public PrologLong getPrologLong() {
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
	public PrologTerm[] getArguments() {
		return new JiPrologLong[0];
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
		long l = getLongValue();
		return new JiPrologLong(l);
	}

}
