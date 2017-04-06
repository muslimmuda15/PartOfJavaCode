/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package template;

import static global.component.GlobalComponent.mainTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import utility.TextLine;

/**
 *
 * @author RACHMAD
 */
public class TextCodeArea extends JScrollPane{
    private TextLine tl;
    private JScrollPane scrollText;
            
    public TextCodeArea()
    {
        super(mainTextArea);
        
        tl = new TextLine(mainTextArea);
        setRowHeaderView(tl);
    }
    
    public String getText()
    {
        return mainTextArea.getText();
    }
    
    public void setText(String text)
    {
        mainTextArea.setText(text);
    }
}
