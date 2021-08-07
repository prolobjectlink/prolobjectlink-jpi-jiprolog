/*****************************************
 * 27/03/2003
 *
 * Copyright (C) 1999-2003 Ugo Chirico
 * http://www.ugochirico.com
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
 *****************************************/

package com.ugos.jiprolog.extensions.terms;

import com.ugos.jiprolog.engine.*;

import java.util.*;

public class Numbervars3 extends JIPXCall
{
    public final boolean unify(final JIPCons input, Hashtable varsTbl)
    {
        JIPTerm term = input.getNth(1);

        // check if input is a variable
        if (term instanceof JIPVariable)
        {
            // try to extract the term
            if(!((JIPVariable)term).isBounded())
            {
                throw new JIPParameterUnboundedException(1);
            }
            else
            {
                //extracts the term
                term = ((JIPVariable)term).getValue();
            }
        }

        JIPTerm start = input.getNth(2);
        // check if input is a variable
        if (start instanceof JIPVariable)
        {
            // try to extract the term
            if(!((JIPVariable)start).isBounded())
            {
                throw new JIPParameterUnboundedException(1);
            }
            else
            {
                //extracts the term
                start = ((JIPVariable)start).getValue();
            }
        }

        JIPTerm end = input.getNth(3);

        if(!(start instanceof JIPNumber))
            throw new JIPParameterTypeException(2, JIPParameterTypeException.INTEGER);

        if(!((JIPNumber)start).isInteger())
            throw new JIPParameterTypeException(2, JIPParameterTypeException.INTEGER);

        int nStart = (int)((JIPNumber)start).getDoubleValue();
        int nCount = 0;
        JIPVariable vars[] = term.getVariables();

        for(int i = 0; i < vars.length; i++)
        {
            if(!vars[i].isBounded())
            {
                vars[i].unify(JIPAtom.create("'$VAR'(" + (nCount + 1 + nStart) + ")"), varsTbl);
                nCount++;
            }
        }

        if(nCount == 0)
            nCount++;

        return end.unify(JIPNumber.create(nCount), varsTbl);
    }

    public boolean hasMoreChoicePoints()
    {
        return false;
    }
}

