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

import java.util.Hashtable;

final class Arg3 extends BuiltIn
{
	@Override
    public final boolean unify(final Hashtable<Variable, Variable> varsTbl)
    {
        final PrologObject place = getRealTerm(getParam(1));
        if(place == null)
            throw new JIPParameterUnboundedException(1);

        if(!(place instanceof Expression))
            throw  new JIPParameterTypeException(1, JIPParameterTypeException.INTEGER);
        if(!((Expression)place).isInteger())
            throw  new JIPParameterTypeException(1, JIPParameterTypeException.INTEGER);

        final PrologObject term = getRealTerm(getParam(2));
        if(term == null)
            throw new JIPParameterUnboundedException(2);

        PrologObject val   = getParam(3);
        PrologObject param = null;

        try
        {

            if(term instanceof List)
            {
                param = new ConsCell(((List)term).getHead(), new ConsCell(((List)term).getTail(), null)).getTerm((int)((Expression)place).getValue());
            }
            else if(term instanceof Functor)
            {
                param = ((Functor)term).getParams().getTerm((int)((Expression)place).getValue());
            }
            else // ConsCell
            {
                param = ((ConsCell)term).getTerm((int)((Expression)place).getValue());
            }

            if(param == null)
                return false;

            return param.unify(val, varsTbl);
        }
        catch (ClassCastException ex)
        {
            throw  new JIPParameterTypeException(2, JIPParameterTypeException.COMPOUND);
        }
    }

	
	

}
