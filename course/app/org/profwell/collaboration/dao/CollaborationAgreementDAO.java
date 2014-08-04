package org.profwell.collaboration.dao;

import java.util.List;

import org.profwell.collaboration.domain.PartnerDTO;
import org.profwell.collaboration.model.CollaborationAgreement;
import org.profwell.collaboration.model.ConnectionType;
import org.profwell.generic.dao.GenericDAO;

public interface CollaborationAgreementDAO extends GenericDAO<CollaborationAgreement> {

    List<PartnerDTO> getMyPartners(Long workspaceId);

    List<PartnerDTO> getWhereIisPartner(Long workspaceId);

    boolean checkCollaborationAgreement(Long ownerId, Long partnerId, ConnectionType type);

    CollaborationAgreement getCollaborationAgreement(Long firstPartnerId, Long secondPartnerId);

}
