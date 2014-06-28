package controllers;

import static utils.DashboardDateUtils.EIGHT_WEEKS;
import static utils.DashboardDateUtils.FOUR_WEEKS;
import static utils.DashboardDateUtils.NOW;
import static utils.DashboardDateUtils.TWO_WEEKS;
import static utils.DashboardDateUtils.isLessThan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.profwell.conf.di.ServiceHolder;
import org.profwell.dashboard.domain.DashboardCategoryDTO;
import org.profwell.dashboard.domain.DashboardForm;
import org.profwell.dashboard.domain.DashboardHookupDTO;
import org.profwell.dashboard.domain.DashboardVacancyDTO;
import org.profwell.generic.web.FilterUtility;
import org.profwell.notification.auxiliary.NoticeFilter;
import org.profwell.notification.model.Notice;
import org.profwell.notification.service.NoticeService;
import org.profwell.security.web.SessionUtility;
import org.profwell.ui.menu.MenuConfiguration;
import org.profwell.vacancy.auxiliary.VacancyFilter;
import org.profwell.vacancy.model.Hookup;
import org.profwell.vacancy.model.Vacancy;
import org.profwell.vacancy.model.VacancyStatus;
import org.profwell.vacancy.service.VacancyService;

import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class DashboardController extends Controller {

    private static final int SUDDENLY = 0;

    private static final int LESS_THAN_IN_TWO_WEEKS = 1;

    private static final int LESS_THAN_IN_FOUR_WEEKS = 2;

    private static final int LESS_THAN_IN_EIGHT_WEEKS = 3;

    private static final int MORE_THAN_EIGHT_WEEKS = 4;

    private static NoticeService noticeService = ServiceHolder.getService(NoticeService.class);

    private static VacancyService vacancyService = ServiceHolder.getService(VacancyService.class);

    @play.db.jpa.Transactional(readOnly = true)
    public static Result open() {

        List<Notice> messages = noticeService.loadMessages(SessionUtility.getCurrentUser());
        DashboardForm form = new DashboardForm();
        form.setMessages(new ArrayList<String>());
        for(Notice notice : messages) {
            form.getMessages().add(notice.getText());
        }

        VacancyFilter filter = new VacancyFilter();
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());
        filter.setStatus(VacancyStatus.OPENED);
        filter.setPageSize(50);
        filter.setOrderBy("dueDate");

        List<Vacancy> vacancies = vacancyService.listArchivedVacancies(filter);

        form.setVacancyData(transformVacanciesData(vacancies));

        return ok(views.html.Dashboard.dashboard.render(
                getMenuConfiguration(),
                form));
    }

    private static List<DashboardCategoryDTO> transformVacanciesData(
            List<Vacancy> vacancies) {
        Date currentDate = new Date();

        Map<Integer, List<Vacancy>> map = new HashMap<>();
        map.put(SUDDENLY, new ArrayList<Vacancy>());
        map.put(LESS_THAN_IN_TWO_WEEKS, new ArrayList<Vacancy>());
        map.put(LESS_THAN_IN_FOUR_WEEKS, new ArrayList<Vacancy>());
        map.put(LESS_THAN_IN_EIGHT_WEEKS, new ArrayList<Vacancy>());
        map.put(MORE_THAN_EIGHT_WEEKS, new ArrayList<Vacancy>());

        for (Vacancy v : vacancies) {

            if (v.getDueDate() == null) {
                map.get(MORE_THAN_EIGHT_WEEKS).add(v);
                continue;
            }

            if (isLessThan(NOW, currentDate, v.getDueDate())) {
                map.get(SUDDENLY).add(v);
            } else if (isLessThan(TWO_WEEKS, currentDate, v.getDueDate())) {
                map.get(LESS_THAN_IN_TWO_WEEKS).add(v);
            } else if (isLessThan(FOUR_WEEKS, currentDate, v.getDueDate())) {
                map.get(LESS_THAN_IN_FOUR_WEEKS).add(v);
            } else if (isLessThan(EIGHT_WEEKS, currentDate, v.getDueDate())) {
                map.get(LESS_THAN_IN_EIGHT_WEEKS).add(v);
            } else {
                map.get(MORE_THAN_EIGHT_WEEKS).add(v);
            }
        }

        for (Entry<Integer, List<Vacancy>> ent : map.entrySet()) {
            Collections.sort(ent.getValue(), new Comparator<Vacancy>() {
                @Override
                public int compare(Vacancy v1, Vacancy v2) {
                    if (v1.getPriority() == v2.getPriority()) {
                        if (v1.getDueDate() == null && v2.getDueDate() == null) {
                            return 0;
                        } else if (v1.getDueDate() == null && v2.getDueDate() != null) {
                            return 1;
                        } else if (v1.getDueDate() != null && v2.getDueDate() == null) {
                            return -1;
                        } else {
                            return v1.getDueDate().compareTo(v2.getDueDate());
                        }
                    } else {
                        return Integer.valueOf(v1.getPriority().ordinal())
                                .compareTo(v2.getPriority().ordinal());
                    }
                }
            });
        }

        List<DashboardCategoryDTO> categories = new ArrayList<>(5);

        categories.add(transformCategory("Overdue", "overdue", map.get(SUDDENLY)));
        categories.add(transformCategory("Less than Two weeks", "weeks", map.get(LESS_THAN_IN_TWO_WEEKS)));
        categories.add(transformCategory("Less than Four weeks", "weeks", map.get(LESS_THAN_IN_FOUR_WEEKS)));
        categories.add(transformCategory("Less than Eight weeks", "weeks", map.get(LESS_THAN_IN_EIGHT_WEEKS)));
        categories.add(transformCategory("Eight Weeks", "more-weeks", map.get(MORE_THAN_EIGHT_WEEKS)));

        if (checkIfEmpty(categories)) {
            categories.clear();
        }

        return categories;
    }

    private static DashboardCategoryDTO transformCategory(
            String caption,
            String style,
            List<Vacancy> vacancies) {

        DashboardCategoryDTO categoryDTO = new DashboardCategoryDTO(caption, style);
        categoryDTO.setVacancies(new ArrayList<DashboardVacancyDTO>());

        for (Vacancy v : vacancies) {

            DashboardVacancyDTO dto = new DashboardVacancyDTO();
            dto.setPositionCaption(v.getPosition().getCaption());
            dto.setProjectName(v.getPosition().getProjectName());
            dto.setCompanyName(v.getCompany().getName());
            dto.setOpeningDate(v.getOpeningDatetime());
            dto.setDueDate(v.getDueDate());
            dto.setPriority(v.getPriority());

            for  (Hookup h : v.getHookups()) {
                DashboardHookupDTO hdto = new DashboardHookupDTO();
                hdto.setCurrentCompany(h.getCandidate().getCurrentCompany());
                hdto.setCurrentPosition(h.getCandidate().getCurrentPosition());
                hdto.setPersonFirstName(h.getCandidate().getPersonFirstName());
                hdto.setPersonSecondName(h.getCandidate().getPersonSecondName());
                hdto.setPersonLastName(h.getCandidate().getPersonLastName());
                hdto.setLastActivityOn(h.getLastActivityOn());
                hdto.setStatus(h.getStatus());
                dto.getHookups().add(hdto);
            }

            categoryDTO.getVacancies().add(dto);
        }

        return categoryDTO;
    }

    private static boolean checkIfEmpty(List<DashboardCategoryDTO> categories) {
        for (DashboardCategoryDTO dto : categories) {
            if (dto.isNotEmpty()) {
                return false;
            }
        }
        return true;
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result noticeList() {

        NoticeFilter filter = prepareFilter();
        List<Notice> notices = noticeService.listNotice(filter);

        Content html = views.html.Notification.noticeList.render(
                getMenuConfiguration(),
                filter,
                notices);

        return ok(html);
    }

    private static NoticeFilter prepareFilter() {

        Map<String, String[]> reqParams = request().body().asFormUrlEncoded();

        NoticeFilter filter;
        if (reqParams != null) {
            filter = FilterUtility.createListFilter(reqParams,
                    NoticeFilter.class);

            String valueFromBrowser = reqParams.get("text")[0];
            filter.setText(valueFromBrowser);
        } else {
            filter = new NoticeFilter();
        }
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        return filter;
    }

    @play.db.jpa.Transactional
    public static Result noticeReadAsync(Long noticeId) {
        noticeService.noticeRead(noticeId);
        return ok();
    }



    static MenuConfiguration getMenuConfiguration() {
        return new MenuConfiguration("Dashboard");
    }

}
