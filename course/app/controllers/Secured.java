package controllers;

import org.profwell.security.web.SessionUtility;

import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return SessionUtility.getCurrentUsername();
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }

}