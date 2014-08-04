package controllers;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.profwell.collaboration.domain.PartnerDTO;
import org.profwell.collaboration.domain.PartnershipRequestDTO;
import org.profwell.collaboration.model.CollaborationAgreement;
import org.profwell.collaboration.model.CollaborationRequest;
import org.profwell.collaboration.model.CollaborationRequestType;
import org.profwell.collaboration.model.ConnectionType;
import org.profwell.collaboration.service.CollaborationService;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.exception.ObjectNotFoundException;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.json.ConvertionUtils;
import org.profwell.notification.service.NoticeService;
import org.profwell.security.model.User;
import org.profwell.security.service.UserService;
import org.profwell.security.web.SessionUtility;

import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.UserUtils;

@Security.Authenticated(Secured.class)
public class CollaborationController extends Controller {

    private static final String OBJECT_NAME = "Partner";

    private static final CollaborationService service = ServiceHolder.getService(CollaborationService.class);

    private static final NoticeService noticeService = ServiceHolder.getService(NoticeService.class);

    private static final UserService userService = ServiceHolder.getService(UserService.class);

    @play.db.jpa.Transactional(readOnly = true)
    public static Result partnerList() {

        List<PartnerDTO> list = service.getMyPartners(SessionUtility.getCurrentUserId());
        list.addAll(service.getWhereIisPartner(SessionUtility.getCurrentUserId()));

        /* PartnerDTO dto;
        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Иванов");
        dto.setPartnerFirstName("Петр");
        dto.setType(ConnectionType.COMPANION);
        dto.setMy(true);
        list.add(dto);

        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Сидоров");
        dto.setPartnerFirstName("Дмитрий");
        dto.setType(ConnectionType.STAFF_RECRUITER);
        dto.setMy(true);
        list.add(dto);

        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Петров");
        dto.setPartnerFirstName("Виталий");
        dto.setType(ConnectionType.FREELANCER_RECRUITER);
        dto.setMy(true);
        list.add(dto);

        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Васильченко");
        dto.setPartnerFirstName("Виталий");
        dto.setType(ConnectionType.RECRUITMENT_AGENCY);
        dto.setMy(true);
        list.add(dto);




        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Картозонов");
        dto.setPartnerFirstName("Виктор");
        dto.setType(ConnectionType.COMPANION);
        dto.setMy(false);
        list.add(dto);

        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Бобов");
        dto.setPartnerFirstName("Иван");
        dto.setType(ConnectionType.STAFF_RECRUITER);
        dto.setMy(false);
        list.add(dto);

        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Кутузов");
        dto.setPartnerFirstName("Вьячеслав");
        dto.setType(ConnectionType.FREELANCER_RECRUITER);
        dto.setMy(false);
        list.add(dto);

        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Попов");
        dto.setPartnerFirstName("Василий");
        dto.setType(ConnectionType.RECRUITMENT_AGENCY);
        dto.setMy(false);
        list.add(dto);*/

        Collections.sort(list, new Comparator<PartnerDTO>() {
            @Override
            public int compare(PartnerDTO o1, PartnerDTO o2) {
                return o1.getPartnerFullName().compareTo(o2.getPartnerFullName());
            }
        });

        List<PartnershipRequestDTO> myPRList = service.getMyPartnershipRequests(SessionUtility.getCurrentUserId());

        /*dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Петров");
        dto.setPartnerFirstName("Виталий");
        dto.setType(ConnectionType.FREELANCER_RECRUITER);
        myPRList.add(dto);

        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Васильченко");
        dto.setPartnerFirstName("Виталий");
        dto.setType(ConnectionType.COMPANION);
        myPRList.add(dto);*/

        List<PartnershipRequestDTO> inPRList = service.getPartnershipRequestsToMe(SessionUtility.getCurrentUserId());

        /*dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Кутузов");
        dto.setPartnerFirstName("Вьячеслав");
        dto.setType(ConnectionType.STAFF_RECRUITER);
        inPRList.add(dto);

        dto = new PartnerDTO();
        dto.setRecordId(1L);
        dto.setPartnerUuid(UUID.randomUUID().toString().toUpperCase());
        dto.setPartnerLastName("Попов");
        dto.setPartnerFirstName("Василий");
        dto.setType(ConnectionType.RECRUITMENT_AGENCY);
        inPRList.add(dto);*/

        Content html = views.html.Collaboration.collaboration.render(
                Routings.collaborationMenu(),
                list,
                myPRList,
                inPRList);

        return ok(html);
    }

    @play.db.jpa.Transactional()
    public static Result partnershipBreak(Long id) {

        CollaborationAgreement agreement = service.getCollaborationAgreement(id);

        Long currentUserId = SessionUtility.getCurrentUserId();
        if (agreement == null
                ||
                agreement.getPartner().getId() != currentUserId
                && agreement.getOwner().getId() != currentUserId) {
            throw new ObjectNotFoundException(OBJECT_NAME);
        }

        service.deleteCollaborationAgreement(agreement);

        User currentUser = SessionUtility.getCurrentUser();

        User receiver = null;
        if (agreement.getPartner().getId() == currentUserId){
            receiver = agreement.getOwner();
        } else if (agreement.getOwner().getId() == currentUserId) {
            receiver = agreement.getPartner();
        }

        noticeService.addNewNotice(
                "User [" + UserUtils.getFullNameOrUUID(currentUser) + "] broke partnership.",
                receiver,
                SessionUtility.getCurrentUser());

        return redirect(routes.CollaborationController.partnerList());
    }

    @play.db.jpa.Transactional()
    public static Result partnershipAccept(Long id) {

        CollaborationRequest request = service.getCollaborationRequest(id);

        Long currentUserId = SessionUtility.getCurrentUserId();
        if (request == null
                ||
                request.getPartner().getId() != currentUserId) {
            throw new ObjectNotFoundException(OBJECT_NAME);
        }

        CollaborationAgreement agreement = new CollaborationAgreement();
        resolveConnectionType(request, agreement);
        service.saveCollaborationAgreement(agreement);

        service.deleteCollaborationRequest(request);

        User currentUser = SessionUtility.getCurrentUser();

        noticeService.addNewNotice(
                "User [" + UserUtils.getFullNameOrUUID(currentUser) + "] accepted your partnership request.",
                request.getOwner(),
                SessionUtility.getCurrentUser());

        return redirect(routes.CollaborationController.partnerList());
    }

    private static void resolveConnectionType(CollaborationRequest request,
            CollaborationAgreement agreement) {
        if (request.getType() == CollaborationRequestType.COMPANION) {
            agreement.setOwner(request.getOwner());
            agreement.setPartner(request.getPartner());
            agreement.setType(ConnectionType.COMPANION);



        } else if (request.getType() == CollaborationRequestType.FREELANCER_RECRUITER) {
            agreement.setOwner(request.getOwner());
            agreement.setPartner(request.getPartner());
            agreement.setType(ConnectionType.FREELANCER_RECRUITER);
        } else if (request.getType() == CollaborationRequestType.CLIENT_HIRER) {
            agreement.setOwner(request.getPartner());
            agreement.setPartner(request.getOwner());
            agreement.setType(ConnectionType.FREELANCER_RECRUITER);



        } else if (request.getType() == CollaborationRequestType.STAFF_RECRUITER) {
            agreement.setOwner(request.getOwner());
            agreement.setPartner(request.getPartner());
            agreement.setType(ConnectionType.STAFF_RECRUITER);
        } else if (request.getType() == CollaborationRequestType.CLIENT_EMPLOYER) {
            agreement.setOwner(request.getPartner());
            agreement.setPartner(request.getOwner());
            agreement.setType(ConnectionType.STAFF_RECRUITER);



        } else if (request.getType() == CollaborationRequestType.RECRUITMENT_AGENCY) {
            agreement.setOwner(request.getOwner());
            agreement.setPartner(request.getPartner());
            agreement.setType(ConnectionType.RECRUITMENT_AGENCY);
        } else if (request.getType() == CollaborationRequestType.CLIENT) {
            agreement.setOwner(request.getPartner());
            agreement.setPartner(request.getOwner());
            agreement.setType(ConnectionType.RECRUITMENT_AGENCY);
        }
    }

    @play.db.jpa.Transactional()
    public static Result partnershipDecline(Long id) {

        CollaborationRequest request = service.getCollaborationRequest(id);

        Long currentUserId = SessionUtility.getCurrentUserId();
        if (request == null
                ||
                request.getPartner().getId() != currentUserId
                && request.getOwner().getId() != currentUserId) {
            throw new ObjectNotFoundException(OBJECT_NAME);
        }

        service.deleteCollaborationRequest(request);

        User currentUser = SessionUtility.getCurrentUser();
        if (request.getPartner().getId() == currentUserId){
            noticeService.addNewNotice(
                    "User [" + UserUtils.getFullNameOrUUID(currentUser) + "] declined your partnership request.",
                    request.getOwner(),
                    SessionUtility.getCurrentUser());
        } else if (request.getOwner().getId() == currentUserId) {
            noticeService.addNewNotice(
                    "User [" + UserUtils.getFullNameOrUUID(currentUser) + "] canceled partnership request.",
                    request.getPartner(),
                    SessionUtility.getCurrentUser());
        }

        return redirect(routes.CollaborationController.partnerList());
    }

    @play.db.jpa.Transactional()
    public static Result partnershipRequestSendAsync(String uuid, String relationshipType) {

        ValidationContext context = new ValidationContext();

        String clearedUuid = uuid.trim();
        boolean valid = true;
        if (StringUtils.isBlank(clearedUuid)) {
            context.add("message", "Please specify partner UUID.");
            valid = false;
        }

        CollaborationRequestType connectionType = null;
        try {
            connectionType = CollaborationRequestType.valueOf(relationshipType);
        } catch (Exception e) {
            context.add("message", "Please specify type of the required partner connection type.");
            valid = false;
        }

        User coworker = null;
        if (valid) {
            coworker = userService.findUserByUUID(uuid);
            if (coworker == null) {
                context.add("message", "User with the specified UUID can't be found.");
                valid = false;
            } else if (coworker.getId() == SessionUtility.getCurrentUserId()) {
                context.add("message", "You can't request partnership to yourself.");
                valid = false;
            }
        }

        if (valid) {
            if (service.checkCollaborationAgreement(SessionUtility.getCurrentUserId(), coworker.getId(), null)
                    || service.checkCollaborationAgreement(coworker.getId(), SessionUtility.getCurrentUserId(), null)) {
                context.add("message", "You already have partnership with the specified user."
                        + " Please break existing partnership first.");
                valid = false;
            } else if (service.checkCollaborationRequest(SessionUtility.getCurrentUserId(), coworker.getId())
                    || service.checkCollaborationRequest(coworker.getId(), SessionUtility.getCurrentUserId())) {
                context.add("message", "You already have an opened partnership request with the specified user."
                        + " Please break existing request first.");
                valid = false;
            }
        }

        if (valid) {

            User currentUser = SessionUtility.getCurrentUser();

            CollaborationRequest request = new CollaborationRequest();
            request.setOwner(SessionUtility.getCurrentUser());
            request.setPartner(coworker);
            request.setType(connectionType);
            service.saveCollaborationRequest(request);
            noticeService.addNewNotice(
                    "User [" + UserUtils.getFullNameOrUUID(currentUser) + "] proposes partnership for you.",
                    coworker,
                    SessionUtility.getCurrentUser());

            context.add("message", "The request has been created successfully.");
        }

        ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
        response.put("validationContext",
                ConvertionUtils.convertToJson(context));

        if (valid) {
            return ok(response);
        } else {
            return badRequest(response);
        }
    }

}
