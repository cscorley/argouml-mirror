// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// File: Init.java
// Classes: Init
// Original Author: jrobbins@ics.uci.edu
// $Id$

// 8 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). CrCrossNamespaceAssoc
// registration corrected to Association, rather than AssociationClass

// 12 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). CrDupRoleNames
// registration put back in (was commented out)


package org.argouml.uml.cognitive.critics;

import org.argouml.cognitive.critics.Agency;
import org.argouml.cognitive.critics.CompoundCritic;
import org.argouml.cognitive.critics.CrNodesOverlap;
import org.argouml.cognitive.critics.CrZeroLengthEdge;
import org.argouml.cognitive.critics.Critic;
import org.argouml.language.java.cognitive.critics.CrMultipleInheritance;
import org.argouml.language.java.cognitive.critics.CrMultipleRealization;
import org.argouml.pattern.cognitive.critics.CrConsiderSingleton;
import org.argouml.pattern.cognitive.critics.CrSingletonViolatedMissingStaticAttr;
import org.argouml.pattern.cognitive.critics.CrSingletonViolatedOnlyPrivateConstructors;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.FigEdgeModelElement;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;

import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationClass;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.model_management.MPackage;
import ru.novosoft.uml.model_management.MModel;

/** Registers critics for use in Argo/UML.  This class is called at
 *  system startup time. If you add a new critic, you need to add a
 *  line here.
 *
 * @see org.argouml.cognitive.critics.Agency */
  
public class Init {
    // domain independent
    //public static Critic crTooManyDisabled = new CrTooManyDisabled();
    //public static Critic crTooMuchFeedback = new CrTooMuchFeedback();

    // UML specific
    public static Critic crAssocNameConflict = new CrAssocNameConflict();
    public static Critic crAttrNameConflict = new CrAttrNameConflict();
    public static Critic crOperNameConflict = new CrOperNameConflict();
    public static Critic crCircularAssocClass = new CrCircularAssocClass();
    public static Critic crCircularInheritance = new CrCircularInheritance();
    public static Critic crCircularComposition = new CrCircularComposition();
    public static Critic crCrossNamespaceAssoc = new CrCrossNamespaceAssoc();
    public static Critic crDupParamName = new CrDupParamName();
    public static Critic crDupRoleNames = new CrDupRoleNames();
    public static Critic crFinalSubclassed = new CrFinalSubclassed();
    public static Critic crIllegalGeneralization = new CrIllegalGeneralization();
    public static Critic crAlreadyRealizes = new CrAlreadyRealizes();
    public static Critic crInterfaceAllPublic = new CrInterfaceAllPublic();
    public static Critic crInterfaceOperOnly = new CrInterfaceOperOnly();
    //public static Critic crMultiComposite = new CrMultiComposite();
    public static Critic crMultipleAgg = new CrMultipleAgg();
    public static Critic crNWayAgg = new CrNWayAgg();
    public static Critic crNavFromInterface = new CrNavFromInterface();
    public static Critic crUnnavigableAssoc = new CrUnnavigableAssoc();
    //public static Critic crNameConflict = new CrNameConflict();
    public static Critic crNameConflictAC = new CrNameConflictAC();
    public static Critic crMissingClassName = new CrMissingClassName();
    public static Critic crMissingAttrName = new CrMissingAttrName();
    public static Critic crMissingOperName = new CrMissingOperName();
    public static Critic crMissingStateName = new CrMissingStateName();
    public static Critic crNoInstanceVariables = new CrNoInstanceVariables();
    public static Critic crNoAssociations = new CrNoAssociations();
    public static Critic crNonAggDataType = new CrNonAggDataType();
    public static Critic crOppEndConflict = new CrOppEndConflict();
    public static Critic crUselessAbstract = new CrUselessAbstract();
    public static Critic crUselessInterface = new CrUselessInterface();
    public static Critic crDisambigClassName = new CrDisambigClassName();
    public static Critic crDisambigStateName = new CrDisambigStateName();
    public static Critic crConflictingComposites = new CrConflictingComposites();

    public static Critic crTooManyAssoc = new CrTooManyAssoc();
    public static Critic crTooManyAttr = new CrTooManyAttr();
    public static Critic crTooManyOper = new CrTooManyOper();
    public static Critic crTooManyStates = new CrTooManyStates();
    public static Critic crTooManyTransitions = new CrTooManyTransitions();
    public static Critic crTooManyClasses = new CrTooManyClasses();

    public static Critic crNoTransitions = new CrNoTransitions();
    public static Critic crNoIncomingTransitions = new CrNoIncomingTransitions();
    public static Critic crNoOutgoingTransitions = new CrNoOutgoingTransitions();
    public static Critic crMultipleInitialStates = new CrMultipleInitialStates();
    public static Critic crNoInitialState = new CrNoInitialState();
    public static Critic crNoTriggerOrGuard = new CrNoTriggerOrGuard();
    public static Critic crNoGuard = new CrNoGuard();
    public static Critic crInvalidFork = new CrInvalidFork();
    public static Critic crInvalidJoin = new CrInvalidJoin();
    public static Critic crInvalidBranch = new CrInvalidBranch();

    public static Critic crEmptyPackage = new CrEmptyPackage();
    public static Critic crNoOperations = new CrNoOperations();
    public static Critic crConstructorNeeded = new CrConstructorNeeded();

    public static Critic crNameConfusion = new CrNameConfusion();
    public static Critic crMergeClasses = new CrMergeClasses();
    public static Critic crSubclassReference = new CrSubclassReference();

    public static Critic crComponentWithoutNode = new CrComponentWithoutNode();
    public static Critic crCompInstanceWithoutNode = new CrCompInstanceWithoutNode();
    public static Critic crClassWithoutComponent = new CrClassWithoutComponent();
    public static Critic crInterfaceWithoutComponent = new CrInterfaceWithoutComponent();
    public static Critic crObjectWithoutComponent = new CrObjectWithoutComponent();
    public static Critic crNodeInsideElement = new CrNodeInsideElement();
    public static Critic crNodeInstanceInsideElement = new CrNodeInstanceInsideElement();
    public static Critic crWrongLinkEnds = new CrWrongLinkEnds();
    public static Critic crInstanceWithoutClassifier = new CrInstanceWithoutClassifier();

    public static Critic crCallWithoutReturn = new CrCallWithoutReturn();
    public static Critic crReturnWithoutCall = new CrReturnWithoutCall();
    public static Critic crLinkWithoutStimulus = new CrLinkWithoutStimulus();
    public static Critic crSeqInstanceWithoutClassifier = new CrSeqInstanceWithoutClassifier();
    public static Critic crStimulusWithWrongPosition = new CrStimulusWithWrongPosition();

    // from UML 1.1 Semantics spec

    // common coding conventions
    public static Critic
	crUnconventionalOperName = new CrUnconventionalOperName();

    public static Critic
	crUnconventionalAttrName = new CrUnconventionalAttrName(); 

    public static Critic
	crUnconventionalClassName = new CrUnconventionalClassName(); 

    public static Critic
	crUnconventionalPackName = new CrUnconventionalPackName(); 

    // Java specific
    public static Critic crClassMustBeAbstract = new CrClassMustBeAbstract();
    public static Critic crReservedName = new CrReservedName();
    public static Critic crMultiInherit = new CrMultipleInheritance();
    public static Critic crMultiRealization = new CrMultipleRealization();
    // code generation
    public static Critic crIllegalName = new CrIllegalName();

    // Pattern specific
    public static Critic crConsiderSingleton = new CrConsiderSingleton();
    public static Critic crSingletonViolatedMSA =
	new CrSingletonViolatedMissingStaticAttr();
    public static Critic crSingletonViolatedOPC =
	new CrSingletonViolatedOnlyPrivateConstructors();

    // Presentation critics
    public static Critic crNodesOverlap = new CrNodesOverlap();
    public static Critic crZeroLengthEdge = new CrZeroLengthEdge();


    // Compound critics
    public static CompoundCritic clsNaming = new CompoundCritic(crMissingClassName,
								crDisambigClassName);
    public static CompoundCritic noTrans1 = new CompoundCritic(crNoTransitions,
							       crNoIncomingTransitions);
    public static CompoundCritic noTrans2 = new CompoundCritic(crNoTransitions,
							       crNoOutgoingTransitions);

    // TODO: under testing - mkl
    //public static Critic crConsiderFacade = new CrConsiderFacade();

    /** static initializer, register all appropriate critics */
    public static void init() {
	//     try {
	java.lang.Class modelCls = MModel.class;
	java.lang.Class packageCls = MPackage.class;
	java.lang.Class modelElementCls = MModelElement.class;
	java.lang.Class classCls = MClass.class;
	java.lang.Class classifierCls = MClassifier.class;
	java.lang.Class interfaceCls = MInterface.class;
	java.lang.Class attrCls = MAttribute.class;
	java.lang.Class paramCls = MParameter.class;
	java.lang.Class operCls = MOperation.class;
	java.lang.Class iassocCls = MAssociation.class;
	java.lang.Class assocCls = MAssociation.class;
	java.lang.Class assocEndCls = MAssociationEnd.class;
	java.lang.Class assocClassCls = MAssociationClass.class;
	java.lang.Class namespaceCls = MNamespace.class;
	java.lang.Class genElementCls = MGeneralizableElement.class;
	java.lang.Class genCls = MGeneralization.class;
	java.lang.Class datatypeCls = MDataType.class;

	java.lang.Class useCaseCls = MUseCase.class;
	java.lang.Class actorCls = MActor.class;

	java.lang.Class stateVertexCls = MStateVertex.class;
	java.lang.Class stateCls = MState.class;
	java.lang.Class compositieStateCls = MCompositeState.class;
	java.lang.Class pseudostateCls = MPseudostate.class;
	java.lang.Class transitionCls = MTransition.class;
	//java.lang.Class stateMachineCls = MStateMachine.class;

	java.lang.Class diagramCls        = UMLDiagram.class;
	java.lang.Class classDiagramCls   = UMLClassDiagram.class;
	java.lang.Class stateDiagramCls   = UMLStateDiagram.class;
	java.lang.Class useCaseDiagramCls = UMLUseCaseDiagram.class;
	java.lang.Class deploymentDiagramCls = UMLDeploymentDiagram.class;
	java.lang.Class sequenceDiagramCls = UMLSequenceDiagram.class;
	java.lang.Class nodeCls           = FigNodeModelElement.class;
	java.lang.Class edgeCls           = FigEdgeModelElement.class;


	// TODO: Agency should allow registration by interface
	// useful for MAssociation.

	Agency.register(crAssocNameConflict, namespaceCls);
	Agency.register(crAttrNameConflict, classifierCls);
	Agency.register(crOperNameConflict, classifierCls);
	Agency.register(crCircularAssocClass, assocClassCls);
	Agency.register(crCircularInheritance, genElementCls);
	Agency.register(crCircularComposition, classCls);
	Agency.register(crClassMustBeAbstract, classCls); 
	Agency.register(crCrossNamespaceAssoc, assocCls); // Jeremy Bennett fix
	Agency.register(crDupParamName, operCls);
	Agency.register(crDupRoleNames, assocCls);  // Jeremy Bennett fix
	Agency.register(crFinalSubclassed, classCls);
	Agency.register(crFinalSubclassed, interfaceCls);
	Agency.register(crIllegalGeneralization, genCls);
	Agency.register(crAlreadyRealizes, classCls);
	Agency.register(crInterfaceAllPublic, interfaceCls);
	Agency.register(crInterfaceOperOnly, interfaceCls);
	//Agency.register(crMultiComposite, assocEndCls);
	Agency.register(crMultipleAgg, assocCls);
	Agency.register(crUnnavigableAssoc, assocCls);
	Agency.register(crNWayAgg, assocCls);
	Agency.register(crNavFromInterface, assocCls);
	//Agency.register(crNameConflict, namespaceCls);
	Agency.register(crNameConflictAC, assocClassCls);

	// Agency.register(crMissingClassName, classCls);
	// Agency.register(crMissingClassName, actorCls);
	// Agency.register(crMissingClassName, useCaseCls);
	Agency.register(clsNaming, classCls);
	Agency.register(clsNaming, actorCls);
	Agency.register(clsNaming, useCaseCls);

	// TODO: should be just CrMissingName with a
	// customized description
	Agency.register(crMissingClassName, modelCls);
	Agency.register(crMissingAttrName, attrCls);
	Agency.register(crMissingOperName, operCls);
	Agency.register(crMissingStateName, stateCls);

	Agency.register(crNoInstanceVariables, classCls);
	Agency.register(crNoAssociations, classCls);
	Agency.register(crNoAssociations, actorCls);
	Agency.register(crNoAssociations, useCaseCls);
	Agency.register(crNoOperations, classCls);
	Agency.register(crConstructorNeeded, classCls);
	Agency.register(crEmptyPackage, packageCls);
	Agency.register(crNonAggDataType, datatypeCls);
	//      Agency.register(crOppEndConflict, classifierCls);
	Agency.register(crUselessAbstract, classCls);
	Agency.register(crUselessInterface, interfaceCls);
	Agency.register(crDisambigStateName, stateCls);
	Agency.register(crNameConfusion, classifierCls);
	Agency.register(crNameConfusion, stateCls);
	Agency.register(crMergeClasses, classCls);
	Agency.register(crSubclassReference, classCls);
	Agency.register(crIllegalName, classCls);
	Agency.register(crIllegalName, interfaceCls);
	Agency.register(crIllegalName, assocCls);
	Agency.register(crIllegalName, operCls);
	Agency.register(crIllegalName, attrCls);
	Agency.register(crIllegalName, paramCls);
	Agency.register(crIllegalName, stateCls);
	Agency.register(crReservedName, classifierCls);
	Agency.register(crReservedName, operCls);
	Agency.register(crReservedName, attrCls);
	Agency.register(crReservedName, stateCls);
	Agency.register(crReservedName, assocCls);
	Agency.register(crMultiInherit, classCls);
	Agency.register(crMultiRealization, interfaceCls);
	// Agency.register(crConflictingComposites, classifierCls);
	Agency.register(crTooManyAssoc, classCls);
	Agency.register(crTooManyAttr, classCls);
	Agency.register(crTooManyOper, classCls);
	Agency.register(crTooManyTransitions, stateVertexCls);
	Agency.register(crTooManyStates, compositieStateCls);
	Agency.register(crTooManyClasses, classDiagramCls);

	// Agency.register(crNoTransitions, stateVertexCls);
	// Agency.register(crNoIncomingTransitions, stateVertexCls);
	// Agency.register(crNoOutgoingTransitions, stateVertexCls);
	Agency.register(noTrans1, stateVertexCls);
	Agency.register(noTrans2, stateVertexCls);
	Agency.register(crMultipleInitialStates, pseudostateCls);
	Agency.register(crNoInitialState, compositieStateCls);
	Agency.register(crNoTriggerOrGuard, transitionCls);
	Agency.register(crInvalidJoin, pseudostateCls);
	Agency.register(crInvalidFork, pseudostateCls);
	Agency.register(crInvalidBranch, pseudostateCls);
	Agency.register(crNoGuard, transitionCls);

	Agency.register(crUnconventionalOperName, operCls);
	Agency.register(crUnconventionalAttrName, attrCls);
	Agency.register(crUnconventionalClassName, classCls);
	Agency.register(crUnconventionalPackName, packageCls);

	Agency.register(crConsiderSingleton, classCls);
	Agency.register(crSingletonViolatedMSA, classCls);
	Agency.register(crSingletonViolatedOPC, classCls);

	Agency.register(crNodeInsideElement, deploymentDiagramCls);
	Agency.register(crNodeInstanceInsideElement, deploymentDiagramCls);
	Agency.register(crComponentWithoutNode, deploymentDiagramCls);
	Agency.register(crCompInstanceWithoutNode, deploymentDiagramCls);
	Agency.register(crClassWithoutComponent, deploymentDiagramCls);
	Agency.register(crInterfaceWithoutComponent, deploymentDiagramCls);
	Agency.register(crObjectWithoutComponent, deploymentDiagramCls);
	Agency.register(crWrongLinkEnds, deploymentDiagramCls);
	Agency.register(crInstanceWithoutClassifier, deploymentDiagramCls);

	Agency.register(crCallWithoutReturn, sequenceDiagramCls);
	Agency.register(crReturnWithoutCall, sequenceDiagramCls);
	Agency.register(crLinkWithoutStimulus, sequenceDiagramCls);
	Agency.register(crSeqInstanceWithoutClassifier, sequenceDiagramCls);
	Agency.register(crStimulusWithWrongPosition, sequenceDiagramCls);

	Agency.register(crNodesOverlap, diagramCls);
	Agency.register(crZeroLengthEdge, edgeCls);
      
	Agency.register(crOppEndConflict, assocEndCls);
	Agency.register(new CrMultiComposite(), assocEndCls);
	Agency.register(new CrNameConflict(), namespaceCls);
	Agency.register(crAlreadyRealizes, classCls);
	Agency.register(new CrUtilityViolated(), classifierCls);
      
	Agency.register(new CrOppEndVsAttr(), classifierCls);

	// under testing
	// Agency.register(crConsiderFacade, packageCls);
    }

      
} /* end class Init */
