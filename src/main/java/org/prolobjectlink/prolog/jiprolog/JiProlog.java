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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import org.prolobjectlink.prolog.AbstractProvider;
import org.prolobjectlink.prolog.PrologAtom;
import org.prolobjectlink.prolog.PrologConverter;
import org.prolobjectlink.prolog.PrologDouble;
import org.prolobjectlink.prolog.PrologEngine;
import org.prolobjectlink.prolog.PrologFloat;
import org.prolobjectlink.prolog.PrologInteger;
import org.prolobjectlink.prolog.PrologJavaConverter;
import org.prolobjectlink.prolog.PrologList;
import org.prolobjectlink.prolog.PrologLogger;
import org.prolobjectlink.prolog.PrologLong;
import org.prolobjectlink.prolog.PrologProvider;
import org.prolobjectlink.prolog.PrologStructure;
import org.prolobjectlink.prolog.PrologTerm;
import org.prolobjectlink.prolog.PrologVariable;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

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

	public PrologTerm newReference(Object reference) {
		throw new UnsupportedOperationException("newReference(Object reference)");
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
