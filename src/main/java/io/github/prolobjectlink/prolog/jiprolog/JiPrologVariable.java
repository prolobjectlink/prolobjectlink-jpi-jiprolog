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

import static io.github.prolobjectlink.prolog.PrologTermType.VARIABLE_TYPE;

import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

class JiPrologVariable extends JiPrologTerm implements PrologVariable {

	JiPrologVariable(PrologProvider provider) {
		super(VARIABLE_TYPE, provider, JIPVariable.create());
	}

	JiPrologVariable(PrologProvider provider, String name) {
		super(VARIABLE_TYPE, provider, JIPVariable.create(name));
	}

	JiPrologVariable(int type, PrologProvider provider) {
		super(type, provider);
	}

	JiPrologVariable(int type, PrologProvider provider, String name) {
		super(type, provider, JIPVariable.create(name));
	}

	JiPrologVariable(int type, PrologProvider provider, JIPTerm var) {
		super(type, provider, var);
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

	public int getPosition() {
		throw new UnsupportedOperationException();
	}

}
