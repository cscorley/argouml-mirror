// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

package org.argouml.moduleloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.log4j.Logger;

import org.argouml.application.api.Argo;

/**
 * This is the module loader that loads modules implementing the
 * ModuleInterface.<p>
 *
 * This is a singleton. There are convenience functions that are static
 * to access the module.<p>
 *
 * @stereotype singleton
 * @author Linus Tolke
 * @since 0.15.4
 */
public class ModuleLoader2 {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ModuleLoader2.class);

    /**
     * This map contains the module loader information about the module.<p>
     *
     * The keys is the list of available modules.<p>
     *
     * The keys are {@link org.argouml.moduleloader.ModuleInterface}.
     * The values are {@link org.argouml.moduleloader.ModuleStatus}.
     */
    private Map moduleStatus;

    /**
     * The module loader object.
     */
    private static final ModuleLoader2 INSTANCE = new ModuleLoader2();

    /** Class file suffix */
    public static final String CLASS_SUFFIX = ".class";

    /**
     * Constructor for this object.
     */
    private ModuleLoader2() {
	moduleStatus = new HashMap();
    }

    /**
     * Get hold of the instance of this object.
     *
     * @return the instance
     */
    public static ModuleLoader2 getInstance() {
	return INSTANCE;
    }

    /**
     * Return a collection of all available modules.
     */
    private Collection availableModules() {
	return Collections.unmodifiableCollection(moduleStatus.keySet());
    }

    // Access methods for program infrastructure.
    /**
     * Enables all selected modules and disabling all modules not selected.<p>
     * 
     * In short this attempts to make the modules obey their selection.<p>
     *
     * @param failingAllowed is <tt>true</tt> if enabling or disabling of 
     *                       some of the modules is allowed to fail. 
     */
    public static void doLoad(boolean failingAllowed) {
	getInstance().doInternal(failingAllowed);
    }

    // Access methods for modules that need to query about the status of
    // other modules.
    /**
     * Gets the loaded status for some other module.
     *
     * @return true if the module exists and is enabled.
     * @param name is the module name of the queried module
     */
    public static boolean isEnabled(String name) {
	return getInstance().isEnabledInternal(name);
    }

    // Access methods for the GUI that the user uses to enable and disable 
    // modules.

    /**
     * Get a Collection with all the names.
     *
     * @return all the names.
     */
    public static Collection allModules() {
	Collection coll = new HashSet();

	Iterator iter = getInstance().availableModules().iterator();
	while (iter.hasNext()) {
	    ModuleInterface mf = (ModuleInterface) iter.next();
	
	    coll.add(mf.getName());
	}

	return coll;
    }

    /**
     * Get the selected.
     *
     * @param name The name of the module.
     * @return <tt>true</tt> if the module is selected.
     */
    public static boolean isSelected(String name) {
	return getInstance().isSelectedInternal(name);
    }

    /**
     * @see #isSelected(String)
     */
    private boolean isSelectedInternal(String name) {
	Map.Entry entry = findModule(name);

	if (entry != null) {
	    ModuleStatus status = (ModuleStatus) entry.getValue();

	    if (status == null) {
		return false;
	    }

	    return status.isSelected();
	}
	return false;
    }

    /**
     * Set the selected value.
     *
     * @param name The name of the module.
     * @param value Selected or not.
     */
    public static void setSelected(String name, boolean value) {
	getInstance().setSelectedInternal(name, value);
    }

    /**
     * @see #setSelected(String, boolean)
     */
    public void setSelectedInternal(String name, boolean value) {
	Map.Entry entry = findModule(name);

	if (entry != null) {
	    ModuleStatus status = (ModuleStatus) entry.getValue();

	    status.setSelected(value);
	}
    }
    
    /**
     * Create a description of the module based on the information provided
     * by the module itself.
     *
     * @param name The name of the module.
     * @return The description.
     */
    public static String getDescription(String name) {
        return getInstance().getDescriptionInternal(name);
    }

    /**
     * @see #getDescription(String)
     */
    private String getDescriptionInternal(String name) {
	Map.Entry entry = findModule(name);

	if (entry == null) {
	    throw new IllegalArgumentException("Module does not exist.");
	}

	ModuleInterface module = (ModuleInterface) entry.getKey();
        StringBuffer sb = new StringBuffer();
        String desc = module.getInfo(ModuleInterface.DESCRIPTION);
        if (desc != null) {
            sb.append(desc);
            sb.append("\n\n");
        }
        String author = module.getInfo(ModuleInterface.AUTHOR);
        if (author != null) {
            sb.append("Author: ").append(author);
            sb.append("\n");
        }
        String version = module.getInfo(ModuleInterface.VERSION);
        if (version != null) {
            sb.append("Version: ").append(version);
            sb.append("\n");
        }
        return sb.toString();
    }

    // Access methods for the program infrastructure
    /**
     * Enables all selected modules.
     *
     * @param failingAllowed is true if this is not the last attempt at 
     * turning on.
     */
    private void doInternal(boolean failingAllowed) {
	huntForModules();

	boolean someModuleSucceeded;
	do {
	    someModuleSucceeded = false;

	    Iterator iter = availableModules().iterator();
	    while (iter.hasNext()) {
		ModuleInterface module = (ModuleInterface) iter.next();

		ModuleStatus status = (ModuleStatus) moduleStatus.get(module);

		if (status == null) {
		    continue;
		}

		if (!status.isEnabled() && status.isSelected()) {
		    boolean result = false;
		    try {
		        result = module.enable();
		    } catch (Exception e) {
		        // An exception was thrown while enabling.
		    }
		    
		    if (result) {
		        someModuleSucceeded = true;
		        status.setEnabled();
		    }
		} else if (status.isEnabled() && !status.isSelected()) {
		    boolean result = module.disable();

		    if (result) {
		        someModuleSucceeded = true;
		        status.setDisabled();
		    }
		}
	    }
	} while (someModuleSucceeded);

	if (!failingAllowed) {
	    // Notify the user that the modules in the list that are selected
	    // but not enabled were not possible to enable and that are not
	    // selected that we cannot disable.
	    //
	    // Currently we just log this.
	    //
	    // TODO: We could eventually pop up some warning window.
	    //
	    Iterator iter = availableModules().iterator();
	    while (iter.hasNext()) {
		ModuleInterface module = (ModuleInterface) iter.next();

		ModuleStatus status = (ModuleStatus) moduleStatus.get(module);

		if (status == null) {
		    continue;
		}

		if (status.isEnabled() && status.isSelected()) {
		    continue;
		}

		if (!status.isEnabled() && !status.isSelected()) {
		    continue;
		}

		if (status.isSelected()) {
		    LOG.warn("ModuleLoader was not able to enable module "
		             + module.getName());
		} else {
		    LOG.warn("ModuleLoader was not able to disable module "
		             + module.getName());
		}
	    }
	}
    }

    /**
     * Gets the loaded status for some other module.
     *
     * @return true if the module exists and is enabled.
     * @param name is the module name of the queried module
     */
    private boolean isEnabledInternal(String name) {
	Map.Entry entry = findModule(name);

	if (entry != null) {
	    ModuleStatus status = (ModuleStatus) entry.getValue();

	    if (status == null) {
		return false;
	    }

	    return status.isEnabled();
	}
	return false;
    }


    /**
     * Return the ModuleInterface, ModuleStatus pair for the module
     * with the given name or <tt>null</tt> if there isn't any.
     *
     * @param name The given name.
     * @return A pair (Map.Entry).
     */
    private Map.Entry findModule(String name) {
	Iterator iter = moduleStatus.entrySet().iterator();
	while (iter.hasNext()) {
	    Map.Entry entry = (Map.Entry) iter.next();
	    ModuleInterface module = (ModuleInterface) entry.getKey();

	    if (name.equalsIgnoreCase(module.getName())) {
		return entry;
	    }
	}
	return null;
    }

    /**
     * Tries to find as many available modules as possible.<p>
     *
     * As the modules are found they are appended to {@link #moduleStatus}.<p>
     */
    private void huntForModules() {
        // TODO: huntForInternalModules();
	huntForModulesFromExtensionDir();
	// TODO: huntForModulesFromJavaWebStart();
    }

    /**
     * Find and enable modules from our "ext" directory and from the
     * directory specified in "argo.ext.dir".<p>
     *
     * TODO: This does a calculation of where our "ext" directory is.
     *       We should eventually make sure that this calculation is
     *       only present in one place in the code and not several.
     */
    private void huntForModulesFromExtensionDir() {
	// Use a little trick to find out where Argo is being loaded from.
        String extForm = getClass().getResource(Argo.ARGOINI).toExternalForm();
	String argoRoot =
	    extForm.substring(0, 
			      extForm.length() - Argo.ARGOINI.length());

	// If it's a jar, clean it up and make it look like a file url
	if (argoRoot.startsWith("jar:")) {
	    argoRoot = argoRoot.substring(4);
	    if (argoRoot.endsWith("!")) {
	        argoRoot = argoRoot.substring(0, argoRoot.length() - 1);
	    }
	}

	String argoHome = null;

	if (argoRoot != null) {
	    LOG.info("argoRoot is " + argoRoot);
	    if (argoRoot.startsWith("file:")) {
	        argoHome = new File(argoRoot.substring(5)).getAbsoluteFile()
		    .getParent();
	    } else {
	        argoHome = new File(argoRoot).getAbsoluteFile().getParent();
	    }

	    argoHome = java.net.URLDecoder.decode(argoHome);

	    LOG.info("argoHome is " + argoHome);
	}
	if (argoHome != null) {
	    if (argoHome.startsWith("file:")) {
	        huntModulesFromNamedDirectory(argoHome.substring(5) 
					      + File.separator + "ext");
	    } else {
	        huntModulesFromNamedDirectory(argoHome 
					      + File.separator + "ext");
	    }

	}
        String extdir = System.getProperty("argo.ext.dir");
	if (extdir != null) {
	    huntModulesFromNamedDirectory(extdir);
	}
    }

    /**
     * Find and enable a module from a given directory.
     *
     * @param dirname The name of the directory.
     */
    private void huntModulesFromNamedDirectory(String dirname) {
	File extensionDir = new File(dirname);
	if (extensionDir.isDirectory()) {
	    File[] file = extensionDir.listFiles(new JarFileFilter());
	    for (int i = 0; i < file.length; i++) {
		JarFile jarfile = null;
		// Try-catch only the JarFile instantiation so we
		// don't accidentally mask anything in ArgoJarClassLoader
		// or processJarFile.
		try {
		    jarfile = new JarFile(file[i]);
		    if (jarfile != null) {
	                ClassLoader classloader =
			    new URLClassLoader(new URL[] {
				file[i].toURL(),
			    });
	                processJarFile(classloader, file[i]);
		    }
		}
		catch (IOException ioe) {
		    LOG.debug("Cannot open the directory " + dirname, ioe);
		}
	    }
	}
    }

    /**
     * Check the manifest of a jar file for an extension.<p>
     *
     * If there isn't a manifest or it isn't readable, we fail silently.
     *
     * @param classloader The classloader to use.
     * @param file The file to process.
     */
    private void processJarFile(ClassLoader classloader, File file) {
	JarFile jarfile = null;
        Manifest manifest = null;
	LOG.info("Opening jar file " + file);
	try {
	    jarfile = new JarFile(file);
	}
	catch (IOException e) {
	    LOG.debug("Unable to open " + file, e);
	}

	if (jarfile != null) {
	    try {
	        manifest = jarfile.getManifest();
	        if (manifest == null) {
	            LOG.debug(file + " does not have a manifest");
		    return;
	        }
	    }
	    catch (IOException e) {
	        LOG.debug("Unable to read manifest of " + file, e);
		return;
	    }
	}

	Map entries = manifest.getEntries();

	Iterator iMap = entries.keySet().iterator();

	while (iMap.hasNext()) {
	    // Look for our specification
	    String cname = (String) iMap.next();
	    if (cname.endsWith(CLASS_SUFFIX)) {
		int classNamelen = cname.length() - CLASS_SUFFIX.length();
		String className = cname.substring(0, classNamelen);
		className = className.replace('/', '.');
		addClass(classloader, className);
	    }
	}
    }


    /**
     * Try to load a module from the given ClassLoader.<p>
     *
     * Only add it as a module if it is a module (i.e. it implements the
     * {@link ModuleInterface} interface.
     *
     * @param classloader The ClassLoader to load from.
     * @param classname The name.
     */
    private void addClass(ClassLoader classLoader, String classname) {
	try {
	    Class moduleClass = classLoader.loadClass(classname);
	    Constructor c = moduleClass.getDeclaredConstructor(new Class[]{});
	    if (!Modifier.isPublic(c.getModifiers())) {
		return;
	    }
	    Object obj = c.newInstance(new Object[]{});

	    if (!(obj instanceof ModuleInterface)) {
		LOG.debug("The class " + classname + " is not a module.");
		return;
	    }
	    ModuleInterface mf = (ModuleInterface) obj;

	    addModule(mf);
	} catch (Exception e) {
	    LOG.error("Could not find class to allocated as module:", e);
	    e.printStackTrace();
	}
    }

    /**
     * Add a newly found module to {@link #moduleStatus}. If we already
     * know about it, don't add it.
     */
    private void addModule(ModuleInterface mf) {
	// Since there is no way to compare the objects as equal, 
	// we have to search through the list at this point.
	Iterator iter = moduleStatus.keySet().iterator();
	while (iter.hasNext()) {
	    ModuleInterface foundMf = (ModuleInterface) iter.next();

	    if (foundMf.getName().equals(mf.getName())) {
		return;
	    }
	}

	// We havn't found it. Add it.
	ModuleStatus ms = new ModuleStatus();

	// Enable it.
	// TODO: This by default selects all modules that are found.
	//       Eventually we would rather obey a default either from the
	//       modules themselves, from how they are found, and also
	//       have information on what modules are selected from the
	//       configuration.
	ms.setSelected();

	moduleStatus.put(mf, ms);
    }


    class JarFileFilter implements FileFilter {
	public boolean accept(File pathname) {
	    return (pathname.canRead()
		    && pathname.isFile()
		    && pathname.getPath().toLowerCase().endsWith(".jar"));
	}
    }
}


/**
 * Status for each of the available modules. This is created in one copy per
 * available module.
 */
class ModuleStatus {
    private boolean enabled;
    private boolean selected;

    /**
     * Tells if the module is enabled or not.
     *
     * @return true if the module is enabled.
     */
    public boolean isEnabled() {
	return enabled;
    }

    /**
     * Setter for enabled.
     */
    public void setEnabled() {
	enabled = true;
    }

    /**
     * Setter for enabled.
     */
    public void setDisabled() {
	enabled = false;
    }

    /**
     * Tells if the module is selected by the user or not.
     *
     * @return true if it is selected.
     */
    public boolean isSelected() {
	return selected;
    }


    /**
     * Setter for selected.
     */
    public void setSelected() {
	selected = true;
    }

    /**
     * Setter for selected.
     */
    public void setUnselect() {
	selected = false;
    }

    /**
     * Setter for selected.
     */
    public void setSelected(boolean value) {
	if (value) {
	    setSelected();
	} else {
	    setUnselect();
	}
    }
}
