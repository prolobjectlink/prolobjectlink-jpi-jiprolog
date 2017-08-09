package org.logicware.jpi.jiprolog;

import java.util.Iterator;

import org.logicware.jpi.PrologList;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPTerm;

public class JiPrologList extends JiPrologCompound implements PrologList {

	protected JiPrologList() {
		super(LIST_TYPE);
	}

	protected JiPrologList(int type) {
		super(type);
	}

	protected JiPrologList(PrologTerm[] arguments) {
		super(LIST_TYPE);
		value = adaptList(arguments);
	}

	protected JiPrologList(JIPTerm[] arguments) {
		super(LIST_TYPE);
		value = JIPList.NIL;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = JIPList.create(arguments[i], value);
		}
	}

	protected JiPrologList(JIPTerm[] arguments, JIPTerm tail) {
		super(LIST_TYPE);
		value = tail;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = JIPList.create(arguments[i], value);
		}
	}

	protected JiPrologList(PrologTerm head, PrologTerm tail) {
		super(LIST_TYPE);
		value = JIPList.create(adapter.toNativeTerm(head), adapter.toNativeTerm(tail));
	}

	protected JiPrologList(PrologTerm[] arguments, PrologTerm tail) {
		super(LIST_TYPE);
		value = adapter.toNativeTerm(tail);
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = JIPList.create(adapter.toNativeTerm(arguments[i]), value);
		}
	}

	public int size() {
		return ((JIPList) value).length();
	}

	public void clear() {
		value = JIPList.NIL;
	}

	public boolean isEmpty() {
		return ((JIPList) value).isNIL();
	}

	public Iterator<PrologTerm> iterator() {
		JIPList list = (JIPList) value;
		return new JIPrologListIter(list);
	}

	public PrologTerm getHead() {
		JIPList list = (JIPList) value;
		return adapter.toTerm(list.getHead());
	}

	public PrologTerm getTail() {
		JIPList list = (JIPList) value;
		return adapter.toTerm(list.getTail());
	}

	@Override
	public int getArity() {
		return 2;
	}

	@Override
	public String getFunctor() {
		return ".";
	}

	@Override
	public String getIndicator() {
		return getFunctor() + "/" + getArity();
	}

	@Override
	public boolean hasIndicator(String functor, int arity) {
		return getFunctor().equals(functor) && getArity() == arity;
	}

	@Override
	public PrologTerm[] getArguments() {
		JIPList list = (JIPList) value;
		PrologTerm[] arguments = new PrologTerm[list.length()];
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = adapter.toTerm(list.getNth(i + 1));
		}
		return arguments;
	}

	@Override
	public PrologTerm clone() {
		PrologTerm[] array = getArguments();
		return new JiPrologList(array);
	}

	private class JIPrologListIter implements Iterator<PrologTerm> {

		private JIPList list;
		private int nextIndex;

		private JIPrologListIter(JIPList list) {
			this.list = list;
			this.nextIndex = 1;
		}

		public boolean hasNext() {
			return nextIndex < list.length();
		}

		public PrologTerm next() {
			return adapter.toTerm(list.getNth(nextIndex++));
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
