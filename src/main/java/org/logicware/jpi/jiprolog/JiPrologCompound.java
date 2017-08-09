package org.logicware.jpi.jiprolog;

import org.logicware.jpi.IPrologTerm;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPTerm;

public abstract class JiPrologCompound extends JiPrologTerm {

	protected static final String SIMPLE_ATOM_REGEX = ".|[a-z][A-Za-z0-9_]*";

	protected JiPrologCompound(int type) {
		super(type);
	}

	protected JiPrologCompound(int type, JIPTerm value) {
		super(type, value);
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

	protected final JIPList adaptList(IPrologTerm[] arguments) {
		JIPList list = JIPList.NIL;
		for (int i = arguments.length - 1; i >= 0; --i) {
			list = JIPList.create(adapter.toNativeTerm(arguments[i]), list);
		}
		return list;
	}

	protected final JIPCons adaptCons(IPrologTerm[] arguments) {
		JIPCons cons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			cons = JIPCons.create(adapter.toNativeTerm(arguments[i]), cons);
		}
		return cons;
	}

}
