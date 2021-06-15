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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

import io.github.prolobjectlink.prolog.AbstractProvider;
import io.github.prolobjectlink.prolog.PrologAtom;
import io.github.prolobjectlink.prolog.PrologConverter;
import io.github.prolobjectlink.prolog.PrologDouble;
import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologFloat;
import io.github.prolobjectlink.prolog.PrologInteger;
import io.github.prolobjectlink.prolog.PrologJavaConverter;
import io.github.prolobjectlink.prolog.PrologList;
import io.github.prolobjectlink.prolog.PrologLogger;
import io.github.prolobjectlink.prolog.PrologLong;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologStructure;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologVariable;

public final class JiProlog extends AbstractProvider implements PrologProvider {

	private static final PrologLogger logger = new JiPrologLogger();

	public JiProlog() {
		super(new JiPrologConverter());
	}

	JiProlog(PrologConverter<JIPTerm> converter) {
		super(converter);
	}

	public boolean isCompliant() {
		return false;
	}

	public PrologTerm prologNil() {
		return new JiPrologNil(this);
	}

	public PrologTerm prologCut() {
		return new JiPrologCut(this);
	}

	public PrologTerm prologFail() {
		return new JiPrologFail(this);
	}

	public PrologTerm prologTrue() {
		return new JiPrologTrue(this);
	}

	public PrologTerm prologFalse() {
		return new JiPrologFalse(this);
	}

	public PrologTerm prologEmpty() {
		return new JiPrologEmpty(this);
	}

	public PrologTerm prologInclude(String file) {
		return newStructure("include", newAtom(file));
	}

	public PrologEngine newEngine() {
		return new JiPrologEngine(this);
	}

	public final PrologEngine newEngine(String path) {
		PrologEngine engine = newEngine();
		engine.consult(path);
		return engine;
	}

	public PrologTerm parseTerm(String term) {
		JIPTermParser parser = new JIPEngine().getTermParser();
		return toTerm(parser.parseTerm(term), PrologTerm.class);
	}

	public PrologTerm[] parseTerms(String stringTerms) {
		JIPTermParser parser = new JIPEngine().getTermParser();
		InputStream inputStream = new ByteArrayInputStream(stringTerms.getBytes());
		Enumeration<JIPTerm> e = parser.parseStream(inputStream, inputStream.toString());
//		PushbackLineNumberInputStream stream = new PushbackLineNumberInputStream(inputStream);
//		Enumeration<JIPTerm> e = parser.parseStream(stream, stream.toString());
		ArrayList<PrologTerm> terms = new ArrayList<PrologTerm>();
		while (e.hasMoreElements()) {
			JIPTerm term = e.nextElement();
			if (!(term instanceof JIPCons)) {
				terms.add(toTerm(term, PrologTerm.class));
			} else {

				JIPCons structure = (JIPCons) term;
				int deep = structure.getHeight();
				for (int i = 1; i <= deep; i++) {

//					JIPTerm j = structure.getNth(i)

//					System.out.println(j + "[ " + j.getClass() + " ]")

					PrologTerm k = toTerm(structure.getNth(i), PrologTerm.class);

					terms.add(k);

				}

			}
		}
		return terms.toArray(new PrologTerm[0]);
	}

	// terms

	public PrologAtom newAtom(String functor) {
		return new JiPrologAtom(this, functor);
	}

	public PrologFloat newFloat(Number value) {
		return new JiPrologFloat(this, value);
	}

	public PrologDouble newDouble(Number value) {
		return new JiPrologDouble(this, value);
	}

	public PrologInteger newInteger(Number value) {
		return new JiPrologInteger(this, value);
	}

	public PrologLong newLong(Number value) {
		return new JiPrologLong(this, value);
	}

	public PrologVariable newVariable(int position) {
		return new JiPrologVariable(this);
	}

	public PrologVariable newVariable(String name, int position) {
		return new JiPrologVariable(this, name);
	}

	public PrologList newList() {
		return new JiPrologEmpty(this);
	}

	public PrologList newList(PrologTerm[] arguments) {
		if (arguments != null && arguments.length > 0) {
			return new JiPrologList(this, arguments);
		}
		return new JiPrologList(this);
	}

	public PrologList newList(PrologTerm head, PrologTerm tail) {
		return new JiPrologList(this, head, tail);
	}

	public PrologList newList(PrologTerm[] arguments, PrologTerm tail) {
		return new JiPrologList(this, arguments, tail);
	}

	public PrologStructure newStructure(String functor, PrologTerm... arguments) {
		return new JiPrologStructure(this, functor, arguments);
	}

	public PrologStructure newStructure(PrologTerm left, String operator, PrologTerm right) {
		return new JiPrologStructure(this, left, operator, right);
	}

	public final PrologTerm newEntry(PrologTerm key, PrologTerm value) {
		return new JiPrologEntry(this, key, value);
	}

	public final PrologTerm newEntry(Object key, Object value) {
		PrologJavaConverter transformer = getJavaConverter();
		PrologTerm keyTerm = transformer.toTerm(key);
		PrologTerm valueTerm = transformer.toTerm(value);
		return new JiPrologEntry(this, keyTerm, valueTerm);
	}

	public final PrologTerm newMap(Map<PrologTerm, PrologTerm> map) {
		return new JiPrologMap(this, map);
	}

	public final PrologTerm newMap(int initialCapacity) {
		return new JiPrologMap(this, initialCapacity);
	}

	public final PrologTerm newMap() {
		return new JiPrologMap(this);
	}

	public PrologTerm newReference(Object reference) {
		return new JiPrologReference(this, reference);
	}

	public PrologTerm falseReference() {
		return newReference(false);
	}

	public PrologTerm trueReference() {
		return newReference(true);
	}

	public PrologTerm nullReference() {
		return newReference(null);
	}

	public PrologTerm voidReference() {
		return newReference(void.class);
	}

	public PrologJavaConverter getJavaConverter() {
		return new JiPrologJavaConverter(this);
	}

	public PrologLogger getLogger() {
		return logger;
	}

	@Override
	public String toString() {
		return "JiProlog [converter=" + converter + "]";
	}

}
