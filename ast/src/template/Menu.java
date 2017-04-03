/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package template;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author RACHMAD
 */
public class Menu
{
    JMenuBar menu = new JMenuBar();
    
    JMenu file = new JMenu("  File  ");
    JMenu edit = new JMenu("  Edit  ");
    JMenu search = new JMenu("  Search  ");
    JMenu build = new JMenu("  Build  ");
    JMenu help = new JMenu("  Help  ");
    
    JMenu menuNew = new JMenu("New")
    {
        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = Math.max(d.width, 300); // set minimums
            return d;
        }
    };
        JMenuItem menuText = new JMenuItem("Enter Text Code");
        JMenuItem menuClass = new JMenuItem("Load Class File");
        JMenuItem menuJar = new JMenuItem("Load Jar File");
        
    JMenuItem menuOpen = new JMenuItem("Open\u2026", new ImageIcon(getClass().getResource("/icon/icon1.png")));
    JMenuItem menuClose = new JMenuItem("Close");
    JMenuItem menuSave = new JMenuItem("Save", new ImageIcon(getClass().getResource("/icon/icon11.png")));
    JMenuItem menuSaveAll = new JMenuItem("Save All", new ImageIcon(getClass().getResource("/icon/icon12.png")));
    
    JMenuItem menuCompile = new JMenuItem("Compile", new ImageIcon(getClass().getResource("/icon/icon7.png")))
    {
        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = Math.max(d.width, 300); // set minimums
            return d;
        }
    };
    JMenuItem menuRun = new JMenuItem("Run", new ImageIcon(getClass().getResource("/icon/icon8.png")));
    
    JMenuItem menuUndo = new JMenuItem("Undo", new ImageIcon(getClass().getResource("/icon/icon5.png")))
    {
        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = Math.max(d.width, 300); // set minimums
            return d;
        }
    };
    JMenuItem menuRedo = new JMenuItem("Redo", new ImageIcon(getClass().getResource("/icon/icon6.png")));
    JMenuItem menuCut = new JMenuItem("Cut", new ImageIcon(getClass().getResource("/icon/icon2.png")));
    JMenuItem menuCopy = new JMenuItem("Copy", new ImageIcon(getClass().getResource("/icon/icon3.png")));
    JMenuItem menuPaste = new JMenuItem("Paste", new ImageIcon(getClass().getResource("/icon/icon4.png")));
    JMenuItem menuDelete = new JMenuItem("Delete");
    JMenuItem menuSelectAll = new JMenuItem("SelectAll");

    JMenuItem menuSearch = new JMenuItem("Search")
    {
        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = Math.max(d.width, 300); // set minimums
            return d;
        }
    };
    JMenuItem menuGoto = new JMenuItem("Go to line");
    
    JMenuItem menuHelp = new JMenuItem("Help")
    {
        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            d.width = Math.max(d.width, 300); // set minimums
            return d;
        }
    };
    JMenuItem menuAbout = new JMenuItem("About");
    
    private JMenuItem menuItemKeyCombination(JMenuItem menuItem, char keyCode)
    {
        menuItem.setMnemonic(keyCode);
        menuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl "+keyCode));
        return menuItem;
    }
    
    public JMenuBar setMenu()
    {
        menu.add(file);
            file.add(menuNew);
                menuNew.add(menuText);
                menuNew.add(menuClass);
                menuNew.add(menuJar);
            file.add(menuItemKeyCombination(menuOpen, 'O'));
            file.addSeparator();
            file.add(menuItemKeyCombination(menuSave, 'S'));
            file.add(menuSaveAll);
            file.addSeparator();
            file.add(menuClose);
            
        menu.add(edit);
            edit.add(menuItemKeyCombination(menuUndo,'Z'));
            edit.add(menuItemKeyCombination(menuRedo,'Y'));
            edit.addSeparator();
            edit.add(menuItemKeyCombination(menuCut, 'X'));
            edit.add(menuItemKeyCombination(menuCopy,'C'));
            edit.add(menuItemKeyCombination(menuPaste, 'V'));
            edit.add(menuDelete);
            edit.addSeparator();
            edit.add(menuItemKeyCombination(menuSelectAll, 'A'));
        menu.add(search);
            search.add(menuItemKeyCombination(menuSearch, 'F'));
            search.add(menuItemKeyCombination(menuGoto, 'G'));
        menu.add(build);
            build.add(menuCompile);
            build.add(menuRun);
        menu.add(help);
            help.add(menuHelp);
            help.add(menuAbout);
            
        return menu;
    }
}

