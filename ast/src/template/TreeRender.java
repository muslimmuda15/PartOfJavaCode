/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package template;

import static global.component.GlobalComponent.codeTree;
import java.awt.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author RACHMAD
 */
public class TreeRender extends DefaultTreeCellRenderer
{
    private Icon loadIcon = UIManager.getIcon("OptionPane.errorIcon");
    private Icon saveIcon = UIManager.getIcon("OptionPane.informationIcon");
    @Override
    public Component getTreeCellRendererComponent(JTree tree,
            Object value, boolean selected, boolean expanded,
            boolean isLeaf, int row, boolean focused) {
        Component c = super.getTreeCellRendererComponent(tree, value,
                selected, expanded, isLeaf, row, focused);
//        if (selected)
//            setIcon(loadIcon);
//        else
//            setIcon(saveIcon);
        structureTreeIcon(value);
        return c;
    }
    
    private void structureTreeIcon(Object value)
    {
        /*
         * loop
        */
        Pattern pattern = Pattern.compile("(^for\\s*\\()|(^while\\s*\\()|(^do-while\\s*\\()"); 
        Matcher matcher = pattern.matcher(value.toString());
        if(matcher.find())
        {
            Icon icon = new ImageIcon(getClass().getResource("/icon/tree icon/loop.png"));
            setIcon(icon);
        }
        
        /*
         * condition
        */
        pattern = Pattern.compile("(^if\\s*\\()|(^switch\\s*\\()|(^else\\s*$)"); 
        matcher = pattern.matcher(value.toString());
        if(matcher.find())
        {
            Icon icon = new ImageIcon(getClass().getResource("/icon/tree icon/condition.png"));
            setIcon(icon);
        }
        
        /*
         * package
        */
        if(((DefaultMutableTreeNode)value).isRoot())
        {
            Icon icon = new ImageIcon(getClass().getResource("/icon/tree icon/package.png"));
            setIcon(icon);
        }
        
        /*
         * class
        */
        if(((DefaultMutableTreeNode)value).getLevel() == 1)
        {
            if(getText().contains("class"))
            {
                Icon icon = new ImageIcon(getClass().getResource("/icon/tree icon/class.png"));
                setIcon(icon);
                setText(value.toString().replaceAll("class ", ""));
            }
            else if(getText().contains("interface"))
            {
                Icon icon = new ImageIcon(getClass().getResource("/icon/tree icon/interface.png"));
                setIcon(icon);
                setText(value.toString().replaceAll("interface ", ""));
            }
        }
        
        /*
         * extends
        */
        if(getText().contains("extends "))
        {
            Icon icon = new ImageIcon(getClass().getResource("/icon/tree icon/extends.png"));
            setIcon(icon);
            setText(value.toString().replaceAll("extends ", ""));
        }
        
        /*
         * implements
        */
        else if(getText().contains("implements "))
        {
            Icon icon = new ImageIcon(getClass().getResource("/icon/tree icon/implements.png"));
            setIcon(icon);
            setText(value.toString().replaceAll("implements ", ""));
        }
    }
}
