1. For new versions put the parse method to use an InputStream or replace the code by PushBackLineInputStream.
2. do public getOperatorManager() in JIPEngine
3. do public OperatorManager class
4. do public Operator class
5. do default visible to m_clauseTable in GlobalDB
6. add the below code in JIPEngine




	public Collection<JIPClausesDatabase> getGlobalDataBase() {
		Collection<JIPClausesDatabase> databases = new Vector<JIPClausesDatabase>();
		for (JIPClausesDatabase jipClausesDatabase : m_globalDB.m_clauseTable.values()) {
			databases.add(jipClausesDatabase);
		}
		return databases;
	}

	public boolean clause(final JIPTerm term) {
		Collection<JIPClausesDatabase> databases = getDataBase();
		for (JIPClausesDatabase jipClausesDatabase : databases) {
			Enumeration enumeration = jipClausesDatabase.clauses();
			while (enumeration.hasMoreElements()) {
				Clause clause = (Clause) enumeration.nextElement();
				JIPClause jipClause = new JIPClause(clause);
				if (term.unifiable(jipClause)) {
					return true;
				}
			}
		}
		return false;
	}

	public Collection<JIPClausesDatabase> getDataBase() {
		Collection<JIPClausesDatabase> databases = new Vector<JIPClausesDatabase>();
		for (JIPClausesDatabase jipClausesDatabase : m_globalDB.m_clauseTable.values()) {
			if (jipClausesDatabase.isDynamic()) {
				databases.add(jipClausesDatabase);
			}
		}
		return databases;
	}

6. For new versions put the parse method to use an InputStream or replace the code by PushBackLineInputStream.