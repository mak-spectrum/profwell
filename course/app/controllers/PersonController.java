package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.validation.ValidationContext;
import org.profwell.generic.web.FilterUtility;
import org.profwell.json.ConvertionUtils;
import org.profwell.person.auxiliary.PersonFilter;
import org.profwell.person.domain.ContactForm;
import org.profwell.person.domain.PersonForm;
import org.profwell.person.model.Person;
import org.profwell.person.service.PersonService;
import org.profwell.person.service.PersonValidator;
import org.profwell.security.model.Workspace;
import org.profwell.security.service.UserService;
import org.profwell.security.web.SessionUtility;
import org.profwell.ui.menu.MenuConfiguration;

import play.data.Form;
import play.mvc.Content;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.PersonUtils;
import utils.SecurityControl;

@Security.Authenticated(Secured.class)
public class PersonController extends Controller {

    private static final String OBJECT_NAME = "Person";

    @play.db.jpa.Transactional(readOnly = true)
    public static Result personList() {

        PersonService service = ServiceHolder.getService(PersonService.class);

        PersonFilter filter = prepareFilter();
        List<Person> persons = service.listPerson(filter);

        Content html = views.html.Person.personList.render(
                getMenuConfiguration(),
                filter,
                persons);

        return ok(html);
    }

    private static PersonFilter prepareFilter() {

        Map<String, String[]> reqParams = request().body().asFormUrlEncoded();

        PersonFilter filter;
        if (reqParams != null) {
            filter = FilterUtility.createListFilter(reqParams,
                    PersonFilter.class);

            filter.setName(reqParams.get("name")[0]);
            filter.setProfessionValue(reqParams.get("professionValue")[0]);
        } else {
            filter = new PersonFilter();
        }
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        return filter;
    }

    public static Result personCreate() {

        PersonForm person = new PersonForm();
        Content html = views.html.Person.personEdit.render(getMenuConfiguration(),
                        person);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result personView(String id) {

        PersonService service = ServiceHolder.getService(PersonService.class);

        Person person = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(person, OBJECT_NAME);

        PersonForm personDTO = new PersonForm();
        personDTO.transferFrom(person);

        Content html = views.html.Person.personView.render(getMenuConfiguration(),
                personDTO);

        return ok(html);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result personEdit(String id) {

        PersonService service = ServiceHolder.getService(PersonService.class);

        Person person = service.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(person, OBJECT_NAME);

        PersonForm personDTO = new PersonForm();
        personDTO.transferFrom(person);

        Content html = views.html.Person.personEdit.render(getMenuConfiguration(),
                personDTO);

        return ok(html);
    }

    @play.db.jpa.Transactional
    public static Result personEditSubmit() {

        PersonService service = ServiceHolder.getService(PersonService.class);

        Form<PersonForm> playForm = Form.form(PersonForm.class);

        PersonForm personForm = playForm.bindFromRequest().get();

        transferContactData(personForm);

        if (ServiceHolder.getService(PersonValidator.class)
                .validate(personForm)) {

            Person person = null;
            if (personForm.isNew()) {
                person = new Person();
            } else {
                person = service.getFromWorkspace(personForm.getId(),
                        SessionUtility.getCurrentUserId());
            }
            SecurityControl.checkObjectExists(person, OBJECT_NAME);

            Workspace wsp = ServiceHolder.getService(UserService.class)
                    .getWorkspace(SessionUtility.getCurrentUserId());
            personForm.transferTo(person);

            person.setWorkspace(wsp);
            service.save(person);

            return redirect(routes.PersonController.personView(
                    String.valueOf(person.getId())));
        } else {

            Content html = views.html.Person.personEdit.render(getMenuConfiguration(),
                    personForm);

            return badRequest(html);
        }
    }

    @play.db.jpa.Transactional
    public static Result personEditSubmitAsync() {

        PersonService service = ServiceHolder.getService(PersonService.class);

        Form<PersonForm> playForm = Form.form(PersonForm.class);

        PersonForm form = playForm.bindFromRequest().get();

        transferContactData(form);

        ValidationContext vc = new ValidationContext();
        form.setVc(vc);

        if (ServiceHolder.getService(PersonValidator.class)
                .validate(form)) {

            Person person = null;
            if (form.isNew()) {
                person = new Person();
            } else {
                person = service.getFromWorkspace(form.getId(),
                        SessionUtility.getCurrentUserId());
            }
            SecurityControl.checkObjectExists(person, OBJECT_NAME);

            Workspace wsp = ServiceHolder.getService(UserService.class)
                    .getWorkspace(SessionUtility.getCurrentUserId());
            form.transferTo(person);

            person.setWorkspace(wsp);
            service.save(person);

            ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
            response.put("personId", person.getId());
            response.put("personName", PersonUtils.getFullName(person));
            return ok(response);

        } else {
            ObjectNode response = new ObjectNode(JsonNodeFactory.instance);
            response.put("validationContext",
                    ConvertionUtils.convertToJson(vc));
            return badRequest(response);
        }
    }

    private static void transferContactData(PersonForm personDTO) {
        ContactForm contactDto;

        Map<String, ContactForm> contactsMap = new HashMap<>();

        Map<String, String[]> requestParams = request().body().asFormUrlEncoded();
        for (Entry<String, String[]> ent : requestParams.entrySet()) {
            if (ent.getKey().startsWith("contact:")) {

                String number = ent.getKey().substring(ent.getKey().lastIndexOf(':') + 1);
                contactDto = contactsMap.get(number);
                if (contactDto == null) {
                    contactDto = new ContactForm();
                    contactsMap.put(number, contactDto);
                }

                if (ent.getKey().contains("id")) {
                    contactDto.setIdValue(ent.getValue()[0]);
                }
                if (ent.getKey().contains("type")) {
                    contactDto.setContactType(ent.getValue()[0]);
                }
                if (ent.getKey().contains("value")) {
                    contactDto.setValue(ent.getValue()[0]);
                }
                if (ent.getKey().contains("primary")) {
                    contactDto.setPrimary(true);
                }

            }
        }

        List<ContactForm> sortedList = new ArrayList<>();
        for (Entry<String, ContactForm> ent : contactsMap.entrySet()) {
            ent.getValue().setIndexValue(ent.getKey());
            sortedList.add(ent.getValue());
        }

        Collections.sort(sortedList, new Comparator<ContactForm>() {
            @Override
            public int compare(ContactForm o1, ContactForm o2) {
                return Integer.valueOf(o1.getIndex())
                        .compareTo(Integer.valueOf(o2.getIndex()));
            }
        });

        personDTO.getContacts().addAll(sortedList);
    }

    @play.db.jpa.Transactional
    public static Result personDelete(String id) {
        PersonService personService = ServiceHolder.getService(PersonService.class);

        Person person = personService.getFromWorkspace(Long.parseLong(id),
                SessionUtility.getCurrentUserId());

        SecurityControl.checkObjectExists(person, OBJECT_NAME);

        personService.delete(person);
        return redirect(routes.PersonController.personList());
    }

    static MenuConfiguration getMenuConfiguration() {
        return new MenuConfiguration("Contact Book");
    }

}
