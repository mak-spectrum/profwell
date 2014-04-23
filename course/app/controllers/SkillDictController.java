package controllers;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.domain.SkillForm;
import org.profwell.common.model.Skill;
import org.profwell.common.service.SkillService;
import org.profwell.common.service.SkillValidator;
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
public class SkillDictController extends Controller {

    private static final String OBJECT_NAME = "Skill";

    @play.db.jpa.Transactional(readOnly = true)
    public static Result skillList() {

        SkillService service = ServiceHolder.getService(SkillService.class);

        SingleFieldFilter filter = FilterUtility.createSingleFieldFilter(
                request().body().asFormUrlEncoded());
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        List<Skill> skills = service.listSkill(filter);

        Content html = views.html.Dictionary.skillList.render(
                Routings.dictionariesMenu(),
                filter,
                skills);

        return ok(html);
    }

    public static Result skillCreate() {

        SkillForm form = new SkillForm();
        Content html = views.html.Dictionary.skillEdit.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result skillView(String id) {

        SkillService service = ServiceHolder.getService(SkillService.class);

        Skill skill = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(skill, OBJECT_NAME);

        SkillForm form = new SkillForm();
        form.transferFrom(skill);

        Content html = views.html.Dictionary.skillView.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result skillEdit(String id) {

        SkillService service = ServiceHolder.getService(SkillService.class);

        Skill skill = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(skill, OBJECT_NAME);

        SkillForm form = new SkillForm();
        form.transferFrom(skill);

        Content html = views.html.Dictionary.skillEdit.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result skillEditSubmit() {

        SkillService service = ServiceHolder.getService(SkillService.class);

        Form<SkillForm> playForm = Form.form(SkillForm.class);

        SkillForm form = playForm.bindFromRequest().get();

        if (ServiceHolder.getService(SkillValidator.class)
                .validate(form)) {

            Skill skill = null;
            if (form.isNew()) {
                skill = new Skill();
            } else {
                skill = service.getFromWorkspace(form.getId(),
                        SessionUtility.getCurrentUserId());
            }
            SecurityControl.checkObjectExists(skill, OBJECT_NAME);

            Workspace wsp = ServiceHolder.getService(UserService.class)
                    .getWorkspace(SessionUtility.getCurrentUserId());
            form.transferTo(skill);

            skill.setWorkspace(wsp);
            service.save(skill);

            return redirect(routes.SkillDictController.skillView(
                    String.valueOf(skill.getId())));
        } else {

            Content html = views.html.Dictionary.skillEdit.render(
                    Routings.dictionariesMenu(),
                    form);

            return badRequest(html);
        }
    }

    @play.db.jpa.Transactional
    public static Result skillDelete(String id) {
        SkillService skillService = ServiceHolder.getService(SkillService.class);

        Skill skill = skillService.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(skill, OBJECT_NAME);

        skillService.delete(skill);
        return redirect(routes.SkillDictController.skillList());
    }

}
