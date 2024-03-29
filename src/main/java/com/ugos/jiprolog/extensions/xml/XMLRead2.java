/*
 * 15/10/2002
 *
 * Copyright (C) 2002 Ugo Chirico
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


package com.ugos.jiprolog.extensions.xml;

import com.ugos.jiprolog.engine.*;
import com.ugos.jiprolog.extensions.io.*;

import java.io.*;
import java.util.*;

public class XMLRead2 extends XMLRead1
{
    public boolean unify(JIPCons params, Hashtable varsTbl)
    {
        // get first parameter
        JIPTerm input = params.getNth(1);

        // check if input is a variable
        if (input instanceof JIPVariable)
        {
            // try to extract the term
            if(!((JIPVariable)input).isBounded())
            {
                throw new JIPParameterUnboundedException(1);
            }
            else
            {
                //extracts the term
                input = ((JIPVariable)input).getValue();
            }
        }

        // check if input is a Number
        if(!(input instanceof JIPAtom))
            throw new JIPParameterTypeException(2, JIPParameterTypeException.ATOM);

        // Gets the handle to the stream
        final String strStreamHandle = ((JIPAtom)input).getName();

        // Get the stream
        String strStream = JIPio.getStreamName(strStreamHandle);

        if(strStream == null)
        {
        	throw new JIPDomainException("stream_or_alias", strStreamHandle);
        }

        JIPTerm xmlDoc = createXMLTerm(strStream);

        return params.getNth(2).unify(xmlDoc, varsTbl);
    }
}

