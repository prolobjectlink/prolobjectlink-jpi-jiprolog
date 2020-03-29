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

import static io.github.prolobjectlink.prolog.PrologTermType.LIST_TYPE;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPTerm;

import io.github.prolobjectlink.prolog.AbstractIterator;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;

class JiPrologList extends JiPrologCompound implements PrologList {

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

	public final int size() {
		return ((JIPList) value).length();
	}

	public final void clear() {
		value = JIPList.NIL;
	}

	public final boolean isEmpty() {
		return ((JIPList) value).isNIL();
	}

	public final Iterator<PrologTerm> iterator() {
		JIPList list = (JIPList) value;
		return new JIPrologListIter(list);
	}

	public final PrologTerm getHead() {
		JIPList list = (JIPList) value;
		return toTerm(list.getHead(), PrologTerm.class);
	}

	public final PrologTerm getTail() {
		JIPList list = (JIPList) value;
		return toTerm(list.getTail(), PrologTerm.class);
	}

	public final int getArity() {
		if (value != null) {
			JIPList list = (JIPList) value;
			if (list.getHead() == null && list.getTail() == null) {
				return 0;
			}
			return 2;
		}
		return 0;
	}

	public final String getFunctor() {
		if (value != null) {
			JIPList list = (JIPList) value;
			if (list.getHead() == null && list.getTail() == null) {
				return "[]";
			}
			return ".";
		}
		return "[]";
	}

	public final String getIndicator() {
		return getFunctor() + "/" + getArity();
	}

	public final boolean hasIndicator(String functor, int arity) {
		return getFunctor().equals(functor) && getArity() == arity;
	}

	public final PrologTerm[] getArguments() {
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

	private class JIPrologListIter extends AbstractIterator<PrologTerm> implements Iterator<PrologTerm> {

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

			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			return toTerm(list.getNth(nextIndex++), PrologTerm.class);
		}

	}

}
