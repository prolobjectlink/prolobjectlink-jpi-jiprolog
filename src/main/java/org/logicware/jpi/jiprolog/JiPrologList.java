package org.logicware.jpi.jiprolog;

import java.util.Iterator;

import org.logicware.jpi.PrologList;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPTerm;

public class JiPrologList extends JiPrologCompound implements PrologList {

	protected JiPrologList(PrologProvider provider) {
		super(LIST_TYPE, provider);
	}

	protected JiPrologList(int type, PrologProvider provider) {
		super(type, provider);
	}

	protected JiPrologList(PrologProvider provider, PrologTerm[] arguments) {
		super(LIST_TYPE, provider);
		value = adaptList(arguments);
	}

	protected JiPrologList(PrologProvider provider, JIPTerm[] arguments) {
		super(LIST_TYPE, provider);
		value = JIPList.NIL;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = JIPList.create(arguments[i], value);
		}
	}

	protected JiPrologList(PrologProvider provider, JIPTerm[] arguments, JIPTerm tail) {
		super(LIST_TYPE, provider);
		value = tail;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = JIPList.create(arguments[i], value);
		}
	}

	protected JiPrologList(PrologProvider provider, PrologTerm head, PrologTerm tail) {
		super(LIST_TYPE, provider);
		value = JIPList.create(fromTerm(head, JIPTerm.class), fromTerm(tail, JIPTerm.class));
	}

	protected JiPrologList(PrologProvider provider, PrologTerm[] arguments, PrologTerm tail) {
		super(LIST_TYPE, provider);
		value = fromTerm(tail, JIPTerm.class);
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = JIPList.create(fromTerm(arguments[i], JIPTerm.class), value);
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
		return toTerm(list.getHead(), PrologTerm.class);
	}

	public PrologTerm getTail() {
		JIPList list = (JIPList) value;
		return toTerm(list.getTail(), PrologTerm.class);
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
		if (list != null) {
			PrologTerm[] arguments = new PrologTerm[list.length()];
			for (int i = 0; i < arguments.length; i++) {
				arguments[i] = toTerm(list.getNth(i + 1), PrologTerm.class);
			}
			return arguments;
		}
		return new PrologTerm[0];
	}

	@Override
	public PrologTerm clone() {
		PrologTerm[] array = getArguments();
		return new JiPrologList(provider, array);
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
			return toTerm(list.getNth(nextIndex++), PrologTerm.class);
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
