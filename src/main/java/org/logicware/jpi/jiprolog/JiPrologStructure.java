package org.logicware.jpi.jiprolog;

import org.logicware.jpi.PrologStructure;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPTerm;

public class JiPrologStructure extends JiPrologCompound implements PrologStructure {

	protected JiPrologStructure(String functor, PrologTerm... arguments) {
		super(STRUCTURE_TYPE);
		value = JIPFunctor.create(removeQuoted(functor), adaptCons(arguments));
	}

	protected JiPrologStructure(String functor, JIPTerm... arguments) {
		super(STRUCTURE_TYPE);
		JIPCons cons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			cons = JIPCons.create(arguments[i], cons);
		}
		value = JIPFunctor.create(removeQuoted(functor), cons);
	}

	public PrologTerm getArgument(int index) {
		PrologTerm[] arguments = getArguments();
		checkIndexOutOfBound(index, arguments.length);
		return arguments[index];
	}

	@Override
	public PrologTerm[] getArguments() {
		JIPFunctor structure = (JIPFunctor) value;
		int arity = structure.getArity();
		PrologTerm[] arguments = new PrologTerm[arity];
		for (int i = 0; i < arity; i++) {
			arguments[i] = adapter.toTerm(structure.getTerm(i + 1));
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
		return analys(structure.getName());
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
		String f = getFunctor();
		PrologTerm[] a = getArguments();
		return new JiPrologStructure(f, a);
	}

}
