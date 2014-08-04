package conf;

import static play.mvc.Results.badRequest;

import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.exception.FileNotFoundException;
import org.profwell.generic.exception.ObjectNotFoundException;
import org.profwell.security.model.Role;
import org.profwell.security.model.User;
import org.profwell.security.service.UserService;

import play.Application;
import play.GlobalSettings;
import play.api.templates.Html;
import play.db.jpa.JPA;
import play.libs.F.Callback0;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        JPA.withTransaction(new Callback0() {

            @Override
            public void invoke() throws Throwable {
                InitialData.insert();
            }
        });
    }

    @Override
    public Result onError(RequestHeader arg0, Throwable throwable) {
        if (throwable.getCause() instanceof ObjectNotFoundException) {
            ObjectNotFoundException onfe =
                    (ObjectNotFoundException) throwable.getCause();

            return badRequest(views.html.common.error.render(
                    "Profwell - Object not found",
                    "Requested object \"" + onfe.getObjectName() + "\" is not found."));
        } else if (throwable.getCause() instanceof FileNotFoundException) {
            FileNotFoundException fnfe =
                    (FileNotFoundException) throwable.getCause();

            return badRequest(views.html.common.error.render(
                    "Profwell - File not found", fnfe.getMessage()));
        } else {
            return super.onError(arg0, throwable);
        }
    }

    @Override
    public Result onBadRequest(RequestHeader arg0, String arg1) {
        return badRequest(views.html.common.error.render(
                "Profwell - Bad request",
                "Bad request."));
    }

    @Override
    public Result onHandlerNotFound(RequestHeader arg0) {
        return badRequest(actionNotFoundHtml());
    }

    public static Result forbidden() {
        return play.mvc.Results.forbidden(actionNotFoundHtml());
    }

    private static Html actionNotFoundHtml() {
        return views.html.common.error.render(
                "Profwell - Action not found",
                "Action for the requested address is not found.");
    }

    static class InitialData {

        public static void insert() {
            if (ServiceHolder.getService(UserService.class).countAll() == 0) {
                User admin = new User();

                admin.setUsername("hradmin@gmail.com");
                admin.setEmail("hradmin@gmail.com");
                admin.setFirstName("Admin");
                admin.setLastName("Adminchenko");
                admin.setPassword("secret1@");
                admin.setRole(Role.ADMINISTRATOR);

                ServiceHolder.getService(UserService.class).addNewUser(admin);

                User ivan = new User();

                ivan.setUsername("ivanchenko");
                ivan.setEmail("hradmin@gmail.com");
                ivan.setFirstName("Ivan");
                ivan.setLastName("Ivanchenko");
                ivan.setPassword("!2qwaszx");
                ivan.setRole(Role.HR_MANAGER);

                ServiceHolder.getService(UserService.class).addNewUser(ivan);

                User petr = new User();

                petr.setUsername("petrenko");
                petr.setEmail("hradmin@gmail.com");
                petr.setFirstName("Petr");
                petr.setLastName("Petrenko");
                petr.setPassword("!2qwaszx");
                petr.setRole(Role.HR_MANAGER);

                ServiceHolder.getService(UserService.class).addNewUser(petr);

                User vasiliy = new User();

                vasiliy.setUsername("vasilchenko");
                vasiliy.setEmail("hradmin@gmail.com");
                vasiliy.setFirstName("Vasiliy");
                vasiliy.setLastName("Vasilchenko");
                vasiliy.setPassword("!2qwaszx");
                vasiliy.setRole(Role.HR_MANAGER);

                ServiceHolder.getService(UserService.class).addNewUser(vasiliy);
            }
        }

    }

}