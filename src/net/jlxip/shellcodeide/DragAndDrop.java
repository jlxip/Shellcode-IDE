package net.jlxip.shellcodeide;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

import javax.swing.TransferHandler;

public class DragAndDrop extends TransferHandler {
	private static final long serialVersionUID = 1L;
	
	Main main;
	public DragAndDrop(Main main) {
		this.main = main;
	}

	@Override
    public boolean canImport(TransferHandler.TransferSupport info) {
        if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
	@Override
    public boolean importData(TransferHandler.TransferSupport info) {
        if (!info.isDrop()) {
            return false;
        }

        // Check for FileList flavor
        if (!info.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            System.out.println("List doesn't accept a drop of this type.");
            return false;
        }

        // Get the fileList that is being dropped.
        Transferable t = info.getTransferable();
        List<File> data;
        try {
            data = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
        } 
        catch (Exception e) { return false; }
        
        if(data.size() > 1) {	// More than one file!
        	main.ready = false;
        	return false;
        }
        
        main.receiveFile(data.get(0));
        
        return true;
    }
}
