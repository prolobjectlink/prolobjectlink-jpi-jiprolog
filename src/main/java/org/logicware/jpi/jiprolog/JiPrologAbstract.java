package org.logicware.jpi.jiprolog;

import java.util.Arrays;
import java.util.HashMap;

import org.logicware.jpi.IPrologAbstract;
import org.logicware.jpi.IPrologAtom;
import org.logicware.jpi.IPrologDouble;
import org.logicware.jpi.IPrologExpression;
import org.logicware.jpi.IPrologFloat;
import org.logicware.jpi.IPrologInteger;
import org.logicware.jpi.IPrologList;
import org.logicware.jpi.IPrologLong;
import org.logicware.jpi.IPrologStructure;
import org.logicware.jpi.IPrologTerm;
import org.logicware.jpi.IPrologVariable;
import org.logicware.jpi.UnknownTermError;

import com.ugos.jiprolog.engine.JIPAtom;
import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPNumber;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;

public abstract class JiPrologAbstract extends IPrologAbstract<JIPTerm> {

	protected static final JiPrologOperatorSet OPERATORS = new JiPrologOperatorSet();

	protected final HashMap<String, IPrologVariable> sharedVariables;
	protected final HashMap<String, JIPVariable> sharedPrologVariables;

	protected JiPrologAbstract() {
		sharedPrologVariables = new HashMap<String, JIPVariable>();
		sharedVariables = new HashMap<String, IPrologVariable>();
	}

	protected final IPrologTerm adapt(JIPTerm prologTerm) {
		if (prologTerm instanceof JIPAtom) {
			JIPAtom atom = (JIPAtom) prologTerm;
			if (atom.getName().equals("!")) {
				return JiPrologProvider.CUT;
			} else if (atom.getName().equals("nil")) {
				return JiPrologProvider.NIL;
			} else if (atom.getName().equals("true")) {
				return JiPrologProvider.TRUE;
			} else if (atom.getName().equals("false")) {
				return JiPrologProvider.FALSE;
			} else if (atom.getName().equals("fail")) {
				return JiPrologProvider.FAIL;
			} else {
				return new JiPrologAtom(atom.getName());
			}
		} else if (prologTerm instanceof JIPNumber) {
			JIPNumber number = (JIPNumber) prologTerm;
			if (number.isInteger()) {
				return new JiPrologInteger(number.getDoubleValue());
			} else {
				return new JiPrologDouble(number.getDoubleValue());
			}
		} else if (prologTerm instanceof JIPVariable) {
			String name = ((JIPVariable) prologTerm).getName();
			IPrologVariable variable = sharedVariables.get(name);
			if (variable == null) {
				variable = new JiPrologVariable(name);
				sharedVariables.put(variable.getName(), variable);
			}
			return variable;
		} else if (prologTerm instanceof JIPList) {
			JIPList list = (JIPList) prologTerm;
			if (!list.isNIL()) {
				JIPTerm[] arguments = new JIPTerm[list.length()];
				for (int i = 0; i < arguments.length; i++) {
					try {
						arguments[i] = list.getNth(i + 1);
					} catch (NullPointerException e) {
						// is raised because the current list has [T|[]] format
						arguments = Arrays.copyOf(arguments, arguments.length - 1);
					}
				}
				return new JiPrologList(arguments);
			}
			return JiPrologProvider.EMPTY;
		} else if (prologTerm instanceof JIPFunctor) {
			JIPFunctor structure = (JIPFunctor) prologTerm;
			String functor = structure.getName();
			if (!OPERATORS.currentOp(functor)) {
				JIPTerm[] arguments = new JIPTerm[structure.getArity()];
				for (int i = 0; i < arguments.length; i++) {
					arguments[i] = structure.getTerm(i + 1);
				}
				return new JiPrologStructure(functor, arguments);
			} else if (structure.getArity() == 2) {
				JIPTerm left = structure.getTerm(1);
				JIPTerm right = structure.getTerm(2);
				return new JiPrologExpression(left, functor, right);
			}
		}
		throw new UnknownTermError(prologTerm);
	}

	protected final JIPTerm adapt(IPrologTerm term) {
		switch (term.getType()) {
		case IPrologTerm.NIL_TYPE:
			return JIPAtom.create("nil");
		case IPrologTerm.CUT_TYPE:
			return JIPAtom.create("!");
		case IPrologTerm.FAIL_TYPE:
			return JIPAtom.create("fail");
		case IPrologTerm.TRUE_TYPE:
			return JIPAtom.create("true");
		case IPrologTerm.FALSE_TYPE:
			return JIPAtom.create("false");
		case IPrologTerm.EMPTY_TYPE:
			return JIPList.create(null, null);
		case IPrologTerm.ATOM_TYPE:
			return JIPAtom.create(((IPrologAtom) term).getStringValue());
		case IPrologTerm.FLOAT_TYPE:
			return JIPNumber.create(((IPrologFloat) term).getFloatValue());
		case IPrologTerm.INTEGER_TYPE:
			return JIPNumber.create(((IPrologInteger) term).getIntValue());
		case IPrologTerm.DOUBLE_TYPE:
			return JIPNumber.create(((IPrologDouble) term).getDoubleValue());
		case IPrologTerm.LONG_TYPE:
			return JIPNumber.create(((IPrologLong) term).getLongValue());
		case IPrologTerm.VARIABLE_TYPE:
			String name = ((IPrologVariable) term).getName();
			JIPVariable variable = sharedPrologVariables.get(name);
			if (variable == null) {
				variable = JIPVariable.create(name);
				sharedPrologVariables.put(name, variable);
			}
			return variable;
		case IPrologTerm.LIST_TYPE:
			IPrologTerm[] arguments = ((IPrologList) term).getArguments();
			return adaptList(arguments);
		case IPrologTerm.STRUCTURE_TYPE:
			String functor = term.getFunctor();
			arguments = ((IPrologStructure) term).getArguments();
			return JIPFunctor.create(removeQuoted(functor), adaptCons(arguments));
		case IPrologTerm.EXPRESSION_TYPE:
			IPrologExpression exp = (IPrologExpression) term;
			return JIPFunctor.create(exp.getOperator(), adaptCons(exp.getArguments()));
		default:
			throw new UnknownTermError(term);
		}
	}

	protected final JIPTerm[] adapt(IPrologTerm[] terms) {
		JIPTerm[] prologTerms = new JIPTerm[terms.length];
		for (int i = 0; i < terms.length; i++) {
			prologTerms[i] = adapt(terms[i]);
		}
		return prologTerms;
	}

	protected final JIPList adaptList(IPrologTerm[] arguments) {
		JIPList list = JIPList.NIL;
		for (int i = arguments.length - 1; i >= 0; --i) {
			list = JIPList.create(adapt(arguments[i]), list);
		}
		return list;
	}

	protected final JIPTerm adapt(IPrologTerm head, IPrologTerm[] body) {
		JIPTerm clauseHead = adapt(head);
		if (body != null && body.length > 0) {
			JIPTerm clauseBody = adapt(body[body.length - 1]);
			for (int i = body.length - 2; i >= 0; --i) {
				clauseBody = JIPFunctor.create(",", JIPCons.create(adapt(body[i]), clauseBody));
			}
			return JIPFunctor.create(":-", JIPCons.create(clauseHead, clauseBody));
		}
		return clauseHead;
	}

	protected final JIPCons adaptCons(IPrologTerm[] arguments) {
		JIPCons cons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			cons = JIPCons.create(adapt(arguments[i]), cons);
		}
		return cons;
	}

}
