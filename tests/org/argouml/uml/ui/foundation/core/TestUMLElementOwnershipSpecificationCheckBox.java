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

// $header$
package org.argouml.uml.ui.foundation.core;

import junit.framework.TestCase;

import org.argouml.uml.ui.MockUMLUserInterfaceContainer;

import ru.novosoft.uml.MFactoryImpl;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestUMLElementOwnershipSpecificationCheckBox extends TestCase {

    private UMLElementOwnershipSpecificationCheckBox box = null;
    private MModelElement elem = null;
    
    /**
     * Constructor for TestUMLElementOwnershipSpecificationCheckBox.
     * @param arg0
     */
    public TestUMLElementOwnershipSpecificationCheckBox(String arg0) {
        super(arg0);
    }
    
    public void testDoClick() {
        boolean spec = elem.isSpecification();
        box.doClick();
        assert("click did not work!", spec != elem.isSpecification());
    }
    
    public void testPropertySet() {
        boolean selected = box.isSelected();
        elem.setSpecification(!selected);
        assert("elem change did not work!", selected != box.isSelected());
    }


    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
        elem = new MClassImpl();
        MockUMLUserInterfaceContainer mockcomp = new MockUMLUserInterfaceContainer();
        mockcomp.setTarget(elem);
        box = new UMLElementOwnershipSpecificationCheckBox(mockcomp);
        elem.addMElementListener(box);
    }

    /**
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        elem.remove();
        elem = null;
        box = null;
    }

}