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

import static org.prolobjectlink.prolog.PrologTermType.ATOM_TYPE;

import org.prolobjectlink.prolog.PrologAtom;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;

import com.ugos.jiprolog.engine.JIPAtom;

final class JiPrologAtom extends JiPrologTerm implements PrologAtom {

	JiPrologAtom(PrologProvider provider, String value) {
		super(ATOM_TYPE, provider, JIPAtom.create(value));
	}

	public String getStringValue() {
		return getFunctor();
	}

	public void setStringValue(String value) {
		this.value = JIPAtom.create(value);
	}

	public PrologTerm[] getArguments() {
		return new JiPrologAtom[0];
	}

	public int getArity() {
		return 0;
	}

	public String getFunctor() {
		return "" + value + "";
	}

	public String getIndicator() {
		return getFunctor() + "/" + getArity();
	}

	public boolean hasIndicator(String functor, int arity) {
		return getFunctor().equals(functor) && getArity() == arity;
	}

}
