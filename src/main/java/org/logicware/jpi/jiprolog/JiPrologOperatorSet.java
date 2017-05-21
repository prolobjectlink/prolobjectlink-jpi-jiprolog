package org.logicware.jpi.jiprolog;

import java.util.AbstractSet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.logicware.jpi.IPrologOperator;
import org.logicware.jpi.OperatorEntry;

import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.Operator;
import com.ugos.jiprolog.engine.OperatorManager;

final class JiPrologOperatorSet extends AbstractSet<OperatorEntry> {

	protected final Set<OperatorEntry> operators;

	public JiPrologOperatorSet() {
		operators = new HashSet<OperatorEntry>();
		OperatorManager manager = new JIPEngine().getOperatorManager();
		Enumeration<?> e = manager.getOperators();
		while (e.hasMoreElements()) {
			Object object = e.nextElement();
			if (object instanceof Operator) {
				Operator op = (Operator) object;
				String s = op.m_strAssoc;
				String o = op.m_strName;
				int p = op.m_nPrecedence;
				OperatorEntry oe = new OperatorEntry(p, s, o);
				operators.add(oe);
			}
		}
	}

	protected boolean currentOp(String opreator) {
		for (IPrologOperator op : operators) {
			if (op.getOperator().equals(opreator)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Iterator<OperatorEntry> iterator() {
		return operators.iterator();
	}

	@Override
	public int size() {
		return operators.size();
	}

}
