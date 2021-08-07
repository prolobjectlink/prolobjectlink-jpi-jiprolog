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

/** Exception raised when a built-in predicate find a parameter of unexpected type.
 * @version 3.0
 * @author Ugo Chirico 2005<br>
 * Home Page: http://www.ugochirico.com
 * @see com.ugos.jiprolog.engine.JIPRuntimeException
 */
public class JIPParameterTypeException extends JIPTypeException
{
	/** Constructs a new JIPParameterTypeException
     * @param nParam the index of the bad parameter
     * @param nExpectedType the type of the expected term
     */
    public JIPParameterTypeException(final int nParam, final int nExpectedType, JIPEngine engine)
    {
        super(nExpectedType, "" + nParam);
        m_engine = engine;
    }

    /** Constructs a new JIPParameterTypeException
     * @param nParam the index of the bad parameter
     * @param nExpectedType the type of the expected term
     */
    public JIPParameterTypeException(final int nParam, final int nExpectedType)
    {
        super(nExpectedType, "" + nParam);
    }

    /** Constructs a new JIPParameterTypeException
     */
    public JIPParameterTypeException()
    {
    	super(UNDEFINED, "" + "Undefined");
    }
}
