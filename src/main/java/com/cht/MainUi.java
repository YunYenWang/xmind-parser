package com.cht;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import org.springframework.stereotype.Service;

import com.cht.xmind.Node;
import com.cht.xmind.XMindParser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MainUi {

	@PostConstruct
	void start() {
		JFrame jf = new JFrame("拖入 *.xmind 檔案");
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jf.setSize(320, 240);
		
		jf.setDropTarget(new DropTarget() {			
			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void drop(DropTargetDropEvent event) {
				try {
					event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					
					Transferable t = event.getTransferable();
					
					for (DataFlavor df : t.getTransferDataFlavors()) {
						if (df.isFlavorJavaFileListType()) {
							@SuppressWarnings("unchecked")
							List<File> files = (List<File>) t.getTransferData(df);
							for (File f : files) {
								if (f.getName().endsWith(".xmind") == false) {
									continue;
								}
								
								if (f.isFile() && f.canRead()) {
									try {
										convert(f);
										
										JOptionPane.showMessageDialog(jf, "Successful");
										
									} catch (Exception e) {
										log.error("Error", e);
										
										JOptionPane.showMessageDialog(
												jf,
												String.format("%s: %s",
														e.getClass().getSimpleName(),
														e.getMessage()),
												"Error",
												JOptionPane.ERROR_MESSAGE);
									}
										
									break;
								}
							}
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});		
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension d = jf.getSize();
		jf.setLocation(
				(screen.width - d.width)   / 2, 
				(screen.height - d.height) / 2);
		
		jf.setVisible(true);
	}
	
	void convert(File file) throws IOException {
		XMindParser parser = new XMindParser();
		Node node = parser.parse(file);
		
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf(".xmind")) + ".csv";
		
		File csv = new File(file.getParentFile(), name);
		
		try (FileOutputStream fos = new FileOutputStream(csv)) {
			OutputStreamWriter osw = new OutputStreamWriter(fos, "MS950");
			PrintWriter pw = new PrintWriter(osw);
			
			dump(pw, node, 0);
			
			pw.flush();
		}
	}
	
	void dump(PrintWriter pw, Node node, int level) {
		for (int i = 0;i < level;i++) {
			pw.print("\"\",");
		}
		
		pw.printf("\"%s\"\n", node.getTitle());
		
		for (Node child : node.getChildren()) {
			dump(pw, child, level + 1);
		}
	}
}
