// $Id: ActionImportXMI.java 12546 2007-05-05 16:54:40Z linus $
// Copyright (c) 2005-2007 The Regents of the University of California. All
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

package org.argouml.ui;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.PersistenceManager;
import org.argouml.uml.ui.ProjectFileView;

/**
 * This Action allows import of a XMI file.
 *
 * @author mvw@tigris.org
  */
public class ActionImportXMI extends AbstractAction {

    /**
     * The constructor.
     */
    public ActionImportXMI() {
        super(Translator.localize("action.import-xmi"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // Most of this code originates from ActionOpenProject.
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();
        PersistenceManager pm = PersistenceManager.getInstance();

        if (!ProjectBrowser.getInstance().askConfirmationAndSave()) {
            return;
        }

        JFileChooser chooser = null;
        if (p != null && p.getURI() != null) {
            File file = new File(p.getURI());
            if (file.getParentFile() != null) {
                chooser = new JFileChooser(file.getParent());
            }
        } else {
            chooser = new JFileChooser();
        }

        if (chooser == null) {
            chooser = new JFileChooser();
        }

        chooser.setDialogTitle(
                Translator.localize("filechooser.import-xmi"));

        chooser.setFileView(ProjectFileView.getInstance());

        chooser.setAcceptAllFileFilterUsed(true);

        pm.setXmiFileChooserFilter(chooser);

        String fn =
            Configuration.getString(
                PersistenceManager.KEY_IMPORT_XMI_PATH);
        if (fn.length() > 0) {
            chooser.setSelectedFile(new File(fn));
        }

        int retval = chooser.showOpenDialog(pb);
        if (retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();

            if (!theFile.canRead()) {
                /* Try adding the extension from the chosen filter. */
                FileFilter ffilter = chooser.getFileFilter();
                if (ffilter instanceof AbstractFilePersister) {
                    AbstractFilePersister afp =
                        (AbstractFilePersister) ffilter;
                    File m =
                        new File(theFile.getPath() + "."
                                + afp.getExtension());
                    if (m.canRead()) {
                        theFile = m;
                    }
                }
            }
            Configuration.setString(
                    PersistenceManager.KEY_IMPORT_XMI_PATH,
                    theFile.getPath());

            ProjectBrowser.getInstance().loadProjectWithProgressMonitor(
                    theFile, true);
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8756142027376622496L;
}
