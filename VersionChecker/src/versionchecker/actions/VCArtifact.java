package versionchecker.actions;

// VCArtifact is a data model for artifact instance.
public class VCArtifact implements Comparable{
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

	@Override
	public int compareTo(Object arg0) {
		return this.id.compareTo(((VCArtifact) arg0).id);
	}
}
