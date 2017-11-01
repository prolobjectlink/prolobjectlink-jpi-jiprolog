/*-
 * #%L
 * prolobjectlink-jpi-jiprolog
 * %%
 * Copyright (C) 2012 - 2019 Prolobjectlink Project
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

import java.util.Iterator;

import javax.script.ScriptEngineFactory;

import org.prolobjectlink.prolog.ArrayIterator;
import org.prolobjectlink.prolog.PrologScriptEngineFactory;

public final class JiPrologScriptFactory extends PrologScriptEngineFactory implements ScriptEngineFactory {

	public JiPrologScriptFactory() {
		super(new JiProlog().newEngine());
	}

	public String getMethodCallSyntax(String obj, String m, String... args) {
		StringBuilder result = new StringBuilder();
		result.append(obj + " <- " + m);
		result.append('(');
		Iterator<String> i = new ArrayIterator<String>(args);
		if (i.hasNext()) {
			while (i.hasNext()) {
				result.append(i.next());
				if (i.hasNext()) {
					result.append(',');
				}
			}
		}
		result.append(')');
		return "" + result + "";
	}

}
