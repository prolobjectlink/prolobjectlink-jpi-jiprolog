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

import java.util.AbstractSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.Operator;
import com.ugos.jiprolog.engine.OperatorManager;

import io.github.prolobjectlink.prolog.PrologOperator;
import io.github.prolobjectlink.prolog.PrologOperatorSet;

final class JiPrologOperatorSet extends AbstractSet<PrologOperator> implements PrologOperatorSet {

	private final Set<PrologOperator> operators;

	public JiPrologOperatorSet() {
		operators = new HashSet<PrologOperator>();
		OperatorManager manager = new JIPEngine().getOperatorManager();
		Enumeration<?> e = manager.getOperators();
		while (e.hasMoreElements()) {
			Object object = e.nextElement();
			if (object instanceof Operator) {
				Operator op = (Operator) object;
				String s = op.m_strAssoc;
				String o = op.m_strName;
				int p = op.m_nPrecedence;
				PrologOperator oe = new JiPrologOperator(p, s, o);
				operators.add(oe);
			}
		}
	}

	public boolean currentOp(String opreator) {
		for (PrologOperator op : operators) {
			if (op.getOperator().equals(opreator)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<PrologOperator> iterator() {
		return operators.iterator();
	}

	@Override
	public int size() {
		return operators.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((operators == null) ? 0 : operators.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JiPrologOperatorSet other = (JiPrologOperatorSet) obj;
		if (operators == null) {
			if (other.operators != null)
				return false;
		} else if (!operators.equals(other.operators)) {
			return false;
		}
		return true;
	}

}
