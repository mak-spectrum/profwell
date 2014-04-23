package controllers;

import org.profwell.ui.menu.MenuConfiguration;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

@Security.Authenticated(Secured.class)
public class Routings extends Controller {

    public static Result administration() {
        return ok(views.html.Administration.administration.render(
                administrationMenu()));
    }

    public static Result dictionaries() {
        return ok(views.html.Dictionary.dictionaries.render(
                dictionariesMenu()));
    }

    static MenuConfiguration administrationMenu() {
        return new MenuConfiguration("Administration");
    }

    static MenuConfiguration collaborationMenu() {
        return new MenuConfiguration("Collaboration");
    }

    static MenuConfiguration dictionariesMenu() {
        return new MenuConfiguration("Dictionaries");
    }

}
