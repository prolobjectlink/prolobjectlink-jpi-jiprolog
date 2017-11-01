/*
 * #%L
 * prolobjectlink-jpi-jiprolog
 * %%
 * Copyright (C) 2019 Prolobjectlink Project
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

import org.prolobjectlink.prolog.AbstractIndicator;
import org.prolobjectlink.prolog.PrologIndicator;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class JiPrologIndicator extends AbstractIndicator implements PrologIndicator {

	JiPrologIndicator(String functor, int arity) {
		super(functor, arity);
	}

}
