package org.logicware.jpi.jiprolog;

import org.logicware.jpi.IPrologExpression;
import org.logicware.jpi.IPrologTerm;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPTerm;

public final class JiPrologExpression extends JiPrologTerm implements IPrologExpression {

	@Deprecated
	JiPrologExpression(IPrologTerm left, String operator, IPrologTerm right) {
		super(EXPRESSION_TYPE);
		value = JIPFunctor.create(operator, adaptCons(new IPrologTerm[] { left, right }));
	}

	@Deprecated
	JiPrologExpression(JIPTerm left, String functor, JIPTerm right) {
		super(EXPRESSION_TYPE, JIPFunctor.create(functor, JIPCons.create(left, JIPCons.create(right, null))));
	}

	public String getOperator() {
		return getFunctor();
	}

	public IPrologTerm getLeft() {
		return adapt(((JIPFunctor) value).getTerm(1));
	}

	public IPrologTerm getRight() {
		return adapt(((JIPFunctor) value).getTerm(2));
	}

	@Override
	public IPrologTerm[] getArguments() {
		JIPFunctor structure = (JIPFunctor) value;
		int arity = structure.getArity();
		IPrologTerm[] arguments = new IPrologTerm[arity];
		for (int i = 0; i < arity; i++) {
			arguments[i] = adapt(structure.getTerm(i + 1));
		}
		return arguments;
	}

	@Override
	public int getArity() {
		JIPFunctor structure = (JIPFunctor) value;
		return structure.getArity();
	}

	@Override
	public String getFunctor() {
		JIPFunctor structure = (JIPFunctor) value;
		return structure.getName();
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
		IPrologTerm l = getLeft();
		String o = getOperator();
		IPrologTerm r = getRight();
		return new JiPrologExpression(l, o, r);
	}

}
