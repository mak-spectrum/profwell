package controllers;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.file.service.FileService;
import org.profwell.file.service.FileStorageCallback;
import org.profwell.generic.exception.FileNotFoundException;
import org.profwell.generic.web.FilterUtility;
import org.profwell.security.model.Workspace;
import org.profwell.security.service.UserService;
import org.profwell.security.web.SessionUtility;
import org.profwell.ui.menu.MenuConfiguration;
import org.profwell.vacancy.auxiliary.VacancyFilter;
import org.profwell.vacancy.domain.HookupDTO;
import org.profwell.vacancy.domain.HookupForm;
import org.profwell.vacancy.domain.RequiredSkillForm;
import org.profwell.vacancy.domain.VacancyEditForm;
import org.profwell.vacancy.domain.VacancyViewForm;
import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.HookupDocuments;
import org.profwell.vacancy.model.HookupStatus;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancyStatus;
import org.profwell.vacancy.service.HookupLifecycle;
import org.profwell.vacancy.service.HookupValidator;
import org.profwell.vacancy.service.VacancyService;
import org.profwell.vacancy.service.VacancyValidator;
import org.profwell.vacancy.web.HookupStatusControl;

import play.Logger;
import play.data.Form;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;
import utils.DateTimeFormatUtils;
import utils.SecurityControl;

@Security.Authenticated(Secured.class)
public class VacancyController extends Controller {

    private static final String OBJECT_NAME = "Vacancy";

    private static FileService fileService = ServiceHolder.getService(FileService.class);

    private static HookupValidator hookupValidator = ServiceHolder.getService(HookupValidator.class);

    private static HookupLifecycle hookupLifecycle = ServiceHolder.getService(HookupLifecycle.class);

    private static UserService userService = ServiceHolder.getService(UserService.class);

    private static VacancyService service = ServiceHolder.getService(VacancyService.class);

    private static VacancyValidator validator = ServiceHolder.getService(VacancyValidator.class);

    @play.db.jpa.Transactional(readOnly = true)
    public static Result vacancyArchiveList() {

        VacancyFilter filter = prepareFilter();
        List<Vacancy> vacancies = service.listArchivedVacancies(filter);

        Content html = views.html.Vacancy.vacancyArchiveList.render(
                getMenuConfiguration(),
                filter,
                vacancies);

        return ok(html);
    }

    private static VacancyFilter prepareFilter() {
        Map<String, String[]> requestParams =
                request().body().asFormUrlEncoded();

        VacancyFilter filter;
        if (requestParams != null) {
            filter = FilterUtility.createListFilter(requestParams,
                    VacancyFilter.class);

            filter.setCompany(requestParams.get("company")[0]);
            filter.setPosition(requestParams.get("position")[0]);
            filter.setProject(requestParams.get("project")[0]);
            filter.setStatusValue(requestParams.get("status")[0]);
            filter.setCountryValue(requestParams.get("country")[0]);
            filter.setCity(requestParams.get("city")[0]);

            filter.setAssigneeIdValue(requestParams.get("assigneeId")[0]);

        } else {
            filter = new VacancyFilter();
        }

        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        return filter;
    }

    public static Result vacancyCreate() {

        VacancyEditForm form = new VacancyEditForm();

        Content html = views.html.Vacancy.vacancyEdit.render(
                getMenuConfiguration(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result vacancyEditSubmit() {

        Form<VacancyEditForm> playForm = Form.form(VacancyEditForm.class);

        VacancyEditForm form = playForm.bindFromRequest().get();

        transferSkillsData(form);

        if (validator.validate(form)) {

            Vacancy vacancy = null;
            if (form.isNew()) {
                vacancy = new Vacancy();
            } else {
                vacancy = service.getFromWorkspace(form.getId(),
                        SessionUtility.getCurrentUserId());
            }
            SecurityControl.checkObjectExists(vacancy, OBJECT_NAME);

            Workspace wsp = userService.getWorkspace(
                    SessionUtility.getCurrentUserId());

            form.transferTo(vacancy);

            service.saveVacancyWithDictValues(vacancy, wsp);

            return redirect(
                    routes.VacancyController.vacancyView(
                            vacancy.getId(),
                            VacancyViewForm.MAIN_TAB,
                            false)
                    );
        } else {

            Content html = views.html.Vacancy.vacancyEdit.render(
                    getMenuConfiguration(),
                    form);

            return badRequest(html);
        }
    }

    private static void transferSkillsData(VacancyEditForm form) {
        Map<String, String[]> requestParams =
                request().body().asFormUrlEncoded();

        for (Entry<String, String[]> ent : requestParams.entrySet()) {
            if (ent.getKey().startsWith("skill:")) {

                boolean mandatory;
                if (ent.getKey().endsWith(":m")) {
                    mandatory = true;
                } else if (ent.getKey().endsWith(":o")) {
                    mandatory = false;
                } else {
                    throw new IllegalArgumentException("Bad request");
                }

                for (int index = 0; index < ent.getValue().length; index++) {
                    RequiredSkillForm skillForm = new RequiredSkillForm();
                    skillForm.setName(ent.getValue()[index]);
                    skillForm.setIndex(index);
                    skillForm.setMandatory(mandatory);
                    form.getSkills().add(skillForm);
                }
            }
        }

    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result vacancyView(Long id, String activeTab, Boolean archived) {

        Vacancy vacancy = service.getFromWorkspace(id,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(vacancy, OBJECT_NAME);

        VacancyViewForm form = new VacancyViewForm();
        form.setActiveTab(activeTab);
        form.transferFrom(vacancy);

        if (form.isHookupsTabActive()) {
            List<HookupDTO> list = service.loadHookupsForVacancy(
                    form.getId(),
                    SessionUtility.getCurrentUserId(),
                    archived);

            for (HookupDTO dto : list) {
                dto.setStatusMoves(hookupLifecycle.getStatusMoves(dto));
                if (dto.isDocumentAttachable()) {
                    dto.setAttachableDocumentTitle(hookupLifecycle
                            .getAttachableDocumentTitle(dto));
                }
            }
            form.getHookups().addAll(list);
        }

        Content html = views.html.Vacancy.vacancyView.render(
                getMenuConfiguration(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result vacancyEdit(Long id) {

        Vacancy vacancy = service.getFromWorkspace(id,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(vacancy, OBJECT_NAME);

        VacancyEditForm form = new VacancyEditForm();
        form.transferFrom(vacancy);

        Content html = views.html.Vacancy.vacancyEdit.render(
                getMenuConfiguration(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result vacancyDelete(Long id) {
        Vacancy vacancy = service.getFromWorkspace(id,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(vacancy, OBJECT_NAME);

        service.delete(vacancy);
        return redirect(routes.VacancyController.vacancyArchiveList());
    }

    @play.db.jpa.Transactional
    public static Result vacancyOpen(Long id) {
        Vacancy vacancy = service.getFromWorkspace(id,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(vacancy, OBJECT_NAME);

        if (vacancy.getStatus() == VacancyStatus.SUSPENDED) {
            vacancy.setStatus(VacancyStatus.OPENED);
            service.save(vacancy);

            return redirect(routes.VacancyController.vacancyView(id, VacancyViewForm.MAIN_TAB, false));
        } else {
            return badRequest();
        }
    }

    @play.db.jpa.Transactional
    public static Result vacancySuspend(Long id) {
        Vacancy vacancy = service.getFromWorkspace(id,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(vacancy, OBJECT_NAME);

        if (vacancy.getStatus() == VacancyStatus.OPENED) {
            vacancy.setStatus(VacancyStatus.SUSPENDED);
            service.save(vacancy);

            return redirect(routes.VacancyController.vacancyView(id, VacancyViewForm.MAIN_TAB, false));
        } else {
            return badRequest();
        }
    }

    @play.db.jpa.Transactional
    public static Result vacancyClose(Long id) {
        Vacancy vacancy = service.getFromWorkspace(id,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(vacancy, OBJECT_NAME);

        if (vacancy.getStatus() == VacancyStatus.OPENED
                || vacancy.getStatus() == VacancyStatus.SUSPENDED) {
            vacancy.setStatus(VacancyStatus.CLOSED);
            vacancy.setClosingDatetime(new Date());
            service.save(vacancy);

            return redirect(routes.VacancyController.vacancyView(id, VacancyViewForm.MAIN_TAB, false));
        } else {
            return badRequest();
        }
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result vacancyHookups(Long vacancyId, Boolean archived) {
        boolean includeArchived = (archived == null || archived);

        List<HookupDTO> list = service.loadHookupsForVacancy(
                        vacancyId,
                        SessionUtility.getCurrentUserId(),
                        includeArchived);

        for (HookupDTO dto : list) {
            dto.setStatusMoves(hookupLifecycle.getStatusMoves(dto));
            if (dto.isDocumentAttachable()) {
                dto.setAttachableDocumentTitle(hookupLifecycle
                        .getAttachableDocumentTitle(dto));
            }
        }

        Content html = views.html.Vacancy.listHookupItems.render(list);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result vacancyStaffPerson(Long vacancyId) {

        Vacancy vacancy = service.getFromWorkspace(vacancyId,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(vacancy, OBJECT_NAME);

        if (vacancy.getStatus() == VacancyStatus.OPENED) {
            HookupForm form = new HookupForm();
            form.setVacancyId(vacancyId);

            form.setContactedOn(new Date());

            Content html = views.html.Vacancy.hookupEdit.render(
                    getMenuConfiguration(),
                    form);

            return ok(html);
        } else {
            return badRequest();
        }
    }

    @play.db.jpa.Transactional
    public static Result hookupSubmit() {
        Form<HookupForm> playForm = Form.form(HookupForm.class);

        HookupForm form = playForm.bindFromRequest().get();

        Vacancy vacancy = service.getFromWorkspace(form.getVacancyId(),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(vacancy, OBJECT_NAME);

        if (form.isNew() && vacancy.getStatus() == VacancyStatus.SUSPENDED
                || vacancy.getStatus() == VacancyStatus.CLOSED) {
            badRequest();
        }

        if (hookupValidator.validate(form)) {

            Hookup hookup = null;
            if (form.isNew()) {
                hookup = new Hookup();
            } else {
                hookup = service.loadHookup(form.getId(),
                        SessionUtility.getCurrentUserId());
            }
            SecurityControl.checkObjectExists(hookup, "Hookup");

            Workspace wsp = userService.getWorkspace(
                    SessionUtility.getCurrentUserId());

            form.transferTo(hookup);

            service.saveHookupWithDictionaries(hookup, wsp);

            return redirect(
                    routes.VacancyController.vacancyView(
                            hookup.getVacancy().getId(),
                            VacancyViewForm.HOOKUPS_TAB,
                            false)
                    );
        } else {

            Content html = views.html.Vacancy.hookupEdit.render(
                    getMenuConfiguration(),
                    form);

            return badRequest(html);
        }
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result hookupEdit(Long hookupId) {

        Hookup hookup = service.loadHookup(
                hookupId,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(hookup, "Hookup");

        HookupForm form = new HookupForm();
        form.transferFrom(hookup);
        form.setVacancyId(hookup.getVacancy().getId());

        Content html = views.html.Vacancy.hookupEdit.render(
                getMenuConfiguration(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result hookupFile(Long hookupId, String type, String index) throws UnsupportedEncodingException {

        Hookup hookup = service.loadHookup(
                Long.valueOf(hookupId),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(hookup, "Hookup");

        String key = null;

        if ("resume".equals(type)) {
            key = HookupDocuments.RESUME;
        } else if ("testtask".equals(type)) {
            key = HookupDocuments.TEST_TASK;
        } else if ("interview".equals(type)) {
            key = HookupDocuments.INTERVIEW_FEEDBACK_PREFIX + "_" + index;
        } else if ("probation".equals(type)) {
            key = HookupDocuments.PROBATION_FEEDBACK_PREFIX + "_" + index;
        } else {
            throw new FileNotFoundException("Bad file request");
        }

        String hash = hookup.getDocuments().getFileHash(key);
        String name = hookup.getDocuments().getFileName(key);
        String mimeType = hookup.getDocuments().getFileMimeType(key);
        Long length = hookup.getDocuments().getFileLength(key);

        if (hash != null) {
            if (FileService.FILE_HASH_PLACEHOLDER.equals(hash)) {
                throw new FileNotFoundException("File is under processing, please try again later");
            } else {
                InputStream is = fileService.retriveFile(mimeType, hash);
                if (is != null) {
                    String userAgent = request().headers().get("User-Agent")[0];
                    if (userAgent == null) {
                        userAgent = "";
                    }

                    response().setHeader("Content-Description",         "File Transfer");
                    response().setHeader("Content-Transfer-Encoding",   "binary");
                    response().setHeader("Cache-Control",               "must-revalidate, post-check=0, pre-check=0");
                    response().setHeader("Content-Type",                mimeType);

                    if (userAgent.contains("Firefox")) {
                        response().setHeader("Content-Disposition",         "attachment; filename*=''" + URLEncoder.encode(name, "UTF-8"));
                    } else {
                        response().setHeader("Content-Disposition",         "attachment; filename=" + URLEncoder.encode(name, "UTF-8"));
                    }

                    response().setHeader("Content-Length",              String.valueOf(length));
                    return ok(is);
                } else {
                    throw new FileNotFoundException("File has been lost, please contact administration");
                }
            }
        } else {
            throw new FileNotFoundException("Bad file request");
        }
    }

    @play.db.jpa.Transactional()
    public static Result hookupDeleteAsync(Long hookupId) {

        Hookup hookup = service.loadHookup(
                hookupId,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(hookup, "Hookup");

        if (!hookup.isArchived()) {
            service.deleteHookup(hookup);
        }

        return ok();
    }

    @play.db.jpa.Transactional()
    public static Result hookupToStatusAsync(Long hookupId, String hookupStatus) {

        Hookup hookup = service.loadHookup(
                hookupId,
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(hookup, "Hookup");


        HookupStatus nextStatus = HookupStatus.valueOf(hookupStatus);
        if (hookupLifecycle.isStatusMoveAllowed(hookup, nextStatus)) {
            hookup.setStatus(nextStatus);
            service.saveHookup(hookup);

            ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
            result.put("newStatus", hookup.getStatus().getName());
            result.put("newStatusCaption", hookup.getStatus().getCaption());
            result.put("canBeArchived", hookup.getStatus().isCanBeArchived());
            result.put("lastActivityTime", DateTimeFormatUtils
                    .getDatetimeFormatted(hookup.getLastActivityOn()));

            if (hookup.getStatus().isDocumentAttachable()) {
                result.put("documentAttachable", true);
                result.put("attachableDocumentTitle",
                        hookupLifecycle.getAttachableDocumentTitle(hookup));
            }

            ArrayNode actionNodes = result.putArray("statusMoves");

            for (HookupStatusControl c : hookupLifecycle.getStatusMoves(hookup)) {
                ObjectNode hsc = new ObjectNode(JsonNodeFactory.instance);
                hsc.put("toStatus", c.getToStatus().getName());
                hsc.put("actionDescription", c.getActionDescription());
                hsc.put("statusImage", routes.Assets.at("images/icons/h-status/" + c.getStatusImage() + "-status.png").url());
                hsc.put("statusImageAlt", c.getStatusImageAlt());
                actionNodes.add(hsc);
            }

            return ok(result);
        } else {
            return badRequest();
        }
    }

    @play.db.jpa.Transactional()
    public static Result hookupArchiveAsync(Long hookupId) {

        Hookup hookup = service.loadHookup(
                Long.valueOf(hookupId),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(hookup, "Hookup");

        if (hookup.isCanBeArchived()) {
            hookup.setArchived(true);
            service.saveHookup(hookup);

            ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
            result.put("lastActivityTime", DateTimeFormatUtils
                    .getDatetimeFormatted(hookup.getLastActivityOn()));

            return ok(result);
        } else {
            return badRequest();
        }
    }

    @play.db.jpa.Transactional()
    public static Result hookupUnarchiveAsync(Long hookupId) {

        Hookup hookup = service.loadHookup(
                Long.valueOf(hookupId),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(hookup, "Hookup");

        if (hookup.isArchived()) {
            hookup.setArchived(false);
            service.saveHookup(hookup);

            ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
            result.put("lastActivityTime", DateTimeFormatUtils
                    .getDatetimeFormatted(hookup.getLastActivityOn()));
            result.put("documentAttachable", false);
            result.put("attachableDocumentTitle", "");

            ArrayNode actionNodes = result.putArray("statusMoves");

            for (HookupStatusControl c : hookupLifecycle.getStatusMoves(hookup)) {
                ObjectNode hsc = new ObjectNode(JsonNodeFactory.instance);
                hsc.put("toStatus", c.getToStatus().getName());
                hsc.put("actionDescription", c.getActionDescription());
                hsc.put("statusImage", routes.Assets.at("images/icons/h-status/" + c.getStatusImage() + "-status.png").url());
                hsc.put("statusImageAlt", c.getStatusImageAlt());
                actionNodes.add(hsc);
            }

            return ok(result);
        } else {
            return badRequest();
        }
    }

    @play.db.jpa.Transactional()
    public static Result hookupFileUploadAsync() {

        MultipartFormData playForm = request().body().asMultipartFormData();
        Long hookupId = Long.parseLong(playForm.asFormUrlEncoded().get("hookupIdValue")[0]);

        final Hookup hookup = service.loadHookup(
                Long.valueOf(hookupId),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(hookup, "Hookup");

        if (!hookup.isArchived()) {

            String key = null;
            String uiSideCode = null;
            boolean successful = false;
            boolean lengthInvalid = true;
            FilePart part = playForm.getFile("fileUploadFileValue");
            if (part != null) {
                if (part.getFile().length() < FileService.FILE_LENGTH_LIMIT) {
                    lengthInvalid = false;

                    if (hookup.getStatus() == HookupStatus.GOTTEN_RESUME) {
                        key = HookupDocuments.RESUME;
                        uiSideCode = "resume";
                    } else if (hookup.getStatus() == HookupStatus.GOTTEN_TEST_TASK) {
                        key = HookupDocuments.TEST_TASK;
                        uiSideCode = "testtask";
                    } else if (hookup.getStatus() == HookupStatus.ON_INTERVIEWING) {
                        key = findInterviewDocEmptyCell(hookup);
                        uiSideCode = "interview";
                    } else if (hookup.getStatus() == HookupStatus.PROBATION_IN_PROGRESS) {
                        key = findProbationDocEmptyCell(hookup);
                        uiSideCode = "probation";
                    }

                    if (key != null) {
                        successful = storeFileData(hookup, part, key);
                    }
                }
            }


            if (successful) {
                service.saveHookup(hookup);
                ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
                result.put("lastActivityTime", DateTimeFormatUtils
                        .getDatetimeFormatted(hookup.getLastActivityOn()));
                result.put("docType", uiSideCode);
                if (hookup.getStatus() == HookupStatus.ON_INTERVIEWING
                        || hookup.getStatus() == HookupStatus.PROBATION_IN_PROGRESS) {
                    result.put("index", (key.charAt(key.length() - 1) - 48));
                }
                result.put("fileName", part.getFilename().replaceAll(" ", "_"));
                result.put("message", "Success!");
                return ok(result);

            } else if (part == null) {
                ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
                result.put("message", "Please, select a file.");
                return badRequest(result);

            } else if (lengthInvalid) {
                ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
                result.put("message", "File is too big. Please try to reduce its size."
                        + " You can try to remove images, if there is any,"
                        + " or compress the file with help of an archiver"
                        + " or convert the file to PDF.");
                return badRequest(result);

            } else if (key == null) {
                ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
                result.put("message", "No empty cells, to store the file.");
                return badRequest(result);

            } else {
                ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
                result.put("message", "Error on file loading.");
                return badRequest(result);
            }
        }

        ObjectNode result = new ObjectNode(JsonNodeFactory.instance);
        result.put("message", "Error on file loading.");
        return badRequest(result);
    }

    private static String findInterviewDocEmptyCell(final Hookup hookup) {
        String fileKey = null;
        if (hookup.getDocuments().getFileName(HookupDocuments.INTERVIEW_FEEDBACK_0) == null) {
            fileKey = HookupDocuments.INTERVIEW_FEEDBACK_0;
        } else if (hookup.getDocuments().getFileName(HookupDocuments.INTERVIEW_FEEDBACK_1) == null) {
            fileKey = HookupDocuments.INTERVIEW_FEEDBACK_1;
        } else if (hookup.getDocuments().getFileName(HookupDocuments.INTERVIEW_FEEDBACK_2) == null) {
            fileKey = HookupDocuments.INTERVIEW_FEEDBACK_2;
        } else if (hookup.getDocuments().getFileName(HookupDocuments.INTERVIEW_FEEDBACK_3) == null) {
            fileKey = HookupDocuments.INTERVIEW_FEEDBACK_3;
        } else if (hookup.getDocuments().getFileName(HookupDocuments.INTERVIEW_FEEDBACK_4) == null) {
            fileKey = HookupDocuments.INTERVIEW_FEEDBACK_4;
        } else if (hookup.getDocuments().getFileName(HookupDocuments.INTERVIEW_FEEDBACK_5) == null) {
            fileKey = HookupDocuments.INTERVIEW_FEEDBACK_5;
        } else if (hookup.getDocuments().getFileName(HookupDocuments.INTERVIEW_FEEDBACK_6) == null) {
            fileKey = HookupDocuments.INTERVIEW_FEEDBACK_6;
        }
        return fileKey;
    }

    private static String findProbationDocEmptyCell(final Hookup hookup) {
        String fileKey = null;
        if (hookup.getDocuments().getFileName(HookupDocuments.PROBATION_FEEDBACK_0) == null) {
            fileKey = HookupDocuments.PROBATION_FEEDBACK_0;
        } else if (hookup.getDocuments().getFileName(HookupDocuments.PROBATION_FEEDBACK_1) == null) {
            fileKey = HookupDocuments.PROBATION_FEEDBACK_1;
        } else if (hookup.getDocuments().getFileName(HookupDocuments.PROBATION_FEEDBACK_2) == null) {
            fileKey = HookupDocuments.PROBATION_FEEDBACK_2;
        }
        return fileKey;
    }

    private static boolean storeFileData(final Hookup hookup, FilePart part, final String fileKey) {
        boolean successful = true;

        if (hookup.getDocuments() == null) {
            hookup.setDocuments(new HookupDocuments());
        }
        hookup.getDocuments().setFileName(fileKey, part.getFilename().replaceAll(" ", "_"));
        hookup.getDocuments().setFileHash(fileKey, FileService.FILE_HASH_PLACEHOLDER);
        hookup.getDocuments().setFileMimeType(fileKey, part.getContentType());
        hookup.getDocuments().setFileLength(fileKey, part.getFile().length());

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(part.getFile());
            fileService.storeFile(part.getContentType(), fis, new FileStorageCallback() {
                @Override
                public void onSuccess(String hash) {
                    hookup.getDocuments().setFileHash(fileKey, hash);
                    service.saveHookup(hookup);
                }

                @Override
                public void onError(String message) {
                    hookup.getDocuments().setFileHash(fileKey, null);
                    service.saveHookup(hookup);
                    Logger.error(message);
                }
            });
        } catch (Exception e) {
            successful = false;
            Logger.error("Error on file loading.", e);
        } finally {
            IOUtils.closeQuietly(fis);
        }

        FileUtils.deleteQuietly(part.getFile());

        return successful;
    }

    @play.db.jpa.Transactional()
    public static Result hookupFileDeleteAsync(Long hookupId, String type, String index) {

        Hookup hookup = service.loadHookup(hookupId, SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(hookup, "Hookup");

        if (!hookup.isArchived()) {

            String key = null;

            if ("resume".equals(type) || "testtask".equals(type)) {
                if ("resume".equals(type)) {
                    key = HookupDocuments.RESUME;
                } else {
                    key = HookupDocuments.TEST_TASK;
                }
                hookup.getDocuments().setFileHash(key, null);
                hookup.getDocuments().setFileName(key, null);
                hookup.getDocuments().setFileMimeType(key, null);
                hookup.getDocuments().setFileLength(key, null);
            } else {
                int indexToRemove = Integer.valueOf(index);
                if ("interview".equals(type)) {
                    permuteDocuments(hookup, indexToRemove, HookupDocuments.INTERVIEW_FEEDBACK_PREFIX, HookupDocuments.INTERVIEW_FEEDBACK_COUNT);
                } else if ("probation".equals(type)) {
                    key = HookupDocuments.PROBATION_FEEDBACK_PREFIX;
                    permuteDocuments(hookup, indexToRemove, HookupDocuments.PROBATION_FEEDBACK_PREFIX, HookupDocuments.PROBATION_FEEDBACK_COUNT);
                }
            }

            service.saveHookup(hookup);
        }

        return ok();
    }

    private static void permuteDocuments(Hookup hookup, int indexToRemove, String documentsPrefix, Integer documentsLimit) {
        String key;
        String nextKey;
        for (int i = indexToRemove; i < documentsLimit - 1; i++) {
            key = documentsPrefix + "_" + i;
            nextKey = documentsPrefix + "_" + (i + 1);
            if (hookup.getDocuments().getFileHash(key) == null) {
                break;
            } else {
                hookup.getDocuments().setFileHash(key, hookup.getDocuments().getFileHash(nextKey));
                hookup.getDocuments().setFileName(key, hookup.getDocuments().getFileName(nextKey));
                hookup.getDocuments().setFileMimeType(key, hookup.getDocuments().getFileMimeType(nextKey));
                hookup.getDocuments().setFileLength(key, hookup.getDocuments().getFileLength(nextKey));
            }
        }

        key = documentsPrefix + "_" + (documentsLimit - 1);
        hookup.getDocuments().setFileHash(key, null);
        hookup.getDocuments().setFileName(key, null);
        hookup.getDocuments().setFileMimeType(key, null);
        hookup.getDocuments().setFileLength(key, null);
    }

    static MenuConfiguration getMenuConfiguration() {
        return new MenuConfiguration("Vacancies");
    }

}
