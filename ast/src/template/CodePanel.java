/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package template;

import static global.component.GlobalComponent.codeTree;
import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author RACHMAD
 */
public class CodePanel extends JPanel{
    private final TextCodeArea mainText;
    private DefaultMutableTreeNode field;
    private DefaultMutableTreeNode method;
    private JSplitPane mainSplit = new JSplitPane();
    
    public String getCode()
    {
        return mainText.getText();
    }
    
    public CodePanel()
    {
        codeTree.setModel(null);
        mainText = new TextCodeArea();
        setLayout(new BorderLayout());
        add(mainSplit, "Center");
        mainSplit.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        mainSplit.setResizeWeight(0.2);
        mainSplit.setLeftComponent(new JScrollPane(codeTree));
        mainSplit.setRightComponent(mainText);
    }
}