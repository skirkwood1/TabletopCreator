package UI.UIHelpers;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;
import java.io.File;

public class FileViewerUI extends FileView {
    public Icon getIcon(File f)
    {
        FileSystemView view=FileSystemView.getFileSystemView();
        return view.getSystemIcon(f);
    }
}