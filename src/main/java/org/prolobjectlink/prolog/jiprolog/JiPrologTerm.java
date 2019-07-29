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
package org.prolobjectlink.prolog.jiprolog;

import static org.prolobjectlink.prolog.PrologTermType.ATOM_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.CUT_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.DOUBLE_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.FAIL_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.FALSE_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.FLOAT_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.INTEGER_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.LIST_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.LONG_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.NIL_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.STRUCTURE_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.TRUE_TYPE;
import static org.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import org.prolobjectlink.prolog.AbstractTerm;
import org.prolobjectlink.prolog.PrologNumber;
import org.prolobjectlink.prolog.PrologOperatorSet;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;

import com.ugos.jiprolog.engine.JIPNumber;
import com.ugos.jiprolog.engine.JIPTerm;

abstract class JiPrologTerm extends AbstractTerm implements PrologTerm {

	protected JIPTerm value;

	protected JiPrologTerm(int type, PrologProvider provider) {
		super(type, provider);
	}

	protected JiPrologTerm(int type, PrologProvider provider, JIPTerm value) {
		super(type, provider);
		this.value = value;
	}

	public final boolean isAtom() {
		return type == ATOM_TYPE || type == FAIL_TYPE || type == FALSE_TYPE || type == TRUE_TYPE || isEmptyList()
				|| type == CUT_TYPE || type == NIL_TYPE;
	}

	public final boolean isNumber() {
		return isDouble() || isFloat() || isInteger() || isLong();
	}

	public final boolean isFloat() {
		return this instanceof JiPrologFloat;
	}

	public final boolean isDouble() {
		return this instanceof JiPrologDouble;
	}

	public final boolean isInteger() {
		return this instanceof JiPrologInteger;
	}

	public final boolean isLong() {
		return this instanceof JiPrologLong;
	}

	public final boolean isVariable() {
		return this instanceof JiPrologVariable;
	}

	public final boolean isList() {
		return (this instanceof JiPrologList)

				||

				(this instanceof JiPrologEmpty);
	}

	public final boolean isStructure() {
		return this instanceof JiPrologStructure;
	}

	public final boolean isNil() {
		return this instanceof JiPrologNil;
	}

	public final boolean isEmptyList() {
		return this instanceof JiPrologEmpty;
	}

	public final boolean isEvaluable() {
		if (isCompound()) {
			PrologOperatorSet op = new JiPrologOperatorSet();
			return op.currentOp(getFunctor());
		}
		return false;
	}

	public boolean isAtomic() {
		return !isCompound();
	}

	public boolean isCompound() {
		return isStructure() || isList();
	}

	public final boolean unify(PrologTerm term) {
		return value != null && value.unifiable(fromTerm(term, JIPTerm.class));
	}

	public int compareTo(PrologTerm term) {

		int termType = term.getType();

		if ((type >> 8) < (termType >> 8)) {
			return -1;
		} else if ((type >> 8) > (termType >> 8)) {
			return 1;
		}

		switch (type) {
		case ATOM_TYPE:

			// alphabetic functor comparison
			int result = value.toString().compareTo(term.getFunctor());
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			}
			break;

		case FLOAT_TYPE:
		case LONG_TYPE:
		case DOUBLE_TYPE:
		case INTEGER_TYPE:

			double thisValue = ((JIPNumber) value).getDoubleValue();
			double otherValue = ((PrologNumber) term).getDoubleValue();

			if (thisValue < otherValue) {
				return -1;
			} else if (thisValue > otherValue) {
				return 1;
			}

			break;

		case LIST_TYPE:
		case STRUCTURE_TYPE:

			PrologTerm thisCompound = this;
			PrologTerm otherCompound = term;

			// comparison by arity
			if (thisCompound.getArity() < otherCompound.getArity()) {
				return -1;
			} else if (thisCompound.getArity() > otherCompound.getArity()) {
				return 1;
			}

			// alphabetic functor comparison
			result = thisCompound.getFunctor().compareTo(otherCompound.getFunctor());
			if (result < 0) {
				return -1;
			} else if (result > 0) {
				return 1;
			}

			// arguments comparison
			PrologTerm[] thisArguments = thisCompound.getArguments();
			PrologTerm[] otherArguments = otherCompound.getArguments();

			for (int i = 0; i < thisArguments.length; i++) {
				PrologTerm thisArgument = thisArguments[i];
				PrologTerm otherArgument = otherArguments[i];
				if (thisArgument != null && otherArgument != null) {
					result = thisArgument.compareTo(otherArgument);
					if (result != 0) {
						return result;
					}
				}
			}
			break;

		case VARIABLE_TYPE:

			PrologTerm thisVariable = this;
			PrologTerm otherVariable = term;
			if (thisVariable.hashCode() < otherVariable.hashCode()) {
				return -1;
			} else if (thisVariable.hashCode() > otherVariable.hashCode()) {
				return 1;
			}
			break;

		default:
			return 0;

		}

		return 0;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type;
		// JIPTerm not implement hashCode()
		// result = prime * result + ((value == null) ? 0 : value.hashCode())
		result = prime * result + ((value == null) ? 0 : value.toString().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JiPrologTerm other = (JiPrologTerm) obj;
		if (type != other.type)
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (value.toString().equals(other.value.toString())) {
			return true;
		} else if (!value.unifiable(other.value)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "" + value + "";
	}

}
