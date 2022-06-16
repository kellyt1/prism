package us.mn.state.health.model.inventory;

import us.mn.state.health.model.common.ModelMember;

import java.util.Date;
import java.sql.Blob;
import java.sql.SQLException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.hibernate.Hibernate;

/**
 * AttachedFileTbl generated by MyEclipse Persistence Tools
 */

public class AttachedFile extends ModelMember implements java.io.Serializable {

	// Fields

	private Long attachedFileId;
//	private Item item;
	private String fileName;
	private String fileExtension;
	private Long fileSize;
	private Date fileDate;
	private String fileType;
	private byte[] fileContents;

    // Constructors

	/** default constructor */
	public AttachedFile() {
	}

	/** minimal constructor */
	public AttachedFile(Long attachedFileId) {
		this.attachedFileId = attachedFileId;
	}

	/** full constructor */
	public AttachedFile(Long attachedFileId,
//            Item item,
			String fileName, String fileExtension, Long fileSize,
			Date fileDate, String fileType, byte[] fileContents) {
		this.attachedFileId = attachedFileId;
//		this.item = item;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		this.fileSize = fileSize;
		this.fileDate = fileDate;
		this.fileType = fileType;
		this.fileContents = fileContents;
	}

	// Property accessors

	public Long getAttachedFileId() {
		return this.attachedFileId;
	}

	public void setAttachedFileId(Long attachedFileId) {
		this.attachedFileId = attachedFileId;
	}

//	public Item getItem() {
//		return this.item;
//	}
//
//	public void setItem(Item item) {
//		this.item = item;
//	}

//    public Item getStockItem() {
//        return this.item;
//    }

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return this.fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public Long getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Date getFileDate() {
		return this.fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public byte[] getFileContents() {
		return this.fileContents;
	}

	public void setFileContents(byte[] fileContents) {
		this.fileContents = fileContents;
	}

    public void setFileContentsHibernate(Blob imageBlob) {
        this.fileContents = this.toByteArray(imageBlob);
    }


    /**
     * Don't invoke this.  Used by Hibernate only.
     */
    public Blob getFileContentsHibernate() {
        byte[] fileContents = this.fileContents;
        if (fileContents == null) {
            return null;
        }
        return Hibernate.createBlob(fileContents);
    }

    private byte[] toByteArray(Blob fromBlob) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            return toByteArrayImpl(fromBlob, baos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    private byte[] toByteArrayImpl(Blob fromBlob, ByteArrayOutputStream baos)
            throws SQLException, IOException {
        byte[] buf = new byte[4000];
        if (fromBlob == null) {
            return null;
        }
        InputStream is = fromBlob.getBinaryStream();
        try {
            for (; ;) {
                int dataSize = is.read(buf);

                if (dataSize == -1)
                    break;
                baos.write(buf, 0, dataSize);
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                }
            }
        }
        return baos.toByteArray();
    }
}