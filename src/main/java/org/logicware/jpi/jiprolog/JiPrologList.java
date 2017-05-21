package org.logicware.jpi.jiprolog;

import java.util.Iterator;

import org.logicware.jpi.IPrologList;
import org.logicware.jpi.IPrologTerm;

import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPTerm;

public class JiPrologList extends JiPrologTerm implements IPrologList {

	protected JiPrologList() {
		super(LIST_TYPE);
	}

	protected JiPrologList(IPrologTerm[] arguments) {
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

	protected JiPrologList(IPrologTerm head, IPrologTerm tail) {
		super(LIST_TYPE);
		value = JIPList.create(adapt(head), adapt(tail));
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

	public Iterator<IPrologTerm> iterator() {
		JIPList list = (JIPList) value;
		return new JIPrologListIter(list);
	}

	public IPrologTerm getHead() {
		JIPList list = (JIPList) value;
		return adapt(list.getHead());
	}

	public IPrologTerm getTail() {
		JIPList list = (JIPList) value;
		return adapt(list.getTail());
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
	public IPrologTerm[] getArguments() {
		JIPList list = (JIPList) value;
		IPrologTerm[] arguments = new IPrologTerm[list.length()];
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = adapt(list.getNth(i + 1));
		}
		return arguments;
	}

	@Override
	public IPrologTerm clone() {
		IPrologTerm[] array = getArguments();
		return new JiPrologList(array);
	}

	private class JIPrologListIter implements Iterator<IPrologTerm> {

		private JIPList list;
		private int nextIndex;

		private JIPrologListIter(JIPList list) {
			this.list = list;
			this.nextIndex = 1;
		}

		public boolean hasNext() {
			return nextIndex < list.length();
		}

		public IPrologTerm next() {
			return adapt(list.getNth(nextIndex++));
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
