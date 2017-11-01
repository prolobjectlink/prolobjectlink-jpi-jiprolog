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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.prolobjectlink.prolog.AbstractEngine;
import org.prolobjectlink.prolog.AbstractQuery;
import org.prolobjectlink.prolog.PrologQuery;
import org.prolobjectlink.prolog.PrologTerm;

import com.ugos.jiprolog.engine.JIPCons;
import com.ugos.jiprolog.engine.JIPQuery;
import com.ugos.jiprolog.engine.JIPTerm;
import com.ugos.jiprolog.engine.JIPVariable;

class JiPrologQuery extends AbstractQuery implements PrologQuery {

	private JIPQuery query;
	private JIPTerm solution;

	JiPrologQuery(AbstractEngine engine, String stringQuery) {
		super(engine);
		JiPrologEngine pe = (JiPrologEngine) engine;
		query = pe.engine.openSynchronousQuery(stringQuery);
		solution = query.nextSolution();
	}

	JiPrologQuery(AbstractEngine engine, PrologTerm[] terms) {
		super(engine);
		JiPrologEngine pe = (JiPrologEngine) engine;
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
		// return query.hasMoreChoicePoints()
		return solution != null;
	}

	public boolean hasMoreSolutions() {
		// return query.hasMoreChoicePoints()
		return solution != null;
	}

	public PrologTerm[] oneSolution() {
		if (hasSolution()) {
			JIPVariable[] variables = solution.getVariables();
			PrologTerm[] solutions = new PrologTerm[variables.length];
			for (int i = 0; i < solutions.length; i++) {
				if (!variables[i].isAnonymous()) {
					JIPTerm instance = variables[i].getValue();
					if (instance != null) {
						solutions[i] = toTerm(instance, PrologTerm.class);
					} else {
						solutions[i] = toTerm(variables[i], PrologTerm.class);
					}
				}
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
				if (!variables[i].isAnonymous()) {
					String name = variables[i].getName();
					JIPTerm instance = variables[i].getValue();
					if (instance != null) {
						solutions.put(name, toTerm(instance, PrologTerm.class));
					} else {
						solutions.put(name, toTerm(variables[i], PrologTerm.class));
					}
				}
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
			int m = 0;
			int index = 0;
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
				PrologTerm[] array = all.get(i);
				for (int j = 0; j < m; j++) {
					allSolutions[i][j] = array[j];
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
		int n = 0;
		int m = 0;
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
			PrologTerm[] array = all.get(i);
			for (int j = 0; j < m; j++) {
				allSolutions[i][j] = array[j];
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

	public List<Map<String, PrologTerm>> all() {
		List<Map<String, PrologTerm>> allVariables = new ArrayList<Map<String, PrologTerm>>();
		while (hasMoreSolutions()) {
			Map<String, PrologTerm> variables = oneVariablesSolution();
			allVariables.add(variables);
			solution = query.nextSolution();
		}
		return allVariables;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((query == null) ? 0 : query.hashCode());
		result = prime * result + ((solution == null) ? 0 : solution.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		JiPrologQuery other = (JiPrologQuery) obj;
		if (query == null && other.query != null) {
			return false;
		}
		return !(solution == null && other.solution != null);
	}

}
