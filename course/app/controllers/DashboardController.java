package controllers;

import java.util.List;

import org.profwell.conf.di.ServiceHolder;
import org.profwell.dashboard.domain.DashboardForm;
import org.profwell.notification.model.Notice;
import org.profwell.notification.service.NoticeService;
import org.profwell.security.web.SessionUtility;
import org.profwell.ui.menu.MenuConfiguration;

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
        for(Notice notice : messages) {
            form.getMessages().add(notice.getText());
        }

        return ok(views.html.Dashboard.dashboard.render(
                getMenuConfiguration(),
                form));
    }

    static MenuConfiguration getMenuConfiguration() {
        return new MenuConfiguration("Dashboard");
    }

}
