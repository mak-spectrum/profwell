package org.profwell.collaboration.service;

import java.util.List;

import org.profwell.collaboration.dao.CollaborationAgreementDAO;
import org.profwell.collaboration.dao.CollaborationRequestDAO;
import org.profwell.collaboration.domain.PartnerDTO;
import org.profwell.collaboration.domain.PartnershipRequestDTO;
import org.profwell.collaboration.model.CollaborationAgreement;
import org.profwell.collaboration.model.CollaborationRequest;
import org.profwell.collaboration.model.ConnectionType;

public class CollaborationServiceImpl implements CollaborationService {

    private CollaborationAgreementDAO agreementDAO;

    private CollaborationRequestDAO requestDAO;

    @Override
    public List<PartnerDTO> getMyPartners(Long workspaceId) {
        return agreementDAO.getMyPartners(workspaceId);
    }

    @Override
    public List<PartnerDTO> getWhereIisPartner(Long workspaceId) {
        return agreementDAO.getWhereIisPartner(workspaceId);
    }

    @Override
    public List<PartnershipRequestDTO> getMyPartnershipRequests(Long workspaceId) {
        return requestDAO.getMyPartnerRequests(workspaceId);
    }

    @Override
    public List<PartnershipRequestDTO> getPartnershipRequestsToMe(Long workspaceId) {
        return requestDAO.getRequestsToMe(workspaceId);
    }

    @Override
    public CollaborationAgreement getCollaborationAgreement(Long agreementId) {
        return agreementDAO.get(agreementId);
    }

    @Override
    public CollaborationAgreement getCollaborationAgreement(Long firstPartnerId, Long secondPartnerId) {
        return agreementDAO.getCollaborationAgreement(firstPartnerId, secondPartnerId);
    }

    @Override
    public CollaborationRequest getCollaborationRequest(Long requestId) {
        return requestDAO.get(requestId);
    }

    @Override
    public void deleteCollaborationAgreement(CollaborationAgreement agreement) {
        agreementDAO.delete(agreement);
    }

    @Override
    public void deleteCollaborationRequest(CollaborationRequest request) {
        requestDAO.delete(request);
    }

    @Override
    public void saveCollaborationRequest(CollaborationRequest request) {
        requestDAO.save(request);
    }

    @Override
    public void saveCollaborationAgreement(CollaborationAgreement agreement) {
        agreementDAO.save(agreement);
    }

    @Override
    public boolean checkCollaborationAgreement(Long ownerId, Long partnerId, ConnectionType type) {
        return agreementDAO.checkCollaborationAgreement(ownerId, partnerId, type);
    }

    @Override
    public boolean checkCollaborationRequest(Long userId, Long partnerId) {
        return requestDAO.checkCollaborationRequest(userId, partnerId);
    }

    public void setRecordDao(CollaborationAgreementDAO recordDAO) {
        this.agreementDAO = recordDAO;
    }

    public void setRequestDao(CollaborationRequestDAO requestDAO) {
        this.requestDAO = requestDAO;
    }

}
