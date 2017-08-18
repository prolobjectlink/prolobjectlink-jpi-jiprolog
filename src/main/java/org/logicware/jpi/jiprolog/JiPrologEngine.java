package org.logicware.jpi.jiprolog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.logicware.jpi.AbstractEngine;
import org.logicware.jpi.OperatorEntry;
import org.logicware.jpi.PredicateIndicator;
import org.logicware.jpi.PrologEngine;
import org.logicware.jpi.PrologIndicator;
import org.logicware.jpi.IPrologOperator;
import org.logicware.jpi.PrologProvider;
import org.logicware.jpi.PrologQuery;
import org.logicware.jpi.PrologTerm;
import org.logicware.jpi.RuntimeError;

import com.ugos.jiprolog.engine.JIPClause;
import com.ugos.jiprolog.engine.JIPClausesDatabase;
import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPFunctor;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPTermParser;
import com.ugos.jiprolog.engine.Operator;
import com.ugos.jiprolog.engine.OperatorManager;

public final class JiPrologEngine extends AbstractEngine implements PrologEngine {

    JIPEngine engine;
    JIPTermParser parser;

    JiPrologEngine(PrologProvider provider) {
	this(provider, new JIPEngine());
    }

    JiPrologEngine(PrologProvider provider, JIPEngine engine) {
	super(provider);

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

    private JIPCons adaptCons(PrologTerm[] arguments) {
	JIPCons cons = null;
	for (int i = arguments.length - 1; i >= 0; --i) {
	    cons = JIPCons.create(fromTerm(arguments[i], JIPTerm.class), cons);
	}
	return cons;
    }

    public void include(String path) {
	try {
	    FileInputStream fins = new FileInputStream(path);
	    Enumeration loadEnumeration = parser.parseStream(fins, path);
	    while (loadEnumeration.hasMoreElements()) {
		JIPTerm jipTerm = (JIPTerm) loadEnumeration.nextElement();
		engine.assertz(jipTerm);
	    }
	} catch (FileNotFoundException e) {
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
	    FileInputStream fins = new FileInputStream(path);
	    Enumeration loadEnumeration = parser.parseStream(fins, path);
	    while (loadEnumeration.hasMoreElements()) {
		JIPTerm jipTerm = (JIPTerm) loadEnumeration.nextElement();
		engine.assertz(jipTerm);
	    }
	} catch (FileNotFoundException e) {
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
	    e.printStackTrace();
	}
    }

    public void abolish(String functor, int arity) {
	engine.abolish(parser.parseTerm(functor + "/" + arity));
    }

    public void asserta(String stringClause) {
	asserta(JIPClause.create(parser.parseTerm(stringClause)));
    }

    public void asserta(PrologTerm head, PrologTerm... body) {
	asserta(JIPClause.create(fromTerm(head, JIPFunctor.class), adaptCons(body)));
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
	assertz(JIPClause.create(fromTerm(head, JIPFunctor.class), adaptCons(body)));
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
	return clause(JIPClause.create(fromTerm(head, JIPFunctor.class), adaptCons(body)));
    }

    private boolean clause(JIPClause clause) {
	return engine.clause(clause);
    }

    public void retract(String stringClause) {
	retract(JIPClause.create(parser.parseTerm(stringClause)));
    }

    public void retract(PrologTerm head, PrologTerm... body) {
	retract(JIPClause.create(fromTerm(head, JIPFunctor.class), adaptCons(body)));
    }

    private void retract(JIPClause clause) {
	engine.retract(clause);
    }

    public PrologQuery query(String stringQuery) {
	return new JiPrologQuery(this, stringQuery);
    }

    public PrologQuery query(PrologTerm... terms) {
	return new JiPrologQuery(this, terms);
    }

    public void operator(int priority, String specifier, String operator) {
	engine.getOperatorManager().put(priority, specifier, operator);
    }

    public boolean currentPredicate(String functor, int arity) {
	PrologIndicator pi = new PredicateIndicator(functor, arity);
	return currentPredicates().contains(pi);
    }

    public boolean currentOperator(int priority, String specifier, String operator) {
	Operator op = engine.getOperatorManager().get(operator);
	return op != null && op.m_nPrecedence == priority && op.m_strAssoc.equals(specifier);
    }

    public Set<PrologIndicator> currentPredicates() {
	Set<PrologIndicator> builtins = new HashSet<PrologIndicator>();
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

    public <T> T unwrap(Class<T> cls) {
	if (cls.equals(JiPrologEngine.class)) {
	    return (T) this;
	}
	throw new RuntimeError("Impossible unwrap to " + cls.getName());
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

	}
    }

}
