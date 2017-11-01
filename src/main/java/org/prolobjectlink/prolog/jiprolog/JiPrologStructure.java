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

import static org.prolobjectlink.prolog.PrologTermType.STRUCTURE_TYPE;

import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologStructure;
import org.prolobjectlink.prolog.PrologTerm;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPTerm;

final class JiPrologStructure extends JiPrologCompound implements PrologStructure {

	JiPrologStructure(PrologProvider provider, String functor, PrologTerm... arguments) {
		super(STRUCTURE_TYPE, provider);
		value = JIPFunctor.create(removeQuoted(functor), adaptCons(arguments));
	}

	JiPrologStructure(PrologProvider provider, String functor, JIPTerm... arguments) {
		super(STRUCTURE_TYPE, provider);
		JIPCons cons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			cons = JIPCons.create(arguments[i], cons);
		}
		value = JIPFunctor.create(removeQuoted(functor), cons);
	}

	JiPrologStructure(PrologProvider provider, PrologTerm left, String operator, PrologTerm right) {
		super(STRUCTURE_TYPE, provider);
		value = JIPFunctor.create(operator, adaptCons(new PrologTerm[] { left, right }));
	}

	JiPrologStructure(PrologProvider provider, JIPTerm left, String functor, JIPTerm right) {
		super(STRUCTURE_TYPE, provider, JIPFunctor.create(functor, JIPCons.create(left, JIPCons.create(right, null))));
	}

	public PrologTerm getArgument(int index) {
		PrologTerm[] arguments = getArguments();
		checkIndexOutOfBound(index, arguments.length);
		return arguments[index];
	}

	public PrologTerm[] getArguments() {
		JIPFunctor structure = (JIPFunctor) value;
		int arity = structure.getArity();
		PrologTerm[] arguments = new PrologTerm[arity];
		for (int i = 0; i < arity; i++) {
			arguments[i] = toTerm(structure.getTerm(i + 1), PrologTerm.class);
		}
		return arguments;
	}

	public int getArity() {
		JIPFunctor structure = (JIPFunctor) value;
		return structure.getArity();
	}

	public String getFunctor() {
		JIPFunctor structure = (JIPFunctor) value;
		return removeQuoted(structure.getName());
	}

	public String getIndicator() {
		return getFunctor() + "/" + getArity();
	}

	public boolean hasIndicator(String functor, int arity) {
		return getFunctor().equals(functor) && getArity() == arity;
	}

	public final PrologTerm getRight() {
		return getArgument(1);
	}

	public final PrologTerm getLeft() {
		return getArgument(0);
	}

	public final String getOperator() {
		return getFunctor();
	}

}
