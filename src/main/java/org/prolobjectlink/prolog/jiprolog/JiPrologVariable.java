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

import static org.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import org.prolobjectlink.prolog.ArityError;
import org.prolobjectlink.prolog.FunctorError;
import org.prolobjectlink.prolog.IndicatorError;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;
import org.prolobjectlink.prolog.PrologVariable;

import com.ugos.jiprolog.engine.JIPVariable;

class JiPrologVariable extends JiPrologTerm implements PrologVariable {

	JiPrologVariable(PrologProvider provider) {
		super(VARIABLE_TYPE, provider, JIPVariable.create());
	}

	JiPrologVariable(PrologProvider provider, String name) {
		super(VARIABLE_TYPE, provider, JIPVariable.create(name));
	}

	public boolean isAnonymous() {
		return ((JIPVariable) value).isAnonymous();
	}

	public String getName() {
		String name = ((JIPVariable) value).getName();
		if (name.matches("\\+[0-9]*")) {
			name = "_";
		}
		return name;
	}

	public void setName(String name) {
		this.value = JIPVariable.create(name);
	}

	public PrologTerm[] getArguments() {
		return new JiPrologVariable[0];
	}

	public int getArity() {
		throw new ArityError(this);
	}

	public String getFunctor() {
		throw new FunctorError(this);
	}

	public String getIndicator() {
		throw new IndicatorError(this);
	}

	public boolean hasIndicator(String functor, int arity) {
		throw new IndicatorError(this);
	}

	public int getPosition() {
		throw new UnsupportedOperationException();
	}

}
