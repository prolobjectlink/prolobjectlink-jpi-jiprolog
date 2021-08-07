/*
 * 23/04/2014
 *
 * Copyright (C) 1999-2014 Ugo Chirico - http://www.ugochirico.com
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.*;

//import com.ugos.util.MapperHolder;

final class Compile1 extends Consult1
{
    public final boolean unify(final Hashtable<Variable, Variable> varsTbl)
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

        compile(strPath, getJIPEngine());

        return true;
    }

    public static final void compile(String strPath, final JIPEngine engine)
    {
        InputStream ins = null;
        InputStream oldins = null;
        String strOldInputStreamName = null;
        try
        {
            String strFileName[] = new String[1];
            String strCurDir[] = new String[1];
            ins = StreamManager.getStreamManager().getInputStream(strPath, engine.getSearchPath(), strFileName, strCurDir);
            oldins = engine.getCurrentInputStream();
            strOldInputStreamName = engine.getCurrentInputStreamName();
            engine.setCurrentInputStream(ins, strPath);

//            List predList = null;
            //ParserInputStream pins = new ParserInputStream(ins);
            ArrayList<PrologObject> program = new ArrayList<PrologObject>();
            PrologParser parser = new PrologParser(new ParserReader(new InputStreamReader(ins, engine.getEncoding())), engine.getOperatorManager(), strPath);

            try
            {
                PrologObject term;

                while ((term = parser.parseNext()) != null)
                {
                	program.add(term);
                    //System.out.println(term);
//                    predList = new List(term, predList);
                }

                ins.close();

//                predList = (List)predList.reverse();
            }
            catch(IOException ex)
            {
                engine.setCurrentInputStream(oldins, strOldInputStreamName);
                throw new JIPJVMException(ex);
                //throw new JIPRuntimeException("Unable to consult " + strStreamName + ": " + ex.toString());
            }

            ins.close();

            engine.setCurrentInputStream(ins, strOldInputStreamName);

            final int nPos = strFileName[0].lastIndexOf('.');
            strPath = strFileName[0].substring(0, nPos) + ".jip";

            final File outf = new File(strPath);

//            MapperHolder.mapper().writeValue(outf, program);

            final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outf));
            out.writeObject(program);
            out.close();
        }
        catch(FileNotFoundException ex)
        {
            try
            {
                if(ins != null)
                    ins.close();
            }
            catch(IOException ex1){};

            if(oldins != null)
                engine.setCurrentInputStream(oldins, strOldInputStreamName);

            throw JIPExistenceException.createSourceSynkException(strPath);
        }
        catch(IOException ex)
        {
            if(oldins != null)
                engine.setCurrentInputStream(oldins, strOldInputStreamName);

            try
            {
                if(ins != null)
                    ins.close();
            }
            catch(IOException ex1){}

            throw new JIPJVMException(ex);
        }
        catch(SecurityException ex)
        {
            if(oldins != null)
                engine.setCurrentInputStream(oldins, strOldInputStreamName);

            try
            {
                if(ins != null)
                    ins.close();
            }
            catch(IOException ex1){}

            throw new JIPPermissionException("access", "source_sink", strPath);
        }
        catch(JIPRuntimeException ex)
        {

            if(oldins != null)
                engine.setCurrentInputStream(oldins, strOldInputStreamName);

            ex.m_strFileName = strPath;

            try
            {
                if(ins != null)
                    ins.close();
            }
            catch(IOException ex1){}

            throw ex;
        }
    }
}
