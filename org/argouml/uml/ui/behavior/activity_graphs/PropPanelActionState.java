// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: PropPanelState.java
// Classes: PropPanelState
// Original Author: your email address here
// $Id$

package org.argouml.uml.ui.behavior.activity_graphs;

import javax.swing.ImageIcon;

import org.argouml.i18n.Translator;
import org.argouml.swingext.Orientation;
import org.argouml.uml.ui.behavior.state_machines.PropPanelState;
import org.argouml.util.ConfigLoader;

/**
 * User interface panel shown at the bottom of the screen that allows the user
 * to edit the properties of the selected UML model element.
 */
public class PropPanelActionState extends PropPanelState {

    /**
     * The constructor.
     * 
     */
    public PropPanelActionState() {
        this("Action State", actionStateIcon, ConfigLoader
                .getTabPropsOrientation());
    }

    /**
     * The constructor.
     * 
     * @param name the name of the properties panel
     * @param icon the icon to be shown next to the name
     * @param orientation the orientation of the panel
     */
    public PropPanelActionState(String name, ImageIcon icon,
            Orientation orientation) {

        super(name, actionStateIcon, orientation);

        addField(Translator.localize("UMLMenu", "label.name"),
                getNameTextField());
        //        addField(Translator.localize("UMLMenu", "label.stereotype"),
        //		 new UMLComboBoxNavigator(this,
        //					  Translator.localize("UMLMenu",
        //						"tooltip.nav-stereo"),
        //					  getStereotypeBox()));
        addField(Translator.localize("UMLMenu", "label.stereotype"),
                getStereotypeBox());

        addField(Translator.localize("UMLMenu", "label.entry"), getEntryScroll());
        addSeperator();

        addField(Translator.localize("UMLMenu", "label.incoming"),
                getIncomingScroll());
        addField(Translator.localize("UMLMenu", "label.outgoing"),
                getOutgoingScroll());

    }

} /* end class PropPanelActionState */
