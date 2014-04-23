package org.profwell.vacancy.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.profwell.generic.model.ModelConstants;

/**
 * Quite stupid structure, however it works fast.
 * TODO : refactor me
 */
@Embeddable
@Access(AccessType.FIELD)
public class HookupDocuments {

    public static final String RESUME                       = "RESUME";
    public static final String TEST_TASK                    = "TEST_TASK";
    public static final String INTERVIEW_FEEDBACK_PREFIX    = "INTERVIEW_FEEDBACK";
    public static final String INTERVIEW_FEEDBACK_0         = "INTERVIEW_FEEDBACK_0";
    public static final String INTERVIEW_FEEDBACK_1         = "INTERVIEW_FEEDBACK_1";
    public static final String INTERVIEW_FEEDBACK_2         = "INTERVIEW_FEEDBACK_2";
    public static final String INTERVIEW_FEEDBACK_3         = "INTERVIEW_FEEDBACK_3";
    public static final String INTERVIEW_FEEDBACK_4         = "INTERVIEW_FEEDBACK_4";
    public static final String INTERVIEW_FEEDBACK_5         = "INTERVIEW_FEEDBACK_5";
    public static final String INTERVIEW_FEEDBACK_6         = "INTERVIEW_FEEDBACK_6";

    public static final Integer INTERVIEW_FEEDBACK_COUNT    = 7;

    public static final String PROBATION_FEEDBACK_PREFIX    = "PROBATION_FEEDBACK";
    public static final String PROBATION_FEEDBACK_0         = "PROBATION_FEEDBACK_0";
    public static final String PROBATION_FEEDBACK_1         = "PROBATION_FEEDBACK_1";
    public static final String PROBATION_FEEDBACK_2         = "PROBATION_FEEDBACK_2";

    public static final Integer PROBATION_FEEDBACK_COUNT    = 3;

    @Column(name="RESUME_FILE_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String resumeFileName;

    @Column(name="RESUME_FILE_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String resumeFileHash;

    @Column(name="RESUME_FILE_MIME_TYPE", nullable = true)
    private String resumeFileMimeType;

    @Column(name="RESUME_FILE_LENGTH", nullable = true)
    private Long resumeFileLength;




    @Column(name="TEST_TASK_FILE_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String testtaskFileName;

    @Column(name="TEST_TASK_FILE_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String testtaskFileHash;

    @Column(name="TEST_TASK_FILE_MIME_TYPE", nullable = true)
    private String testtaskFileMimeType;

    @Column(name="TEST_TASK_FILE_LENGTH", nullable = true)
    private Long testtaskFileLength;




    @Column(name="INTERVIEW_FEEDBACK_FILE_0_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String interviewFeedbackFile0Name;

    @Column(name="INTERVIEW_FEEDBACK_FILE_0_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String interviewFeedbackFile0Hash;

    @Column(name="INTERVIEW_FEEDBACK_FILE_0_MIME_TYPE", nullable = true)
    private String interviewFeedbackFile0MimeType;

    @Column(name="INTERVIEW_FEEDBACK_FILE_0_LENGTH", nullable = true)
    private Long interviewFeedbackFile0Length;

    @Column(name="INTERVIEW_FEEDBACK_FILE_1_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String interviewFeedbackFile1Name;

    @Column(name="INTERVIEW_FEEDBACK_FILE_1_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String interviewFeedbackFile1Hash;

    @Column(name="INTERVIEW_FEEDBACK_FILE_1_MIME_TYPE", nullable = true)
    private String interviewFeedbackFile1MimeType;

    @Column(name="INTERVIEW_FEEDBACK_FILE_1_LENGTH", nullable = true)
    private Long interviewFeedbackFile1Length;

    @Column(name="INTERVIEW_FEEDBACK_FILE_2_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String interviewFeedbackFile2Name;

    @Column(name="INTERVIEW_FEEDBACK_FILE_2_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String interviewFeedbackFile2Hash;

    @Column(name="INTERVIEW_FEEDBACK_FILE_2_MIME_TYPE", nullable = true)
    private String interviewFeedbackFile2MimeType;

    @Column(name="INTERVIEW_FEEDBACK_FILE_2_LENGTH", nullable = true)
    private Long interviewFeedbackFile2Length;

    @Column(name="INTERVIEW_FEEDBACK_FILE_3_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String interviewFeedbackFile3Name;

    @Column(name="INTERVIEW_FEEDBACK_FILE_3_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String interviewFeedbackFile3Hash;

    @Column(name="INTERVIEW_FEEDBACK_FILE_3_MIME_TYPE", nullable = true)
    private String interviewFeedbackFile3MimeType;

    @Column(name="INTERVIEW_FEEDBACK_FILE_3_LENGTH", nullable = true)
    private Long interviewFeedbackFile3Length;

    @Column(name="INTERVIEW_FEEDBACK_FILE_4_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String interviewFeedbackFile4Name;

    @Column(name="INTERVIEW_FEEDBACK_FILE_4_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String interviewFeedbackFile4Hash;

    @Column(name="INTERVIEW_FEEDBACK_FILE_4_MIME_TYPE", nullable = true)
    private String interviewFeedbackFile4MimeType;

    @Column(name="INTERVIEW_FEEDBACK_FILE_4_LENGTH", nullable = true)
    private Long interviewFeedbackFile4Length;

    @Column(name="INTERVIEW_FEEDBACK_FILE_5_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String interviewFeedbackFile5Name;

    @Column(name="INTERVIEW_FEEDBACK_FILE_5_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String interviewFeedbackFile5Hash;

    @Column(name="INTERVIEW_FEEDBACK_FILE_5_MIME_TYPE", nullable = true)
    private String interviewFeedbackFile5MimeType;

    @Column(name="INTERVIEW_FEEDBACK_FILE_5_LENGTH", nullable = true)
    private Long interviewFeedbackFile5Length;

    @Column(name="INTERVIEW_FEEDBACK_FILE_6_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String interviewFeedbackFile6Name;

    @Column(name="INTERVIEW_FEEDBACK_FILE_6_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String interviewFeedbackFile6Hash;

    @Column(name="INTERVIEW_FEEDBACK_FILE_6_MIME_TYPE", nullable = true)
    private String interviewFeedbackFile6MimeType;

    @Column(name="INTERVIEW_FEEDBACK_FILE_6_LENGTH", nullable = true)
    private Long interviewFeedbackFile6Length;

    @Column(name="PROBATION_FEEDBACK_FILE_0_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String probationFeedbackFile0Name;

    @Column(name="PROBATION_FEEDBACK_FILE_0_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String probationFeedbackFile0Hash;

    @Column(name="PROBATION_FEEDBACK_FILE_0_MIME_TYPE", nullable = true)
    private String probationFeedbackFile0MimeType;

    @Column(name="PROBATION_FEEDBACK_FILE_0_LENGTH", nullable = true)
    private Long probationFeedbackFile0Length;

    @Column(name="PROBATION_FEEDBACK_FILE_1_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String probationFeedbackFile1Name;

    @Column(name="PROBATION_FEEDBACK_FILE_1_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String probationFeedbackFile1Hash;

    @Column(name="PROBATION_FEEDBACK_FILE_1_MIME_TYPE", nullable = true)
    private String probationFeedbackFile1MimeType;

    @Column(name="PROBATION_FEEDBACK_FILE_1_LENGTH", nullable = true)
    private Long probationFeedbackFile1Length;

    @Column(name="PROBATION_FEEDBACK_FILE_2_NAME", nullable = true,
            length = ModelConstants.LONG_TEXT_LIMIT)
    private String probationFeedbackFile2Name;

    @Column(name="PROBATION_FEEDBACK_FILE_2_HASH", nullable = true, columnDefinition = "CHAR(64)",
            length = ModelConstants.SHA256_TEXT_LIMIT)
    private String probationFeedbackFile2Hash;

    @Column(name="PROBATION_FEEDBACK_FILE_2_MIME_TYPE", nullable = true)
    private String probationFeedbackFile2MimeType;

    @Column(name="PROBATION_FEEDBACK_FILE_2_LENGTH", nullable = true)
    private Long probationFeedbackFile2Length;




    public String getFileName(String code) {
        if (RESUME.equals(code)) {
            return this.resumeFileName;
        } else if (TEST_TASK.equals(code)) {
            return this.testtaskFileName;
        } else if (INTERVIEW_FEEDBACK_0.equals(code)) {
            return this.interviewFeedbackFile0Name;
        } else if (INTERVIEW_FEEDBACK_1.equals(code)) {
            return this.interviewFeedbackFile1Name;
        } else if (INTERVIEW_FEEDBACK_2.equals(code)) {
            return this.interviewFeedbackFile2Name;
        } else if (INTERVIEW_FEEDBACK_3.equals(code)) {
            return this.interviewFeedbackFile3Name;
        } else if (INTERVIEW_FEEDBACK_4.equals(code)) {
            return this.interviewFeedbackFile4Name;
        } else if (INTERVIEW_FEEDBACK_5.equals(code)) {
            return this.interviewFeedbackFile5Name;
        } else if (INTERVIEW_FEEDBACK_6.equals(code)) {
            return this.interviewFeedbackFile6Name;
        } else if (PROBATION_FEEDBACK_0.equals(code)) {
            return this.probationFeedbackFile0Name;
        } else if (PROBATION_FEEDBACK_1.equals(code)) {
            return this.probationFeedbackFile1Name;
        } else if (PROBATION_FEEDBACK_2.equals(code)) {
            return this.probationFeedbackFile2Name;
        }
        return null;
    }

    public void setFileName(String code, String fileName) {
        if (RESUME.equals(code)) {
            this.resumeFileName = fileName;
        } else if (TEST_TASK.equals(code)) {
            this.testtaskFileName = fileName;
        } else if (INTERVIEW_FEEDBACK_0.equals(code)) {
            this.interviewFeedbackFile0Name = fileName;
        } else if (INTERVIEW_FEEDBACK_1.equals(code)) {
            this.interviewFeedbackFile1Name = fileName;
        } else if (INTERVIEW_FEEDBACK_2.equals(code)) {
            this.interviewFeedbackFile2Name = fileName;
        } else if (INTERVIEW_FEEDBACK_3.equals(code)) {
            this.interviewFeedbackFile3Name = fileName;
        } else if (INTERVIEW_FEEDBACK_4.equals(code)) {
            this.interviewFeedbackFile4Name = fileName;
        } else if (INTERVIEW_FEEDBACK_5.equals(code)) {
            this.interviewFeedbackFile5Name = fileName;
        } else if (INTERVIEW_FEEDBACK_6.equals(code)) {
            this.interviewFeedbackFile6Name = fileName;
        } else if (PROBATION_FEEDBACK_0.equals(code)) {
            this.probationFeedbackFile0Name = fileName;
        } else if (PROBATION_FEEDBACK_1.equals(code)) {
            this.probationFeedbackFile1Name = fileName;
        } else if (PROBATION_FEEDBACK_2.equals(code)) {
            this.probationFeedbackFile2Name = fileName;
        }
    }

    public String getFileHash(String code) {
        if (RESUME.equals(code)) {
            return this.resumeFileHash;
        } else if (TEST_TASK.equals(code)) {
            return this.testtaskFileHash;
        } else if (INTERVIEW_FEEDBACK_0.equals(code)) {
            return this.interviewFeedbackFile0Hash;
        } else if (INTERVIEW_FEEDBACK_1.equals(code)) {
            return this.interviewFeedbackFile1Hash;
        } else if (INTERVIEW_FEEDBACK_2.equals(code)) {
            return this.interviewFeedbackFile2Hash;
        } else if (INTERVIEW_FEEDBACK_3.equals(code)) {
            return this.interviewFeedbackFile3Hash;
        } else if (INTERVIEW_FEEDBACK_4.equals(code)) {
            return this.interviewFeedbackFile4Hash;
        } else if (INTERVIEW_FEEDBACK_5.equals(code)) {
            return this.interviewFeedbackFile5Hash;
        } else if (INTERVIEW_FEEDBACK_6.equals(code)) {
            return this.interviewFeedbackFile6Hash;
        } else if (PROBATION_FEEDBACK_0.equals(code)) {
            return this.probationFeedbackFile0Hash;
        } else if (PROBATION_FEEDBACK_1.equals(code)) {
            return this.probationFeedbackFile1Hash;
        } else if (PROBATION_FEEDBACK_2.equals(code)) {
            return this.probationFeedbackFile2Hash;
        }
        return null;
    }

    public void setFileHash(String code, String fileHash) {
        if (RESUME.equals(code)) {
            this.resumeFileHash = fileHash;
        } else if (TEST_TASK.equals(code)) {
            this.testtaskFileHash = fileHash;
        } else if (INTERVIEW_FEEDBACK_0.equals(code)) {
            this.interviewFeedbackFile0Hash = fileHash;
        } else if (INTERVIEW_FEEDBACK_1.equals(code)) {
            this.interviewFeedbackFile1Hash = fileHash;
        } else if (INTERVIEW_FEEDBACK_2.equals(code)) {
            this.interviewFeedbackFile2Hash = fileHash;
        } else if (INTERVIEW_FEEDBACK_3.equals(code)) {
            this.interviewFeedbackFile3Hash = fileHash;
        } else if (INTERVIEW_FEEDBACK_4.equals(code)) {
            this.interviewFeedbackFile4Hash = fileHash;
        } else if (INTERVIEW_FEEDBACK_5.equals(code)) {
            this.interviewFeedbackFile5Hash = fileHash;
        } else if (INTERVIEW_FEEDBACK_6.equals(code)) {
            this.interviewFeedbackFile6Hash = fileHash;
        } else if (PROBATION_FEEDBACK_0.equals(code)) {
            this.probationFeedbackFile0Hash = fileHash;
        } else if (PROBATION_FEEDBACK_1.equals(code)) {
            this.probationFeedbackFile1Hash = fileHash;
        } else if (PROBATION_FEEDBACK_2.equals(code)) {
            this.probationFeedbackFile2Hash = fileHash;
        }
    }

    public Long getFileLength(String code) {
        if (RESUME.equals(code)) {
            return this.resumeFileLength;
        } else if (TEST_TASK.equals(code)) {
            return this.testtaskFileLength;
        } else if (INTERVIEW_FEEDBACK_0.equals(code)) {
            return this.interviewFeedbackFile0Length;
        } else if (INTERVIEW_FEEDBACK_1.equals(code)) {
            return this.interviewFeedbackFile1Length;
        } else if (INTERVIEW_FEEDBACK_2.equals(code)) {
            return this.interviewFeedbackFile2Length;
        } else if (INTERVIEW_FEEDBACK_3.equals(code)) {
            return this.interviewFeedbackFile3Length;
        } else if (INTERVIEW_FEEDBACK_4.equals(code)) {
            return this.interviewFeedbackFile4Length;
        } else if (INTERVIEW_FEEDBACK_5.equals(code)) {
            return this.interviewFeedbackFile5Length;
        } else if (INTERVIEW_FEEDBACK_6.equals(code)) {
            return this.interviewFeedbackFile6Length;
        } else if (PROBATION_FEEDBACK_0.equals(code)) {
            return this.probationFeedbackFile0Length;
        } else if (PROBATION_FEEDBACK_1.equals(code)) {
            return this.probationFeedbackFile1Length;
        } else if (PROBATION_FEEDBACK_2.equals(code)) {
            return this.probationFeedbackFile2Length;
        }
        return null;
    }

    public void setFileLength(String code, Long fileLength) {
        if (RESUME.equals(code)) {
            this.resumeFileLength = fileLength;
        } else if (TEST_TASK.equals(code)) {
            this.testtaskFileLength = fileLength;
        } else if (INTERVIEW_FEEDBACK_0.equals(code)) {
            this.interviewFeedbackFile0Length = fileLength;
        } else if (INTERVIEW_FEEDBACK_1.equals(code)) {
            this.interviewFeedbackFile1Length = fileLength;
        } else if (INTERVIEW_FEEDBACK_2.equals(code)) {
            this.interviewFeedbackFile2Length = fileLength;
        } else if (INTERVIEW_FEEDBACK_3.equals(code)) {
            this.interviewFeedbackFile3Length = fileLength;
        } else if (INTERVIEW_FEEDBACK_4.equals(code)) {
            this.interviewFeedbackFile4Length = fileLength;
        } else if (INTERVIEW_FEEDBACK_5.equals(code)) {
            this.interviewFeedbackFile5Length = fileLength;
        } else if (INTERVIEW_FEEDBACK_6.equals(code)) {
            this.interviewFeedbackFile6Length = fileLength;
        } else if (PROBATION_FEEDBACK_0.equals(code)) {
            this.probationFeedbackFile0Length = fileLength;
        } else if (PROBATION_FEEDBACK_1.equals(code)) {
            this.probationFeedbackFile1Length = fileLength;
        } else if (PROBATION_FEEDBACK_2.equals(code)) {
            this.probationFeedbackFile2Length = fileLength;
        }
    }

    public String getFileMimeType(String code) {
        if (RESUME.equals(code)) {
            return this.resumeFileMimeType;
        } else if (TEST_TASK.equals(code)) {
            return this.testtaskFileMimeType;
        } else if (INTERVIEW_FEEDBACK_0.equals(code)) {
            return this.interviewFeedbackFile0MimeType;
        } else if (INTERVIEW_FEEDBACK_1.equals(code)) {
            return this.interviewFeedbackFile1MimeType;
        } else if (INTERVIEW_FEEDBACK_2.equals(code)) {
            return this.interviewFeedbackFile2MimeType;
        } else if (INTERVIEW_FEEDBACK_3.equals(code)) {
            return this.interviewFeedbackFile3MimeType;
        } else if (INTERVIEW_FEEDBACK_4.equals(code)) {
            return this.interviewFeedbackFile4MimeType;
        } else if (INTERVIEW_FEEDBACK_5.equals(code)) {
            return this.interviewFeedbackFile5MimeType;
        } else if (INTERVIEW_FEEDBACK_6.equals(code)) {
            return this.interviewFeedbackFile6MimeType;
        } else if (PROBATION_FEEDBACK_0.equals(code)) {
            return this.probationFeedbackFile0MimeType;
        } else if (PROBATION_FEEDBACK_1.equals(code)) {
            return this.probationFeedbackFile1MimeType;
        } else if (PROBATION_FEEDBACK_2.equals(code)) {
            return this.probationFeedbackFile2MimeType;
        }
        return null;
    }

    public void setFileMimeType(String code, String fileMimeType) {
        if (RESUME.equals(code)) {
            this.resumeFileMimeType = fileMimeType;
        } else if (TEST_TASK.equals(code)) {
            this.testtaskFileMimeType = fileMimeType;
        } else if (INTERVIEW_FEEDBACK_0.equals(code)) {
            this.interviewFeedbackFile0MimeType = fileMimeType;
        } else if (INTERVIEW_FEEDBACK_1.equals(code)) {
            this.interviewFeedbackFile1MimeType = fileMimeType;
        } else if (INTERVIEW_FEEDBACK_2.equals(code)) {
            this.interviewFeedbackFile2MimeType = fileMimeType;
        } else if (INTERVIEW_FEEDBACK_3.equals(code)) {
            this.interviewFeedbackFile3MimeType = fileMimeType;
        } else if (INTERVIEW_FEEDBACK_4.equals(code)) {
            this.interviewFeedbackFile4MimeType = fileMimeType;
        } else if (INTERVIEW_FEEDBACK_5.equals(code)) {
            this.interviewFeedbackFile5MimeType = fileMimeType;
        } else if (INTERVIEW_FEEDBACK_6.equals(code)) {
            this.interviewFeedbackFile6MimeType = fileMimeType;
        } else if (PROBATION_FEEDBACK_0.equals(code)) {
            this.probationFeedbackFile0MimeType = fileMimeType;
        } else if (PROBATION_FEEDBACK_1.equals(code)) {
            this.probationFeedbackFile1MimeType = fileMimeType;
        } else if (PROBATION_FEEDBACK_2.equals(code)) {
            this.probationFeedbackFile2MimeType = fileMimeType;
        }
    }

}
