package controllers;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.domain.PositionForm;
import org.profwell.common.model.Position;
import org.profwell.common.service.PositionService;
import org.profwell.common.service.PositionValidator;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.web.FilterUtility;
import org.profwell.security.model.Workspace;
import org.profwell.security.service.UserService;
import org.profwell.security.web.SessionUtility;

import play.data.Form;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.SecurityControl;

@Security.Authenticated(Secured.class)
public class PositionDictController extends Controller {

    private static final String OBJECT_NAME = "Position";

    @play.db.jpa.Transactional(readOnly = true)
    public static Result positionList() {

        PositionService service = ServiceHolder.getService(PositionService.class);

        SingleFieldFilter filter = FilterUtility.createSingleFieldFilter(
                request().body().asFormUrlEncoded());
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        List<Position> positions = service.listPosition(filter);

        Content html = views.html.Dictionary.positionList.render(
                Routings.dictionariesMenu(),
                filter,
                positions);

        return ok(html);
    }

    public static Result positionCreate() {

        PositionForm form = new PositionForm();
        Content html = views.html.Dictionary.positionEdit.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result positionView(String id) {

        PositionService service = ServiceHolder.getService(PositionService.class);

        Position position = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(position, OBJECT_NAME);

        PositionForm form = new PositionForm();
        form.transferFrom(position);

        Content html = views.html.Dictionary.positionView.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result positionEdit(String id) {

        PositionService service = ServiceHolder.getService(PositionService.class);

        Position position = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(position, OBJECT_NAME);

        PositionForm form = new PositionForm();
        form.transferFrom(position);

        Content html = views.html.Dictionary.positionEdit.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result positionEditSubmit() {

        PositionService service = ServiceHolder.getService(PositionService.class);

        Form<PositionForm> playForm = Form.form(PositionForm.class);

        PositionForm form = playForm.bindFromRequest().get();

        if (ServiceHolder.getService(PositionValidator.class)
                .validate(form)) {

            Position position = null;
            if (form.isNew()) {
                position = new Position();
            } else {
                position = service.getFromWorkspace(form.getId(),
                        SessionUtility.getCurrentUserId());
            }
            SecurityControl.checkObjectExists(position, OBJECT_NAME);

            Workspace wsp = ServiceHolder.getService(UserService.class)
                    .getWorkspace(SessionUtility.getCurrentUserId());
            form.transferTo(position);

            position.setWorkspace(wsp);
            service.save(position);

            return redirect(routes.PositionDictController.positionView(
                    String.valueOf(position.getId())));
        } else {

            Content html = views.html.Dictionary.positionEdit.render(
                    Routings.dictionariesMenu(),
                    form);

            return badRequest(html);
        }
    }

    @play.db.jpa.Transactional
    public static Result positionDelete(String id) {
        PositionService positionService = ServiceHolder.getService(PositionService.class);

        Position position = positionService.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(position, OBJECT_NAME);

        positionService.delete(position);
        return redirect(routes.PositionDictController.positionList());
    }

}
