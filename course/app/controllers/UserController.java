package controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.validation.ValidationMessageBuilder;
import org.profwell.generic.web.FilterUtility;
import org.profwell.json.ConvertionUtils;
import org.profwell.security.auxiliary.UserFilter;
import org.profwell.security.domain.PasswordForm;
import org.profwell.security.domain.UserForm;
import org.profwell.security.model.User;
import org.profwell.security.service.PasswordValidator;
import org.profwell.security.service.UserService;
import org.profwell.security.service.UserValidator;
import org.profwell.security.web.SessionUtility;
import org.profwell.ui.menu.MenuConfiguration;

import play.data.Form;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.SecurityControl;
import conf.Global;

@Security.Authenticated(Secured.class)
public class UserController extends Controller {

    private static final String OBJECT_NAME = "User";

    @play.db.jpa.Transactional(readOnly = true)
    public static Result userList() {

        if (!SecurityControl.admin()) {
            return Global.forbidden();
        }

        UserService service = ServiceHolder.getService(UserService.class);

        UserFilter filter = prepareFilter();
        List<User> users = service.listUser(filter);

        Content html = views.html.Administration.userList.render(
                getMenuConfiguration(),
                filter,
                users);

        return ok(html);
    }

    private static UserFilter prepareFilter() {
        Map<String, String[]> requestParams =
                request().body().asFormUrlEncoded();

        UserFilter filter;
        if (requestParams != null) {
            filter = FilterUtility.createListFilter(requestParams,
                    UserFilter.class);

            filter.setName(requestParams.get("name")[0]);
            filter.setUsername(requestParams.get("username")[0]);
            filter.setRoleValue(requestParams.get("roleValue")[0]);
        } else {
            filter = new UserFilter();
        }

        return filter;
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result userView(String id) {

        if (!SecurityControl.admin()) {
            return Global.forbidden();
        }

        UserService service = ServiceHolder.getService(UserService.class);

        User user = service.get(Long.parseLong(id));

        SecurityControl.checkObjectExists(user, OBJECT_NAME);

        UserForm form = new UserForm();
        form.transferFrom(user);

        if (user.getTimeoutStamp() != null) {;            Calendar calendar = Calendar.getInstance();
            calendar.setTime(user.getTimeoutStamp());
            calendar.add(Calendar.HOUR, 1);

            form.setTimeoutStampTo(SimpleDateFormat.getDateTimeInstance()                    .format(calendar.getTime()));
        };

        Content html = views.html.Administration.userView.render(
                getMenuConfiguration(),
                form,
                SessionUtility.isCurrentUserId(id));

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result unblockTimeouted(String id) {

        if (!SecurityControl.admin()) {
            return Global.forbidden();
        }

        UserService service = ServiceHolder.getService(UserService.class);

        User user = service.get(Long.parseLong(id));

        SecurityControl.checkObjectExists(user, OBJECT_NAME);

        user.setTimeoutStamp(null);
        service.save(user);

        return redirect(routes.UserController.userView(id));
    }

    @play.db.jpa.Transactional
    public static Result userPasswordChangeSubmitAsync() {

        Form<PasswordForm> playForm = Form.form(PasswordForm.class);
        PasswordForm passwordForm = playForm.bindFromRequest().get();

        if (!SecurityControl.admin()
                && !SessionUtility.isCurrentUserId(passwordForm.getId())) {
            return Global.forbidden();
        }

        UserService service = ServiceHolder.getService(UserService.class);

        ValidationContext vc = new ValidationContext();
        passwordForm.setVc(vc);

        if (ServiceHolder.getService(PasswordValidator.class)
                .validateUserPasswordOnChange(passwordForm, vc)) {

            service.changeUserPassword(passwordForm.getId(),
                    passwordForm.getPassword());

            vc.add(new ValidationMessageBuilder()
                    .source("generalMessage")
                    .info()
                    .message("Password has been changed successfully.")
                    .build());

            ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
            response.put("validationContext",
                    ConvertionUtils.convertToJson(vc));
            return ok(response);

        } else {
            ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
            response.put("validationContext",
                    ConvertionUtils.convertToJson(vc));
            return badRequest(response);
        }
    }

    public static Result userCreate() {

        if (!SecurityControl.admin()) {
            return Global.forbidden();
        }

        UserForm form = new UserForm();
        Content html = views.html.Administration.userEdit.render(getMenuConfiguration(),
                        form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result userEdit(String id) {

        if (!SecurityControl.admin()) {
            return Global.forbidden();
        }

        UserService service = ServiceHolder.getService(UserService.class);

        User user = service.get(Long.parseLong(id));

        SecurityControl.checkObjectExists(user, OBJECT_NAME);

        UserForm userForm = new UserForm();
        userForm.transferFrom(user);

        Content html = views.html.Administration.userEdit.render(getMenuConfiguration(),
                userForm);

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result userEditSubmit() {

        if (!SecurityControl.admin()) {
            return Global.forbidden();
        }

        UserService service = ServiceHolder.getService(UserService.class);

        Form<UserForm> playForm = Form.form(UserForm.class);

        UserForm userForm = playForm.bindFromRequest().get();

        if (ServiceHolder.getService(UserValidator.class)
                .validate(userForm)) {

            User user = null;
            if (userForm.isNew()) {
                user = new User();
            } else {
                user = service.get(userForm.getId());
            }

            SecurityControl.checkObjectExists(user, OBJECT_NAME);

            if (userForm.isNew()) {
                userForm.transferToOnCreation(user);
                service.addNewUser(user);
            } else {
                userForm.transferTo(user);
                service.save(user);
            }

            return redirect(routes.UserController
                    .userView(String.valueOf(user.getId())));
        } else {

            Content html = views.html.Administration.userEdit.render(getMenuConfiguration(),
                    userForm);

            return badRequest(html);
        }
    }

    @play.db.jpa.Transactional
    public static Result userDelete(String id) {

        if (!SecurityControl.admin()) {
            return Global.forbidden();
        }

        UserForm form = new UserForm();

        if (ServiceHolder.getService(UserValidator.class)
                .validateCurrentUserDeletion(
                        SessionUtility.getCurrentUsername(),
                        form)) {

            UserService userService = ServiceHolder
                    .getService(UserService.class);

            User user = userService.get(Long.parseLong(id));

            SecurityControl.checkObjectExists(user, OBJECT_NAME);

            userService.delete(user);
            return redirect(routes.UserController.userList());
        } else {
            Content html = views.html.Administration.userView.render(
                    getMenuConfiguration(),
                    form,
                    SessionUtility.isCurrentUserId(id));

            return badRequest(html);
        }
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result accountView() {

        UserService service = ServiceHolder.getService(UserService.class);

        User user = service.get(SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(user, OBJECT_NAME);

        UserForm form = new UserForm();
        form.transferFrom(user);

        Content html = views.html.Administration.accountView.render(
                getMenuConfiguration(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result accountEdit() {

        UserService service = ServiceHolder.getService(UserService.class);

        User user = service.get(SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(user, OBJECT_NAME);

        UserForm form = new UserForm();
        form.transferFrom(user);

        Content html = views.html.Administration.accountEdit.render(
                getMenuConfiguration(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result accountEditSubmit() {

        UserService service = ServiceHolder.getService(UserService.class);

        Form<UserForm> playForm = Form.form(UserForm.class);

        UserForm userForm = playForm.bindFromRequest().get();
        userForm.setNoRole(true);

        User user = service.get(userForm.getId());

        SecurityControl.checkObjectExists(user, OBJECT_NAME);

        if (ServiceHolder.getService(UserValidator.class)
                .validate(userForm)) {

            userForm.transferToMin(user);
            service.save(user);

            return redirect(routes.UserController.accountView());
        } else {

            Content html = views.html.Administration.accountEdit.render(
                    getMenuConfiguration(),
                    userForm);

            return badRequest(html);
        }
    }

    static MenuConfiguration getMenuConfiguration() {
        return Routings.administrationMenu();
    }
}
