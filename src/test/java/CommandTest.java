package mavenp2versionmatch.main.test;
import mavenp2versionmatch.main.Command;
import org.junit.*;
import static org.junit.Assert.*;


public class CommandTest {

	@Test
	public void testAddCommand() throws Exception {
		assertEquals(Command.ADD, Command.findByStr("add"));
	}
	
	@Test
	public void testModifyCommand() throws Exception{
		assertEquals(Command.FIND, Command.findByStr("find"));
	}
	
	@Test
	public void testUpdateCommand() throws Exception{
		assertEquals(Command.UPDATE, Command.findByStr("update"));
	}
	
	@Test
	public void testNotCommand() throws Exception{
		assertEquals(null, Command.findByStr("notacommand"));
	}
	
	@Test
	public void testEmptyCommand() throws Exception{
		assertEquals(null, Command.findByStr(""));
	}

}
