package controllers;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;
import org.profwell.common.auxiliary.CityFilter;
import org.profwell.common.auxiliary.SingleFieldFilter;
import org.profwell.common.model.City;
import org.profwell.common.model.Company;
import org.profwell.common.model.Position;
import org.profwell.common.model.Skill;
import org.profwell.common.service.CityService;
import org.profwell.common.service.CompanyService;
import org.profwell.common.service.PositionService;
import org.profwell.common.service.SkillService;
import org.profwell.conf.di.ServiceHolder;
import org.profwell.generic.model.Marker;
import org.profwell.marker.model.ContactMarker;
import org.profwell.marker.model.MarkerType;
import org.profwell.marker.service.MarkerService;
import org.profwell.person.auxiliary.PersonFilter;
import org.profwell.person.model.Person;
import org.profwell.person.service.PersonService;
import org.profwell.security.web.SessionUtility;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.PersonUtils;

@Security.Authenticated(Secured.class)
public class MarkerController extends Controller {

    @play.db.jpa.Transactional(readOnly = true)
    public static Result autocompletePosition(String snippet) {
        PositionService service = ServiceHolder.getService(PositionService.class);

        SingleFieldFilter filter = new SingleFieldFilter();
        filter.setValue(snippet);
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        List<Position> filteredMarkers = service.listPosition(filter);

        ArrayNode resultNode = new ArrayNode(JsonNodeFactory.instance);

        for (Position p : filteredMarkers) {
            resultNode.add(convertToJson(p));
        }

        return ok(resultNode);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result autocompleteCompany(String snippet) {
        CompanyService service = ServiceHolder.getService(CompanyService.class);

        SingleFieldFilter filter = new SingleFieldFilter();
        filter.setValue(snippet);
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        List<Company> filteredMarkers = service.listCompany(filter);

        ArrayNode resultNode = new ArrayNode(JsonNodeFactory.instance);

        for (Company c : filteredMarkers) {
            ObjectNode object = new ObjectNode(JsonNodeFactory.instance);

            object.put("label", c.getName());
            object.put("value", c.getName());
            object.put("details", c.getDetails());
            object.put("socialBenefits", c.getSocialBenefits());

            resultNode.add(object);
        }

        return ok(resultNode);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result autocompleteSkill(String snippet) {
        SkillService service = ServiceHolder.getService(SkillService.class);

        SingleFieldFilter filter = new SingleFieldFilter();
        filter.setValue(snippet);
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        List<Skill> filteredMarkers = service.listSkill(filter);

        ArrayNode resultNode = new ArrayNode(JsonNodeFactory.instance);

        for (Skill s : filteredMarkers) {
            resultNode.add(convertToJson(s));
        }

        return ok(resultNode);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result autocompleteCity(String country, String snippet) {
        CityService service = ServiceHolder.getService(CityService.class);

        CityFilter filter = new CityFilter();
        filter.setCountryValue(country);
        filter.setValue(snippet);
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());

        List<City> filteredMarkers = service.listCity(filter);

        ArrayNode resultNode = new ArrayNode(JsonNodeFactory.instance);

        for (City c : filteredMarkers) {
            ObjectNode object = new ObjectNode(JsonNodeFactory.instance);

            object.put("label", c.getName());
            object.put("value", c.getName());
            object.put("country", c.getCountry().getName());

            resultNode.add(object);
        }

        return ok(resultNode);
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result autocompletePerson(String term) {
        PersonService service = ServiceHolder.getService(PersonService.class);

        PersonFilter filter = new PersonFilter();
        filter.setWorkspaceId(SessionUtility.getCurrentUserId());
        filter.setName(term);
        List<Person> persons = service.listPerson(filter);

        ArrayNode resultNode = new ArrayNode(JsonNodeFactory.instance);

        for (Person p : persons) {
            ObjectNode object = new ObjectNode(JsonNodeFactory.instance);

            String fullName = PersonUtils.getFullName(p);
            StringBuilder label = new StringBuilder(fullName)
                .append(" (")
                .append(p.getProfession().getGeneralType().getCaption());
            if (StringUtils.isNotBlank(p.getShortNote())) {
                label.append(", ").append(p.getShortNote());
            }
            label.append(")");

            object.put("label", label.toString());
            object.put("value", fullName);
            object.put("personId", p.getId());

            resultNode.add(object);
        }

        return ok(resultNode);
    }

    public static Result autocompleteContactMarkers(String snippet) {
        MarkerService service = ServiceHolder.getService(MarkerService.class);

        List<ContactMarker> filteredMarkers =
                service.listMarker(MarkerType.CONTACT_TYPE_MARKER, snippet);

        ArrayNode resultNode = new ArrayNode(JsonNodeFactory.instance);

        for (ContactMarker m : filteredMarkers) {
            resultNode.add(convertToJson(m));
        }

        return ok(resultNode);
    }

    private static JsonNode convertToJson(Marker m) {
        ObjectNode object = new ObjectNode(JsonNodeFactory.instance);

        String label = m.getMarkerValue();
        if (m.isSystem()) {
            label += " (system)";
        }
        object.put("label", label);
        object.put("value", m.getMarkerValue());

        return object;
    }

}
