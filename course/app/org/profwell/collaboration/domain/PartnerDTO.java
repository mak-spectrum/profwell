package org.profwell.collaboration.domain;

import org.profwell.collaboration.model.ConnectionType;

public class PartnerDTO {

    private Long recordId;

    private String partnerUuid;

    private String partnerFirstName;

    private String partnerLastName;

    private String partnerFullName;

    private Long partnerId;

    private boolean my;

    private ConnectionType type;

    public String getPartnerFullName() {
        if (this.partnerFullName == null) {
            this.partnerFullName = this.partnerFirstName + " " + this.partnerLastName;
        }

        return this.partnerFullName;
    }

    public String getPartnershipDescriptionPostfix() {
        if (this.type == ConnectionType.COMPANION) {
            return "";
        } else {
            if (this.my) {
                return "";
            } else {
                if (this.type == ConnectionType.STAFF_RECRUITER) {
                    return "(You are an " + this.type.getCaption() + ")";
                } else {
                    return "(You are a " + this.type.getCaption() + ")";
                }
            }
        }
    }

    public String getPartnershipDescription() {
        if (this.my) {
            return this.type.getCaption();
        } else {
            return this.type.getOppositeCaption();
        }
    }

    public String getPartnerFirstName() {
        return this.partnerFirstName;
    }

    public void setPartnerFirstName(String partnerFirstName) {
        this.partnerFirstName = partnerFirstName;
    }

    public String getPartnerLastName() {
        return this.partnerLastName;
    }

    public void setPartnerLastName(String partnerLastName) {
        this.partnerLastName = partnerLastName;
    }

    public void setType(ConnectionType type) {
        this.type = type;
    }

    public void setMy(boolean my) {
        this.my = my;
    }

    public Long getRecordId() {
        return this.recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    public Long getPartnerId() {
        return this.partnerId;
    }

    public void setPartnerId(Long partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerUuid() {
        return this.partnerUuid;
    }

    public void setPartnerUuid(String partnerUuid) {
        this.partnerUuid = partnerUuid;
    }

    public ConnectionType getType() {
        return this.type;
    }

    public boolean isMy() {
        return this.my;
    }

}
