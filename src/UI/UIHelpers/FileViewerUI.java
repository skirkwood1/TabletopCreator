package UI.UIHelpers;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import java.io.File;
//import java.util.ArrayList;
import java.util.HashMap;

public class FileViewerUI extends FileView {
    HashMap<String,Icon> folders = new HashMap<>();
    FileSystemView view = FileSystemView.getFileSystemView();
    public Icon getIcon(File f)
    {
        //return view.getSystemIcon(f);
        if(folders.containsKey(f.getName())){
            return folders.get(f.getName());
        }else{
            Icon icon = view.getSystemIcon(f);
            folders.put(f.getName(),icon);
            return icon;
        }
    }
}