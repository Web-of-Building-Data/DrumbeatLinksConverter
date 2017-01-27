package fi.aalto.drumbeat;

public class CSVBeean {
    String GUID;
    String correspondingGUID;
	public CSVBeean() {
		
	}
	public String getGUID() {
		return GUID;
	}
	public void setGUID(String gUID) {
		GUID = gUID;
	}
	public String getCorrespondingGUID() {
		return correspondingGUID;
	}
	public void setCorrespondingGUID(String correspondingGUID) {
		this.correspondingGUID = correspondingGUID;
	}
}
