package org.logicware.jpi.jiprolog;

import org.logicware.jpi.IPrologTerm;

import com.ugos.jiprolog.engine.JIPTerm;

public abstract class JiPrologTerm extends JiPrologAbstract implements IPrologTerm {

	protected int type;
	protected JIPTerm value;

	protected JiPrologTerm(int type) {
		this.type = type;
	}

	protected JiPrologTerm(int type, JIPTerm value) {
		this.type = type;
		this.value = value;
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

	public final boolean unify(IPrologTerm term) {
		return value.unifiable(adapt(term));
	}

	public int compareTo(IPrologTerm o) {
		return value.compareTo(adapt(o));
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
