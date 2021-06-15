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

import static io.github.prolobjectlink.prolog.PrologTermType.FLOAT_TYPE;

import com.ugos.jiprolog.engine.JIPNumber;

import io.github.prolobjectlink.prolog.ArityError;
import io.github.prolobjectlink.prolog.FunctorError;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologTerm;

final class JiPrologFloat extends JiPrologTerm implements PrologFloat {

	JiPrologFloat(PrologProvider provider) {
		super(FLOAT_TYPE, provider, JIPNumber.create(0));
	}

	JiPrologFloat(PrologProvider provider, Number value) {
		super(FLOAT_TYPE, provider, JIPNumber.create(value.floatValue()));
	}

	public PrologInteger getPrologInteger() {
		return new JiPrologInteger(provider, getIntegerValue());
	}

	public PrologFloat getPrologFloat() {
		return new JiPrologFloat(provider, getFloatValue());
	}

	public PrologDouble getPrologDouble() {
		return new JiPrologDouble(provider, getDoubleValue());
	}

	public PrologLong getPrologLong() {
		return new JiPrologLong(provider, getLongValue());
	}

	public long getLongValue() {
		return (long) ((JIPNumber) value).getDoubleValue();
	}

	public double getDoubleValue() {
		return ((JIPNumber) value).getDoubleValue();
	}

	public int getIntegerValue() {
		return (int) ((JIPNumber) value).getDoubleValue();
	}

	public float getFloatValue() {
		return (float) ((JIPNumber) value).getDoubleValue();
	}

	public PrologTerm[] getArguments() {
		return new JiPrologFloat[0];
	}

	public int getArity() {
		throw new ArityError(this);
	}

	public String getFunctor() {
		throw new FunctorError(this);
	}

	public PrologTerm clone() {
		float f = getFloatValue();
		return new JiPrologFloat(provider, f);
	}

}
