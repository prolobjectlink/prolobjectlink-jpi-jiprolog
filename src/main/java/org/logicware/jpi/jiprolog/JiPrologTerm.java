package org.logicware.jpi.jiprolog;

import static org.logicware.jpi.PrologAdapterFactory.createPrologAdapter;

import org.logicware.jpi.AbstractTerm;
import org.logicware.jpi.IPrologIndex;
import org.logicware.jpi.IPrologNumber;
import org.logicware.jpi.IPrologTerm;
import org.logicware.jpi.NumberExpectedError;
import org.logicware.jpi.PrologAdapter;

import com.ugos.jiprolog.engine.JIPNumber;
import com.ugos.jiprolog.engine.JIPTerm;

public abstract class JiPrologTerm extends AbstractTerm implements IPrologTerm {

	protected int type;
	protected JIPTerm value;

	final PrologAdapter<JIPTerm> adapter = createPrologAdapter(JiPrologAdapter.class);

	protected JiPrologTerm(int type) {
		this.type = type;
	}

	protected JiPrologTerm(int type, JIPTerm value) {
		this.type = type;
		this.value = value;
	}

	protected final void checkNumberType(IPrologTerm term) {
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

	public final boolean isExpression() {
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

	public abstract IPrologTerm[] getArguments();

	public IPrologIndex getIndex() {
		throw new UnsupportedOperationException();
	}

	public final boolean unify(IPrologTerm term) {
		return value.unifiable(adapter.toNativeTerm(term));
	}

	public int compareTo(IPrologTerm term) {

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
			double otherValue = ((IPrologNumber) term).getDoubleValue();

			if (thisValue < otherValue) {
				return -1;
			} else if (thisValue > otherValue) {
				return 1;
			}

			break;

		case LIST_TYPE:
		case EMPTY_TYPE:
		case STRUCTURE_TYPE:

			IPrologTerm thisCompound = this;
			IPrologTerm otherCompound = term;

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
			IPrologTerm[] thisArguments = thisCompound.getArguments();
			IPrologTerm[] otherArguments = otherCompound.getArguments();

			for (int i = 0; i < thisArguments.length; i++) {
				IPrologTerm thisArgument = thisArguments[i];
				IPrologTerm otherArgument = otherArguments[i];
				if (thisArgument != null && otherArgument != null) {
					result = thisArgument.compareTo(otherArgument);
					if (result != 0) {
						return result;
					}
				}
			}
			break;

		case VARIABLE_TYPE:

			IPrologTerm thisVariable = this;
			IPrologTerm otherVariable = (IPrologTerm) term;
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
	public abstract IPrologTerm clone();

}
