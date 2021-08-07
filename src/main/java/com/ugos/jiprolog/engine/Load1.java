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

import java.io.*;
import java.util.*;

//import com.ugos.util.MapperHolder;

class Load1 extends Consult1
{
    public final boolean unify(final Hashtable varsTbl)
    {
        String strPath = null;
        final PrologObject path = getRealTerm(getParam(1));

        if(path instanceof Atom)
        {
            strPath = ((Atom)path).getName();
        }
        else if(path instanceof PString)
        {
            strPath = ((PString)path).getString();
        }
        else
        {
            throw new JIPParameterTypeException(1, JIPParameterTypeException.ATOM_OR_STRING);
        }

        try
        {
            load(strPath, getJIPEngine());
        }
        catch(ClassNotFoundException ex)
        {
        	throw new JIPTypeException(JIPTypeException.LIBRARY, strPath);
//            throw JIPRuntimeException.create(28, strPath);
        }
        catch(FileNotFoundException ex)
        {
        	throw JIPExistenceException.createSourceSynkException(strPath);
//            throw JIPRuntimeException.create(6, strPath);
        }
        catch(IOException ex)
        {
            throw new JIPJVMException(ex);
        }
        return true;
    }

    static final void load(String strPath, JIPEngine engine) throws IOException, ClassNotFoundException
    {
        String strFileName[] = new String[1];
        String strCurDir[] = new String[1];

        InputStream ins = StreamManager.getStreamManager().getInputStream(strPath, engine.getSearchPath(), strFileName, strCurDir);

        String strOldSearchPath = engine.getSearchPath();
        engine.setSearchPath(strCurDir[0]);
        load(ins, strFileName[0], engine);
        engine.setSearchPath(strOldSearchPath);
    }

    static final void load(InputStream ins, String strStramName, JIPEngine engine) throws IOException, ClassNotFoundException
    {
        try
        {
//        	JSON Serialization
//        	ArrayList<PrologObject> program = new ArrayList<PrologObject>();
//        	MapperHolder.mapper().readerForUpdating(program).readValue(ins);

            final ObjectInputStream oins = new ObjectInputStream(ins);
            ArrayList<PrologObject> program = (ArrayList<PrologObject>)oins.readObject();
            oins.close();

            //System.out.println("load + " + predList.toString(engine));

            engine.getGlobalDB().unconsult(strStramName);

            final Vector<PrologObject> initializationVector = new Vector<PrologObject>();
            final Hashtable exportTbl = new Hashtable(10,1);
            exportTbl.put("#module", GlobalDB.USER_MODULE);
            final WAM wam = new WAM(engine);

            for(PrologObject pred : program)
            {
                pred = getRealTerm(pred);
                _assert(pred, engine, strStramName, null, exportTbl, initializationVector, wam);
            }

            for(PrologObject goal : initializationVector)
            {
            	// chiama la wam
                if(!wam.query(goal))
                {
                    wam.closeQuery();
                    throw JIPRuntimeException.createRuntimeException(27, strStramName + "-" + goal.toString(engine));
                }

                wam.closeQuery();
            }
        }
        catch(SecurityException ex)
        {
            try
            {
                ins.close();
            }
            catch(IOException ex1){}

            throw new JIPPermissionException("access", "source_sink", strStramName);
//            throw JIPRuntimeException.create(9, strStramName);
        }
        finally
        {
        	try
            {
                ins.close();
            }
            catch(IOException ex1){}

        }
    }
}
