package io.github.prolobjectlink.prolog.jiprolog;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import io.github.prolobjectlink.prolog.PrologClause;
import io.github.prolobjectlink.prolog.PrologClauses;

class JiPrologClauses extends AbstractList<PrologClause> implements PrologClauses {

	private final int arity;
	private final String functor;
	private final List<PrologClause> list;

	JiPrologClauses(String functor, int arity) {
		list = new ArrayList<PrologClause>();
		this.functor = functor;
		this.arity = arity;
	}

	public void add(int index, PrologClause element) {
		list.add(index, element);
	}

	public PrologClause remove(int index) {
		return list.remove(index);
	}

	public PrologClause get(int index) {
		return list.get(index);
	}

	public int size() {
		return list.size();
	}

	public boolean isDynamic() {
		for (PrologClause prologClause : list) {
			if (!prologClause.isDynamic()) {
				return false;
			}
		}
		return true;
	}

	public boolean isMultifile() {
		for (PrologClause prologClause : list) {
			if (!prologClause.isMultifile()) {
				return false;
			}
		}
		return true;
	}

	public boolean isDiscontiguous() {
		for (PrologClause prologClause : list) {
			if (!prologClause.isDiscontiguous()) {
				return false;
			}
		}
		return true;
	}

	public String getIndicator() {
		return functor + "/" + arity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((list == null) ? 0 : list.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JiPrologClauses other = (JiPrologClauses) obj;
		if (list == null) {
			if (other.list != null)
				return false;
		} else if (!list.equals(other.list)) {
			return false;
		}
		return true;
	}

}
