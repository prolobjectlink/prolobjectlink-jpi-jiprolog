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

final class Nospy1 extends BuiltIn
{
    public final boolean unify(final Hashtable varsTbl)
    {
        PrologObject preds = getRealTerm(getParam(1));
        noSpy(preds);
        
        Hashtable spyTable = (Hashtable)getJIPEngine().getEnvVariable("__spy__");
        if(spyTable != null && spyTable.size() == 0 && getJIPEngine().getEnvVariable("__trace__") == null)
        {
            if(getJIPEngine().getTrace())
            {
                getJIPEngine().setTrace(false);
                ((WAMTrace)getWAM()).notifyStop();
            }
        }
        
        return true;
    }
    
    final void noSpy(PrologObject pred)
    {
        if(pred == null)
        {
            return;
        }
        else if(pred instanceof List)
        {
            noSpy(getRealTerm(((List)pred).getHead()));
            noSpy(((List)pred).getTail());
        }
        else if(pred instanceof Functor)
        {
            // head deve essere instanza di funtore /2 del tipo name/arity
            if(pred instanceof Functor && ((Functor)pred).getName().equals("//2"))
            {
                ConsCell params = ((Functor )pred).getParams();
                String strPredDef = ((Atom)params.getHead()).getName() + "/" + ((ConsCell)params.getTail()).getHead();
                Hashtable spyTable = (Hashtable)getJIPEngine().getEnvVariable("__spy__");
                if(spyTable != null)
                    spyTable.remove(strPredDef);
            }
            else
            {
                throw new JIPParameterTypeException(1, JIPParameterTypeException.PREDICATE_INDICATOR);
            }
        }
        else
            throw new JIPParameterTypeException(1, JIPParameterTypeException.PREDICATE_INDICATOR);
        
//        else if(pred instanceof Atom)
//        {
//            Hashtable spyTable = (Hashtable)getJIPEngine().getEnvVariable("__spy__");
//            if(spyTable != null)
//                spyTable.remove(((Atom)pred).getName());
//        }
    }
}
