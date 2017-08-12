package org.logicware.jpi.jiprolog;

import java.util.Iterator;

import org.logicware.jpi.PrologList;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPTerm;

public class JiPrologList extends JiPrologCompound implements PrologList {

	protected JiPrologList(PrologProvider<JIPTerm> provider) {
		super(LIST_TYPE, provider);
	}

	protected JiPrologList(int type, PrologProvider<JIPTerm> provider) {
		super(type, provider);
	}

	protected JiPrologList(PrologProvider<JIPTerm> provider, PrologTerm[] arguments) {
		super(LIST_TYPE, provider);
		value = adaptList(arguments);
	}

	protected JiPrologList(PrologProvider<JIPTerm> provider, JIPTerm[] arguments) {
		super(LIST_TYPE, provider);
		value = JIPList.NIL;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = JIPList.create(arguments[i], value);
		}
	}

	protected JiPrologList(PrologProvider<JIPTerm> provider, JIPTerm[] arguments, JIPTerm tail) {
		super(LIST_TYPE, provider);
		value = tail;
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = JIPList.create(arguments[i], value);
		}
	}

	protected JiPrologList(PrologProvider<JIPTerm> provider, PrologTerm head, PrologTerm tail) {
		super(LIST_TYPE, provider);
		value = JIPList.create(provider.fromTerm(head), provider.fromTerm(tail));
	}

	protected JiPrologList(PrologProvider<JIPTerm> provider, PrologTerm[] arguments, PrologTerm tail) {
		super(LIST_TYPE, provider);
		value = provider.fromTerm(tail);
		for (int i = arguments.length - 1; i >= 0; --i) {
			value = JIPList.create(provider.fromTerm(arguments[i]), value);
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
		return provider.toTerm(list.getHead());
	}

	public PrologTerm getTail() {
		JIPList list = (JIPList) value;
		return provider.toTerm(list.getTail());
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
			arguments[i] = provider.toTerm(list.getNth(i + 1));
		}
		return arguments;
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
			return provider.toTerm(list.getNth(nextIndex++));
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

}
