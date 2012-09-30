package mavenp2versionmatch.main;

public enum Prefix {
		  DASH('-')
		  ;
		  private char c;
		  private Prefix(char c) {
		    this.c = c;
		  }
		  char getName() {
		    return c;
		  }
}
