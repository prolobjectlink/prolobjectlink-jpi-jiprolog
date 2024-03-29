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

import static io.github.prolobjectlink.prolog.PrologTermType.NIL_TYPE;

import com.ugos.jiprolog.engine.JIPAtom;

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;

final class JiPrologNil extends JiPrologTerm implements PrologTerm {

	JiPrologNil(PrologProvider provider) {
		super(NIL_TYPE, provider, JIPAtom.create("nil"));
	}

	public PrologTerm[] getArguments() {
		return new PrologTerm[0];
	}

	public int getArity() {
		return 0;
	}

	public String getFunctor() {
		return "" + value + "";
	}

}
