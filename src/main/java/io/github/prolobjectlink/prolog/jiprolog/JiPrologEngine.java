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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import com.ugos.jiprolog.engine.JIPClause;
import com.ugos.jiprolog.engine.JIPClausesDatabase;
import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import com.ugos.jiprolog.engine.Operator;
import com.ugos.jiprolog.engine.OperatorManager;

import io.github.prolobjectlink.prolog.AbstractEngine;
import io.github.prolobjectlink.prolog.Licenses;
import io.github.prolobjectlink.prolog.PrologClause;
import io.github.prolobjectlink.prolog.PrologEngine;
import io.github.prolobjectlink.prolog.PrologIndicator;
import io.github.prolobjectlink.prolog.PrologLogger;
import io.github.prolobjectlink.prolog.PrologOperator;
import io.github.prolobjectlink.prolog.PrologProvider;
import io.github.prolobjectlink.prolog.PrologQuery;
import io.github.prolobjectlink.prolog.PrologTerm;
import io.github.prolobjectlink.prolog.PrologTermType;

final class JiPrologEngine extends AbstractEngine implements PrologEngine {

	final JIPEngine engine;
	private final JIPTermParser parser;
	private final JiPrologConverter converter;

	private static final String JIPXSETS_VERSION = "ver(jipxsets, '4.0.1')";
	private static final String JIPXIO_VERSION = "ver(jipxio, '4.0.2')";
	private static final String JIPXDB_VERSION = "ver(jipxdb, '3.0.0')";
	private static final String JIPXEXCEPTION_VERSION = "ver(jipxexception, '3.0.2')";
	private static final String JIPXREFLECT_VERSION = "ver(jipxreflect, '3.0.0')";
	private static final String JIPXSYSTEM_VERSION = "ver(jipxsystem, '3.0.1')";
	private static final String JIPXTERMS_VERSION = "ver(jipxterms, '4.0.1')";
	private static final String JIPXXML_VERSION = "ver(jipxxml, '3.0.0')";

	JiPrologEngine(PrologProvider provider) {
		this(provider, new JIPEngine());
	}

	private JiPrologEngine(PrologProvider provider, JIPEngine engine) {
		super(provider);

		this.engine = engine;
		this.parser = engine.getTermParser();
		this.converter = new JiPrologConverter();

		// retract versions predicates
		retract(JIPXSETS_VERSION);
		retract(JIPXIO_VERSION);
		retract(JIPXDB_VERSION);
		retract(JIPXEXCEPTION_VERSION);
		retract(JIPXREFLECT_VERSION);
		retract(JIPXSYSTEM_VERSION);
		retract(JIPXTERMS_VERSION);
		retract(JIPXXML_VERSION);

	}

	public void consult(String path) {
		try {

			// clear database
			engine.reset();

			// retract versions predicates
			retract(JIPXSETS_VERSION);
			retract(JIPXIO_VERSION);
			retract(JIPXDB_VERSION);
			retract(JIPXEXCEPTION_VERSION);
			retract(JIPXREFLECT_VERSION);
			retract(JIPXSYSTEM_VERSION);
			retract(JIPXTERMS_VERSION);
			retract(JIPXXML_VERSION);

			// load and assert
			FileInputStream fins = new FileInputStream(path);
			Enumeration<?> loadEnumeration = parser.parseStream(fins, path);
//			PushbackLineNumberInputStream stream = new PushbackLineNumberInputStream(fins);
//			Enumeration<?> loadEnumeration = parser.parseStream(stream, path);
			while (loadEnumeration.hasMoreElements()) {
				JIPTerm jipTerm = (JIPTerm) loadEnumeration.nextElement();
				engine.assertz(jipTerm);
			}
		} catch (FileNotFoundException e) {
			getLogger().error(getClass(), PrologLogger.FILE_NOT_FOUND, e);
		}
	}

	public void consult(Reader reader) {
		try {

			// clear database
			engine.reset();

			// retract versions predicates
			retract(JIPXSETS_VERSION);
			retract(JIPXIO_VERSION);
			retract(JIPXDB_VERSION);
			retract(JIPXEXCEPTION_VERSION);
			retract(JIPXREFLECT_VERSION);
			retract(JIPXSYSTEM_VERSION);
			retract(JIPXTERMS_VERSION);
			retract(JIPXXML_VERSION);

			BufferedReader bfr = new BufferedReader(reader);
			StringBuilder script = new StringBuilder();
			String line = bfr.readLine();
			while (line != null) {
				script.append(line);
				script.append("\n");
				line = bfr.readLine();
			}

			// load and assert
			String name = reader.toString();
			byte[] bs = script.toString().getBytes();
			ByteArrayInputStream fins = new ByteArrayInputStream(bs);
			Enumeration<?> loadEnumeration = parser.parseStream(fins, name);
//			PushbackLineNumberInputStream stream = new PushbackLineNumberInputStream(fins);
//			Enumeration<?> loadEnumeration = parser.parseStream(stream, name);
			while (loadEnumeration.hasMoreElements()) {
				JIPTerm jipTerm = (JIPTerm) loadEnumeration.nextElement();
				engine.assertz(jipTerm);
			}
		} catch (FileNotFoundException e) {
			getLogger().error(getClass(), PrologLogger.FILE_NOT_FOUND, e);
		} catch (IOException e) {
			getLogger().error(getClass(), PrologLogger.IO, e);
		}
	}

	public void include(String path) {
		try {
			FileInputStream fins = new FileInputStream(path);
			Enumeration<?> loadEnumeration = parser.parseStream(fins, path);
//			PushbackLineNumberInputStream stream = new PushbackLineNumberInputStream(fins);
//			Enumeration<?> loadEnumeration = parser.parseStream(stream, path);
			while (loadEnumeration.hasMoreElements()) {
				JIPTerm jipTerm = (JIPTerm) loadEnumeration.nextElement();
				engine.assertz(jipTerm);
			}
		} catch (FileNotFoundException e) {
			getLogger().error(getClass(), PrologLogger.FILE_NOT_FOUND, e);
		}
	}

	public void include(Reader reader) {
		try {
			BufferedReader bfr = new BufferedReader(reader);
			StringBuilder script = new StringBuilder();
			String line = bfr.readLine();
			while (line != null) {
				script.append(line);
				script.append("\n");
				line = bfr.readLine();
			}

			// load and assert
			String name = reader.toString();
			byte[] bs = script.toString().getBytes();
			ByteArrayInputStream fins = new ByteArrayInputStream(bs);
			Enumeration<?> loadEnumeration = parser.parseStream(fins, name);
//			PushbackLineNumberInputStream stream = new PushbackLineNumberInputStream(fins);
//			Enumeration<?> loadEnumeration = parser.parseStream(stream, name);
			while (loadEnumeration.hasMoreElements()) {
				JIPTerm jipTerm = (JIPTerm) loadEnumeration.nextElement();
				engine.assertz(jipTerm);
			}
		} catch (FileNotFoundException e) {
			getLogger().error(getClass(), PrologLogger.FILE_NOT_FOUND, e);
		} catch (IOException e) {
			getLogger().error(getClass(), PrologLogger.IO, e);
		}
	}

	public void persist(String path) {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(path));
			Collection<JIPClausesDatabase> collection = engine.getDataBase();
			for (JIPClausesDatabase jipClausesDatabase : collection) {
				Enumeration<JIPClause> enumeration = jipClausesDatabase.clauses();
				while (enumeration.hasMoreElements()) {
					writer.println(enumeration.nextElement());
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			getLogger().error(getClass(), PrologLogger.IO, e);
		}
	}

	public void abolish(String functor, int arity) {
		engine.abolish(parser.parseTerm(functor + "/" + arity));
	}

	public void asserta(String stringClause) {
		asserta(JIPClause.create(parser.parseTerm(stringClause)));
	}

	public void asserta(PrologTerm head, PrologTerm... body) {
		JIPCons cons = converter.adaptCons(body);
		asserta(JIPClause.create(fromTerm(head, JIPFunctor.class), cons));
	}

	private void asserta(JIPClause clause) {
		if (!clause(clause)) {
			engine.asserta(clause);
		}
	}

	public void assertz(String stringClause) {
		assertz(JIPClause.create(parser.parseTerm(stringClause)));
	}

	public void assertz(PrologTerm head, PrologTerm... body) {
		JIPCons cons = converter.adaptCons(body);
		assertz(JIPClause.create(fromTerm(head, JIPFunctor.class), cons));
	}

	private void assertz(JIPClause clause) {
		if (!clause(clause)) {
			engine.assertz(clause);
		}
	}

	public boolean clause(String stringClause) {
		return clause(JIPClause.create(parser.parseTerm(stringClause)));
	}

	public boolean clause(PrologTerm head, PrologTerm... body) {
		String c = "" + head + "";
		if (body != null && body.length > 0) {
			String cb = Arrays.toString(body);
			cb = cb.substring(1, cb.length() - 1);
			c = c + " :- " + cb;
		}
		c = c + ".";
		return clause(c);
	}

	private boolean clause(JIPClause clause) {
		return engine.clause(clause);
	}

	public void retract(String stringClause) {
		retract(JIPClause.create(parser.parseTerm(stringClause)));
	}

	public void retract(PrologTerm head, PrologTerm... body) {
		JIPCons cons = converter.adaptCons(body);
		retract(JIPClause.create(fromTerm(head, JIPFunctor.class), cons));
	}

	private void retract(JIPClause clause) {
		engine.retract(clause);
	}

	public PrologQuery query(String stringQuery) {
		return new JiPrologQuery(this, stringQuery);
	}

	public PrologQuery query(PrologTerm[] terms) {
		return new JiPrologQuery(this, terms);
	}

	public PrologQuery query(PrologTerm term, PrologTerm... terms) {
		PrologTerm[] query = new PrologTerm[terms.length + 1];
		System.arraycopy(terms, 0, query, 1, terms.length);
		query[0] = term;
		return query(query);
	}

	public void operator(int priority, String specifier, String operator) {
		engine.getOperatorManager().put(priority, specifier, operator);
	}

	public boolean currentPredicate(String functor, int arity) {
		PrologIndicator pi = new JiPrologIndicator(functor, arity);
		return currentPredicates().contains(pi);
	}

	public boolean currentOperator(int priority, String specifier, String operator) {
		Operator op = engine.getOperatorManager().get(operator);
		return op != null && op.m_nPrecedence == priority && op.m_strAssoc.equals(specifier);
	}

	public Set<PrologOperator> currentOperators() {
		HashSet<PrologOperator> operators = new HashSet<PrologOperator>();
		OperatorManager manager = engine.getOperatorManager();
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
		return operators;
	}

	public int getProgramSize() {
		int counter = 0;
		Collection<JIPClausesDatabase> collection = engine.getDataBase();
		for (JIPClausesDatabase jipClausesDatabase : collection) {
			Enumeration<JIPClause> enumeration = jipClausesDatabase.clauses();
			while (enumeration.hasMoreElements()) {
				enumeration.nextElement();
				counter++;
			}
		}
		return counter;
	}

	public String getVersion() {
		return JIPEngine.getVersion();
	}

	public Iterator<PrologClause> iterator() {
		Collection<PrologClause> cls = new LinkedList<PrologClause>();
		Collection<JIPClausesDatabase> collection = engine.getDataBase();
		for (JIPClausesDatabase jipClausesDatabase : collection) {
			Enumeration<JIPClause> enumeration = jipClausesDatabase.clauses();
			while (enumeration.hasMoreElements()) {
				Object object = enumeration.nextElement();
				JIPTerm term = parser.parseTerm("" + object + "");
				if (term instanceof JIPFunctor) {
					JIPFunctor clause = (JIPFunctor) term;
					if (clause.getName().equals(":-") && clause.getArity() == 2) {
						PrologTerm head = toTerm(clause.getTerm(1), PrologTerm.class);
						PrologTerm body = toTerm(clause.getTerm(2), PrologTerm.class);
						if (body.getType() != PrologTermType.TRUE_TYPE) {
							cls.add(new JiPrologClause(provider, head, body, false, false, false));
						} else {
							cls.add(new JiPrologClause(provider, head, false, false, false));
						}
					} else {
						cls.add(new JiPrologClause(provider, toTerm(clause, PrologTerm.class), false, false, false));
					}
				}
			}
		}
		return new PrologProgramIterator(cls);
	}

	public List<String> verify() {
		return Arrays.asList("OK");
	}

	public Set<PrologIndicator> getPredicates() {
		Set<PrologIndicator> builtins = new HashSet<PrologIndicator>();
		Collection<JIPClausesDatabase> collection = engine.getDataBase();
		for (JIPClausesDatabase jipClausesDatabase : collection) {
			String functor = jipClausesDatabase.getFunctorName();
			int arity = jipClausesDatabase.getArity();
			PrologIndicator pi = new JiPrologIndicator(functor, arity);
			builtins.add(pi);
		}
		return builtins;
	}

	public Set<PrologIndicator> getBuiltIns() {
		Set<PrologIndicator> builtins = new HashSet<PrologIndicator>();
		Collection<JIPClausesDatabase> collection = engine.getGlobalDataBase();
		for (JIPClausesDatabase jipClausesDatabase : collection) {
			String functor = jipClausesDatabase.getFunctorName();
			int arity = jipClausesDatabase.getArity();
			PrologIndicator pi = new JiPrologIndicator(functor, arity);
			builtins.add(pi);
		}
		return builtins;
	}

	public String getLicense() {
		return Licenses.AGPL_V3;
	}

	public String getName() {
		String credits = JIPEngine.getInfo();
		StringTokenizer tokenizer = new StringTokenizer(credits);
		return tokenizer.nextToken();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((engine == null) ? 0 : engine.getEncoding().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JiPrologEngine other = (JiPrologEngine) obj;
		if (engine == null && other.engine != null) {
			return false;
		}
		return !(parser == null && other.parser != null);
	}

	public void dispose() {
		if (engine != null) {

			// clear database
			engine.reset();

			// retract versions predicates
			retract(JIPXSETS_VERSION);
			retract(JIPXIO_VERSION);
			retract(JIPXDB_VERSION);
			retract(JIPXEXCEPTION_VERSION);
			retract(JIPXREFLECT_VERSION);
			retract(JIPXSYSTEM_VERSION);
			retract(JIPXTERMS_VERSION);
			retract(JIPXXML_VERSION);

		}
	}

}
