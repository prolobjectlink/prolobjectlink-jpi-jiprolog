package org.logicware.jpi.jiprolog;

import java.util.Map;

import org.logicware.jpi.AbstractTerm;
import org.logicware.jpi.NumberExpectedError;
import org.logicware.jpi.PrologIndex;
import org.logicware.jpi.PrologNumber;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPNumber;
import com.ugos.jiprolog.engine.JIPTerm;

public abstract class JiPrologTerm extends AbstractTerm implements PrologTerm {

	protected JIPTerm value;

	protected JiPrologTerm(int type, PrologProvider provider) {
		super(type, provider);
	}

	protected JiPrologTerm(int type, PrologProvider provider, JIPTerm value) {
		super(type, provider);
		this.value = value;
	}

	protected final void checkNumberType(PrologTerm term) {
		if (!term.isNumber()) {
			throw new NumberExpectedError(term);
		}
	}

	public final int getType() {
		return type;
	}

	public final boolean isAtom() {
		return this instanceof JiPrologAtom;
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
		return this instanceof JiPrologExpression;
	}

	public boolean isAtomic() {
		switch (type) {
		case ATOM_TYPE:
			return true;
		case LONG_TYPE:
			return true;
		case FLOAT_TYPE:
			return true;
		case DOUBLE_TYPE:
			return true;
		case INTEGER_TYPE:
			return true;
		case VARIABLE_TYPE:
			return true;
		}
		return false;
	}

	public boolean isCompound() {
		switch (type) {
		case LIST_TYPE:
			return true;
		case STRUCTURE_TYPE:
			return true;
		}
		return false;
	}

	public abstract String getIndicator();

	public abstract boolean hasIndicator(String functor, int arity);

	public abstract int getArity();

	public abstract String getFunctor();

	public abstract PrologTerm[] getArguments();

	public PrologIndex getIndex() {
		throw new UnsupportedOperationException();
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

			checkNumberType(term);
			double thisValue = ((JIPNumber) value).getDoubleValue();
			double otherValue = ((PrologNumber) term).getDoubleValue();

			if (thisValue < otherValue) {
				return -1;
			} else if (thisValue > otherValue) {
				return 1;
			}

			break;

		case LIST_TYPE:
		case EMPTY_TYPE:
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
			PrologTerm otherVariable = (PrologTerm) term;
			if (thisVariable.hashCode() < otherVariable.hashCode()) {
				return -1;
			} else if (thisVariable.hashCode() > otherVariable.hashCode()) {
				return 1;
			}
			break;

		}

		return 0;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type;
		// JIPTerm not implement hashCode()
		// result = prime * result + ((value == null) ? 0 : value.hashCode());
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
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "" + value + "";
	}

	@Override
	public abstract PrologTerm clone();

}
