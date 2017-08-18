package org.logicware.jpi.jiprolog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.logicware.jpi.AbstractQuery;
import org.logicware.jpi.PrologEngine;
import org.logicware.jpi.PrologQuery;
import org.logicware.jpi.PrologTerm;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;

public class JiPrologQuery extends AbstractQuery implements PrologQuery {

    private JIPQuery query;
    private JIPTerm solution;

    JiPrologQuery(PrologEngine engine, String stringQuery) {
	super(engine);
	JiPrologEngine pe = engine.unwrap(JiPrologEngine.class);
	query = pe.engine.openSynchronousQuery(stringQuery);
	solution = query.nextSolution();
    }

    JiPrologQuery(PrologEngine engine, PrologTerm[] terms) {
	super(engine);
	JiPrologEngine pe = engine.unwrap(JiPrologEngine.class);
	query = pe.engine.openSynchronousQuery(adaptCons(terms));
	solution = query.nextSolution();
    }

    private JIPCons adaptCons(PrologTerm[] arguments) {
	JIPCons cons = null;
	for (int i = arguments.length - 1; i >= 0; --i) {
	    cons = JIPCons.create(fromTerm(arguments[i], JIPTerm.class), cons);
	}
	return cons;
    }

    public boolean hasSolution() {
	// return query.hasMoreChoicePoints();
	return solution != null;
    }

    public boolean hasMoreSolutions() {
	// return query.hasMoreChoicePoints();
	return solution != null;
    }

    public PrologTerm[] oneSolution() {
	if (hasSolution()) {
	    JIPVariable[] variables = solution.getVariables();
	    PrologTerm[] solutions = new PrologTerm[variables.length];
	    for (int i = 0; i < solutions.length; i++) {
		solutions[i] = toTerm(variables[i].getValue(), PrologTerm.class);
	    }
	    return solutions;
	}
	return new PrologTerm[0];
    }

    public Map<String, PrologTerm> oneVariablesSolution() {
	if (hasSolution()) {
	    JIPVariable[] variables = solution.getVariables();
	    Map<String, PrologTerm> solutions = new HashMap<String, PrologTerm>(variables.length);
	    for (int i = 0; i < variables.length; i++) {
		solutions.put(variables[i].getName(), toTerm(variables[i].getValue(), PrologTerm.class));
	    }
	    return solutions;
	}
	return new HashMap<String, PrologTerm>(0);
    }

    public PrologTerm[] nextSolution() {
	if (hasMoreSolutions()) {
	    PrologTerm[] solutions = oneSolution();
	    solution = query.nextSolution();
	    return solutions;
	}
	return new PrologTerm[0];
    }

    public Map<String, PrologTerm> nextVariablesSolution() {
	if (hasMoreSolutions()) {
	    Map<String, PrologTerm> solutions = oneVariablesSolution();
	    solution = query.nextSolution();
	    return solutions;
	}
	return new HashMap<String, PrologTerm>(0);
    }

    public PrologTerm[][] nSolutions(int n) {
	if (n > 0) {
	    int m = 0, index = 0;
	    List<PrologTerm[]> all = new ArrayList<PrologTerm[]>();
	    while (hasMoreSolutions() && index < n) {
		PrologTerm[] solutions = oneSolution();
		m = solutions.length > m ? solutions.length : m;
		all.add(solutions);
		index++;
		solution = query.nextSolution();
	    }

	    PrologTerm[][] allSolutions = new PrologTerm[n][m];
	    for (int i = 0; i < n; i++) {
		PrologTerm[] solution = all.get(i);
		for (int j = 0; j < m; j++) {
		    allSolutions[i][j] = solution[j];
		}
	    }
	    return allSolutions;
	}
	return new PrologTerm[0][0];
    }

    @SuppressWarnings("unchecked")
    public Map<String, PrologTerm>[] nVariablesSolutions(int n) {
	if (n > 0) {
	    int index = 0;
	    Map<String, PrologTerm>[] solutionMaps = new HashMap[n];
	    while (hasMoreSolutions() && index < n) {
		Map<String, PrologTerm> solutionMap = oneVariablesSolution();
		solutionMaps[index++] = solutionMap;
		solution = query.nextSolution();
	    }
	    return solutionMaps;
	}
	return new HashMap[0];
    }

    public PrologTerm[][] allSolutions() {
	// n:solutionCount, m:solutionSize
	int n = 0, m = 0;
	List<PrologTerm[]> all = new ArrayList<PrologTerm[]>();
	while (hasMoreSolutions()) {
	    PrologTerm[] solutions = oneSolution();
	    m = solutions.length > m ? solutions.length : m;
	    n++;
	    all.add(solutions);
	    solution = query.nextSolution();
	}

	PrologTerm[][] allSolutions = new PrologTerm[n][m];
	for (int i = 0; i < n; i++) {
	    PrologTerm[] solution = all.get(i);
	    for (int j = 0; j < m; j++) {
		allSolutions[i][j] = solution[j];
	    }
	}
	return allSolutions;
    }

    @SuppressWarnings("unchecked")
    public Map<String, PrologTerm>[] allVariablesSolutions() {
	List<Map<String, PrologTerm>> allVariables = new ArrayList<Map<String, PrologTerm>>();
	while (hasMoreSolutions()) {
	    Map<String, PrologTerm> variables = oneVariablesSolution();
	    allVariables.add(variables);
	    solution = query.nextSolution();
	}

	int lenght = allVariables.size();
	Map<String, PrologTerm>[] allVariablesSolution = new HashMap[lenght];
	for (int i = 0; i < lenght; i++) {
	    allVariablesSolution[i] = allVariables.get(i);
	}
	return allVariablesSolution;
    }

    public void dispose() {
	if (query != null) {
	    query.close();
	    query = null;
	}
    }

}
