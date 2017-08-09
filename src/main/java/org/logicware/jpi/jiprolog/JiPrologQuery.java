package org.logicware.jpi.jiprolog;

import static org.logicware.jpi.PrologAdapterFactory.createPrologAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.logicware.jpi.AbstractQuery;
import org.logicware.jpi.IPrologEngine;
import org.logicware.jpi.IPrologQuery;
import org.logicware.jpi.IPrologTerm;
import org.logicware.jpi.PrologAdapter;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPEngine;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;

public class JiPrologQuery extends AbstractQuery implements IPrologQuery {

	private JIPQuery query;
	private JIPTerm solution;

	private final JIPEngine engine;

	final PrologAdapter<JIPTerm> adapter = createPrologAdapter(JiPrologAdapter.class);

	JiPrologQuery(JIPEngine engine, String query) {
		this.engine = engine;
		this.query = engine.openSynchronousQuery(query);
		this.solution = this.query.nextSolution();
	}

	JiPrologQuery(JIPEngine engine, IPrologTerm[] terms) {
		this.engine = engine;
		query = engine.openSynchronousQuery(adaptCons(terms));
		solution = query.nextSolution();
	}

	private JIPCons adaptCons(IPrologTerm[] arguments) {
		JIPCons cons = null;
		for (int i = arguments.length - 1; i >= 0; --i) {
			cons = JIPCons.create(adapter.toNativeTerm(arguments[i]), cons);
		}
		return cons;
	}

	public IPrologEngine getEngine() {
		return new JiPrologEngine(engine);
	}

	public boolean hasSolution() {
		// return query.hasMoreChoicePoints();
		return solution != null;
	}

	public boolean hasMoreSolutions() {
		// return query.hasMoreChoicePoints();
		return solution != null;
	}

	public IPrologTerm[] oneSolution() {
		if (hasSolution()) {
			JIPVariable[] variables = solution.getVariables();
			IPrologTerm[] solutions = new IPrologTerm[variables.length];
			for (int i = 0; i < solutions.length; i++) {
				solutions[i] = adapter.toTerm(variables[i].getValue());
			}
			return solutions;
		}
		return new IPrologTerm[0];
	}

	public Map<String, IPrologTerm> oneVariablesSolution() {
		if (hasSolution()) {
			JIPVariable[] variables = solution.getVariables();
			Map<String, IPrologTerm> solutions = new HashMap<String, IPrologTerm>(variables.length);
			for (int i = 0; i < variables.length; i++) {
				solutions.put(variables[i].getName(), adapter.toTerm(variables[i].getValue()));
			}
			return solutions;
		}
		return new HashMap<String, IPrologTerm>(0);
	}

	public IPrologTerm[] nextSolution() {
		if (hasMoreSolutions()) {
			IPrologTerm[] solutions = oneSolution();
			solution = query.nextSolution();
			return solutions;
		}
		return new IPrologTerm[0];
	}

	public Map<String, IPrologTerm> nextVariablesSolution() {
		if (hasMoreSolutions()) {
			Map<String, IPrologTerm> solutions = oneVariablesSolution();
			solution = query.nextSolution();
			return solutions;
		}
		return new HashMap<String, IPrologTerm>(0);
	}

	public IPrologTerm[][] nSolutions(int n) {
		if (n > 0) {
			int m = 0, index = 0;
			List<IPrologTerm[]> all = new ArrayList<IPrologTerm[]>();
			while (hasMoreSolutions() && index < n) {
				IPrologTerm[] solutions = oneSolution();
				m = solutions.length > m ? solutions.length : m;
				all.add(solutions);
				index++;
				solution = query.nextSolution();
			}

			IPrologTerm[][] allSolutions = new IPrologTerm[n][m];
			for (int i = 0; i < n; i++) {
				IPrologTerm[] solution = all.get(i);
				for (int j = 0; j < m; j++) {
					allSolutions[i][j] = solution[j];
				}
			}
			return allSolutions;
		}
		return new IPrologTerm[0][0];
	}

	@SuppressWarnings("unchecked")
	public Map<String, IPrologTerm>[] nVariablesSolutions(int n) {
		if (n > 0) {
			int index = 0;
			Map<String, IPrologTerm>[] solutionMaps = new HashMap[n];
			while (hasMoreSolutions() && index < n) {
				Map<String, IPrologTerm> solutionMap = oneVariablesSolution();
				solutionMaps[index++] = solutionMap;
				solution = query.nextSolution();
			}
			return solutionMaps;
		}
		return new HashMap[0];
	}

	public IPrologTerm[][] allSolutions() {
		// n:solutionCount, m:solutionSize
		int n = 0, m = 0;
		List<IPrologTerm[]> all = new ArrayList<IPrologTerm[]>();
		while (hasMoreSolutions()) {
			IPrologTerm[] solutions = oneSolution();
			m = solutions.length > m ? solutions.length : m;
			n++;
			all.add(solutions);
			solution = query.nextSolution();
		}

		IPrologTerm[][] allSolutions = new IPrologTerm[n][m];
		for (int i = 0; i < n; i++) {
			IPrologTerm[] solution = all.get(i);
			for (int j = 0; j < m; j++) {
				allSolutions[i][j] = solution[j];
			}
		}
		return allSolutions;
	}

	@SuppressWarnings("unchecked")
	public Map<String, IPrologTerm>[] allVariablesSolutions() {
		List<Map<String, IPrologTerm>> allVariables = new ArrayList<Map<String, IPrologTerm>>();
		while (hasMoreSolutions()) {
			Map<String, IPrologTerm> variables = oneVariablesSolution();
			allVariables.add(variables);
			solution = query.nextSolution();
		}

		int lenght = allVariables.size();
		Map<String, IPrologTerm>[] allVariablesSolution = new HashMap[lenght];
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
