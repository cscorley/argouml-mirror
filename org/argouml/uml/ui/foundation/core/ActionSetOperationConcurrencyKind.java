// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;

import javax.swing.JRadioButton;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.UMLAction;
import org.argouml.uml.ui.UMLRadioButtonPanel;

/**
 * An action to set the concurrency of an operation.
 *
 * @author mkl
 *
 */
public class ActionSetOperationConcurrencyKind extends UMLAction {

    private static final ActionSetOperationConcurrencyKind SINGLETON =
        new ActionSetOperationConcurrencyKind();

    /**
     * SEQUENTIAL_COMMAND determines the kind of concurrency.
     */
    public static final String SEQUENTIAL_COMMAND = "sequential";

    /**
     * GUARDED_COMMAND determines the kind of concurrency.
     */
    public static final String GUARDED_COMMAND = "guarded";

    /**
     * CONCURRENT_COMMAND determines the kind of concurrency.
     */
    public static final String CONCURRENT_COMMAND = "concurrent";

    /**
     * Constructor for ActionSetElementOwnershipSpecification.
     */
    protected ActionSetOperationConcurrencyKind() {
        super(Translator.localize("Set"), true, NO_ICON);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() instanceof JRadioButton) {
            JRadioButton source = (JRadioButton) e.getSource();
            String actionCommand = source.getActionCommand();
            Object target = ((UMLRadioButtonPanel) source.getParent())
                    .getTarget();
            if (org.argouml.model.ModelFacade.isAOperation(target)) {
                Object m = /* (MModelElement) */target;
                Object kind = null;
                if (actionCommand.equals(SEQUENTIAL_COMMAND)) {
                    kind = ModelFacade.SEQUENTIAL_CONCURRENCYKIND;
                } else if (actionCommand.equals(GUARDED_COMMAND)) {
                    kind = ModelFacade.GUARDED_CONCURRENCYKIND;
                } else {
                    kind = ModelFacade.CONCURRENT_CONCURRENCYKIND;
                }
                ModelFacade.setConcurrency(m, kind);
            }
        }
    }

    /**
     * @return Returns the sINGLETON.
     */
    public static ActionSetOperationConcurrencyKind getInstance() {
        return SINGLETON;
    }
}
