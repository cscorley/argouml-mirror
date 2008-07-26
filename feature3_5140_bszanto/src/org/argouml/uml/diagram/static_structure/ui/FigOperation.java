// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Font;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;
import org.tigris.gef.presentation.Fig;

/**
 * Fig with specific knowledge of Operation display. 
 * Makes the text italic in case the Operation is abstract.
 *
 * @since 0.23.5
 * @author Bob Tarling
 */
public class FigOperation extends FigFeature {

    /**
     * Constructor.
     * 
     * @param x x
     * @param y x
     * @param w w
     * @param h h
     * @param aFig the fig
     * @param np the notation provider for the text
     */
    public FigOperation(int x, int y, int w, int h, Fig aFig, 
            NotationProvider np) {
        super(x, y, w, h, aFig, np);
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#setOwner(java.lang.Object)
     */
    public void setOwner(Object owner) {
        super.setOwner(owner);

        if (owner != null) {
            diagramFontChanged(null);
            Model.getPump().addModelEventListener(this, owner, "isAbstract");
        }
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#removeFromDiagram()
     */
    public void removeFromDiagram() {
        Model.getPump().removeModelEventListener(this, getOwner(), 
                "isAbstract");
        super.removeFromDiagram();
    }

    /*
     * @see org.argouml.uml.diagram.ui.FigSingleLineText#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        super.propertyChange(pce);
        if ("isAbstract".equals(pce.getPropertyName())) {
            diagramFontChanged(null);    
        }
    }

    /*
     * If the Operation is abstract, then the text will be set to italics.
     */
    @Override
    protected int getFigFontStyle() {
        return Model.getFacade().isAbstract(getOwner()) 
            ? Font.ITALIC : Font.PLAIN;
    }
    
    /**
     * Cyclies the visibility of an operation when clicking in the beggining 
     * part of the FigOperation.
     * @param r Hit rectangle.
     * @author bszanto
     */
    public void changeVisibility(Rectangle r) {
        if (super.hit(r)) {         
            String notation = ProjectManager.getManager().getCurrentProject()
                    .getProjectSettings().getNotationLanguage();
            
            // The hit zone is different for the different notations.
            // Java uses words (public, protected, private) while UML 1.4 uses
            // signs (+, -, ~, #).
            int offset = 0;
            if (notation.equals("Java")) {
                offset = 50;
            } else if (notation.equals("UML 1.4")) {
                offset = 10;
            }
             
            if (r.x < (_x + offset)) {
                Object operation = getOwner();
                Object visibity = Model.getFacade().getVisibility(operation);
                
                // visibility is chandes according to prop panel order which is:
                // public, package, protected, private
                if (Model.getVisibilityKind().getPrivate().equals(visibity)) {
                    Model.getCoreHelper().setVisibility(operation,
                            Model.getVisibilityKind().getPublic());
                } else if (Model.getVisibilityKind().getPublic().equals(
                        visibity)) {
                    Model.getCoreHelper().setVisibility(operation,
                            Model.getVisibilityKind().getPackage());
                } else if (Model.getVisibilityKind().getPackage().equals(
                        visibity)) {
                    Model.getCoreHelper().setVisibility(operation,
                            Model.getVisibilityKind().getProtected());
                } else {
                    Model.getCoreHelper().setVisibility(operation,
                            Model.getVisibilityKind().getPrivate());
                }
            }
        }
    }

}
