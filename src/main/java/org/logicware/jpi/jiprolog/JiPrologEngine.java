package org.logicware.jpi.jiprolog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.logicware.jpi.IPrologEngine;
import org.logicware.jpi.IPrologIndicator;
import org.logicware.jpi.IPrologOperator;
import org.logicware.jpi.IPrologQuery;
import org.logicware.jpi.IPrologTerm;
import org.logicware.jpi.OperatorEntry;
import org.logicware.jpi.PredicateIndicator;

import com.ugos.a.a;
import com.ugos.jiprolog.engine.JIPClause;
import com.ugos.jiprolog.engine.JIPClausesDatabase;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;

public final class JiPrologEngine extends JiPrologAbstract implements IPrologEngine {

	JIPEngine engine;
	JIPTermParser parser;

	JiPrologEngine() {
		this(new JIPEngine());
	}

	JiPrologEngine(JIPEngine engine) {
		this.engine = engine;
		this.parser = engine.getTermParser();

		// retract versions predicates
		retract("ver(jipxsets, '4.0.1')");
		retract("ver(jipxio, '4.0.2')");
		retract("ver(jipxdb, '3.0.0')");
		retract("ver(jipxexception, '3.0.2')");
		retract("ver(jipxreflect, '3.0.0')");
		retract("ver(jipxsystem, '3.0.1')");
		retract("ver(jipxterms, '4.0.1')");
		retract("ver(jipxxml, '3.0.0')");

	}

	public void include(String path) {
		try {
			// FileInputStream fins = new FileInputStream(path);
			a fins = new a(new FileInputStream(path));
			Enumeration<JIPTerm> loadEnumeration = parser.parseStream(fins, path);
			while (loadEnumeration.hasMoreElements()) {
				JIPTerm jipTerm = (JIPTerm) loadEnumeration.nextElement();
				engine.assertz(jipTerm);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void consult(String path) {
		try {

			// clear database
			engine.reset();

			// retract versions predicates
			retract("ver(jipxsets, '4.0.1')");
			retract("ver(jipxio, '4.0.2')");
			retract("ver(jipxdb, '3.0.0')");
			retract("ver(jipxexception, '3.0.2')");
			retract("ver(jipxreflect, '3.0.0')");
			retract("ver(jipxsystem, '3.0.1')");
			retract("ver(jipxterms, '4.0.1')");
			retract("ver(jipxxml, '3.0.0')");

			// load and assert
			// FileInputStream fins = new FileInputStream(path);
			a fins = new a(new FileInputStream(path));
			Enumeration<JIPTerm> loadEnumeration = parser.parseStream(fins, path);
			while (loadEnumeration.hasMoreElements()) {
				JIPTerm jipTerm = (JIPTerm) loadEnumeration.nextElement();
				engine.assertz(jipTerm);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void abolish(String functor, int arity) {
		engine.abolish(parser.parseTerm(functor + "/" + arity));
	}

	public void asserta(String stringClause) {
		asserta(JIPClause.create(parser.parseTerm(stringClause)));
	}

	public void asserta(IPrologTerm head, IPrologTerm... body) {
		asserta(JIPClause.create((JIPFunctor) adapt(head), adaptCons(body)));
	}

	private void asserta(JIPClause clause) {
		if (!clause(clause)) {
			engine.asserta(clause);
		}
	}

	public void assertz(String stringClause) {
		assertz(JIPClause.create(parser.parseTerm(stringClause)));
	}

	public void assertz(IPrologTerm head, IPrologTerm... body) {
		assertz(JIPClause.create((JIPFunctor) adapt(head), adaptCons(body)));
	}

	private void assertz(JIPClause clause) {
		if (!clause(clause)) {
			engine.assertz(clause);
		}
	}

	public boolean clause(String stringClause) {
		return clause(JIPClause.create(parser.parseTerm(stringClause)));
	}

	public boolean clause(IPrologTerm head, IPrologTerm... body) {
		return clause(JIPClause.create((JIPFunctor) adapt(head), adaptCons(body)));
	}

	private boolean clause(JIPClause clause) {
		return engine.clause(clause);
	}

	public void retract(String stringClause) {
		retract(JIPClause.create(parser.parseTerm(stringClause)));
	}

	public void retract(IPrologTerm head, IPrologTerm... body) {
		retract(JIPClause.create((JIPFunctor) adapt(head), adaptCons(body)));
	}

	private void retract(JIPClause clause) {
		engine.retract(clause);
	}

	public Map<String, IPrologTerm> find(String goal) {
		return createQuery(goal).oneVariablesSolution();
	}

	public Map<String, IPrologTerm> find(IPrologTerm... goal) {
		return createQuery(goal).oneVariablesSolution();
	}

	public Map<String, IPrologTerm>[] findAll(String goal) {
		return createQuery(goal).allVariablesSolutions();
	}

	public Map<String, IPrologTerm>[] findAll(IPrologTerm... goal) {
		return createQuery(goal).allVariablesSolutions();
	}

	public IPrologQuery createQuery(String stringQuery) {
		return new JiPrologQuery(engine, stringQuery);
	}

	public IPrologQuery createQuery(IPrologTerm... terms) {
		return new JiPrologQuery(engine, terms);
	}

	public void operator(int priority, String specifier, String operator) {
		engine.getOperatorManager().put(priority, specifier, operator);
	}

	public boolean currentPredicate(String functor, int arity) {
		IPrologIndicator pi = new PredicateIndicator(functor, arity);
		return currentPredicates().contains(pi);
	}

	public boolean currentOperator(int priority, String specifier, String operator) {
		Operator op = engine.getOperatorManager().get(operator);
		return op != null && op.m_nPrecedence == priority && op.m_strAssoc.equals(specifier);
	}

	public Set<IPrologIndicator> currentPredicates() {
		Set<IPrologIndicator> builtins = new HashSet<IPrologIndicator>();
		Collection<JIPClausesDatabase> collection = engine.getGlobalDataBase();
		for (JIPClausesDatabase jipClausesDatabase : collection) {
			String functor = jipClausesDatabase.getFunctorName();
			int arity = jipClausesDatabase.getArity();
			PredicateIndicator pi = new PredicateIndicator(functor, arity);
			builtins.add(pi);
		}
		return builtins;
	}

	public Set<IPrologOperator> currentOperators() {
		HashSet<IPrologOperator> operators = new HashSet<IPrologOperator>();
		OperatorManager manager = engine.getOperatorManager();
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

	public boolean isProgramEmpty() {
		return getProgramSize() == 0;
	}

	public String getVersion() {
		return JIPEngine.getVersion();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((engine == null) ? 0 : engine.hashCode());
		result = prime * result + ((parser == null) ? 0 : parser.hashCode());
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
		if (engine == null) {
			if (other.engine != null)
				return false;
		} else if (!engine.equals(other.engine))
			return false;
		if (parser == null) {
			if (other.parser != null)
				return false;
		} else if (!parser.equals(other.parser))
			return false;
		return true;
	}

	public void dispose() {
		if (engine != null) {
			engine.releaseAllResources();
			engine = new JIPEngine();
		}
	}

}
