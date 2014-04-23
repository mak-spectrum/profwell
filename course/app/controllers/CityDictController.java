package controllers;

import java.util.List;
import java.util.Map;

import org.profwell.common.auxiliary.CityFilter;
import org.profwell.common.domain.CityForm;
import org.profwell.common.model.City;
import org.profwell.common.service.CityService;
import org.profwell.common.service.CityValidator;
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
public class CityDictController extends Controller {

    private static final String OBJECT_NAME = "City";

    @play.db.jpa.Transactional(readOnly = true)
    public static Result cityList() {

        CityService service = ServiceHolder.getService(CityService.class);

        CityFilter filter = prepareFilter();

        List<City> cities = service.listCity(filter);

        Content html = views.html.Dictionary.cityList.render(
                Routings.dictionariesMenu(),
                filter,
                cities);

        return ok(html);
    }

    private static CityFilter prepareFilter() {

        Map<String, String[]> reqParams = request().body().asFormUrlEncoded();

        CityFilter filter;
        if (reqParams != null) {
            filter = FilterUtility.createListFilter(reqParams,
                    CityFilter.class);

            filter.setValue(reqParams.get("value")[0]);
            filter.setCountryValue(reqParams.get("countryValue")[0]);
        } else {
            filter = new CityFilter();
        }
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        return filter;
    }

    public static Result cityCreate() {

        CityForm form = new CityForm();
        Content html = views.html.Dictionary.cityEdit.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result cityView(String id) {

        CityService service = ServiceHolder.getService(CityService.class);

        City city = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(city, OBJECT_NAME);

        CityForm form = new CityForm();
        form.transferFrom(city);

        Content html = views.html.Dictionary.cityView.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result cityEdit(String id) {

        CityService service = ServiceHolder.getService(CityService.class);

        City city = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(city, OBJECT_NAME);

        CityForm form = new CityForm();
        form.transferFrom(city);

        Content html = views.html.Dictionary.cityEdit.render(
                Routings.dictionariesMenu(),
                form);

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result cityEditSubmit() {

        CityService service = ServiceHolder.getService(CityService.class);

        Form<CityForm> playForm = Form.form(CityForm.class);

        CityForm form = playForm.bindFromRequest().get();

        if (ServiceHolder.getService(CityValidator.class)
                .validate(form)) {

            City city = null;
            if (form.isNew()) {
                city = new City();
            } else {
                city = service.getFromWorkspace(form.getId(),
                        SessionUtility.getCurrentUserId());
            }

            SecurityControl.checkObjectExists(city, OBJECT_NAME);

            Workspace wsp = ServiceHolder.getService(UserService.class)
                    .getWorkspace(SessionUtility.getCurrentUserId());
            form.transferTo(city);

            city.setWorkspace(wsp);
            service.save(city);

            return redirect(routes.CityDictController.cityView(
                    String.valueOf(city.getId())));
        } else {

            Content html = views.html.Dictionary.cityEdit.render(
                    Routings.dictionariesMenu(),
                    form);

            return badRequest(html);
        }
    }

    @play.db.jpa.Transactional
    public static Result cityDelete(String id) {
        CityService cityService = ServiceHolder.getService(CityService.class);

        City city = cityService.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(city, OBJECT_NAME);

        cityService.delete(city);
        return redirect(routes.CityDictController.cityList());
    }

}
