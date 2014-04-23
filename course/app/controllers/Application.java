package controllers;

import org.profwell.conf.di.ServiceHolder;
import org.profwell.security.domain.LoginForm;
import org.profwell.security.model.User;
import org.profwell.security.service.Authenticator;
import org.profwell.security.web.SessionUtility;

import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;


public class Application extends Controller {

    public static Result index() {
        return redirect(routes.DashboardController.open());
    }

    public static Result login() {
        return ok(views.html.login.render(new LoginForm()));
    }

    @Transactional(readOnly = true)
    public static Result authenticate() {

        Form<LoginForm> playForm = Form.form(LoginForm.class);
        LoginForm loginForm = playForm.bindFromRequest().get();

        User user = ServiceHolder.getService(Authenticator.class)
                .authenticate(loginForm);
        if (user == null) {
            return badRequest(views.html.login.render(loginForm));
        } else {
            SessionUtility.setCurrentUser(user);
            return redirect(routes.DashboardController.open());
        }
    }

    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.login());
    }

}
