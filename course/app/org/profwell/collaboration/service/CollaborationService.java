package org.profwell.collaboration.service;

import java.util.List;

import org.profwell.collaboration.domain.PartnerDTO;
import org.profwell.collaboration.domain.PartnershipRequestDTO;
import org.profwell.collaboration.model.CollaborationAgreement;
import org.profwell.collaboration.model.CollaborationRequest;

public interface CollaborationService {

    CollaborationAgreement getCollaborationAgreement(Long agreementId);

    CollaborationRequest getCollaborationRequest(Long requestId);

    void deleteCollaborationAgreement(CollaborationAgreement agreement);

    void deleteCollaborationRequest(CollaborationRequest request);

    List<PartnerDTO> getMyPartners(Long workspaceId);

    List<PartnerDTO> getWhereIisPartner(Long workspaceId);

    List<PartnershipRequestDTO> getMyPartnershipRequests(Long workspaceId);

    List<PartnershipRequestDTO> getPartnershipRequestsToMe(Long workspaceId);

    void saveCollaborationAgreement(CollaborationAgreement agreement);

    void saveCollaborationRequest(CollaborationRequest request);

    boolean checkCollaborationAgreement(Long userId, Long partnerId);

    boolean checkCollaborationRequest(Long userId, Long partnerId);
}
