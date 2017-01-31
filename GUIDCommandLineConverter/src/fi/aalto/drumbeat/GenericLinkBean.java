package fi.aalto.drumbeat;

import guidcompressor.GuidCompressor;

public class GenericLinkBean {
		private String guid;
		private String connected_guid;
		
		
		public GenericLinkBean() {
			super();
		}
		public GenericLinkBean(String guid, String connected_guid) {
			super();
			setGuid(guid);
			setConnected_guid(connected_guid);
		}
		public String getGuid() {
			return guid;
		}
		public void setGuid(String guid) {
			if(guid==null || guid.length()==0)
				this.guid=null;
			else
			    this.guid = GuidCompressor.uncompressGuidString(guid).toUpperCase();
		}
		public String getConnected_guid() {
			return connected_guid;
		}
		public void setConnected_guid(String guid) {
			if(guid==null || guid.length()==0)
				this.connected_guid=null;
			else
				this.connected_guid = GuidCompressor.uncompressGuidString(guid).toUpperCase();
		}
		@Override
		public String toString() {
			return "GenericLinkBean [guid=" + guid + ", connected_guid=" + connected_guid + "]";
		}
		
		
		
}
