package fi.aalto.drumbeat;


public class GenericLinkBean {
		private String guid;
		private String connected_guid;
		
		
		public GenericLinkBean() {
			super();
		}
		public GenericLinkBean(String guid, String connected_guid) {
			super();
			this.guid = guid;
			this.connected_guid = connected_guid;
		}
		public String getGuid() {
			return guid;
		}
		public void setGuid(String guid) {
			this.guid = guid;
		}
		public String getConnected_guid() {
			return connected_guid;
		}
		public void setConnected_guid(String connected_guid) {
			this.connected_guid = connected_guid;
		}
		@Override
		public String toString() {
			return "GenericLinkBean [guid=" + guid + ", connected_guid=" + connected_guid + "]";
		}
		
		
		
}
