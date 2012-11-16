package versionchecker.actions;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class VCMainWindow {
	private Object[] oriData;
    private JTextField searchField;
    private JButton searchButton;

	public VCMainWindow(Object[] data) {
		this.oriData = data;
		createAndShowGUI(this.oriData);
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private void createAndShowGUI(Object[] data) {
		// Create and set up the window.
		JFrame frame = new JFrame("Version Checker");

		JPanel mainPanel = new JPanel();
		frame.setContentPane(mainPanel);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.LINE_AXIS));
		
		searchPanel.setPreferredSize(new Dimension(300, 20));
		searchPanel.setMaximumSize(searchPanel.getPreferredSize()); // prevent growth
		searchPanel.setMinimumSize(searchPanel.getPreferredSize()); // prevent shrink
		
        this.searchField = new JTextField();
        this.searchField.setSize(100, 20);
        searchPanel.add(searchField);
        
        searchPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        
        this.searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        mainPanel.add(searchPanel);

		// Create and set up the content pane.
		VCArtifactTable tablePanel = new VCArtifactTable(data);
		tablePanel.setOpaque(true); // content panes must be opaque

		mainPanel.add(tablePanel);

		JPanel clonePanel = new JPanel();
		clonePanel.setLayout(new BoxLayout(clonePanel, BoxLayout.LINE_AXIS));

		JButton fetchButton = new JButton("Fetch");
		JButton cloneButton = new JButton("Clone");

		clonePanel.add(fetchButton);
		clonePanel.add(Box.createRigidArea(new Dimension(50, 0)));
		clonePanel.add(cloneButton);

		mainPanel.add(clonePanel);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

}
