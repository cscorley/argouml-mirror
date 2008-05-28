/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * OCL Compiler                                                      *
 * Copyright (C) 2001 Steffen Zschaler.                              *
 *                                                                   *
 * This work is free software; you can redistribute it and/or        *
 * modify it under the terms of the GNU Library General Public       *
 * License as published by the Free Software Foundation; either      *
 * version 2 of the License, or (at your option) any later version.  *
 *                                                                   *
 * This work is distributed in the hope that it will be useful,      *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU *
 * Library General Public License for more details.                  *
 *                                                                   *
 * You should have received a copy of the GNU Library General Public *
 * License along with this library; if not, write to the             *
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,      *
 * Boston, MA  02111-1307, USA.                                      *
 *                                                                   *
 * See CREDITS file for further details.                             *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

// HISTORY
//
// 03/13/2001  [sz9 ]  Created.

package tudresden.ocl20.gui;

import javax.swing.*;

/** 
 * OCL editor preference dialog.
 *
 * @author  sz9
 */
public class OCLEditorPreferences extends javax.swing.JDialog {

  /**
	 * 
	 */
	private static final long serialVersionUID = 8238830991495314346L;
	private OCLEditor m_ocleEditor;
  
  /** Creates new form OCLEditorPreferences */
  public OCLEditorPreferences (JFrame parent, OCLEditor ocle) {
    super (parent, true);
    m_ocleEditor = ocle;
    
    initComponents();
    
    int nOptionMask = m_ocleEditor.getOptionMask();
    
    m_jcbTypeCheck.setVisible (
        (nOptionMask & OCLEditor.OPTIONMASK_TYPECHECK) != 0);
    m_jcbTypeCheck.setSelected (m_ocleEditor.getDoTypeCheck());
    
    m_jcbAutoSplit.setVisible (
        (nOptionMask & OCLEditor.OPTIONMASK_AUTOSPLIT) != 0);
    m_jcbAutoSplit.setSelected (m_ocleEditor.getDoAutoSplit());
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the FormEditor.
   */
  private void initComponents () {//GEN-BEGIN:initComponents
    m_jpOptionsGroup = new javax.swing.JPanel ();
    m_jcbTypeCheck = new javax.swing.JCheckBox ();
    m_jcbAutoSplit = new javax.swing.JCheckBox ();
    jPanel3 = new javax.swing.JPanel ();
    m_jpButtonsGroup = new javax.swing.JPanel ();
    m_jbOKButton = new javax.swing.JButton ();
    m_jbCancelButton = new javax.swing.JButton ();
    getContentPane ().setLayout (new java.awt.GridBagLayout ());
    java.awt.GridBagConstraints gridBagConstraints1;
    setTitle ("OCL Editor Preferences");
    addWindowListener (new java.awt.event.WindowAdapter () {
      public void windowClosing (java.awt.event.WindowEvent evt) {
        closeDialog (evt);
      }
    }
    );

    m_jpOptionsGroup.setLayout (new java.awt.GridBagLayout ());
    java.awt.GridBagConstraints gridBagConstraints2;
    m_jpOptionsGroup.setBorder (new javax.swing.border.TitledBorder(
    new javax.swing.border.EtchedBorder(), "Options"));

      m_jcbTypeCheck.setToolTipText ("Check this option to have the editor perform type checks on each constraint you submit.");
      m_jcbTypeCheck.setSelected (true);
//      m_jcbTypeCheck.setText ("Check type conformance of OCL constraints");
      m_jcbTypeCheck.setText ("Check syntax and type conformance of OCL constraints");
  
      gridBagConstraints2 = new java.awt.GridBagConstraints ();
      gridBagConstraints2.gridwidth = 0;
      gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints2.insets = new java.awt.Insets (5, 5, 0, 5);
      gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints2.weightx = 1.0;
      m_jpOptionsGroup.add (m_jcbTypeCheck, gridBagConstraints2);
  
      m_jcbAutoSplit.setToolTipText ("Check this option to have the editor automatically split multiple constraints that were entered in one go.");
      m_jcbAutoSplit.setSelected (true);
      m_jcbAutoSplit.setText ("AutoSplit: Automatically split apart multiple constraints entered as a single text");
  
      gridBagConstraints2 = new java.awt.GridBagConstraints ();
      gridBagConstraints2.gridwidth = 0;
      gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints2.insets = new java.awt.Insets (0, 5, 0, 5);
      gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints2.weightx = 1.0;
      m_jpOptionsGroup.add (m_jcbAutoSplit, gridBagConstraints2);
  
  
      gridBagConstraints2 = new java.awt.GridBagConstraints ();
      gridBagConstraints2.gridwidth = 0;
      gridBagConstraints2.gridheight = 0;
      gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
      gridBagConstraints2.insets = new java.awt.Insets (0, 5, 0, 5);
      gridBagConstraints2.anchor = java.awt.GridBagConstraints.NORTHWEST;
      gridBagConstraints2.weightx = 1.0;
      gridBagConstraints2.weighty = 1.0;
      m_jpOptionsGroup.add (jPanel3, gridBagConstraints2);
  

    gridBagConstraints1 = new java.awt.GridBagConstraints ();
    gridBagConstraints1.gridwidth = 0;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints1.insets = new java.awt.Insets (5, 5, 5, 5);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints1.weightx = 1.0;
    gridBagConstraints1.weighty = 1.0;
    getContentPane ().add (m_jpOptionsGroup, gridBagConstraints1);

    m_jpButtonsGroup.setLayout (new java.awt.FlowLayout (2, 5, 5));

      m_jbOKButton.setText ("OK");
      m_jbOKButton.addActionListener (new java.awt.event.ActionListener () {
        public void actionPerformed (java.awt.event.ActionEvent evt) {
          onOKButton (evt);
        }
      }
      );
  
      m_jpButtonsGroup.add (m_jbOKButton);
  
      m_jbCancelButton.setText ("Cancel");
      m_jbCancelButton.addActionListener (new java.awt.event.ActionListener () {
        public void actionPerformed (java.awt.event.ActionEvent evt) {
          onCancelButton (evt);
        }
      }
      );
  
      m_jpButtonsGroup.add (m_jbCancelButton);
  

    gridBagConstraints1 = new java.awt.GridBagConstraints ();
    gridBagConstraints1.gridwidth = 0;
    gridBagConstraints1.gridheight = 0;
    gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints1.insets = new java.awt.Insets (0, 0, 10, 0);
    gridBagConstraints1.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints1.weightx = 1.0;
    getContentPane ().add (m_jpButtonsGroup, gridBagConstraints1);

    pack ();
    java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    java.awt.Dimension dialogSize = getSize();
    setLocation((screenSize.width-dialogSize.width)/2, (screenSize.height-dialogSize.height)/2);
  }//GEN-END:initComponents

  private void onOKButton (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOKButton
    m_ocleEditor.setDoAutoSplit (m_jcbAutoSplit.isSelected());
    m_ocleEditor.setDoTypeCheck (m_jcbTypeCheck.isSelected());
    
    closeDialog();
  }//GEN-LAST:event_onOKButton

  private void onCancelButton (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCancelButton
    closeDialog();
  }//GEN-LAST:event_onCancelButton

  /** Closes the dialog */
  private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
    closeDialog();
  }//GEN-LAST:event_closeDialog

  private void closeDialog() {
    setVisible (false);
    dispose ();
  }
  
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JPanel m_jpOptionsGroup;
  private javax.swing.JCheckBox m_jcbTypeCheck;
  private javax.swing.JCheckBox m_jcbAutoSplit;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel m_jpButtonsGroup;
  private javax.swing.JButton m_jbOKButton;
  private javax.swing.JButton m_jbCancelButton;
  // End of variables declaration//GEN-END:variables

}