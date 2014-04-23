package controllers;

import java.util.List;

import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.domain.CompanyForm;
import org.profwell.common.model.Company;
import org.profwell.common.service.CompanyService;
import org.profwell.common.service.CompanyValidator;
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
public class CompanyDictController extends Controller {

    private static final String OBJECT_NAME = "Company";

    @play.db.jpa.Transactional(readOnly = true)
    public static Result companyList() {

        CompanyService service = ServiceHolder.getService(CompanyService.class);

        SingleFieldFilter filter = FilterUtility.createSingleFieldFilter(
                request().body().asFormUrlEncoded());
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        List<Company> companies = service.listCompany(filter);

        Content html = views.html.Dictionary.companyList.render(
                Routings.dictionariesMenu(),
                filter,
                companies);

        return ok(html);
    }

    public static Result companyCreate() {

        CompanyForm form = new CompanyForm();
        Content html = views.html.Dictionary.companyEdit.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result companyView(String id) {

        CompanyService service = ServiceHolder.getService(CompanyService.class);

        Company company = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(company, OBJECT_NAME);

        CompanyForm form = new CompanyForm();
        form.transferFrom(company);

        Content html = views.html.Dictionary.companyView.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result companyEdit(String id) {

        CompanyService service = ServiceHolder.getService(CompanyService.class);

        Company company = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(company, OBJECT_NAME);

        CompanyForm form = new CompanyForm();
        form.transferFrom(company);

        Content html = views.html.Dictionary.companyEdit.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result companyEditSubmit() {

        CompanyService service = ServiceHolder.getService(CompanyService.class);

        Form<CompanyForm> playForm = Form.form(CompanyForm.class);

        CompanyForm form = playForm.bindFromRequest().get();

        if (ServiceHolder.getService(CompanyValidator.class)
                .validate(form)) {

            Company company = null;
            if (form.isNew()) {
                company = new Company();
            } else {
                company = service.getFromWorkspace(form.getId(),
                        SessionUtility.getCurrentUserId());
            }
            SecurityControl.checkObjectExists(company, OBJECT_NAME);

            Workspace wsp = ServiceHolder.getService(UserService.class)
                    .getWorkspace(SessionUtility.getCurrentUserId());
            form.transferTo(company);

            company.setWorkspace(wsp);
            service.save(company);

            return redirect(routes.CompanyDictController.companyView(
                    String.valueOf(company.getId())));
        } else {

            Content html = views.html.Dictionary.companyEdit.render(
                    Routings.dictionariesMenu(),
                    form);

            return badRequest(html);
        }
    }

    @play.db.jpa.Transactional
    public static Result companyDelete(String id) {
        CompanyService companyService = ServiceHolder.getService(CompanyService.class);

        Company company = companyService.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(company, OBJECT_NAME);

        companyService.delete(company);
        return redirect(routes.CompanyDictController.companyList());
    }

}
