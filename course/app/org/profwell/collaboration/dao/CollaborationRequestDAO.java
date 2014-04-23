package org.profwell.collaboration.dao;

import java.util.List;

import org.profwell.collaboration.domain.PartnershipRequestDTO;
import org.profwell.collaboration.model.CollaborationRequest;
import org.profwell.generic.dao.GenericDAO;

public interface CollaborationRequestDAO extends GenericDAO<CollaborationRequest> {

    List<PartnershipRequestDTO> getMyPartnerRequests(Long workspaceId);

    List<PartnershipRequestDTO> getRequestsToMe(Long workspaceId);

    boolean checkCollaborationRequest(Long userId, Long partnerId);
}
