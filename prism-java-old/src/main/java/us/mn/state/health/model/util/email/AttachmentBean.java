package us.mn.state.health.model.util.email;
import java.io.InputStream;


public class AttachmentBean {
	private int attachmentId;
	private int messageId;
	private String fileTitle;
	private InputStream fileBlob;

	private byte[] byteBlob;

	public int getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(int attachmentId) {
		this.attachmentId = attachmentId;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public InputStream getFileBlob() {
		return fileBlob;
	}

	public void setFileBlob(InputStream fileBlob) {
		this.fileBlob = fileBlob;
	}

	public byte[] getByteBlob() {
		return byteBlob;
	}

	public void setByteBlob(byte[] byteBlob) {
		this.byteBlob = byteBlob;
	}

	public String getFileExtension(){
		String fileExtension = fileTitle.substring((fileTitle.lastIndexOf(".") + 1), fileTitle.length());
		return fileExtension;
	}

	public String getContentType(String f) {
		if (f.equalsIgnoreCase("txt")
				|| f.equalsIgnoreCase("htm")
				|| f.equalsIgnoreCase("html")
				|| f.equalsIgnoreCase("fla")   // flash file
				|| f.equalsIgnoreCase("swf")   // flash movie
				|| f.equalsIgnoreCase("csv")
				|| f.equalsIgnoreCase("tsv")
				|| f.equalsIgnoreCase("jpg")
				|| f.equalsIgnoreCase("properties")
				|| f.equalsIgnoreCase("jpeg")
				|| f.equalsIgnoreCase("gif")) {
			return "text/html";
		} else if (f.equalsIgnoreCase("rtf")
				|| f.equalsIgnoreCase("pdf")
				|| f.equalsIgnoreCase("zip")) {
			return "application/" + f;
		} else if (f.equalsIgnoreCase("mp3")    // mp3 audio
				|| f.equalsIgnoreCase("wav")) { // wav audio
			return "audio/" + f;
		} else if (f.equalsIgnoreCase("ra")     // real audio
				|| f.equalsIgnoreCase("ram")) { // real audio
			return "audio/vnd.rn-realaudio";
		} else if (f.equalsIgnoreCase("rv")) { // real audio
			return "video/vnd.rn-realvideo";
		} else if (f.equalsIgnoreCase("wma")) { // windows media audio
			return "audio/x-ms-wma";
		} else if (f.equalsIgnoreCase("wmv")) { // windows media video
			return "video/x-ms-wmv";
		} else if (f.equalsIgnoreCase("asx")) { // windows media video
			return "video/x-ms-asf";
		} else if (f.equalsIgnoreCase("xls")) { // microsoft exel
			return "application/msexcel";
		} else if (f.equalsIgnoreCase("ppt")) { // microsoft power point
			return "application/mspowerpoint";
		} else if (f.equalsIgnoreCase("doc")) { // microsoft word
			return "application/msword";
		} else if (f.equalsIgnoreCase("mpeg")   // mpeg video
				|| f.equalsIgnoreCase("mpg")) { // mpg video
			return "video/mpeg";
		} else if (f.equalsIgnoreCase("qt")     // quicktime video
				|| f.equalsIgnoreCase("mov")) { // quicktime video
			return "video/quicktime";
		} else if (f.equalsIgnoreCase("avi")) { // avi video
			return "video/avi";
		} else {
			return "application/octet-stream";
		}
	}
	
}
