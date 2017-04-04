/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package template;

/**
 *
 * @author RACHMAD
 */
import global.component.GlobalComponent;
import global.data.Path;
import utility.ExtensionFileFilter;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author RACHMAD
 */
public class OpenDialog
{
    private boolean approved = false;
    private String getFileName;
    private File getFullPathFile;
    private JFileChooser fc;
    
    public OpenDialog(String extInfo, String ext[])
    {
        UIManager.put("FileChooser.readOnly", Boolean.TRUE);
        fc = new JFileChooser(Path.location);
        FileFilter filter1 = new ExtensionFileFilter(extInfo, ext);
        fc.setFileFilter(filter1);
        int status = fc.showOpenDialog(GlobalComponent.mainFrame);
        if (status==JFileChooser.APPROVE_OPTION)
        {    
            approved = true;
            Path.location = fc.getSelectedFile().getParent();
            this.getFileName = fc.getSelectedFile().getName();
            this.getFullPathFile = fc.getSelectedFile().getAbsoluteFile();
        }
    }
    
    public boolean isApproved()
    {
        return approved;
    }
    
    public String getFileName()
    {
        if(getFileName == null)
        {
            JOptionPane.showMessageDialog(null, "File location is null");
            return null;
        }
        return getFileName;
    }
    
    public File getFullPathFile()
    {
        return this.getFullPathFile;
    }
    
    public File getParentFile()
    {
        return this.getFullPathFile.getParentFile();
    }
    
    public String getExtentionFile()
    {
        return this.getFileName.replaceAll("\\w+\\.", "");
    }
}

