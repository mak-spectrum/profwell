package org.profwell.conf.di;

import java.util.HashMap;
import java.util.Map;

import org.profwell.collaboration.dao.CollaborationAgreementDAO;
import org.profwell.collaboration.dao.CollaborationAgreementDAOImpl;
import org.profwell.collaboration.dao.CollaborationRequestDAO;
import org.profwell.collaboration.dao.CollaborationRequestDAOImpl;
import org.profwell.collaboration.service.CollaborationService;
import org.profwell.collaboration.service.CollaborationServiceImpl;
import org.profwell.common.dao.CityDAO;
import org.profwell.common.dao.CityDAOImpl;
import org.profwell.common.dao.CompanyDAO;
import org.profwell.common.dao.CompanyDAOImpl;
import org.profwell.common.dao.PositionDAO;
import org.profwell.common.dao.PositionDAOImpl;
import org.profwell.common.dao.SkillDAO;
import org.profwell.common.dao.SkillDAOImpl;
import org.profwell.common.service.CityService;
import org.profwell.common.service.CityServiceImpl;
import org.profwell.common.service.CityValidator;
import org.profwell.common.service.CompanyService;
import org.profwell.common.service.CompanyServiceImpl;
import org.profwell.common.service.CompanyValidator;
import org.profwell.common.service.PositionService;
import org.profwell.common.service.PositionServiceImpl;
import org.profwell.common.service.PositionValidator;
import org.profwell.common.service.SkillService;
import org.profwell.common.service.SkillServiceImpl;
import org.profwell.common.service.SkillValidator;
import org.profwell.file.service.FileService;
import org.profwell.file.service.FileServiceImpl;
import org.profwell.marker.service.MarkerService;
import org.profwell.marker.service.MarkerServiceImpl;
import org.profwell.notification.dao.NoticeDAOImpl;
import org.profwell.notification.service.NoticeService;
import org.profwell.notification.service.NoticeServiceImpl;
import org.profwell.person.dao.PersonDAO;
import org.profwell.person.dao.PersonDAOImpl;
import org.profwell.person.service.PersonService;
import org.profwell.person.service.PersonServiceImpl;
import org.profwell.person.service.PersonValidator;
import org.profwell.security.dao.UserDAO;
import org.profwell.security.dao.UserDAOImpl;
import org.profwell.security.service.Authenticator;
import org.profwell.security.service.PasswordValidator;
import org.profwell.security.service.UserService;
import org.profwell.security.service.UserServiceImpl;
import org.profwell.security.service.UserValidator;
import org.profwell.vacancy.dao.VacancyDAO;
import org.profwell.vacancy.dao.VacancyDAOImpl;
import org.profwell.vacancy.service.HookupLifecycle;
import org.profwell.vacancy.service.HookupValidator;
import org.profwell.vacancy.service.VacancyService;
import org.profwell.vacancy.service.VacancyServiceImpl;
import org.profwell.vacancy.service.VacancyValidator;

public final class ServiceHolder {

    private static final ServiceHolder INSTANCE = new ServiceHolder();

    private Map<String, Object> services = new HashMap<>();

    private ServiceHolder() {

        utilities();

        commonData();

        collaborationRelatedData();

        personRelated();

        MarkerServiceImpl markerService = new MarkerServiceImpl();
        this.services.put(MarkerService.class.getSimpleName(), markerService);

        NoticeServiceImpl noticeService = new NoticeServiceImpl();
        NoticeDAOImpl noticeDAO = new NoticeDAOImpl();
        noticeService.setDao(noticeDAO);
        this.services.put(NoticeService.class.getSimpleName(), noticeService);

        userRelated();


        vacancyRelated();
    }

    private void utilities() {
        FileService fileService = new FileServiceImpl();
        this.services.put(FileService.class.getSimpleName(), fileService);
    }

    private void commonData() {
        SkillServiceImpl skillService = new SkillServiceImpl();
        SkillDAO skillDAO = new SkillDAOImpl();
        skillService.setDao(skillDAO);
        this.services.put(SkillService.class.getSimpleName(),
                skillService);

        this.services.put(SkillValidator.class.getSimpleName(),
                new SkillValidator());



        CityServiceImpl cityService = new CityServiceImpl();
        CityDAO cityDAO = new CityDAOImpl();
        cityService.setDao(cityDAO);
        this.services.put(CityService.class.getSimpleName(), cityService);

        this.services.put(CityValidator.class.getSimpleName(),
                new CityValidator());



        CompanyServiceImpl companyService = new CompanyServiceImpl();
        CompanyDAO companyDAO = new CompanyDAOImpl();
        companyService.setDao(companyDAO);
        this.services.put(CompanyService.class.getSimpleName(), companyService);

        this.services.put(CompanyValidator.class.getSimpleName(),
                new CompanyValidator());



        PositionServiceImpl positionService = new PositionServiceImpl();
        PositionDAO positionDAO = new PositionDAOImpl();
        positionService.setDao(positionDAO);
        this.services.put(PositionService.class.getSimpleName(), positionService);

        this.services.put(PositionValidator.class.getSimpleName(),
                new PositionValidator());
    }

    private void collaborationRelatedData() {
        CollaborationServiceImpl collaborationService =
                new CollaborationServiceImpl();
        CollaborationAgreementDAO cRecDAO = new CollaborationAgreementDAOImpl();
        collaborationService.setRecordDao(cRecDAO);
        CollaborationRequestDAO cReqDAO = new CollaborationRequestDAOImpl();
        collaborationService.setRequestDao(cReqDAO);
        this.services.put(CollaborationService.class.getSimpleName(),
                collaborationService);
    }

    private void personRelated() {
        PersonServiceImpl personService = new PersonServiceImpl();
        PersonDAO personDAO = new PersonDAOImpl();
        personService.setDao(personDAO);
        this.services.put(PersonService.class.getSimpleName(), personService);

        this.services.put(PersonValidator.class.getSimpleName(),
                new PersonValidator());
    }

    private void userRelated() {
        UserServiceImpl userService = new UserServiceImpl();
        UserDAO userDAO = new UserDAOImpl();
        userService.setDao(userDAO);
        this.services.put(UserService.class.getSimpleName(), userService);

        this.services.put(Authenticator.class.getSimpleName(),
                new Authenticator());

        this.services.put(UserValidator.class.getSimpleName(),
                new UserValidator());

        this.services.put(PasswordValidator.class.getSimpleName(),
                new PasswordValidator());
    }

    private void vacancyRelated() {
        VacancyServiceImpl vacancyService = new VacancyServiceImpl();
        VacancyDAO vacancyDAO = new VacancyDAOImpl();
        vacancyService.setDao(vacancyDAO);
        this.services.put(VacancyService.class.getSimpleName(), vacancyService);

        this.services.put(VacancyValidator.class.getSimpleName(),
                new VacancyValidator());

        this.services.put(HookupLifecycle.class.getSimpleName(),
                new HookupLifecycle());
        this.services.put(HookupValidator.class.getSimpleName(),
                new HookupValidator());
    }

    @SuppressWarnings("unchecked")
    public static <T> T getService(Class<T> clazz) {
        return (T) INSTANCE.services.get(clazz.getSimpleName());
    }

}
