package org.profwell.collaboration.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.profwell.collaboration.domain.PartnershipRequestDTO;
import org.profwell.collaboration.model.CollaborationRequest;
import org.profwell.collaboration.model.CollaborationRequestType;
import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.security.model.User;

public class CollaborationRequestDAOImpl extends GenericDAOImpl<CollaborationRequest>
        implements CollaborationRequestDAO {

    @Override
    public List<PartnershipRequestDTO> getMyPartnerRequests(Long workspaceId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<CollaborationRequest> request = criteria.from(getEntityClass());
        Join<CollaborationRequest, User> owner = request.join("owner");
        Join<CollaborationRequest, User> partner = request.join("partner");

        criterions.add(cb.equal(owner.<Long>get("id"), workspaceId));

        criteria.multiselect(
                request.get("id").alias("requestId"),
                partner.get("id").alias("partnerId"),
                partner.get("uuid").alias("partnerUuid"),
                partner.get("firstName").alias("partnerFirstName"),
                partner.get("lastName").alias("partnerLastName"),
                request.get("type").alias("partnershipType"));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        TypedQuery<Tuple> resultQuery = this.getEM().createQuery(criteria);

        List<PartnershipRequestDTO> result = new ArrayList<>();

        for (Tuple tuple : resultQuery.getResultList()) {
            PartnershipRequestDTO dto = new PartnershipRequestDTO();
            dto.setRecordId(           (Long)                          tuple.get("requestId"));
            dto.setPartnerId(          (Long)                          tuple.get("partnerId"));
            dto.setPartnerUuid(        (String)                        tuple.get("partnerUuid"));
            dto.setPartnerFirstName(   (String)                        tuple.get("partnerFirstName"));
            dto.setPartnerLastName(    (String)                        tuple.get("partnerLastName"));
            dto.setType(               (CollaborationRequestType)      tuple.get("partnershipType"));
            result.add(dto);
        }

        return result;
    }

    @Override
    public List<PartnershipRequestDTO> getRequestsToMe(Long workspaceId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<CollaborationRequest> request = criteria.from(getEntityClass());
        Join<CollaborationRequest, User> owner = request.join("owner");
        Join<CollaborationRequest, User> iam = request.join("partner");

        criterions.add(cb.equal(iam.<Long>get("id"), workspaceId));

        criteria.multiselect(
                request.get("id").alias("requestId"),
                owner.get("id").alias("partnerId"),
                owner.get("uuid").alias("partnerUuid"),
                owner.get("firstName").alias("partnerFirstName"),
                owner.get("lastName").alias("partnerLastName"),
                request.get("type").alias("partnershipType"));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        TypedQuery<Tuple> resultQuery = this.getEM().createQuery(criteria);

        List<PartnershipRequestDTO> result = new ArrayList<>();

        for (Tuple tuple : resultQuery.getResultList()) {
            PartnershipRequestDTO dto = new PartnershipRequestDTO();
            dto.setRecordId(           (Long)                          tuple.get("requestId"));
            dto.setPartnerId(          (Long)                          tuple.get("partnerId"));
            dto.setPartnerUuid(        (String)                        tuple.get("partnerUuid"));
            dto.setPartnerFirstName(   (String)                        tuple.get("partnerFirstName"));
            dto.setPartnerLastName(    (String)                        tuple.get("partnerLastName"));
            dto.setType(               (CollaborationRequestType)      tuple.get("partnershipType"));
            result.add(dto);
        }

        return result;
    }

    @Override
    public boolean checkCollaborationRequest(Long userId, Long partnerId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<CollaborationRequest> request = criteria.from(getEntityClass());
        Join<CollaborationRequest, User> owner = request.join("owner");
        Join<CollaborationRequest, User> partner = request.join("partner");

        criterions.add(cb.equal(owner.<Long>get("id"), userId));
        criterions.add(cb.equal(partner.<Long>get("id"), partnerId));

        criteria.multiselect(request.get("id"));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        return this.getEM().createQuery(criteria).getResultList().size() > 0;
    }

    @Override
    protected Class<CollaborationRequest> getEntityClass() {
        return CollaborationRequest.class;
    }

}
