// $Id: FigGeneralization.java 17046 2009-04-05 17:36:02Z mvw $
// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.structure2.diagram;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;

import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.diagram.DiagramSettings;
import org.argouml.uml.diagram.ui.ArgoFigText;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigTextGroup;
import org.argouml.uml.diagram.ui.PathItemPlacement;
import org.tigris.gef.presentation.ArrowHeadTriangle;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigText;

/**
 * The Fig for a Generalization.
 * 
 * @author abonner@ics.uci.edu, jaap.branderhorst@xs4all.nl
 */
public class FigGeneralization2 extends FigEdgeModelElement {

    private static final int TEXT_HEIGHT = 20;

    private static final int DISCRIMINATOR_WIDTH = 90;

    /*
     * The serialVersionUID (generated by Eclipse)
     */
    private static final long serialVersionUID = 3983170503390943894L;


    /**
     * Group for the FigTexts concerning the name and stereotype of the
     * association itself.
     */
    private FigTextGroup middleGroup;
    
    /**
     * Text box for discriminator.
     */
    private FigText discriminator;

    private ArrowHeadTriangle endArrow;

    private void initialize() {
        // UML spec for Generalizations doesn't call for name

	discriminator.setFilled(false);
	discriminator.setLineWidth(0);
	discriminator.setReturnAction(FigText.END_EDITING);
	discriminator.setTabAction(FigText.END_EDITING);
	
        middleGroup.addFig(discriminator);
        middleGroup.addFig(getStereotypeFig());        
	
	addPathItem(middleGroup, 
	        new PathItemPlacement(this, middleGroup, 50, -10));

        endArrow = new ArrowHeadTriangle();
	endArrow.setFillColor(FILL_COLOR);
	setDestArrowHead(endArrow);
	
	setBetweenNearestPoints(true);
    }

    /**
     * Construct a new generalization edge with the given model element as the
     * owner.
     * 
     * @param owner owning model element
     * @param settings render settings
     */
    public FigGeneralization2(Object owner, DiagramSettings settings) {
        super(owner, settings);
        discriminator = new ArgoFigText(owner, new Rectangle(X0, Y0,
                DISCRIMINATOR_WIDTH, TEXT_HEIGHT), settings, false);
        middleGroup = new FigTextGroup(owner, settings);
        
        
        initialize();
        fixup(owner);
        addListener(owner);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#canEdit(org.tigris.gef.presentation.Fig)
     */
    @Override
    protected boolean canEdit(Fig f) {
        return false;
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    @Override
    protected void modelChanged(PropertyChangeEvent e) {
        // Name & stereotypes get updated by superclass
        super.modelChanged(e);
        // Update the discriminator if it changed
        if (e instanceof AttributeChangeEvent
                && "discriminator".equals(e.getPropertyName())) {
            updateDiscriminatorText();
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigEdgeModelElement#updateListeners(java.lang.Object, java.lang.Object)
     */
    @Override
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeElementListener(oldOwner);
        }
        if (newOwner != null) {
            addListener(newOwner);
        }
    }

    private void addListener(Object owner) {
        addElementListener(owner, new String[] {"remove", "discriminator"});
    }

    /**
     * Updates the discriminator text. Called if the model is changed
     * and on construction time.
     */
    public void updateDiscriminatorText() {
  	Object generalization = getOwner();
  	if (generalization == null) {
	    return;
  	}
  	String disc = 
            (String) Model.getFacade().getDiscriminator(generalization);
  	if (disc == null) {
	    disc = "";
  	}
  	discriminator.setFont(getSettings().getFont(Font.PLAIN));
  	discriminator.setText(disc);
    }

    /*
     * @see org.tigris.gef.presentation.Fig#paint(java.awt.Graphics)
     */
    @Override
    public void paint(Graphics g) {
        endArrow.setLineColor(getLineColor());
        super.paint(g);
    }

    /**
     * Fix up bad figs.
     * <p>
     * Due to errors in earlier releases of argouml it can happen that there is
     * a generalization without a child or parent.
     * <p>
     * TODO: Move into XSL. We should not remove from the graph model while
     * we're writing to it or we have a possible cause of concurrent
     * modification exception.
     * 
     * @param owner
     */
    private void fixup(Object owner) {
        if (Model.getFacade().isAGeneralization(owner)) {
            Object subType = Model.getFacade().getSpecific(owner);
            Object superType = Model.getFacade().getGeneral(owner);

            if (subType == null || superType == null) {
                // TODO: We should warn the user we have removed something - tfm
                removeFromDiagram();
        	return;
            }
            updateDiscriminatorText(); // show it
        } else if (owner != null) {
            throw new IllegalStateException(
                    "FigGeneralization has an illegal owner of "
                    + owner.getClass().getName());
        }
    }
}
