package versionchecker.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class VCCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		VCXmlParser xp = new VCXmlParser();
		final Object[] data = xp.parse();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new VCMainWindow(data);
			}
		});
		return null;
	}

}
