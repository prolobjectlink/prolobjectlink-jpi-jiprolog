package org.logicware.jpi.jiprolog;

import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPTerm;

public abstract class JiPrologCompound extends JiPrologTerm {

	protected static final String SIMPLE_ATOM_REGEX = ".|[a-z][A-Za-z0-9_]*";

	protected JiPrologCompound(int type, PrologProvider<JIPTerm> provider) {
		super(type, provider);
	}

	protected JiPrologCompound(int type, PrologProvider<JIPTerm> provider, JIPTerm value) {
		super(type, provider, value);
	}

	protected static final JiPrologOperatorSet OPERATORS = new JiPrologOperatorSet();

	protected final static String analys(String functor) {
		if (!functor.matches(SIMPLE_ATOM_REGEX) && !isQuoted(functor)) {
			return "'" + functor + "'";
		}
		return functor;
	}

	protected static final boolean isQuoted(String functor) {
		if (!functor.isEmpty()) {
			char beginChar = functor.charAt(0);
			char endChar = functor.charAt(functor.length() - 1);
			return beginChar == '\'' && endChar == '\'';
		}
		return false;
	}

	protected static final String removeQuoted(String functor) {
		if (isQuoted(functor)) {
			String newFunctor = "";
			newFunctor += functor.substring(1, functor.length() - 1);
			return newFunctor;
		}
		return functor;
	}

	protected void checkIndexOutOfBound(int index, int lenght) {
		if (index < 0 || index > lenght) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
	}

	protected final JIPList adaptList(PrologTerm[] arguments) {
		JIPList list = JIPList.NIL;
		for (int i = arguments.length - 1; i >= 0; --i) {
			list = JIPList.create(provider.fromTerm(arguments[i]), list);
		}
		return list;
	}

	protected final JIPCons adaptCons(PrologTerm[] arguments) {
		JIPCons cons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			cons = JIPCons.create(provider.fromTerm(arguments[i]), cons);
		}
		return cons;
	}

}
