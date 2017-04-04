/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package template;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

/**
 *
 * @author RACHMAD
 */
public class Toolbar
{
    private JToolBar toolbar;
    private OpenDialog openDialog;
    
    private final Map<String, Component> componentsByName = new HashMap<>();
    
    
    private final JButton btnNew = new JButton("",new ImageIcon(getClass().getResource("/icon/icon0.png")));
    private final JButton btnOpen = new JButton("",new ImageIcon(getClass().getResource("/icon/icon1.png")));
    private final JButton btnSave = new JButton("",new ImageIcon(getClass().getResource("/icon/icon11.png")));
    private final JButton btnSaveAll = new JButton("",new ImageIcon(getClass().getResource("/icon/icon12.png")));
    private final JButton btnCut = new JButton("",new ImageIcon(getClass().getResource("/icon/icon2.png")));
    private final JButton btnCopy = new JButton("",new ImageIcon(getClass().getResource("/icon/icon3.png")));
    private final JButton btnPaste = new JButton("",new ImageIcon(getClass().getResource("/icon/icon4.png")));
    private final JButton btnUndo = new JButton("",new ImageIcon(getClass().getResource("/icon/icon5.png")));
    private final JButton btnRedo = new JButton("",new ImageIcon(getClass().getResource("/icon/icon6.png")));
    private final JButton btnCompile = new JButton("",new ImageIcon(getClass().getResource("/icon/icon7.png")));
    private final JButton btnRun = new JButton("",new ImageIcon(getClass().getResource("/icon/icon8.png")));
    private final JButton btnHelp = new JButton("",new ImageIcon(getClass().getResource("/icon/icon9.png")));
    private final JPopupMenu popup = new JPopupMenu();
    
    private JButton toolBarButtonStyle(JButton button, String toolTipText, boolean setPopUp)
    {
        toolbar.add(button);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setToolTipText(toolTipText);
        
        if(setPopUp)
            button.addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    popup.show(e.getComponent(), e.getX(), e.getY());
                }
            });
        return button;
    }
    
    public JToolBar setToolbar()
    {
        toolbar =new JToolBar();
        toolbar.setRollover(true);
        toolbar.setFloatable(false);
        
        popup.add(new JMenuItem(new AbstractAction("Load Java Code")
        {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) 
            {
                openDialog = new OpenDialog("Java File", new String[]{"java"});
                if(openDialog.isApproved())
                {
                }
            }
        }));
        
        toolBarButtonStyle(btnNew, "New", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                
            }
        });;
        toolbar.addSeparator();
        toolBarButtonStyle(btnOpen, "Open", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                openDialog = new OpenDialog("Java", new String[]{"java"});
                if(openDialog.isApproved())
                {
                    if(openDialog.getExtentionFile().equalsIgnoreCase("java"))
                    {
                    }
                }
            }
        });
        
        toolBarButtonStyle(btnSave, "Save", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
            }
        });
        toolBarButtonStyle(btnSaveAll, "Save As", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
            }
        });
        toolbar.addSeparator();
        toolBarButtonStyle(btnCut, "Cut", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
            }
        });
        toolBarButtonStyle(btnCopy, "Copy", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
            }
        });
        toolBarButtonStyle(btnPaste, "Paste", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
            }
        });
        toolbar.addSeparator();
        toolBarButtonStyle(btnCompile, "Compile", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
            }
        });
        toolBarButtonStyle(btnRun, "Run", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
            }
        });
        
        toolbar.addSeparator();
        toolBarButtonStyle(btnHelp, "Help", false).addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
            }
        });
        
        return toolbar;
    }
}
