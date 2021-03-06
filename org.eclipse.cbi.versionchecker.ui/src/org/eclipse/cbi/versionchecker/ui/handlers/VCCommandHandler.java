package org.eclipse.cbi.versionchecker.ui.handlers;

import org.eclipse.cbi.versionchecker.ui.actions.VCArtifact;
import org.eclipse.cbi.versionchecker.ui.actions.VCMainWindow;
import org.eclipse.cbi.versionchecker.ui.actions.VCXmlParser;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class VCCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		VCXmlParser xp = new VCXmlParser();
		final VCArtifact[] data = xp.parse();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new VCMainWindow(data);
			}
		});
		return null;
	}

}