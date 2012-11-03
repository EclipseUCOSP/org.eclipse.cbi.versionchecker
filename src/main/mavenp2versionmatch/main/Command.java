package mavenp2versionmatch.main;

public enum Command {
	ADD("add"),
	FIND("find"),
	UPDATE("update"),
	;
	private String command_n;

	private Command(String name) {
		this.command_n = name;
	}

	public String getName() {
		return this.command_n;
	}

	public static Command findByStr(String str){
		for (Command e : Command.values()) {
			if (e.getName().equals(str)) {
				return e;
			}
		}
		return null;
	}


}
