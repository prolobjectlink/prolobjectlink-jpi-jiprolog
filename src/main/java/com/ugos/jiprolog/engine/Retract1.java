/*
 * 23/04/2014
 *
 * Copyright (C) 1999-2014 Ugo Chirico
 *
 * This is free software; you can redistribute it and/or
 * modify it under the terms of the Affero GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Affero GNU General Public License for more details.
 *
 * You should have received a copy of the Affero GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package com.ugos.jiprolog.engine;

import java.util.*;

final class Retract1 extends BuiltIn
{
    private boolean m_bEnd = false;

    private Enumeration en;
    private JIPClausesDatabase db;
    private boolean immediateUpdateSemantics;

    public final boolean unify(final Hashtable varsTbl)
    {
//      System.out.println("retract");
        Clause clause = Clause.getClause(getParam(1));
        if(clause.getModuleName().equals(GlobalDB.USER_MODULE))
           clause.setModuleName(getWAM().m_curNode.m_strModule);

        Functor functor = (Functor)clause.getHead();

        if(getJIPEngine().isImmediateUpdateSemantics())
        {
        	immediateUpdateSemantics = true;
            Clause retractedClause = getJIPEngine().getGlobalDB().retract(clause);

            if(retractedClause == null)
	        {
	            m_bEnd = true;
	            return false;
	        }

	        return clause.unify(retractedClause, varsTbl);
        }
        else
        {
	        if(en == null)
	        {
		        GlobalDB globalDB = getJIPEngine().getGlobalDB();

		        if(globalDB.isSystem(functor.getName()))
		        	throw new JIPPermissionException("modify", "static_procedure", functor.getName());
//		            throw JIPRuntimeException.create(13, functor);

		        db = globalDB.search(functor, clause.getModuleName());

		        if(db == null)
		            return false;

		        en = ((DefaultClausesDatabase)db).clauses();
	        }

	        if(!en.hasMoreElements())
	            return false;

	        boolean bFound = false;
	        Clause currentClause = null;
	        while(en.hasMoreElements() && !bFound)
	        {
	            currentClause = ((Clause)en.nextElement());//.getHead();

	            if(clause.getTail() == null)
	            {
	                // si tratta solo di funtore
	                bFound = functor.unifiable(currentClause.getHead());
	            }
	            else
	            {
	                // si tratta di clausola
	                bFound = clause.unifiable(currentClause);
	            }
	        }

	        if (bFound)
	        {
	            db.removeClause(new JIPClause(currentClause));
	            return clause.unify(currentClause, varsTbl);
	        }
	        else
	        {
	            return false;
	        }
        }


    }

    public final boolean hasMoreChoicePoints()
    {
    	if(immediateUpdateSemantics)
    	{
    		return !m_bEnd;
    	}
    	else
    	{
    		return en != null && en.hasMoreElements();
    	}
    }
}
