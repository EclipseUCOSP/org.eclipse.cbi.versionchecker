package mavenp2versionmatch.main;

public class InvalidManifestException extends Exception {
	public InvalidManifestException() {
	}
	public InvalidManifestException(String message) {
		super(message);
	}
	public InvalidManifestException(VersionManifest mft) {
		super(mft.toString());
	}
}
