package mavenp2versionmatch.main.test;
import mavenp2versionmatch.main.Command;
import junit.framework.TestCase;


public class CommandTest extends TestCase{
	public CommandTest(String name){
		super(name);
	}
	
	public void testAddCommand() throws Exception {
		assertEquals(Command.ADD, Command.findByStr("add"));
	}
	public void testModifyCommand() throws Exception{
		assertEquals(Command.FIND, Command.findByStr("find"));
	}
	public void testUpdateCommand() throws Exception{
		assertEquals(Command.UPDATE, Command.findByStr("update"));
	}
	public void testNotCommand() throws Exception{
		assertEquals(null, Command.findByStr("notacommand"));
	}
	public void testEmptyCommand() throws Exception{
		assertEquals(null, Command.findByStr(""));
	}

}
