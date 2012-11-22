package versionchecker.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * VCGitWindow is used as the main git UI for Version Checker plugin. It takes
 * the username and password for git from the user.
 */
public class VCGitWindow {
	private JFrame parentFrame;
	private JDialog dialog;
	private JTextField loginField;
	private JPasswordField passwordField;
	private Boolean setted;
	private JButton okButton;
	private JButton cancelButton;

	public VCGitWindow(JFrame parentFrame) {
		this.parentFrame = parentFrame;
		this.setted = false;
	}

	private void init() {

		this.loginField = new JTextField();
		this.passwordField = new JPasswordField();

		this.okButton = new JButton("OK");
		this.okButton.addActionListener(new OkButtonListener());

		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				close();
			}
		});

		this.dialog = new JDialog(this.parentFrame, "Version Checker", true);
		this.dialog.getContentPane().add(createPane());
		this.dialog.getRootPane().setDefaultButton(this.okButton);
		setEscapeKeyMap();
		this.dialog.pack();
		this.dialog.setLocationRelativeTo(this.parentFrame);
		this.dialog.setSize(300, 150);
	}

	private Container createPane() {
		JPanel topPanel = new JPanel(new GridLayout(3, 2));

		topPanel.add(new Label("Username"));
		topPanel.add(this.loginField);

		topPanel.add(new Label("Password"));
		topPanel.add(this.passwordField);

		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.LINE_AXIS));
		bottomPanel.add(Box.createHorizontalGlue());
		bottomPanel.add(this.okButton);
		bottomPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		bottomPanel.add(this.cancelButton);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9));
		mainPanel.add(topPanel, BorderLayout.CENTER);
		mainPanel.add(bottomPanel, BorderLayout.PAGE_END);
		return mainPanel;
	}

	private void setEscapeKeyMap() {
		String CANCEL_ACTION_KEY = "CANCEL_ACTION_KEY";
		int noModifiers = 0;
		KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,
				noModifiers, false);
		InputMap inputMap = this.dialog.getRootPane().getInputMap(
				JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		inputMap.put(escapeKey, CANCEL_ACTION_KEY);
		AbstractAction cancelAction = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				close();
			}
		};
		this.dialog.getRootPane().getActionMap()
				.put(CANCEL_ACTION_KEY, cancelAction);
	}

	public void show() {
		if (this.dialog == null) {
			init();
		}

		this.dialog.setVisible(true);
	}

	private void close() {
		this.dialog.setVisible(false);
	}

	private void clickOk() {
		this.setted = true;
	}

	public Boolean status() {
		return this.setted;
	}

	public String getLogin() {
		return this.loginField.getText();
	}

	public String getPass() {
		return this.passwordField.getPassword().toString();
	}

	private class OkButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// Do work
			clickOk();
			close();
		}
	}
}
