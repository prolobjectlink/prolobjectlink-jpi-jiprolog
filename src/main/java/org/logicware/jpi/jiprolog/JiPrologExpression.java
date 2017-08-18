package org.logicware.jpi.jiprolog;

import org.logicware.jpi.PrologExpression;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPTerm;

public final class JiPrologExpression extends JiPrologCompound implements PrologExpression {

    @Deprecated
    JiPrologExpression(PrologProvider provider, PrologTerm left, String operator, PrologTerm right) {
	super(EXPRESSION_TYPE, provider);
	value = JIPFunctor.create(operator, adaptCons(new PrologTerm[] { left, right }));
    }

    @Deprecated
    JiPrologExpression(PrologProvider provider, JIPTerm left, String functor, JIPTerm right) {
	super(EXPRESSION_TYPE, provider, JIPFunctor.create(functor, JIPCons.create(left, JIPCons.create(right, null))));
    }

    public String getOperator() {
	return getFunctor();
    }

    public PrologTerm getLeft() {
	return toTerm(((JIPFunctor) value).getTerm(1), PrologTerm.class);
    }

    public PrologTerm getRight() {
	return toTerm(((JIPFunctor) value).getTerm(2), PrologTerm.class);
    }

    @Override
    public PrologTerm[] getArguments() {
	JIPFunctor structure = (JIPFunctor) value;
	int arity = structure.getArity();
	PrologTerm[] arguments = new PrologTerm[arity];
	for (int i = 0; i < arity; i++) {
	    arguments[i] = toTerm(structure.getTerm(i + 1), PrologTerm.class);
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
    public PrologTerm clone() {
	PrologTerm l = getLeft();
	String o = getOperator();
	PrologTerm r = getRight();
	return new JiPrologExpression(provider, l, o, r);
    }

}
