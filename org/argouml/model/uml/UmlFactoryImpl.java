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

package org.argouml.model.uml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.argouml.model.ActivityGraphsFactory;
import org.argouml.model.CollaborationsFactory;
import org.argouml.model.CommonBehaviorFactory;
import org.argouml.model.CoreFactory;
import org.argouml.model.ExtensionMechanismsFactory;
import org.argouml.model.IllegalModelElementConnectionException;
import org.argouml.model.ModelFacade;
import org.argouml.model.ModelManagementFactory;
import org.argouml.model.StateMachinesFactory;
import org.argouml.model.UmlFactory;
import org.argouml.model.UseCasesFactory;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.behavior.activity_graphs.MActivityGraph;
import ru.novosoft.uml.behavior.activity_graphs.MCallState;
import ru.novosoft.uml.behavior.activity_graphs.MClassifierInState;
import ru.novosoft.uml.behavior.activity_graphs.MObjectFlowState;
import ru.novosoft.uml.behavior.activity_graphs.MPartition;
import ru.novosoft.uml.behavior.activity_graphs.MSubactivityState;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAttributeLink;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MCreateAction;
import ru.novosoft.uml.behavior.common_behavior.MDataValue;
import ru.novosoft.uml.behavior.common_behavior.MDestroyAction;
import ru.novosoft.uml.behavior.common_behavior.MException;
import ru.novosoft.uml.behavior.common_behavior.MInstance;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;
import ru.novosoft.uml.behavior.common_behavior.MLinkObject;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MReception;
import ru.novosoft.uml.behavior.common_behavior.MReturnAction;
import ru.novosoft.uml.behavior.common_behavior.MSendAction;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.common_behavior.MTerminateAction;
import ru.novosoft.uml.behavior.common_behavior.MUninterpretedAction;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MFinalState;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MSimpleState;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MStubState;
import ru.novosoft.uml.behavior.state_machines.MSubmachineState;
import ru.novosoft.uml.behavior.state_machines.MSynchState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.behavior.use_cases.MUseCaseInstance;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MBinding;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MElement;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MFlow;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MMethod;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MNode;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MPermission;
import ru.novosoft.uml.foundation.core.MPresentationElement;
import ru.novosoft.uml.foundation.core.MRelationship;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.core.MTemplateParameter;
import ru.novosoft.uml.foundation.core.MUsage;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MElementImport;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MSubsystem;

/**
 * Root factory for UML model element instance creation.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class UmlFactoryImpl
    extends AbstractUmlModelFactory
    implements UmlFactory {
    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * A map of valid connections keyed by the connection type.
     * The constructor builds this from the data in the VALID_CONNECTIONS array
     */
    private Map validConnectionMap = new HashMap();

    /**
     * An array of valid connections, the combination of connecting class
     * and node classes must exist as a row in this list to be considered
     * valid.
     * <ul>
     * <li>The 1st column is the connecting element.
     * <li>The 2nd column is the "from" element type.
     * <li>The 3rd column is the "to" element type.
     * <li>The 3rd column is optional, if not given then it is assumed
     * to be the same as the "to" element.
     * <li>The existence of a 4th column indicates that the connection
     * is valid in one direction only.
     * </ul>
     */
    private static final Object[][] VALID_CONNECTIONS =
    {
	{ModelFacade.GENERALIZATION,   ModelFacade.CLASSIFIER_ROLE, },
	{ModelFacade.GENERALIZATION,   ModelFacade.CLASS, },
	{ModelFacade.GENERALIZATION,   ModelFacade.INTERFACE, },
	{ModelFacade.GENERALIZATION,   ModelFacade.PACKAGE, },
	{ModelFacade.GENERALIZATION,   ModelFacade.USE_CASE, },
	{ModelFacade.GENERALIZATION,   ModelFacade.ACTOR, },
	{ModelFacade.DEPENDENCY,       ModelFacade.PACKAGE, },
	{ModelFacade.DEPENDENCY,       ModelFacade.CLASS, },
	{ModelFacade.DEPENDENCY,       ModelFacade.INTERFACE, },
	{ModelFacade.DEPENDENCY,       ModelFacade.INTERFACE,
	 ModelFacade.CLASS, },
	{ModelFacade.DEPENDENCY,       ModelFacade.INTERFACE,
	 ModelFacade.PACKAGE, },
	{ModelFacade.DEPENDENCY,       ModelFacade.CLASS,
	 ModelFacade.PACKAGE, },
	{ModelFacade.DEPENDENCY,       ModelFacade.USE_CASE, },
	{ModelFacade.DEPENDENCY,       ModelFacade.ACTOR, },
	{ModelFacade.DEPENDENCY,       ModelFacade.ACTOR,
	 ModelFacade.USE_CASE, },
	{ModelFacade.DEPENDENCY,       ModelFacade.COMPONENT, },
	{ModelFacade.DEPENDENCY,       ModelFacade.COMPONENT_INSTANCE, },
	{ModelFacade.DEPENDENCY,       ModelFacade.OBJECT, },
	{ModelFacade.DEPENDENCY,       ModelFacade.COMPONENT,
	 ModelFacade.NODE,               null, },
	{ModelFacade.DEPENDENCY,       ModelFacade.OBJECT,
	 ModelFacade.COMPONENT,          null, },
	{ModelFacade.DEPENDENCY,       ModelFacade.COMPONENT_INSTANCE,
	 ModelFacade.NODE_INSTANCE,      null, },
	{ModelFacade.DEPENDENCY,       ModelFacade.OBJECT,
	 ModelFacade.COMPONENT_INSTANCE, null, },
	{ModelFacade.DEPENDENCY,       ModelFacade.CLASSIFIER_ROLE, },
	{ModelFacade.USAGE,            ModelFacade.CLASS, },
	{ModelFacade.USAGE,            ModelFacade.INTERFACE, },
	{ModelFacade.USAGE,            ModelFacade.PACKAGE, },
	{ModelFacade.USAGE,            ModelFacade.CLASS,
	 ModelFacade.PACKAGE, },
	{ModelFacade.USAGE,            ModelFacade.CLASS,
	 ModelFacade.INTERFACE, },
	{ModelFacade.USAGE,            ModelFacade.INTERFACE,
	 ModelFacade.PACKAGE, },
	{ModelFacade.PERMISSION,       ModelFacade.CLASS, },
	{ModelFacade.PERMISSION,       ModelFacade.INTERFACE, },
	{ModelFacade.PERMISSION,       ModelFacade.PACKAGE, },
	{ModelFacade.PERMISSION,       ModelFacade.CLASS,
	 ModelFacade.PACKAGE, },
	{ModelFacade.PERMISSION,       ModelFacade.CLASS,
	 ModelFacade.INTERFACE, },
	{ModelFacade.PERMISSION,       ModelFacade.INTERFACE,
	 ModelFacade.PACKAGE, },
	{ModelFacade.ABSTRACTION,      ModelFacade.CLASS,
	 ModelFacade.INTERFACE,        null, },
	{ModelFacade.ABSTRACTION,      ModelFacade.CLASS,
	 ModelFacade.CLASS,            null,
	},
	{ModelFacade.ABSTRACTION,      ModelFacade.PACKAGE,
	    ModelFacade.PACKAGE,          null,
	},
	{ModelFacade.ASSOCIATION,      ModelFacade.CLASS, },
	{ModelFacade.ASSOCIATION,      ModelFacade.CLASS,
	 ModelFacade.INTERFACE, },
	{ModelFacade.ASSOCIATION,      ModelFacade.ACTOR, },
	{ModelFacade.ASSOCIATION,      ModelFacade.USE_CASE, },
	{ModelFacade.ASSOCIATION,      ModelFacade.ACTOR,
	 ModelFacade.USE_CASE, },
	{ModelFacade.ASSOCIATION,      ModelFacade.NODE, },
	{ModelFacade.ASSOCIATION_ROLE, ModelFacade.CLASSIFIER_ROLE, },
	{ModelFacade.EXTEND,           ModelFacade.USE_CASE, },
	{ModelFacade.INCLUDE,          ModelFacade.USE_CASE, },
	{ModelFacade.LINK,             ModelFacade.NODE_INSTANCE, },
	{ModelFacade.LINK,             ModelFacade.OBJECT, },
        {ModelFacade.TRANSITION,       ModelFacade.STATEVERTEX, },
        {ModelFacade.ASSOCIATION_CLASS, ModelFacade.CLASS, },
        {ModelFacade.ASSOCIATION_END, ModelFacade.CLASS,
         ModelFacade.ASSOCIATION, },
    };

    /**
     * Don't allow external instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    UmlFactoryImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;

        buildValidConnectionMap();
    }

    private void buildValidConnectionMap() {
        // A list of valid connections between elements, the
        // connection type first and then the elements to be connected

        Object connection = null;
        for (int i = 0; i < VALID_CONNECTIONS.length; ++i) {
            connection = VALID_CONNECTIONS[i][0];
            List validItems =
                (ArrayList) validConnectionMap.get(connection);
            if (validItems == null) {
                validItems = new ArrayList();
                validConnectionMap.put(connection, validItems);
            }
            if (VALID_CONNECTIONS[i].length < 3) {
                // If there isn't a 3rd column then this represents a connection
                // of elements of the same type.
                Object[] modeElementPair = new Class[2];
                modeElementPair[0] = VALID_CONNECTIONS[i][1];
                modeElementPair[1] = VALID_CONNECTIONS[i][1];
                validItems.add(modeElementPair);
            } else {
                // If there is a 3rd column then this represents a connection
                // of between 2 different types of element.
                Object[] modeElementPair = new Class[2];
                modeElementPair[0] = VALID_CONNECTIONS[i][1];
                modeElementPair[1] = VALID_CONNECTIONS[i][2];
                validItems.add(modeElementPair);
                // If the array hasn't been flagged to indicate otherwise
                // swap elements the elemnts and add again.
                if (VALID_CONNECTIONS[i].length < 4) {
                    Object[] reversedModeElementPair = new Class[2];
                    reversedModeElementPair[0] = VALID_CONNECTIONS[i][2];
                    reversedModeElementPair[1] = VALID_CONNECTIONS[i][1];
                    validItems.add(reversedModeElementPair);
                }
            }
        }
    }


    /**
     * Creates a UML model element of the given type and uses
     * this to connect two other existing UML model elements.
     * This only works for UML elements. If a diagram contains
     * elements of another type then it is the responsibility
     * of the diagram manage those items and not call this
     * method. It also only works for UML model elements that
     * are represented in diagrams by an edge, hence the
     * requirement to state the connecting ends.
     *
     * @param elementType the UML object type of the connection
     * @param fromElement    the UML object for the "from" element
     * @param fromStyle      the aggregationkind for the connection
     *                       in case of an association
     * @param toElement      the UML object for the "to" element
     * @param toStyle        the aggregationkind for the connection
     *                       in case of an association
     * @param unidirectional for association and associationrole
     * @param namespace      the namespace to use if it can't be determined
     * @return               the newly build connection (UML object)
     * @throws IllegalModelElementConnectionException if the connection is not
     *                                                a valid thing to do
     */
    public Object buildConnection(Object elementType,
                  Object fromElement, Object fromStyle,
                  Object toElement, Object toStyle,
                  Object unidirectional,
                  Object namespace)
        throws IllegalModelElementConnectionException {

        if (!isConnectionValid(elementType, fromElement, toElement)) {
            throw new IllegalModelElementConnectionException(
                "Cannot make a "
            + elementType.getClass().getName()
            + " between a " + fromElement.getClass().getName()
            + " and a " + toElement.getClass().getName());
        }

        Object connection = null;

        if (elementType == ModelFacade.ASSOCIATION) {
            connection =
                getCore().buildAssociation(
                       (MClassifier) fromElement,
                       (MAggregationKind) fromStyle,
                       (MClassifier) toElement,
                       (MAggregationKind) toStyle,
                       (Boolean) unidirectional);
        } else if (elementType == ModelFacade.ASSOCIATION_END) {
            if (fromElement instanceof MAssociation) {
                connection =
                    getCore().buildAssociationEnd(toElement, fromElement);
            } else if (fromElement instanceof MClassifier) {
                connection =
                    getCore().buildAssociationEnd(fromElement, toElement);
            }
        } else if (elementType == ModelFacade.ASSOCIATION_CLASS) {
            connection =
                getCore().buildAssociationClass(fromElement, toElement);
        } else if (elementType == ModelFacade.ASSOCIATION_ROLE) {
            connection =
                getCollaborations()
                	.buildAssociationRole(fromElement, fromStyle,
                	        toElement, toStyle,
                	        (Boolean) unidirectional);
        } else if (elementType == ModelFacade.GENERALIZATION) {
            connection =
                getCore().buildGeneralization(fromElement, toElement);
        } else if (elementType == ModelFacade.PERMISSION) {
            connection = getCore().buildPermission(fromElement, toElement);
        } else if (elementType == ModelFacade.USAGE) {
            connection =
                getCore().buildUsage(fromElement, toElement);
        } else if (elementType == ModelFacade.GENERALIZATION) {
            connection =
                getCore().buildGeneralization(fromElement, toElement);
        } else if (elementType == ModelFacade.DEPENDENCY) {
            connection = getCore().buildDependency(fromElement, toElement);
        } else if (elementType == ModelFacade.ABSTRACTION) {
            connection =
                getCore().buildRealization(
                        fromElement,
                        toElement,
                        namespace);
        } else if (elementType == ModelFacade.LINK) {
            connection = getCommonBehavior().buildLink(fromElement, toElement);
        } else if (elementType == ModelFacade.EXTEND) {
            // Extend, but only between two use cases. Remember we draw from the
            // extension port to the base port.
            connection = getUseCases().buildExtend(toElement, fromElement);
        } else if (elementType == ModelFacade.INCLUDE) {
            connection = getUseCases().buildInclude(fromElement, toElement);
        }

        if (connection == null) {
            throw new IllegalModelElementConnectionException(
            "Cannot make a "
            + elementType.getClass().getName()
            + " between a " + fromElement.getClass().getName()
            + " and a " + toElement.getClass().getName());
        }

        return connection;
    }

    /**
     * Creates a UML model element of the given type.
     * This only works for UML elements. If a diagram contains
     * elements of another type then it is the responsibility
     * of the diagram manage those items and not call this
     * method. It also only works for UML model elements that
     * are represented in diagrams by a node. <p>
     *
     * The parameter "elementType" stands for the type of
     * model element to build.
     *
     * @see org.argouml.model.UmlFactory#buildNode(java.lang.Object)
     */
    public Object buildNode(Object elementType) {

        Object modelElement = null;
        if (elementType == ModelFacade.ACTOR) {
            return getUseCases().createActor();
        } else if (elementType == ModelFacade.USE_CASE) {
            return getUseCases().createUseCase();
        } else if (elementType == ModelFacade.CLASS) {
            return getCore().buildClass();
        } else if (elementType == ModelFacade.INTERFACE) {
            return getCore().buildInterface();
        } else if (elementType == ModelFacade.PACKAGE) {
            return getModelManagement().createPackage();
        } else if (elementType == ModelFacade.MODEL) {
            return getModelManagement().createModel();
        } else if (elementType == ModelFacade.INSTANCE) {
            return getCommonBehavior().createInstance();
        } else if (elementType == ModelFacade.SUBSYSTEM) {
            return getModelManagement().createSubsystem();
        } else if (elementType == ModelFacade.CALLSTATE) {
            return getActivityGraphs().createCallState();
        } else if (elementType == ModelFacade.FINALSTATE) {
            return getStateMachines().createFinalState();
        } else if (elementType == ModelFacade.PSEUDOSTATE) {
            return getStateMachines().createPseudostate();
        } else if (elementType == ModelFacade.OBJECTFLOWSTATE) {
            return getActivityGraphs().createObjectFlowState();
        } else if (elementType == ModelFacade.ACTION_STATE) {
            return getActivityGraphs().createActionState();
        } else if (elementType == ModelFacade.SUBACTIVITYSTATE) {
            return getActivityGraphs().createSubactivityState();
        } else if (elementType == ModelFacade.COMPOSITESTATE) {
            return getStateMachines().createCompositeState();
        } else if (elementType == ModelFacade.STATE) {
            return getStateMachines().createState();
        } else if (elementType == ModelFacade.CLASSIFIER_ROLE) {
            return getCollaborations().createClassifierRole();
        } else if (elementType == ModelFacade.COMPONENT) {
            return getCore().createComponent();
        } else if (elementType == ModelFacade.COMPONENT_INSTANCE) {
            return getCommonBehavior().createComponentInstance();
        } else if (elementType == ModelFacade.NODE) {
            return getCore().createNode();
        } else if (elementType == ModelFacade.NODE_INSTANCE) {
            return getCommonBehavior().createNodeInstance();
        } else if (elementType == ModelFacade.OBJECT) {
            return getCommonBehavior().createObject();
        } else if (elementType == ModelFacade.COMMENT) {
            return getCore().createComment();
        } else if (elementType == ModelFacade.NAMESPACE) {
            return getCore().createNamespace();
        }
        return modelElement;
    }

    /**
     * Checks if some type of UML model element is valid to
     * connect two other existing UML model elements.
     * This only works for UML elements. If a diagram contains
     * elements of another type then it is the responsibility
     * of the diagram to filter those out before calling this
     * method.
     *
     * @param connectionType  the UML object type of the connection
     * @param fromElement     the UML object type of the "from"
     * @param toElement       the UML object type of the "to"
     * @return true if valid
     */
    public boolean isConnectionValid(Object connectionType,
                     Object fromElement, Object toElement) {
        // Get the list of valid model item pairs for the given connection type
        List validItems =
            (ArrayList) validConnectionMap.get(connectionType);
        if (validItems == null) {
            return false;
        }
        // See if there's a pair in this list that match the given
        // model elements
        Iterator it = validItems.iterator();
        while (it.hasNext()) {
            Class[] modeElementPair = (Class[]) it.next();
            if (modeElementPair[0].isInstance(fromElement)
                && modeElementPair[1].isInstance(toElement)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the package factory for the UML
     * package Foundation::ExtensionMechanisms.
     *
     * @return the ExtensionMechanisms factory instance.
     */
    private ExtensionMechanismsFactory getExtensionMechanisms() {
        return nsmodel.getExtensionMechanismsFactory();
    }

    /**
     * Returns the package factory for the UML
     * package Foundation::Core.
     *
     * @return the Core factory instance.
     */
    public CoreFactory getCore() {
        return nsmodel.getCoreFactory();
    }

    /**
     * Returns the package factory for the UML
     * package BehavioralElements::CommonBehavior.
     *
     * @return the CommonBehavior factory instance.
     */
    public CommonBehaviorFactory getCommonBehavior() {
        return nsmodel.getCommonBehaviorFactory();
    }

    /**
     * Returns the package factory for the UML
     * package BehavioralElements::UseCases.
     *
     * @return the UseCases factory instance.
     */
    public UseCasesFactory getUseCases() {
        return nsmodel.getUseCasesFactory();
    }

    /**
     * Returns the package factory for the UML
     * package BehavioralElements::StateMachines.
     *
     * @return the StateMachines factory instance.
     */
    public StateMachinesFactory getStateMachines() {
        return nsmodel.getStateMachinesFactory();
    }

    /**
     * Returns the package factory for the UML
     * package BehavioralElements::Collaborations.
     *
     * @return the Collaborations factory instance.
     */
    public CollaborationsFactory getCollaborations() {
        return nsmodel.getCollaborationsFactory();
    }

    /**
     * Returns the package factory for the UML
     * package BehavioralElements::ActivityGraphs.
     *
     * @return the ActivityGraphs factory instance.
     */
    private ActivityGraphsFactory getActivityGraphs() {
        return nsmodel.getActivityGraphsFactory();
    }

    /**
     * Returns the package factory for the UML
     * package ModelManagement.
     *
     * @return the ModelManagement factory instance.
     */
    public ModelManagementFactory getModelManagement() {
        return nsmodel.getModelManagementFactory();
    }

    /**
     * Deletes a modelelement. It calls the remove method of the
     * modelelement but also does 'cascading deletes' that are not
     * provided for in the remove method of the modelelement
     * itself. For example: this delete method also removes the binary
     * associations that a class has if the class is deleted. In this
     * way, it is not longer possible that illegal states exist in the
     * model.<p>
     *
     * The actual deletion is delegated to delete methods in the rest of the
     * factories. For example: a method deleteClass exists on CoreHelper.
     * Delete methods as deleteClass should only do those extra actions that are
     * necessary for the deletion of the modelelement itself. I.e. deleteClass
     * should only take care of things specific to MClass.<p>
     *
     * The delete methods in the UML Factories should not be called directly
     * throughout the code! Calls should allways refer to this method and never
     * call the deleteXXX method on XXXFactory directly. The reason that it is
     * possible to call the deleteXXX methods directly is a pure implementation
     * detail.<p>
     *
     * The implementation of this method uses a quite complicate if then else
     * tree. This is done to provide optimal performance and full compliance to
     * the UML 1.3 model. The last remark refers to the fact that the UML 1.3
     * model knows multiple inheritance in several places. This has to be taken
     * into account.<p>
     *
     * Extensions and its children are not taken into account
     * here. They do not require extra cleanup actions. Not in the
     * form of a call to the remove method as is normal for all
     * children of MBase and not in the form of other behaviour we
     * want to implement via this operation.
     *
     * @param elem The element to be deleted
     */
    public void delete(Object elem) {
        if (elem == null) {
            throw new IllegalArgumentException("Element may not be null "
                + "in delete");
        }
        if (elem instanceof MElement) {
            getCore().deleteElement(elem);
            if (elem instanceof MModelElement) {
                getCore().deleteModelElement(elem);
                if (elem instanceof MFeature) {
                    deleteFeature((MFeature) elem);
                } else if (elem instanceof MNamespace) {
                    deleteNamespace((MNamespace) elem);
                }
        // no else here to make sure MClassifier with
        // its double inheritance goes ok
                // no else here too to make sure MAssociationClass goes ok

                if (elem instanceof MGeneralizableElement) {
                    MGeneralizableElement ge = (MGeneralizableElement) elem;
                    getCore().deleteGeneralizableElement(ge);
                    if (elem instanceof MStereotype) {
                        MStereotype s = (MStereotype) elem;
                        getExtensionMechanisms().deleteStereotype(s);
                    }
                } // no else here to make sure MAssociationClass goes ok

                if (elem instanceof MParameter) {
                    getCore().deleteParameter(elem);
                } else if (elem instanceof MConstraint) {
                    getCore().deleteConstraint(elem);
                } else if (elem instanceof MRelationship) {
                    deleteRelationship((MRelationship) elem);
                } else if (elem instanceof MAssociationEnd) {
                    getCore().deleteAssociationEnd(elem);
                    if (elem instanceof MAssociationEndRole) {
                        getCollaborations().deleteAssociationEndRole(elem);
                    }
                } else if (elem instanceof MComment) {
                    getCore().deleteComment(elem);
                } else if (ModelFacade.isAAction(elem)) {
                    deleteAction(elem);
                } else if (elem instanceof MAttributeLink) {
                    getCommonBehavior().deleteAttributeLink(elem);
                } else if (elem instanceof MInstance) {
                    deleteInstance((MInstance) elem);
                } // no else to handle multiple inheritance of linkobject

                if (elem instanceof MLink) {
                    getCommonBehavior().deleteLink(elem);
                } else if (elem instanceof MLinkEnd) {
                    getCommonBehavior().deleteLinkEnd(elem);
                } else if (elem instanceof MInteraction) {
                    getCollaborations().deleteInteraction(elem);
                } else if (elem instanceof MMessage) {
                    getCollaborations().deleteMessage(elem);
                } else if (elem instanceof MExtensionPoint) {
                    getUseCases().deleteExtensionPoint(elem);
                } else if (elem instanceof MStateVertex) {
                    deleteStateVertex((MStateVertex) elem);
                }

                if (elem instanceof MStateMachine) {
                    getStateMachines().deleteStateMachine(elem);
                    if (elem instanceof MActivityGraph) {
                        getActivityGraphs().deleteActivityGraph(elem);
                    }
                } else if (elem instanceof MTransition) {
                    getStateMachines().deleteTransition(elem);
                } else if (elem instanceof MGuard) {
                    getStateMachines().deleteGuard(elem);
                }
        // else if (elem instanceof MEvent) {
        //
        //}
            } else if (elem instanceof MPresentationElement) {
                getCore().deletePresentationElement(elem);
            }
        } else if (elem instanceof MTemplateParameter) {
            getCore().deleteTemplateParameter(elem);
        } else if (elem instanceof MTaggedValue) {
            getExtensionMechanisms().deleteTaggedValue(elem);
        }

        if (elem instanceof MPartition) {
            getActivityGraphs().deletePartition(elem);
        } else if (elem instanceof MElementImport) {
            getModelManagement().deleteElementImport(elem);
        }

        if (elem instanceof MBase) {
            ((MBase) elem).remove();
            UmlModelEventPump.getPump().cleanUp((MBase) elem);
            UmlModelListener.getInstance().deleteElement(elem);
        }
    }

    /**
     * The Project may check if a certain MBase has been removed.
     *
     * @param o the object to be checked
     * @return true if removed
     */
    public boolean isRemoved(Object o) {
        return ((MBase) o).isRemoved();
    }

    /**
     * Factored this method out of delete to simplify the design of the delete
     * operation.
     *
     * @param elem
     */
    private void deleteFeature(MFeature elem) {
        getCore().deleteFeature(elem);
        if (elem instanceof MBehavioralFeature) {
            getCore().deleteBehavioralFeature(elem);
            if (elem instanceof MOperation) {
                getCore().deleteOperation(elem);
            } else if (elem instanceof MMethod) {
                getCore().deleteMethod(elem);
            } else if (elem instanceof MReception) {
                getCommonBehavior().deleteReception(elem);
            }
        } else if (elem instanceof MStructuralFeature) {
            getCore().deleteStructuralFeature(elem);
            if (elem instanceof MAttribute) {
                getCore().deleteAttribute(elem);
            }
        }
    }

    /**
     * Factored this method out of delete to simplify the design of the delete
     * operation.
     *
     * @param elem
     */
    private void deleteNamespace(MNamespace elem) {
        getCore().deleteNamespace(elem);
        if (elem instanceof MClassifier) {
            getCore().deleteClassifier(elem);
            if (elem instanceof MClass) {
                getCore().deleteClass(elem);
                if (elem instanceof MAssociationClass) {
                    getCore().deleteAssociationClass(elem);
                }
            } else if (elem instanceof MInterface) {
                getCore().deleteInterface(elem);
            } else if (elem instanceof MDataType) {
                getCore().deleteDataType(elem);
            } else if (elem instanceof MNode) {
                getCore().deleteNode(elem);
            } else if (elem instanceof MComponent) {
                getCore().deleteComponent(elem);
            } else if (elem instanceof MSignal) {
                getCommonBehavior().deleteSignal(elem);
                if (elem instanceof MException) {
                    getCommonBehavior().deleteException(elem);
                }
            } else if (elem instanceof MClassifierRole) {
                getCollaborations().deleteClassifierRole(elem);
            } else if (elem instanceof MUseCase) {
                getUseCases().deleteUseCase(elem);
            } else if (elem instanceof MActor) {
                getUseCases().deleteActor(elem);
            } else if (elem instanceof MClassifierInState) {
                getActivityGraphs().deleteClassifierInState(elem);
            }
        } else if (elem instanceof MCollaboration) {
            getCollaborations().deleteCollaboration(elem);
        } else if (elem instanceof MPackage) {
            getModelManagement().deletePackage(elem);
            if (elem instanceof MModel) {
                getModelManagement().deleteModel(elem);
            } else if (elem instanceof MSubsystem) {
                getModelManagement().deleteSubsystem(elem);
            }
        }
    }

    /**
     * Factored this method out of delete to simplify the design of the delete
     * operation.
     *
     * @param elem
     */
    private void deleteRelationship(MRelationship elem) {
        getCore().deleteRelationship(elem);
        if (elem instanceof MFlow) {
            getCore().deleteFlow(elem);
        } else if (elem instanceof MGeneralization) {
            getCore().deleteGeneralization(elem);
        } else if (elem instanceof MAssociation) {
            getCore().deleteAssociation(elem);
            if (elem instanceof MAssociationRole) {
                getCollaborations().deleteAssociationRole(elem);
            }
        } else if (elem instanceof MDependency) {
            getCore().deleteDependency(elem);
            if (ModelFacade.isAAbstraction(elem)) {
                getCore().deleteAbstraction(elem);
            } else if (elem instanceof MBinding) {
                getCore().deleteBinding(elem);
            } else if (elem instanceof MUsage) {
                getCore().deleteUsage(elem);
            } else if (elem instanceof MPermission) {
                getCore().deletePermission(elem);
            }
        } else if (elem instanceof MInclude) {
            getUseCases().deleteInclude(elem);
        } else if (elem instanceof MExtend) {
            getUseCases().deleteExtend(elem);
        }
    }

    /**
     * Factored this method out of delete to simplify the design of the delete
     * operation.
     *
     * @param elem
     */
    private void deleteAction(Object elem) {
        getCommonBehavior().deleteAction(elem);
        if (ModelFacade.isAActionSequence(elem)) {
            getCommonBehavior().deleteActionSequence(elem);
        } else if (elem instanceof MCreateAction) {
            getCommonBehavior().deleteCreateAction(elem);
        } else if (elem instanceof MCallAction) {
            getCommonBehavior().deleteCallAction(elem);
        } else if (elem instanceof MReturnAction) {
            getCommonBehavior().deleteReturnAction(elem);
        } else if (elem instanceof MSendAction) {
            getCommonBehavior().deleteSendAction(elem);
        } else if (elem instanceof MTerminateAction) {
            getCommonBehavior().deleteTerminateAction(elem);
        } else if (elem instanceof MUninterpretedAction) {
            getCommonBehavior().deleteUninterpretedAction(elem);
        } else if (elem instanceof MDestroyAction) {
            getCommonBehavior().deleteDestroyAction(elem);
        }
    }

    /**
     * Factored this method out of delete to simplify the design of the delete
     * operation.
     *
     * @param elem
     */
    private void deleteInstance(MInstance elem) {
        getCommonBehavior().deleteInstance(elem);
        if (elem instanceof MDataValue) {
            getCommonBehavior().deleteDataValue(elem);
        } else if (elem instanceof MComponentInstance) {
            getCommonBehavior().deleteComponentInstance(elem);
        } else if (elem instanceof MNodeInstance) {
            getCommonBehavior().deleteNodeInstance(elem);
        } else if (elem instanceof MObject) {
            getCommonBehavior().deleteObject(elem);
            if (elem instanceof MLinkObject) {
                getCommonBehavior().deleteLinkObject(elem);
            }
        }
        if (elem instanceof MUseCaseInstance) {
            getUseCases().deleteUseCaseInstance(elem);
        }
    }

    /**
     * Factored this method out of delete to simplify the design of the delete
     * operation.
     *
     * @param elem
     */
    private void deleteStateVertex(MStateVertex elem) {
        getStateMachines().deleteStateVertex(elem);
        if (elem instanceof MPseudostate) {
            getStateMachines().deletePseudostate(elem);
        } else if (elem instanceof MSynchState) {
            getStateMachines().deleteSynchState(elem);
        } else if (elem instanceof MStubState) {
            getStateMachines().deleteStubState(elem);
        } else if (elem instanceof MState) {
            getStateMachines().deleteState(elem);
            if (elem instanceof MCompositeState) {
                getStateMachines().deleteCompositeState(elem);
                if (elem instanceof MSubmachineState) {
                    getStateMachines().deleteSubmachineState(elem);
                    if (elem instanceof MSubactivityState) {
                        getActivityGraphs().deleteSubactivityState(elem);
                    }
                }
            } else if (elem instanceof MSimpleState) {
                getStateMachines().deleteSimpleState(elem);
                if (ModelFacade.isAActionState(elem)) {
                    getActivityGraphs().deleteActionState(elem);
                    if (elem instanceof MCallState) {
                        getActivityGraphs().deleteCallState(elem);
                    }
                } else if (elem instanceof MObjectFlowState) {
                    getActivityGraphs().deleteObjectFlowState(elem);
                }
            } else if (elem instanceof MFinalState) {
                getStateMachines().deleteFinalState(elem);
            }
        }
    }

    class ObjectCreateInfo {

        private Object factory;

        private String createMethod;

        private Class javaClass;

//        ObjectCreateInfo (Class javaClass, Object fact, String meth) {
//            this(javaClass, fact, meth, fact, meth);
//        }

        ObjectCreateInfo (Class cls, Object fact, String meth) {
            javaClass = cls;
            factory = fact;
            createMethod = meth;
        }
        /**
         * @return
         */
        public Class getJavaClass() {
            return javaClass;
        }

        /**
         * @return
         */
        public String getCreateMethod() {
            return createMethod;
        }

        /**
         * @return
         */
        public Object getFactory() {
            return factory;
        }

    }

    /**
     * Create an empty but initialized instance of a UML ActionExpression.
     * NSUML does not have a factory method for this.
     *
     * @return an initialized UML ActionExpression instance.
     */
    public Object createActionExpression() {
        MActionExpression expression = new MActionExpression("", "");
        initialize(expression);
        return expression;
    }
}

