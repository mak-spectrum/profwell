package org.profwell.collaboration.domain;

import org.profwell.collaboration.model.CollaborationRequestType;

public class PartnershipRequestDTO {

    private Long recordId;

    private String partnerUuid;

    private String partnerFirstName;

    private String partnerLastName;

    private String partnerFullName;

    private Long partnerId;

    private CollaborationRequestType type;

    public String getPartnerFullName() {
        if (this.partnerFullName == null) {
            this.partnerFullName = this.partnerFirstName + " " + this.partnerLastName;
        }

        return this.partnerFullName;
    }

    public String getPartnershipDescription() {
        return this.type.getCaption();
    }

    public String getPartnerFirstName() {
        return partnerFirstName;
    }

    public void setPartnerFirstName(String partnerFirstName) {
        this.partnerFirstName = partnerFirstName;
    }

    public String getPartnerLastName() {
        return partnerLastName;
    }

    public void setPartnerLastName(String partnerLastName) {
        this.partnerLastName = partnerLastName;
    }

    public void setType(CollaborationRequestType type) {
        this.type = type;
    }

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerUuid() {
        return partnerUuid;
    }

    public void setPartnerUuid(String partnerUuid) {
        this.partnerUuid = partnerUuid;
    }

}
