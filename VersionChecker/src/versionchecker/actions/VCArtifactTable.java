package versionchecker.actions;

/*
* SimpleTableSelectionDemo.java requires no other files.
*/

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JComponent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/** 
* SimpleTableSelectionDemo is just like SimpleTableDemo, 
* except that it detects selections, printing information
* about the current selection to standard output.
*/
public class VCArtifactTable extends JPanel {
   private boolean DEBUG = false;
   private boolean ALLOW_ROW_SELECTION = true;
   private Object[][] contents;
   private int size = 0;

   public VCArtifactTable(Object[] data) {
       super(new GridLayout(1,0));

       final String[] columnNames = {"ID",
                                     "Version"};
       
       contents = new Object[data.length][2];
       System.out.println(data.length);
       
       for (int i = 0; i < data.length; i++){
    	   this.addEntry((VCArtifact) data[i]);
    	   //System.out.println(((VCArtifact) data[i]).getId());
       }

       final JTable table = new JTable(contents, columnNames);
       table.setPreferredScrollableViewportSize(new Dimension(500, 70));
       table.setFillsViewportHeight(true);

       table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
       if (ALLOW_ROW_SELECTION) { // true by default
           ListSelectionModel rowSM = table.getSelectionModel();
           rowSM.addListSelectionListener(new ListSelectionListener() {
               public void valueChanged(ListSelectionEvent e) {
                   //Ignore extra messages.
                   if (e.getValueIsAdjusting()) return;

                   ListSelectionModel lsm = (ListSelectionModel)e.getSource();
                   if (lsm.isSelectionEmpty()) {
                       System.out.println("No rows are selected.");
                   } else {
                       int selectedRow = lsm.getMinSelectionIndex();
                       System.out.println("Row " + selectedRow
                                          + " is now selected.");
                   }
               }
           });
       } else {
           table.setRowSelectionAllowed(false);
       }

       if (DEBUG) {
           table.addMouseListener(new MouseAdapter() {
               public void mouseClicked(MouseEvent e) {
                   printDebugData(table);
               }
           });
       }

       //Create the scroll pane and add the table to it.
       JScrollPane scrollPane = new JScrollPane(table);

       //Add the scroll pane to this panel.
       add(scrollPane);
   }
   
   private void addEntry(VCArtifact toAdd){
	   this.contents[this.size] = new String[2];
	   this.contents[this.size][0] = toAdd.getId();
	   this.contents[this.size][1] = toAdd.getVersion();
	   this.size++;
   }
   

   private void printDebugData(JTable table) {
       int numRows = table.getRowCount();
       int numCols = table.getColumnCount();
       javax.swing.table.TableModel model = table.getModel();

       System.out.println("Value of data: ");
       for (int i=0; i < numRows; i++) {
           System.out.print("    row " + i + ":");
           for (int j=0; j < numCols; j++) {
               System.out.print("  " + model.getValueAt(i, j));
           }
           System.out.println();
       }
       System.out.println("--------------------------");
   }

   /**
    * Create the GUI and show it.  For thread safety,
    * this method should be invoked from the
    * event-dispatching thread.
    */
   public static void createAndShowGUI(Object[] data) {
       //Create and set up the window.
       JFrame frame = new JFrame("Version Checker");
       
       JPanel mainPanel = new JPanel();
       frame.setContentPane(mainPanel);
       mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

       //Create and set up the content pane.
       VCArtifactTable newContentPane = new VCArtifactTable(data);
       newContentPane.setOpaque(true); //content panes must be opaque
       
       mainPanel.add(newContentPane);
       
       JPanel buttonPanel = new JPanel();
       buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
       
       JButton fetchButton = new JButton("Fetch");
       JButton cloneButton = new JButton("Clone");
       
       buttonPanel.add(fetchButton);
       buttonPanel.add(Box.createRigidArea(new Dimension(50, 0)));
       buttonPanel.add(cloneButton);
       
       mainPanel.add(buttonPanel);
       
       //Display the window.
       frame.pack();
       frame.setVisible(true);
   }

}