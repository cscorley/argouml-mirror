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

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Category;

import org.argouml.application.api.Argo;
import org.argouml.application.api.QuadrantPanel;
import org.argouml.cognitive.ui.TabToDoTarget;
import org.argouml.swingext.Orientable;
import org.argouml.swingext.Orientation;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.TabModelTarget;
import org.argouml.uml.ui.TabProps;
import org.argouml.util.ConfigLoader;

import org.tigris.gef.presentation.Fig;

/**
 * The lower-right pane of the main Argo/UML window, which shows
 * the details of a selected model element. 
 * 
 * This panel has several tabs that show details of the selected ToDoItem, or the
 * selected model element in the NavigationPane, or the
 * MultiEditorPane.
 *
 * There are requests to have the cursor automatically
 * be set to the primary field.
 *
 * $Id$
 */
public class DetailsPane
    extends JPanel
    implements ChangeListener, MouseListener, QuadrantPanel, Orientable {

    protected static Category cat = Category.getInstance(DetailsPane.class);
    ////////////////////////////////////////////////////////////////
    // constants

    public static int WIDTH = 690;
    public static int HEIGHT = 520;
    public static int INITIAL_WIDTH = 400;
    public static int INITIAL_HEIGHT = 200;

    ////////////////////////////////////////////////////////////////
    // instance variables

    /** The currently selected Fig in the visible diagram.
     */
    protected Fig _figTarget = null;
    
    /** The currently selected object from the UML MModel,
     *  selected from a Fig in the diagram or from the
     *  navigation panel.
     */
    protected Object _modelTarget = null;
    
    /**
     * The currently selected TODO item.
     */
    protected Object _item = null;

    /**
     * The top level pane, which is a tabbed pane.
     */
    protected JTabbedPane _tabs = new JTabbedPane();
    
    /**
     * a list of all the tabs, which are JPanels, in the JTabbedPane _tabs.
     */
    protected Vector _tabPanels = new Vector();
    
    /**
     * index of the selected tab in the JTabbedPane.
     */
    protected int _lastNonNullTab = 0;

    /**
     *
     */
    private Orientation orientation;

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Gets all of the _tabPanels from the ConfigLoader, then 
     * adds them to the JTabbedPane.
     *
     * sets the target to null.
     *
     * registers listeners.
     */
    public DetailsPane(String pane, Orientation orientation) {
        Argo.log.info("making DetailsPane(" + pane + ")");
        this.orientation = orientation;
        ConfigLoader.loadTabs(_tabPanels, pane, orientation);       

        setLayout(new BorderLayout());
        setFont(new Font("Dialog", Font.PLAIN, 10));
        add(_tabs, BorderLayout.CENTER);

        _tabs.addChangeListener(this);
        for (int i = 0; i < _tabPanels.size(); i++) {
            String title = "tab";
            JPanel t = (JPanel)_tabPanels.elementAt(i);
            if (t instanceof TabSpawnable)
                title = ((TabSpawnable)t).getTitle();
            if (t instanceof TabToDoTarget) {
                _tabs.addTab(title, _leftArrowIcon, t);
            } else if (t instanceof TabModelTarget) {
                _tabs.addTab(title, _upArrowIcon, t);
            } else if (t instanceof TabFigTarget) {
                _tabs.addTab(title, _upArrowIcon, t);
            } else {
                _tabs.addTab(title, t);
            }
        } /* end for */

        setTarget(null);
        _item = null;
        _tabs.addMouseListener(this);
        _tabs.addChangeListener(this);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * returns the JTabbedPane that contains all details panels.
     */
    public JTabbedPane getTabs() {
        return _tabs;
    }

    /**
     * selects the TODO tab, and sets the target of that tab.
     */
    public boolean setToDoItem(Object item) {
        _item = item;
        for (int i = 0; i < _tabPanels.size(); i++) {
            JPanel t = (JPanel)_tabPanels.elementAt(i);
            if (t instanceof TabToDoTarget) {
                ((TabToDoTarget)t).setTarget(_item);
                _tabs.setSelectedComponent(t);
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the target of the Details pane to either be a
     * selected model element or
     * the owner(model element) of a selected fig.
     *
     * Decides which panels to enable.
     */
    public void setTarget(Object target) {
        if (target == _modelTarget
            || ((target instanceof Fig)
                && ((Fig)target).getOwner() == _modelTarget))
            return;
        if (target == null) {
            _figTarget = null;
            _modelTarget = null;
        }
        if (target instanceof Fig)
            _figTarget = (Fig)target;
        if (target instanceof Fig && ((Fig)target).getOwner() != null)
            _modelTarget = ((Fig)target).getOwner();
        else
            _modelTarget = target;

        int firstEnabled = -1;
        boolean jumpToFirstEnabledTab = false;
        boolean jumpToPrevEnabled = false;
        int currentTab = _tabs.getSelectedIndex();
        for (int i = 0; i < _tabPanels.size(); i++) {
            JPanel tab = (JPanel)_tabPanels.elementAt(i);
            if (tab instanceof TabModelTarget) {
                TabModelTarget tabMT = (TabModelTarget)tab;
                tabMT.setTarget(_modelTarget);
                boolean shouldEnable = tabMT.shouldBeEnabled();
                _tabs.setEnabledAt(i, shouldEnable);
                if (shouldEnable && firstEnabled == -1)
                    firstEnabled = i;
                if (_lastNonNullTab == i
                    && shouldEnable
                    && _modelTarget != null) {
                    jumpToPrevEnabled = true;
                }
                if (currentTab == i && !shouldEnable) {
                    jumpToFirstEnabledTab = true;
                }
            }
            if (tab instanceof TabFigTarget) {
                TabFigTarget tabFT = (TabFigTarget)tab;
                tabFT.setTarget(_figTarget);
                boolean shouldEnable = tabFT.shouldBeEnabled();
                _tabs.setEnabledAt(i, shouldEnable);
                if (shouldEnable && firstEnabled == -1)
                    firstEnabled = i;
                if (_lastNonNullTab == i
                    && shouldEnable
                    && _figTarget != null) {
                    jumpToPrevEnabled = true;
                }
                if (currentTab == i && !shouldEnable) {
                    jumpToFirstEnabledTab = true;
                }
            }
        }
        if (jumpToPrevEnabled) {
            _tabs.setSelectedIndex(_lastNonNullTab);
            return;
        }
        if (jumpToFirstEnabledTab && firstEnabled != -1)
            _tabs.setSelectedIndex(firstEnabled);
        if (jumpToFirstEnabledTab && firstEnabled == -1)
            _tabs.setSelectedIndex(0);
        if (target != null)
            _lastNonNullTab = _tabs.getSelectedIndex();
    }

    public Object getTarget() {
        return _modelTarget;
    }

    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }

    ////////////////////////////////////////////////////////////////
    // actions

    /**
     * Get the index of the tab with the given name
     *
     * @param tabName the name of the required tab
     * @return index of the tab of the given name
     */
    public int getIndexOfNamedTab(String tabName) {
        for (int i = 0; i < _tabPanels.size(); i++) {
            String title = _tabs.getTitleAt(i);
            if (title != null && title.equals(tabName))
                return i;
        }
        return -1;
    }

    /**
     * Get the JPanel of the tab with the given name
     *
     * @param tabName the name of the required tab
     * @return the tab of the given name
     */
    public JPanel getNamedTab(String tabName) {
        for (int i = 0; i < _tabPanels.size(); i++) {
            String title = _tabs.getTitleAt(i);
            if (title != null && title.equals(tabName))
                return (JPanel)_tabs.getComponentAt(i);
        }
        return null;
    }

    /**
     * Get the number of tabs
     *
     * @return the number of tab pages
     */
    public int getTabCount() {
        return _tabPanels.size();
    }

    public boolean selectTabNamed(String tabName) {
        ProjectBrowser pb = ProjectBrowser.TheInstance;

        int index = getIndexOfNamedTab(tabName);
        if (index != -1) {
            _tabs.setSelectedIndex(index);
            return true;
        }
        return false;
    }

    /**
     * Heper method to add a Property panel for a given class.
     */
    public void addToPropTab(Class c, PropPanel p) {
        for (int i = 0; i < _tabPanels.size(); i++) {
            if (_tabPanels.elementAt(i) instanceof TabProps) {
                ((TabProps)_tabPanels.elementAt(i)).addPanel(c, p);
            }
        }
    }
    
    /**
     * returns the Property panel in the Details Pane.
     */
    public TabProps getTabProps() {
        Iterator iter = _tabPanels.iterator();
        Object o;
        while (iter.hasNext()) {
            o = iter.next();
            if (o instanceof TabProps) {
                return (TabProps)o;
            }
        }
        return null;
    }

    /**
     * only added to the TabProps instance.
     */
    public void addNavigationListener(NavigationListener navListener) {
        Iterator iter = _tabPanels.iterator();
        Object panel;
        while (iter.hasNext()) {
            panel = iter.next();
            if (panel instanceof TabProps) {
                ((TabProps)panel).addNavigationListener(navListener);
            }
        }
    }

    /**
     * only removed from the TabProps instance.
     */
    public void removeNavigationListener(NavigationListener navListener) {
        Iterator iter = _tabPanels.iterator();
        Object panel;
        while (iter.hasNext()) {
            panel = iter.next();
            if (panel instanceof TabProps) {
                ((TabProps)panel).removeNavigationListener(navListener);
            }
        }
    }

    ////////////////////////////////////////////////////////////////
    // event handlers

    /**
     * Reacts to a change in the selected tab by calling refresh()
     * on that tab (a TabToDoTarget, TabModelTarget or TabFigTarget
     * instance).
     * 
     * old notes: called when the user selects a new tab, by clicking or
     *  otherwise.
     */
    public void stateChanged(ChangeEvent e) {
        cat.debug("DetailsPane state changed");
        Component sel = _tabs.getSelectedComponent();
        cat.debug(sel.getClass().getName());
        if (sel instanceof TabToDoTarget)
             ((TabToDoTarget)sel).refresh();
        else if (sel instanceof TabModelTarget)
             ((TabModelTarget)sel).refresh();
        else if (sel instanceof TabFigTarget)
             ((TabFigTarget)sel).refresh();
        if (_modelTarget != null)
            _lastNonNullTab = _tabs.getSelectedIndex();
    }

    /**
     * no action currently executed here.
     * called when the user clicks once on a tab.
     */
    public void mySingleClick(int tab) {
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        cat.debug("single: " + _tabs.getComponentAt(tab).toString());
    }

    /**
     * Spawns a new tab.
     * called when the user clicks twice on a tab.
     */
    public void myDoubleClick(int tab) {
        //TODO: should fire its own event and ProjectBrowser
        //should register a listener
        cat.debug("double: " + _tabs.getComponentAt(tab).toString());
        JPanel t = (JPanel)_tabPanels.elementAt(tab);
        if (t instanceof TabSpawnable)
             ((TabSpawnable)t).spawn();
    }

    /**
     * empty, no action taken.
     */
    public void mousePressed(MouseEvent me) {
    }
    /**
     * empty, no action taken.
     */
    public void mouseReleased(MouseEvent me) {
    }
    /**
     * empty, no action taken.
     */
    public void mouseEntered(MouseEvent me) {
    }
    /**
     * empty, no action taken.
     */
    public void mouseExited(MouseEvent me) {
    }
    
    /**
     * if(the mouse click is not in the bounds of the tabbed panel)
     *      then call mySingleClick() or myDoubleClick()
     */
    public void mouseClicked(MouseEvent me) {
        int tab = _tabs.getSelectedIndex();
        if (tab != -1) {
            Rectangle tabBounds = _tabs.getBoundsAt(tab);
            if (!tabBounds.contains(me.getX(), me.getY()))
                return;
            if (me.getClickCount() == 1)
                mySingleClick(tab);
            else if (me.getClickCount() >= 2)
                myDoubleClick(tab);
        }
    }

    /**
     * graphic that goes on the tab label
     */
    protected Icon _upArrowIcon = new UpArrowIcon();

    /**
     * graphic that goes on the tab label
     */
    protected Icon _leftArrowIcon = new LeftArrowIcon();

    public int getQuadrant() {
        return Q_BOTTOM_RIGHT;
    }

    /**
     * Set the orientation of this details pane;
     * @param the required orientation
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
        for (int i = 0; i < _tabPanels.size(); i++) {
            JPanel t = (JPanel)_tabPanels.elementAt(i);
            if (t instanceof Orientable) {
                Orientable o = (Orientable)t;
                o.setOrientation(orientation);
            }
        } /* end for */
    }

} /* end class DetailsPane */

////////////////////////////////////////////////////////////////
// related classes

    /**
     * class defining a graphic that goes on the tab label
     */
class UpArrowIcon implements Icon {
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int w = getIconWidth(), h = getIconHeight();
        g.setColor(Color.black);
        Polygon p = new Polygon();
        p.addPoint(x, y + h);
        p.addPoint(x + w / 2 + 1, y);
        p.addPoint(x + w, y + h);
        g.fillPolygon(p);
    }
    public int getIconWidth() {
        return 9;
    }
    public int getIconHeight() {
        return 9;
    }
}

    /**
     * class defining a graphic that goes on the tab label
     */
class LeftArrowIcon implements Icon {
    public void paintIcon(Component c, Graphics g, int x, int y) {
        int w = getIconWidth(), h = getIconHeight();
        g.setColor(Color.black);
        Polygon p = new Polygon();
        p.addPoint(x + 1, y + h / 2 + 1);
        p.addPoint(x + w, y);
        p.addPoint(x + w, y + h);
        g.fillPolygon(p);
    }
    public int getIconWidth() {
        return 9;
    }
    public int getIconHeight() {
        return 9;
    }

}
