package us.mn.state.health.model.materialsrequest;

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

public class AttachedFileNonCat extends ModelMember implements java.io.Serializable {

	// Fields

	private Long attachedFileNonCatId;
	private String fileName;
	private String fileExtension;
	private Integer fileSize;
	private Date fileDate;
	private String fileType;
	private byte[] fileContents;

    // Constructors

	/** default constructor */
	public AttachedFileNonCat() {
	}

	/** minimal constructor */
	public AttachedFileNonCat(Long attachedFileNonCatId) {
		this.attachedFileNonCatId = attachedFileNonCatId;
	}

	/** full constructor */
	public AttachedFileNonCat(Long attachedFileNonCatId, String fileName, String fileExtension, int fileSize, Date fileDate, String fileType, byte[] fileContents) {
		this.attachedFileNonCatId = attachedFileNonCatId;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
		this.fileSize = fileSize;
		this.fileDate = fileDate;
		this.fileType = fileType;
		this.fileContents = fileContents;
	}

	// Property accessors

	public Long getAttachedFileNonCatId() {
		return this.attachedFileNonCatId;
	}

	public void setAttachedFileNonCatId(Long attachedFileNonCatId) {
		this.attachedFileNonCatId = attachedFileNonCatId;
	}

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

	public Integer getFileSize() {
		return this.fileSize;
	}

	public void setFileSize(Integer fileSize) {
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
            try {
                baos.close();
            } catch (IOException ignore) {
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
                } catch (IOException ignore) {
                }
            }
        }
        return baos.toByteArray();
    }
}