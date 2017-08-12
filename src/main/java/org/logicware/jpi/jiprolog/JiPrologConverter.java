package org.logicware.jpi.jiprolog;

import java.util.Arrays;

import org.logicware.jpi.AbstractConverter;
import org.logicware.jpi.PrologConverter;
import org.logicware.jpi.PrologAtom;
import org.logicware.jpi.PrologDouble;
import org.logicware.jpi.PrologExpression;
import org.logicware.jpi.PrologFloat;
import org.logicware.jpi.PrologInteger;
import org.logicware.jpi.PrologList;
import org.logicware.jpi.PrologLong;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologStructure;
import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.PrologVariable;
import org.logicware.jpi.UnknownTermError;

import com.ugos.jiprolog.engine.JIPAtom;
import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPNumber;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;

public final class JiPrologConverter extends AbstractConverter<JIPTerm> implements PrologConverter<JIPTerm> {

	protected static final JiPrologOperatorSet OPERATORS = new JiPrologOperatorSet();

	@Override
	public PrologProvider<JIPTerm> createProvider() {
		return new JiPrologProvider(this);
	}

	@Override
	public PrologTerm toTerm(JIPTerm prologTerm) {
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
				return new JiPrologAtom(provider, atom.getName());
			}
		} else if (prologTerm instanceof JIPNumber) {
			JIPNumber number = (JIPNumber) prologTerm;
			if (number.isInteger()) {
				return new JiPrologInteger(provider, number.getDoubleValue());
			} else {
				return new JiPrologDouble(provider, number.getDoubleValue());
			}
		} else if (prologTerm instanceof JIPVariable) {
			String name = ((JIPVariable) prologTerm).getName();
			PrologVariable variable = sharedVariables.get(name);
			if (variable == null) {
				variable = new JiPrologVariable(provider, name);
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
				return new JiPrologList(provider, arguments);
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
				return new JiPrologStructure(provider, functor, arguments);
			} else if (structure.getArity() == 2) {
				JIPTerm left = structure.getTerm(1);
				JIPTerm right = structure.getTerm(2);
				return new JiPrologExpression(provider, left, functor, right);
			}
		}
		throw new UnknownTermError(prologTerm);
	}

	@Override
	public JIPTerm fromTerm(PrologTerm term) {
		switch (term.getType()) {
		case PrologTerm.NIL_TYPE:
			return JIPAtom.create("nil");
		case PrologTerm.CUT_TYPE:
			return JIPAtom.create("!");
		case PrologTerm.FAIL_TYPE:
			return JIPAtom.create("fail");
		case PrologTerm.TRUE_TYPE:
			return JIPAtom.create("true");
		case PrologTerm.FALSE_TYPE:
			return JIPAtom.create("false");
		case PrologTerm.EMPTY_TYPE:
			return JIPList.create(null, null);
		case PrologTerm.ATOM_TYPE:
			return JIPAtom.create(((PrologAtom) term).getStringValue());
		case PrologTerm.FLOAT_TYPE:
			return JIPNumber.create(((PrologFloat) term).getFloatValue());
		case PrologTerm.INTEGER_TYPE:
			return JIPNumber.create(((PrologInteger) term).getIntValue());
		case PrologTerm.DOUBLE_TYPE:
			return JIPNumber.create(((PrologDouble) term).getDoubleValue());
		case PrologTerm.LONG_TYPE:
			return JIPNumber.create(((PrologLong) term).getLongValue());
		case PrologTerm.VARIABLE_TYPE:
			String name = ((PrologVariable) term).getName();
			JIPTerm variable = sharedPrologVariables.get(name);
			if (variable == null) {
				variable = JIPVariable.create(name);
				sharedPrologVariables.put(name, variable);
			}
			return variable;
		case PrologTerm.LIST_TYPE:
			PrologTerm[] arguments = ((PrologList) term).getArguments();
			return adaptList(arguments);
		case PrologTerm.STRUCTURE_TYPE:
			String functor = term.getFunctor();
			arguments = ((PrologStructure) term).getArguments();
			return JIPFunctor.create(removeQuoted(functor), adaptCons(arguments));
		case PrologTerm.EXPRESSION_TYPE:
			PrologExpression exp = (PrologExpression) term;
			return JIPFunctor.create(exp.getOperator(), adaptCons(exp.getArguments()));
		default:
			throw new UnknownTermError(term);
		}
	}

	@Override
	public JIPTerm[] fromTerm(PrologTerm[] terms) {
		JIPTerm[] prologTerms = new JIPTerm[terms.length];
		for (int i = 0; i < terms.length; i++) {
			prologTerms[i] = fromTerm(terms[i]);
		}
		return prologTerms;
	}

	@Override
	public JIPTerm fromTerm(PrologTerm head, PrologTerm[] body) {
		JIPTerm clauseHead = fromTerm(head);
		if (body != null && body.length > 0) {
			JIPTerm clauseBody = fromTerm(body[body.length - 1]);
			for (int i = body.length - 2; i >= 0; --i) {
				clauseBody = JIPFunctor.create(",", JIPCons.create(fromTerm(body[i]), clauseBody));
			}
			return JIPFunctor.create(":-", JIPCons.create(clauseHead, clauseBody));
		}
		return clauseHead;
	}

	private JIPList adaptList(PrologTerm[] arguments) {
		JIPList list = JIPList.NIL;
		for (int i = arguments.length - 1; i >= 0; --i) {
			list = JIPList.create(fromTerm(arguments[i]), list);
		}
		return list;
	}

	private JIPCons adaptCons(PrologTerm[] arguments) {
		JIPCons cons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			cons = JIPCons.create(fromTerm(arguments[i]), cons);
		}
		return cons;
	}

}
