// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.apache.log4j.Logger;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;

import java.lang.reflect.*;
import java.awt.event.*;

import javax.swing.*;
import org.argouml.model.ModelFacade;
import ru.novosoft.uml.MElementEvent;

/**
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.UMLMultiplicityComboBox2},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLMultiplicityComboBox
    extends JComboBox
    implements ItemListener, UMLUserInterfaceComponent 
{
    /**
     * @deprecated by Linus Tolke as of 0.16. Will be private.
     */
    protected static Logger cat = Logger.getLogger(UMLMultiplicityComboBox.class);

    private UMLUserInterfaceContainer _container;
    private Method _getMethod;
    private Method _setMethod;


    private static String[] _sMultiplicities = {
	"*",
	"0..1", "0..2", "0..3", "0..*",
	"1..1", "1..2", "1..3", "1..*",
	"2..2", "2..3", "2..*",
	"3..3", "3..*" 
    };

    private static final Object[] _noArg = {};
    
    public UMLMultiplicityComboBox(UMLUserInterfaceContainer container,
				   Class elementClass) {

        super();

        setModel(new DefaultComboBoxModel(_sMultiplicities));
        _container = container;

	setEditable(true);

        addItemListener(this);

        Class[] getArgs = {};
        Class[] setArgs = {
	    (Class) ModelFacade.MULTIPLICITY
	};

        try {

            _getMethod = elementClass.getMethod("getMultiplicity", getArgs);
	    _setMethod = elementClass.getMethod("setMultiplicity", setArgs);

        } catch (Exception e) {
            cat.error(e.toString() + " in UMLMultiplicityComboBox()", e);

            setEnabled(false);

        }

    }

    public void targetChanged() {
        update();
    }

    public void targetReasserted() {
    }
    
    public void roleAdded(final MElementEvent p1) {
    }

    public void recovered(final MElementEvent p1) {
    }

    public void roleRemoved(final MElementEvent p1) {
    }

    public void listRoleItemSet(final MElementEvent p1) {
    }

    public void removed(final MElementEvent p1) {
    }

    public void propertySet(final MElementEvent event) {
        String eventProp = event.getName();
        if (eventProp == null || eventProp.equals("multiplicity")) {
            update();
        }
    }
    
    private void update() {

        try {
	    Object mult = _getMethod.invoke(_container.getTarget(), _noArg);

            if (ModelFacade.isAMultiplicity(mult)) {
		String s = UmlHelper.getHelper()
			.getDataTypes().multiplicityToString(mult);

		if (!s.equals(getSelectedItem()))
		    setSelectedItem(s);
            }
        } catch (Exception e) {
            cat.error(e.toString() + " in UMLMultiplicityComboBox.update()", e);
        }
    }
    
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
	    String s = (String) getSelectedItem();

	    try {
		Object/*MMultiplicity*/ mult = UmlFactory.getFactory()
			.getDataTypes().createMultiplicity(s.trim());

		try {
		    _setMethod.invoke(_container.getTarget(), new Object[] {
			mult
		    });
		} catch (Exception e) {
		    cat.error(e.toString()
			+ " in UMLMultiplicityComboBox.itemStateChanged()", e);
		}
            } catch (IllegalArgumentException iae) {
		// If s is not a well-formed multiplicity
	    }
        }
    }
}
