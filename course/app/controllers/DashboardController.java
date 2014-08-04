package controllers;

import java.util.List;
import java.util.Map;

import org.profwell.conf.di.ServiceHolder;
import org.profwell.dashboard.domain.DashboardForm;
import org.profwell.generic.web.FilterUtility;
import org.profwell.notification.auxiliary.NoticeFilter;
import org.profwell.notification.model.Notice;
import org.profwell.notification.service.NoticeService;
import org.profwell.security.web.SessionUtility;
import org.profwell.ui.menu.MenuConfiguration;

import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class DashboardController extends Controller {

    private static NoticeService noticeService = ServiceHolder.getService(NoticeService.class);

    @play.db.jpa.Transactional(readOnly = true)
    public static Result open() {
        List<Notice> messages = noticeService.loadMessages(SessionUtility.getCurrentUser());
        DashboardForm form = new DashboardForm();
        for(int i = 0; i < messages.size(); i++) {
            Notice notice = messages.get(i);
            form.getMessages().add(notice);
        }

        return ok(views.html.Dashboard.dashboard.render(
                getMenuConfiguration(),
                form));
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

    @play.db.jpa.Transactional
    public static Result noticeMarkAllAsRead() {
        noticeService.markAllAsRead(SessionUtility.getCurrentUserId());
        return ok();
    }

}
