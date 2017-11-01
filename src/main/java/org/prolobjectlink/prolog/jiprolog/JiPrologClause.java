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

import org.prolobjectlink.prolog.AbstractClause;
import org.prolobjectlink.prolog.PrologClause;
import org.prolobjectlink.prolog.PrologIndicator;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologTerm;

/**
 * 
 * @author Jose Zalacain
 * @since 1.0
 */
final class JiPrologClause extends AbstractClause implements PrologClause {

	private PrologIndicator indicator;

	JiPrologClause(PrologProvider provider, PrologTerm head, boolean dynamic, boolean multifile,
			boolean discontiguous) {
		super(provider, head, dynamic, multifile, discontiguous);
		this.indicator = new JiPrologIndicator(head.getFunctor(), head.getArity());
	}

	JiPrologClause(PrologProvider provider, PrologTerm head, PrologTerm body, boolean dynamic, boolean multifile,
			boolean discontiguous) {
		super(provider, head, body, dynamic, multifile, discontiguous);
		this.indicator = new JiPrologIndicator(head.getFunctor(), head.getArity());
	}

	public PrologIndicator getPrologIndicator() {
		return indicator;
	}

}
