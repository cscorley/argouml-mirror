// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;

import org.argouml.ui.StylePanelFigNodeModelElement;
import org.tigris.gef.util.Converter;

/**
 * Stylepanel which allows to set the arrow of a message.
 * 
 * @see FigMessage
 */
public class StylePanelFigMessage extends StylePanelFigNodeModelElement {

    
    JLabel _arrowLabel = new JLabel("Arrow: ");

    JComboBox _arrowField = new JComboBox(Converter
            .convert(FigMessage.ARROW_DIRECTIONS));

    
    public StylePanelFigMessage() {
        super();
        GridBagLayout gb = (GridBagLayout) getLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.ipadx = 0;
        c.ipady = 0;

        _arrowField.addItemListener(this);
        c.gridy = 4;
        gb.setConstraints(_arrowLabel, c);
        add(_arrowLabel);

        gb.setConstraints(_arrowField, c);
        add(_arrowField);
        _arrowField.setSelectedIndex(0);

        remove(_fillField);
        remove(_fillLabel);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void refresh() {
        super.refresh();
	if (_target instanceof FigMessage) {
	    int direction = ((FigMessage) _target).getArrow();
	    _arrowField.setSelectedItem(FigMessage.ARROW_DIRECTIONS
		.elementAt(direction));
	}
    }

    public void setTargetArrow() {
        String ad = (String) _arrowField.getSelectedItem();
        int arrowDirection = FigMessage.ARROW_DIRECTIONS.indexOf(ad);
        if (!(_target instanceof FigMessage) || arrowDirection == -1) {
	    return;
	}

        ((FigMessage) _target).setArrow(arrowDirection);
        _target.endTrans();
    }

    ////////////////////////////////////////////////////////////////
    // event handling

    public void removeUpdate(DocumentEvent e) {
        insertUpdate(e);
    }

    public void itemStateChanged(ItemEvent e) {
        Object src = e.getSource();
        if (src == _arrowField)
            setTargetArrow();
        else
            super.itemStateChanged(e);
    }

} /* end class StylePanelFigMessage */

