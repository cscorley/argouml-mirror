// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

// File: FigUsage.java
// Classes: FigUsage
// Original Author: Markus Klink
// $Id$

package org.argouml.uml.diagram.ui;

import java.awt.*;
import java.beans.*;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;

import org.argouml.language.helpers.*;

public class FigUsage extends FigDependency {

    ////////////////////////////////////////////////////////////////
    // constructors
    protected ArrowHeadGreater endArrow;

    public FigUsage() {
	super();
    }

    public FigUsage(Object edge) {
        super(edge);
    }

    public FigUsage(Object edge, Layer lay) {
        super(edge, lay);
    }
        
    protected void modelChanged(MElementEvent e) {
	super.modelChanged(e);
	
	String stereoTypeStr = _stereo.getText();

	if (stereoTypeStr == null || "".equals(stereoTypeStr)) {
	    _stereo.setText(NotationHelper.getLeftGuillemot() + "use"
			    + NotationHelper.getRightGuillemot());
	}
    } 

} /* end class FigUsage */

