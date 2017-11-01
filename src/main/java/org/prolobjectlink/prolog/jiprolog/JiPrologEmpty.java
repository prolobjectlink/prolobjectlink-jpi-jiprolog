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

import static org.prolobjectlink.prolog.PrologTermType.LIST_TYPE;

import org.prolobjectlink.prolog.PrologList;
import org.prolobjectlink.prolog.PrologProvider;

import com.ugos.jiprolog.engine.JIPList;

final class JiPrologEmpty extends JiPrologList implements PrologList {

	protected JiPrologEmpty(PrologProvider provider) {
		super(LIST_TYPE, provider);
		value = JIPList.create(null, null);
	}

}
