// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.application.helpers;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.model.DataTypesHelper;
import org.argouml.model.Model;
import org.argouml.uml.util.namespace.StringNamespace;
import org.tigris.gef.util.ResourceLoader;

/**
 * Wrapper around org.tigris.gef.util.ResourceLoader.<p>
 *
 * Necessary since ArgoUML needs some extra init.
 *
 * @since Nov 24, 2002
 * @author jaap.branderhorst@xs4all.nl @stereotype singleton
 */
public final class ResourceLoaderWrapper {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ResourceLoaderWrapper.class);

    private static ImageIcon initialStateIcon;
    private static ImageIcon deepIcon;
    private static ImageIcon shallowIcon;
    private static ImageIcon forkIcon;
    private static ImageIcon joinIcon;
    private static ImageIcon branchIcon;
    private static ImageIcon junctionIcon;
    private static ImageIcon realizeIcon;
    private static ImageIcon signalIcon;
    private static ImageIcon commentIcon;

    private Hashtable iconCache = new Hashtable();

    /**
     * Singleton implementation.
     */
    private static ResourceLoaderWrapper instance = new ResourceLoaderWrapper();


    /**
     * Returns the singleton instance.
     *
     * @return ResourceLoaderWrapper
     */
    public static ResourceLoaderWrapper getInstance() {
        return instance;
    }

    /**
     * Constructor for ResourceLoaderWrapper.
     */
    private ResourceLoaderWrapper() {
        initResourceLoader();
    }

    /**
     * Calculate the path to a look and feel object.
     *
     * @param classname
     *            The look and feel classname
     * @param element
     *            The en part of the path.
     * @return the complete path.
     */
    private static String lookAndFeelPath(String classname, String element) {
        return "/org/argouml/Images/plaf/"
            + classname.replace('.', '/')
            + "/toolbarButtonGraphics/"
            + element;
    }

    /**
     * Initializes the resourceloader.
     *
     * LookupIconResource checks if there are locations and extensions known.
     * If there are none, this method is called to initialize the resource
     * loader. Originally, this method was placed within Main but this coupled
     * Main and the resourceLoader too much.
     */
    private static void initResourceLoader() {
	String lookAndFeelClassName;
	if ("true".equals(System.getProperty("force.nativelaf", "false"))) {
	    lookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
	} else {
	    lookAndFeelClassName = "javax.swing.plaf.metal.MetalLookAndFeel";
	}
	String lookAndFeelGeneralImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "general");
	String lookAndFeelNavigationImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "navigation");
	String lookAndFeelDiagramImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "argouml/diagrams");
	String lookAndFeelElementImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "argouml/elements");
	String lookAndFeelArgoUmlImagePath =
	    lookAndFeelPath(lookAndFeelClassName, "argouml");
	ResourceLoader.addResourceExtension("gif");
	ResourceLoader.addResourceLocation(lookAndFeelGeneralImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelNavigationImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelDiagramImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelElementImagePath);
	ResourceLoader.addResourceLocation(lookAndFeelArgoUmlImagePath);
	ResourceLoader.addResourceLocation("/org/argouml/Images");
	ResourceLoader.addResourceLocation("/org/tigris/gef/Images");

        initialStateIcon = ResourceLoader.lookupIconResource("Initial");
        deepIcon = ResourceLoader.lookupIconResource("DeepHistory");
        shallowIcon = ResourceLoader.lookupIconResource("ShallowHistory");
        forkIcon = ResourceLoader.lookupIconResource("Fork");
        joinIcon = ResourceLoader.lookupIconResource("Join");
        branchIcon = ResourceLoader.lookupIconResource("Choice");
        junctionIcon = ResourceLoader.lookupIconResource("Junction");
        realizeIcon = ResourceLoader.lookupIconResource("Realization");
        signalIcon = ResourceLoader.lookupIconResource("SignalSending");
        commentIcon = ResourceLoader.lookupIconResource("Note");
    }

    /**
     * @see ResourceLoader#lookupIconResource(String)
     */
    public static ImageIcon lookupIconResource(String resource) {
	return ResourceLoader.lookupIconResource(resource);
    }

    /**
     * @see ResourceLoader#lookupIconResource(String, String)
     */
    public static ImageIcon lookupIconResource(String resource, String desc) {
	return ResourceLoader.lookupIconResource(resource, desc);
    }

    /**
     * Look up the Icon for a key.
     *
     * @param key The key to find.
     * @return The found Icon.
     */
    public static Icon lookupIcon(String key) {
        return lookupIconResource(getImageBinding(key),
                		  Translator.localize(key));
    }

    /**
     * Find the Icon for a given model element.
     *
     * @return The Icon.
     * @param value The model element.
     *
     * TODO: This should not use string matching on classnames to do this
     *       since this means that we have knowledge about how the model
     *       elements are implemented outside of the Model component.
     */
    public Icon lookupIcon(Object value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Attempted to get an icon given a null key");
        }

	Icon icon = (Icon) iconCache.get(value.getClass());

        if (Model.getFacade().isAPseudostate(value)) {

            Object kind = Model.getFacade().getKind(value);
            DataTypesHelper helper = Model.getDataTypesHelper();
            if (helper.equalsINITIALKind(kind)) {
                icon = initialStateIcon;
            }
            if (helper.equalsDeepHistoryKind(kind)) {
                icon = deepIcon;
            }
            if (helper.equalsShallowHistoryKind(kind)) {
                icon = shallowIcon;
            }
            if (helper.equalsFORKKind(kind)) {
                icon = forkIcon;
            }
            if (helper.equalsJOINKind(kind)) {
                icon = joinIcon;
            }
            if (helper.equalsBRANCHKind(kind)) {
                icon = branchIcon;
            }
            if (helper.equalsJUNCTIONKind(kind)) {
                icon = junctionIcon;
            }
            // if (MPseudostateKind.FINAL.equals(kind))
            // icon = _FinalStateIcon;
        }

        if (Model.getFacade().isAAbstraction(value)) {
            icon = realizeIcon;
        }
        // needs more work: sending and receiving icons
        if (Model.getFacade().isASignal(value)) {
            icon = signalIcon;
        }

        if (Model.getFacade().isAComment(value)) {
            icon = commentIcon;
        }

        if (icon == null) {

            StringNamespace sns =
                (StringNamespace) StringNamespace.parse(value.getClass());
            StringNamespace org =
                new StringNamespace(new String[] {"org"});
            StringNamespace ru = new StringNamespace(new String[] {"ru"});

            String cName = sns.popNamespaceElement().toString();
            if (ru.equals(sns.getCommonNamespace(ru))
                    || org.equals(sns.getCommonNamespace(org))) {

                if (cName.startsWith("UML")) {
                    cName = cName.substring(3);
                }
                if (cName.startsWith("M")) {
                    cName = cName.substring(1);
                }
                if (cName.endsWith("Impl")) {
                    cName = cName.substring(0, cName.length() - 4);
                }
            }
            icon = lookupIconResource(cName);
            if (icon == null) {
                LOG.warn("Can't find icon for " + cName);
            } else {
                synchronized (iconCache) {
                    iconCache.put(value.getClass(), icon);
                }
            }

        }
	return icon;
    }

    /**
     * Map to convert tokens into file names.
     */
    private static Map images = new HashMap();
    static {
        images.put("action.about-argouml", "AboutArgoUML");
        images.put("action.activity-diagram", "Activity Diagram");
        images.put("action.class-diagram", "Class Diagram");
        images.put("action.collaboration-diagram", "Collaboration Diagram");
        images.put("action.deployment-diagram", "Deployment Diagram");
        images.put("action.sequence-diagram", "Sequence Diagram");
        images.put("action.state-diagram", "State Diagram");
        images.put("action.usecase-diagram", "Use Case Diagram");
    }

    static {
        images.put("action.add-concurrent-region", "Add Concurrent Region");
        images.put("action.add-message", "Add Message");
        images.put("action.configure-perspectives", "ConfigurePerspectives");
        images.put("action.copy", "Copy");
        images.put("action.cut", "Cut");
        images.put("action.delete-concurrent-region", "DeleteConcurrentRegion");
        images.put("action.delete-from-model", "DeleteFromModel");
        images.put("action.find", "Find...");
        images.put("action.import-sources", "Import Sources...");
        images.put("action.more-info", "More Info...");
        images.put("action.navigate-back", "Navigate Back");
        images.put("action.navigate-forward", "Navigate Forward");
        images.put("action.new", "New");
        images.put("action.new-todo-item", "New To Do Item...");
        images.put("action.open-project", "Open Project...");
        images.put("action.page-setup", "Page Setup...");
        images.put("action.paste", "Paste");
        images.put("action.print", "Print...");
        images.put("action.remove-from-diagram", "Remove From Diagram");
        images.put("action.resolve-item", "Resolve Item...");
        images.put("action.save-project", "Save Project");
        images.put("action.save-project-as", "Save Project As...");
        images.put("action.send-email-to-expert", "Send Email To Expert...");
        images.put("action.settings", "Settings...");
        images.put("action.snooze-critic", "Snooze Critic");
        images.put("action.system-information", "System Information");
    }

    static {
        images.put("button.new-attribute", "New Attribute");
        images.put("button.new-extension-point", "New Extension Point");
        images.put("button.new-inner-class", "Inner Class");
        images.put("button.new-operation", "New Operation");
        images.put("button.new-parameter", "New Parameter");
        images.put("button.new-reception", "New Reception");
    }

    /**
     * Convert the key to the image file name.
     *
     * @param name the new i18n key
     * @return the file name (base part only).
     */
    private static String getImageBinding(String name) {
        String found = (String) images.get(name);
        if (found == null) {
            return name;
        }
        return found;
    }
}
