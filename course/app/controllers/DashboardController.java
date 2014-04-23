package controllers;

import org.profwell.dashboard.domain.DashboardForm;
import org.profwell.ui.menu.MenuConfiguration;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class DashboardController extends Controller {

    public static Result open() {
        DashboardForm form = new DashboardForm();

        return ok(views.html.Dashboard.dashboard.render(
                getMenuConfiguration(),
                form));
    }

    static MenuConfiguration getMenuConfiguration() {
        return new MenuConfiguration("Dashboard");
    }

}
