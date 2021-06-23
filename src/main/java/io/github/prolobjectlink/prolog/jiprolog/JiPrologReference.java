/*-
 * #%L
 * prolobjectlink-jpi-jiprolog
 * %%
 * Copyright (C) 2020 - 2021 Prolobjectlink Project
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

import static io.github.prolobjectlink.prolog.PrologTermType.OBJECT_TYPE;

import com.ugos.jiprolog.engine.JIPAtom;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.extensions.reflect.JIPxReflect;

import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologReference;
import io.github.prolobjectlink.prolog.PrologTerm;

public class JiPrologReference extends JiPrologTerm implements PrologReference {

	JiPrologReference(PrologProvider provider, JIPTerm reference) {
		super(OBJECT_TYPE, provider, reference);
	}

	JiPrologReference(PrologProvider provider, Object reference) {
		super(OBJECT_TYPE, provider, set(reference));
	}

	public Class<?> getReferenceType() {
		return getObject().getClass();
	}

	public int getArity() {
		return 0;
	}

	public String getFunctor() {
		JIPAtom object = (JIPAtom) value;
		return object.getName();
	}

	public PrologTerm[] getArguments() {
		return new PrologTerm[0];
	}

	public Object getObject() {
		return get((JIPAtom) value);
	}

	public PrologTerm getTerm() {
		JIPAtom object = (JIPAtom) value;
		String tag = object.getName();
		return provider.newAtom(tag);
	}

	static JIPAtom set(Object reference) {
		if (reference == null) {
			return JIPAtom.create("#0");
		}
		return JIPxReflect.putObject(reference);
	}

	static Object get(JIPAtom id) {
		String str = id.toString();
		str = JiPrologUtil.removeQuoted(str);
		if (str.equalsIgnoreCase("#0")) {
			return null;
		}
		return JIPxReflect.getObject(str);
	}

}
