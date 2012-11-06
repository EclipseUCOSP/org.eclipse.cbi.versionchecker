package versionchecker.actions;

public class VCArtifact {
	private String id;
	private String version;
	
	public VCArtifact(String idf, String ver){
		this.id = idf;
		this.version = ver;
	}
	
	public String getId(){
		return this.id;
	}
	
	public String getVersion(){
		return this.version;
	}
}
