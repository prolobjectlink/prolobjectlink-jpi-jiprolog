/*-
 * #%L
 * prolobjectlink-jpi-jiprolog
 * %%
 * Copyright (C) 2012 - 2019 Logicware Developments
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package io.github.prolobjectlink.prolog.jiprolog;

import java.util.Arrays;

import com.ugos.jiprolog.engine.JIPAtom;
import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPNumber;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;

import io.github.prolobjectlink.prolog.AbstractConverter;
import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologConverter;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologTermType;
import io.github.prolobjectlink.prolog.PrologVariable;
import io.github.prolobjectlink.prolog.UnknownTermError;

final class JiPrologConverter extends AbstractConverter<JIPTerm> implements PrologConverter<JIPTerm> {

	public PrologTerm toTerm(JIPTerm prologTerm) {
		if (prologTerm instanceof JIPAtom) {
			JIPAtom atom = (JIPAtom) prologTerm;
			if (atom.getName().equals("!")) {
				return new JiPrologCut(provider);
			} else if (atom.getName().equals("nil")) {
				return new JiPrologNil(provider);
			} else if (atom.getName().equals("true")) {
				return new JiPrologTrue(provider);
			} else if (atom.getName().equals("false")) {
				return new JiPrologFalse(provider);
			} else if (atom.getName().equals("fail")) {
				return new JiPrologFail(provider);
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
			return new JiPrologEmpty(provider);
		} else if (prologTerm instanceof JIPFunctor) {
			JIPFunctor structure = (JIPFunctor) prologTerm;
			String functor = structure.getName();
			if (!new JiPrologOperatorSet().currentOp(functor)) {
				JIPTerm[] arguments = new JIPTerm[structure.getArity()];
				for (int i = 0; i < arguments.length; i++) {
					arguments[i] = structure.getTerm(i + 1);
				}
				return new JiPrologStructure(provider, functor, arguments);
			} else if (structure.getArity() == 2) {
				JIPTerm left = structure.getTerm(1);
				JIPTerm right = structure.getTerm(2);
				return new JiPrologStructure(provider, left, functor, right);
			}
		} else if (prologTerm instanceof JIPCons) {
			JIPCons cons = (JIPCons) prologTerm;
			JIPTerm left = cons.getHead();
			JIPTerm right = cons.getTail();
			return new JiPrologStructure(provider, left, ",", right);
		}
		throw new UnknownTermError(prologTerm);
	}

	public JIPTerm fromTerm(PrologTerm term) {
		switch (term.getType()) {
		case PrologTermType.NIL_TYPE:
			return JIPAtom.create("nil");
		case PrologTermType.CUT_TYPE:
			return JIPAtom.create("!");
		case PrologTermType.FAIL_TYPE:
			return JIPAtom.create("fail");
		case PrologTermType.TRUE_TYPE:
			return JIPAtom.create("true");
		case PrologTermType.FALSE_TYPE:
			return JIPAtom.create("false");
		case PrologTermType.ATOM_TYPE:
			return JIPAtom.create(((PrologAtom) term).getStringValue());
		case PrologTermType.FLOAT_TYPE:
			return JIPNumber.create(((PrologFloat) term).getFloatValue());
		case PrologTermType.INTEGER_TYPE:
			return JIPNumber.create(((PrologInteger) term).getIntegerValue());
		case PrologTermType.DOUBLE_TYPE:
			return JIPNumber.create(((PrologDouble) term).getDoubleValue());
		case PrologTermType.LONG_TYPE:
			return JIPNumber.create(((PrologLong) term).getLongValue());
		case PrologTermType.VARIABLE_TYPE:
			String name = ((PrologVariable) term).getName();
			JIPTerm variable = sharedPrologVariables.get(name);
			if (variable == null) {
				variable = JIPVariable.create(name);
				sharedPrologVariables.put(name, variable);
			}
			return variable;
		case PrologTermType.LIST_TYPE:
			PrologTerm[] arguments = ((PrologList) term).getArguments();
			JIPList list = JIPList.NIL;
			for (int i = arguments.length - 1; i >= 0; --i) {
				list = JIPList.create(fromTerm(arguments[i]), list);
			}
			return list;
		case PrologTermType.STRUCTURE_TYPE:
			String functor = term.getFunctor();
			arguments = ((PrologStructure) term).getArguments();
			JIPCons cons = adaptCons(arguments);
			return JIPFunctor.create(removeQuoted(functor), cons);
		default:
			throw new UnknownTermError(term);
		}
	}

	public JIPTerm[] fromTermArray(PrologTerm[] terms) {
		JIPTerm[] prologTerms = new JIPTerm[terms.length];
		for (int i = 0; i < terms.length; i++) {
			prologTerms[i] = fromTerm(terms[i]);
		}
		return prologTerms;
	}

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

	public PrologProvider createProvider() {
		return new JiProlog(this);
	}

	@Override
	public String toString() {
		return "JiPrologConverter";
	}

	JIPCons adaptCons(PrologTerm[] arguments) {
		JIPCons cons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			cons = JIPCons.create(fromTerm(arguments[i]), cons);
		}
		return cons;
	}

}
