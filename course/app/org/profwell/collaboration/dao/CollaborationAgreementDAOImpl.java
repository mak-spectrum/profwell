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

import org.profwell.collaboration.domain.PartnerDTO;
import org.profwell.collaboration.model.CollaborationAgreement;
import org.profwell.collaboration.model.ConnectionType;
import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.security.model.User;

public class CollaborationAgreementDAOImpl extends GenericDAOImpl<CollaborationAgreement>
        implements CollaborationAgreementDAO {

    @Override
    public List<PartnerDTO> getMyPartners(Long workspaceId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<CollaborationAgreement> agreement = criteria.from(getEntityClass());
        Join<CollaborationAgreement, User> owner = agreement.join("owner");
        Join<CollaborationAgreement, User> partner = agreement.join("partner");

        criterions.add(cb.equal(owner.<Long>get("id"), workspaceId));

        criteria.multiselect(
                agreement.get("id").alias("recordId"),
                partner.get("id").alias("partnerId"),
                partner.get("uuid").alias("partnerUuid"),
                partner.get("firstName").alias("partnerFirstName"),
                partner.get("lastName").alias("partnerLastName"),
                agreement.get("type").alias("partnershipType"));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        TypedQuery<Tuple> resultQuery = this.getEM().createQuery(criteria);

        List<PartnerDTO> result = new ArrayList<>();

        for (Tuple tuple : resultQuery.getResultList()) {
            PartnerDTO pdto = new PartnerDTO();
            pdto.setRecordId(           (Long)              tuple.get("recordId"));
            pdto.setPartnerId(          (Long)              tuple.get("partnerId"));
            pdto.setPartnerUuid(        (String)            tuple.get("partnerUuid"));
            pdto.setPartnerFirstName(   (String)            tuple.get("partnerFirstName"));
            pdto.setPartnerLastName(    (String)            tuple.get("partnerLastName"));
            pdto.setType(               (ConnectionType)    tuple.get("partnershipType"));
            pdto.setMy(true);
            result.add(pdto);
        }

        return result;
    }

    @Override
    public List<PartnerDTO> getWhereIisPartner(Long workspaceId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<CollaborationAgreement> record = criteria.from(getEntityClass());
        Join<CollaborationAgreement, User> owner = record.join("owner");
        Join<CollaborationAgreement, User> iam = record.join("partner");

        criterions.add(cb.equal(iam.<Long>get("id"), workspaceId));

        criteria.multiselect(
                record.get("id").alias("recordId"),
                owner.get("id").alias("partnerId"),
                owner.get("uuid").alias("partnerUuid"),
                owner.get("firstName").alias("partnerFirstName"),
                owner.get("lastName").alias("partnerLastName"),
                record.get("type").alias("partnershipType"));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        TypedQuery<Tuple> resultQuery = this.getEM().createQuery(criteria);

        List<PartnerDTO> result = new ArrayList<>();

        for (Tuple tuple : resultQuery.getResultList()) {
            PartnerDTO pdto = new PartnerDTO();
            pdto.setRecordId(           (Long)              tuple.get("recordId"));
            pdto.setPartnerId(          (Long)              tuple.get("partnerId"));
            pdto.setPartnerUuid(        (String)            tuple.get("partnerUuid"));
            pdto.setPartnerFirstName(   (String)            tuple.get("partnerFirstName"));
            pdto.setPartnerLastName(    (String)            tuple.get("partnerLastName"));
            pdto.setType(               (ConnectionType)    tuple.get("partnershipType"));
            pdto.setMy(false);
            result.add(pdto);
        }

        return result;
    }

    @Override
    public boolean checkCollaborationAgreement(Long userId, Long partnerId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<CollaborationAgreement> agreement = criteria.from(getEntityClass());
        Join<CollaborationAgreement, User> owner = agreement.join("owner");
        Join<CollaborationAgreement, User> partner = agreement.join("partner");

        criterions.add(cb.equal(owner.<Long>get("id"), userId));
        criterions.add(cb.equal(partner.<Long>get("id"), partnerId));

        criteria.multiselect(agreement.get("id"));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        return this.getEM().createQuery(criteria).getResultList().size() > 0;
    }

    @Override
    protected Class<CollaborationAgreement> getEntityClass() {
        return CollaborationAgreement.class;
    }

}
