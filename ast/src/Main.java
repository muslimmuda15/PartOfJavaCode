/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import static global.component.GlobalComponent.mainTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import template.CodePanel;
import template.Menu;
import template.Toolbar;
import utility.TextLine;

/**
 *
 * @author RACHMAD
 */
public class Main extends JFrame
{
    private final JSplitPane mainSplit = new JSplitPane();
    private JTree mainTree;
    private final JScrollPane mainTextAreaScroll = new JScrollPane(mainTextArea);
    private TextLine textLine;
    
    public Main()
    {
        init();
        view();
    }
    
    /*
     * initial component
    */
    private void init()
    {
        mainTree = new JTree();
        textLine = new TextLine(mainTextArea);
    }
    
    private void view()
    {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        setLocation(0,0);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)screenSize.getWidth()-50,(int)screenSize.getHeight()-50);
        setLayout(new BorderLayout());
        
        addWindowListener(new closeWindows());
        
        setJMenuBar(new Menu().setMenu());
        add(new Toolbar().setToolbar(), BorderLayout.BEFORE_FIRST_LINE);
        
        add(new CodePanel(), "Center");
        
        show();
    }
    
    private class closeWindows extends java.awt.event.WindowAdapter
    {
        public void windowClosing(java.awt.event.WindowEvent windowEvent) 
        {
            if (JOptionPane.showConfirmDialog(null, 
            "Are you sure to close this window?", "Really Closing?", 
                JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
            {
                // new delete().proses();
                System.exit(0);
            }
        }
    }
    
    private static final void lookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel
            (UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex)
        {
            ex.printStackTrace();
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        lookAndFeel();
        new Main();
    }
    
}
