package org.profwell.vacancy.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.apache.commons.lang3.StringUtils;
import org.profwell.collaboration.model.CollaborationAgreement;
import org.profwell.collaboration.model.ConnectionType;
import org.profwell.common.model.Country;
import org.profwell.generic.dao.GenericDAOImpl;
import org.profwell.security.model.User;
import org.profwell.security.model.Workspace;
import org.profwell.vacancy.auxiliary.VacancyFilter;
import org.profwell.vacancy.domain.HookupDTO;
import org.profwell.vacancy.domain.HookupDocumentDTO;
import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.HookupStatus;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancyStatus;

public class VacancyDAOImpl extends GenericDAOImpl<Vacancy> implements VacancyDAO {

    @Override
    public List<Vacancy> listVacancies(VacancyFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();

        CriteriaQuery<Vacancy> criteria = cb.createQuery(this.getEntityClass());
        Root<Vacancy> root = criteria.from(this.getEntityClass());

        criteria.where(
                //cb.or(
                        cb.in(root).value(this.listOwnVacancies(criteria, filter)));
                        //cb.in(root).value(this.listSharedVacancies(criteria, filter))
                        //));

        return this.listPage(criteria, filter);
    }

    private Subquery<Vacancy> listOwnVacancies(CriteriaQuery<Vacancy> criteriaQuery, VacancyFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        Subquery<Vacancy> criteria = criteriaQuery.subquery(this.getEntityClass());
        Root<Vacancy> root = criteria.from(this.getEntityClass());

        Join<Vacancy, Workspace> workspace = root.join("workspace");

        criteria.select(root);

        this.applyVacancyCommonFiltering(filter, cb, criterions, root);

        criterions.add(cb.equal(workspace.<Long>get("id"),
                filter.getWorkspaceId()));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        return criteria;
    }

    private Subquery<Vacancy> listSharedVacancies(CriteriaQuery<Vacancy> criteriaQuery, VacancyFilter filter) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        Subquery<Vacancy> criteria = criteriaQuery.subquery(this.getEntityClass());
        Root<Vacancy> root = criteria.from(this.getEntityClass());

        Join<Vacancy, Workspace> workspace = root.join("workspace");
        Join<Workspace, User> user = workspace.join("owner");

        criteria.select(root);

        this.applyVacancyCommonFiltering(filter, cb, criterions, root);

        criterions.add(cb.in(user).value(
                this.getSharedVacanciesOwnerSubquery(criteria, filter.getWorkspaceId())));

        criterions.add(cb.equal(workspace.<Long>get("id"),
                filter.getWorkspaceId()));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        return criteria;
    }

    private void applyVacancyCommonFiltering(VacancyFilter filter,
            CriteriaBuilder cb, List<Predicate> criterions, Root<Vacancy> root) {
        if (filter.getAssigneeId() != null) {
            Join<Vacancy, User> user = root.join("assignee");
            criterions.add(cb.equal(user.<Long>get("id"),
                    filter.getAssigneeId()));
        }

        if (StringUtils.isNotBlank(filter.getPosition())) {
            criterions.add(cb.like(root.get("position").<String>get("name"),
                    filter.getPosition() + "%"));
        }

        if (StringUtils.isNotBlank(filter.getProject())) {
            criterions.add(cb.like(root.get("position").<String>get("projectName"),
                    filter.getProject() + "%"));
        }

        if (StringUtils.isNotBlank(filter.getCompany())) {
            criterions.add(cb.like(root.get("company").<String>get("name"),
                    filter.getCompany() + "%"));
        }

        if (filter.getCountry() != null) {
            criterions.add(cb.equal(root.<Country>get("country"),
                    filter.getCountry()));
        }

        if (StringUtils.isNotBlank(filter.getCity())) {
            criterions.add(cb.like(root.<String>get("city"),
                    filter.getCity() + "%"));
        }

        if (filter.getStatus() != null) {
            criterions.add(cb.equal(root.<VacancyStatus>get("status"),
                    filter.getStatus()));
        }
    }

    private Subquery<User> getSharedVacanciesOwnerSubquery(AbstractQuery<Vacancy> criteriaQuery, Long currentUserId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        Subquery<User> subquery = criteriaQuery.subquery(User.class);

        Root<CollaborationAgreement> root = subquery.from(CollaborationAgreement.class);

        Join<CollaborationAgreement, User> owner = root.join("owner");
        Join<CollaborationAgreement, User> partner = root.join("partner");

        subquery.select(owner);

        // TODO: make id IN OR id IN
        criterions.add(cb.in(root.<ConnectionType>get("type"))
                .in(ConnectionType.FREELANCER_RECRUITER, ConnectionType.STAFF_RECRUITER));

        criterions.add(cb.equal(partner.<Long>get("id"), currentUserId));

        if (criterions.size() > 0) {
            subquery.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        return subquery;
    }

    @Override
    public Hookup loadHookup(long hookupId, long workspaceId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Hookup> criteria = cb.createQuery(Hookup.class);
        Root<Hookup> root = criteria.from(Hookup.class);

        Join<Hookup, Workspace> workspace = root.join("workspace");
        Join<Hookup, Vacancy> vacancy = root.join("vacancy");
        Join<Vacancy, Workspace> vacancyWorkspace = vacancy.join("workspace");

        criteria.select(root);

        criterions.add(cb.equal(root.<Long>get("id"), hookupId));

        criterions.add(
                cb.or(
                        cb.equal(workspace.<Long>get("id"), workspaceId),
                        cb.equal(vacancyWorkspace.<Long>get("id"), workspaceId)
                )
        );

        criteria.where(cb.and(criterions.toArray(new Predicate[0])));

        TypedQuery<Hookup> resultQuery = this.getEM().createQuery(criteria);

        return this.extractSingleFromList(resultQuery.getResultList());
    }

    @Override
    public void saveHookup(Hookup hookup) {
        this.getEM().persist(hookup);
    }

    @Override
    public boolean checkVacancyAccessible(long vacancyId, long workspaceId) {
        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Vacancy> vacancy = criteria.from(this.getEntityClass());
        Join<Vacancy, Workspace> workspace = vacancy.join("workspace");

        criteria.select(cb.count(vacancy.get("id")));

        criterions.add(cb.equal(vacancy.<Long>get("id"), vacancyId));
        criterions.add(cb.equal(workspace.<Long>get("id"), workspaceId));

        if (criterions.size() > 0) {
            criteria.where(cb.and(criterions.toArray(new Predicate[0])));
        }

        TypedQuery<Long> resultQuery = this.getEM().createQuery(criteria);

        return resultQuery.getSingleResult() == 1;
    }

    @Override
    public List<HookupDTO> loadHookupsForVacancy(
            long vacancyId, long workspaceId, boolean includeArchived) {

        CriteriaBuilder cb = this.getEM().getCriteriaBuilder();
        List<Predicate> criterions = new ArrayList<>();

        CriteriaQuery<Tuple> criteria = cb.createQuery(Tuple.class);
        Root<Hookup> root = criteria.from(Hookup.class);

        Join<Hookup, Workspace> workspace = root.join("workspace");
        Join<Hookup, Vacancy> vacancy = root.join("vacancy");
        Join<Vacancy, Workspace> vacancyWorkspace = vacancy.join("workspace");

        criteria.multiselect(
                root.get("id").alias("hookupId"),
                root.get("candidate").get("firstName").alias("candidateFirstName"),
                root.get("candidate").get("secondName").alias("candidateSecondName"),
                root.get("candidate").get("lastName").alias("candidateLastName"),
                root.get("candidateCurrentPosition").alias("candidateCurrentPosition"),
                root.get("candidateCurrentCompany").alias("candidateCurrentCompany"),
                root.get("archived").alias("archived"),
                root.get("status").alias("hookupStatus"),
                root.get("passedTestTaskStatus").alias("passedTestTaskStatus"),
                root.get("contactedOn").alias("contactedOn"),
                root.get("lastActivityOn").alias("lastActivityOn"),




                root.get("documents").get("resumeFileName")     .alias("resumeFileName"),
                root.get("documents").get("resumeFileHash")     .alias("resumeFileHash"),
                root.get("documents").get("resumeFileMimeType") .alias("resumeFileMimeType"),
                root.get("documents").get("resumeFileLength")   .alias("resumeFileLength"),

                root.get("documents").get("testtaskFileName")     .alias("testtaskFileName"),
                root.get("documents").get("testtaskFileHash")     .alias("testtaskFileHash"),
                root.get("documents").get("testtaskFileMimeType") .alias("testtaskFileMimeType"),
                root.get("documents").get("testtaskFileLength")   .alias("testtaskFileLength"),

                root.get("documents").get("interviewFeedbackFile0Name")     .alias("interviewFeedbackFile0Name"),
                root.get("documents").get("interviewFeedbackFile0Hash")     .alias("interviewFeedbackFile0Hash"),
                root.get("documents").get("interviewFeedbackFile0MimeType") .alias("interviewFeedbackFile0MimeType"),
                root.get("documents").get("interviewFeedbackFile0Length")   .alias("interviewFeedbackFile0Length"),

                root.get("documents").get("interviewFeedbackFile1Name")     .alias("interviewFeedbackFile1Name"),
                root.get("documents").get("interviewFeedbackFile1Hash")     .alias("interviewFeedbackFile1Hash"),
                root.get("documents").get("interviewFeedbackFile1MimeType") .alias("interviewFeedbackFile1MimeType"),
                root.get("documents").get("interviewFeedbackFile1Length")   .alias("interviewFeedbackFile1Length"),

                root.get("documents").get("interviewFeedbackFile2Name")     .alias("interviewFeedbackFile2Name"),
                root.get("documents").get("interviewFeedbackFile2Hash")     .alias("interviewFeedbackFile2Hash"),
                root.get("documents").get("interviewFeedbackFile2MimeType") .alias("interviewFeedbackFile2MimeType"),
                root.get("documents").get("interviewFeedbackFile2Length")   .alias("interviewFeedbackFile2Length"),

                root.get("documents").get("interviewFeedbackFile3Name")     .alias("interviewFeedbackFile3Name"),
                root.get("documents").get("interviewFeedbackFile3Hash")     .alias("interviewFeedbackFile3Hash"),
                root.get("documents").get("interviewFeedbackFile3MimeType") .alias("interviewFeedbackFile3MimeType"),
                root.get("documents").get("interviewFeedbackFile3Length")   .alias("interviewFeedbackFile3Length"),

                root.get("documents").get("interviewFeedbackFile4Name")     .alias("interviewFeedbackFile4Name"),
                root.get("documents").get("interviewFeedbackFile4Hash")     .alias("interviewFeedbackFile4Hash"),
                root.get("documents").get("interviewFeedbackFile4MimeType") .alias("interviewFeedbackFile4MimeType"),
                root.get("documents").get("interviewFeedbackFile4Length")   .alias("interviewFeedbackFile4Length"),

                root.get("documents").get("interviewFeedbackFile5Name")     .alias("interviewFeedbackFile5Name"),
                root.get("documents").get("interviewFeedbackFile5Hash")     .alias("interviewFeedbackFile5Hash"),
                root.get("documents").get("interviewFeedbackFile5MimeType") .alias("interviewFeedbackFile5MimeType"),
                root.get("documents").get("interviewFeedbackFile5Length")   .alias("interviewFeedbackFile5Length"),

                root.get("documents").get("interviewFeedbackFile6Name")     .alias("interviewFeedbackFile6Name"),
                root.get("documents").get("interviewFeedbackFile6Hash")     .alias("interviewFeedbackFile6Hash"),
                root.get("documents").get("interviewFeedbackFile6MimeType") .alias("interviewFeedbackFile6MimeType"),
                root.get("documents").get("interviewFeedbackFile6Length")   .alias("interviewFeedbackFile6Length"),

                root.get("documents").get("probationFeedbackFile0Name")     .alias("probationFeedbackFile0Name"),
                root.get("documents").get("probationFeedbackFile0Hash")     .alias("probationFeedbackFile0Hash"),
                root.get("documents").get("probationFeedbackFile0MimeType") .alias("probationFeedbackFile0MimeType"),
                root.get("documents").get("probationFeedbackFile0Length")   .alias("probationFeedbackFile0Length"),

                root.get("documents").get("probationFeedbackFile1Name")     .alias("probationFeedbackFile1Name"),
                root.get("documents").get("probationFeedbackFile1Hash")     .alias("probationFeedbackFile1Hash"),
                root.get("documents").get("probationFeedbackFile1MimeType") .alias("probationFeedbackFile1MimeType"),
                root.get("documents").get("probationFeedbackFile1Length")   .alias("probationFeedbackFile1Length"),

                root.get("documents").get("probationFeedbackFile2Name")     .alias("probationFeedbackFile2Name"),
                root.get("documents").get("probationFeedbackFile2Hash")     .alias("probationFeedbackFile2Hash"),
                root.get("documents").get("probationFeedbackFile2MimeType") .alias("probationFeedbackFile2MimeType"),
                root.get("documents").get("probationFeedbackFile2Length")   .alias("probationFeedbackFile2Length")
                );

        criterions.add(cb.equal(vacancy.<Long>get("id"), vacancyId));

        criterions.add(
                cb.or(
                        cb.equal(workspace.<Long>get("id"), workspaceId),
                        cb.equal(vacancyWorkspace.<Long>get("id"), workspaceId)
                )
        );

        if (!includeArchived) {
            criterions.add(cb.equal(root.<HookupStatus>get("archived"), false));
        }

        criteria.where(cb.and(criterions.toArray(new Predicate[0])));

        TypedQuery<Tuple> resultQuery = this.getEM().createQuery(criteria);

        List<HookupDTO> result = new ArrayList<>();

        for (Tuple tuple : resultQuery.getResultList()) {
            HookupDTO hdto = new HookupDTO();
            hdto.setId(                     (Long)          tuple.get("hookupId"));
            hdto.setPersonFirstName(        (String)        tuple.get("candidateFirstName"));
            hdto.setPersonSecondName(       (String)        tuple.get("candidateSecondName"));
            hdto.setPersonLastName(         (String)        tuple.get("candidateLastName"));
            hdto.setPersonCurrentPosition(  (String)        tuple.get("candidateCurrentPosition"));
            hdto.setPersonCurrentCompany(   (String)        tuple.get("candidateCurrentCompany"));
            hdto.setArchived(               (Boolean)       tuple.get("archived"));
            hdto.setStatus(                 (HookupStatus)  tuple.get("hookupStatus"));
            hdto.setPassedTestTaskStatus(   (Boolean)       tuple.get("passedTestTaskStatus"));
            hdto.setContactedOn(            (Date)          tuple.get("contactedOn"));
            hdto.setLastUpdateDate(         (Date)          tuple.get("lastActivityOn"));

            hdto.setResume(this.extractDocument(tuple, "resume", ""));
            hdto.setTesttask(this.extractDocument(tuple, "testtask", ""));

            this.addIfExists(hdto.getInterviewFeedbacks(), this.extractDocument(tuple, "interviewFeedback", "0"));
            this.addIfExists(hdto.getInterviewFeedbacks(), this.extractDocument(tuple, "interviewFeedback", "1"));
            this.addIfExists(hdto.getInterviewFeedbacks(), this.extractDocument(tuple, "interviewFeedback", "2"));
            this.addIfExists(hdto.getInterviewFeedbacks(), this.extractDocument(tuple, "interviewFeedback", "3"));
            this.addIfExists(hdto.getInterviewFeedbacks(), this.extractDocument(tuple, "interviewFeedback", "4"));
            this.addIfExists(hdto.getInterviewFeedbacks(), this.extractDocument(tuple, "interviewFeedback", "5"));
            this.addIfExists(hdto.getInterviewFeedbacks(), this.extractDocument(tuple, "interviewFeedback", "6"));

            this.addIfExists(hdto.getProbationFeedbacks(), this.extractDocument(tuple, "probationFeedback", "0"));
            this.addIfExists(hdto.getProbationFeedbacks(), this.extractDocument(tuple, "probationFeedback", "1"));
            this.addIfExists(hdto.getProbationFeedbacks(), this.extractDocument(tuple, "probationFeedback", "2"));

            result.add(hdto);
        }

        return result;
    }

    private void addIfExists(List<HookupDocumentDTO> hdtos, HookupDocumentDTO hddto) {
        if (hddto != null) {
            hdtos.add(hddto);
        }
    }

    private HookupDocumentDTO extractDocument(Tuple tuple, String prefix, String index) {
        HookupDocumentDTO hddto = null;
        String fileHash = (String) tuple.get(prefix + "File" + index + "Hash");
        String fileName = (String) tuple.get(prefix + "File" + index + "Name");
        Long   length =   (Long)   tuple.get(prefix + "File" + index + "Length");
        String mimeType = (String) tuple.get(prefix + "File" + index + "MimeType");

        if (fileHash != null && fileName != null && mimeType != null) {
            hddto = new HookupDocumentDTO();
            if (!"".equals(index)) {
                hddto.setIndex(index);
            }
            hddto.setFileHash(fileHash);
            hddto.setFileName(fileName);
            hddto.setLength(length);
            hddto.setMimeType(mimeType);
        }

        return hddto;
    }

    @Override
    public void deleteHookup(Hookup hookup) {
        this.getEM().remove(hookup);
    }

    @Override
    protected Class<Vacancy> getEntityClass() {
        return Vacancy.class;
    }
}
