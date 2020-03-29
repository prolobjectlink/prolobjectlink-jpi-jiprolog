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

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPList;
import com.ugos.jiprolog.engine.JIPTerm;

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;

abstract class JiPrologCompound extends JiPrologTerm {

	protected JiPrologCompound(int type, PrologProvider provider) {
		super(type, provider);
	}

	protected JiPrologCompound(int type, PrologProvider provider, JIPTerm value) {
		super(type, provider, value);
	}

	protected void checkIndexOutOfBound(int index, int lenght) {
		if (index < 0 || index > lenght) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
	}

	protected final JIPList adaptList(PrologTerm[] arguments) {
		JIPList list = JIPList.NIL;
		for (int i = arguments.length - 1; i >= 0; --i) {
			list = JIPList.create(fromTerm(arguments[i], JIPTerm.class), list);
		}
		return list;
	}

	protected final JIPCons adaptCons(PrologTerm[] arguments) {
		JIPCons cons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			cons = JIPCons.create(fromTerm(arguments[i], JIPTerm.class), cons);
		}
		return cons;
	}

}
