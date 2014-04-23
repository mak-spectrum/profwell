package org.profwell.person.domain;


public class PersonInfoHolderImpl implements PersonInfoHolder {

    private String personFirstName = "";

    private String personSecondName = "";

    private String personLastName = "";

    public void transferFrom(PersonInfoHolder person) {
        this.personFirstName = person.getPersonFirstName();
        this.personSecondName = person.getPersonSecondName();
        this.personLastName = person.getPersonLastName();
    }

    @Override
    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    @Override
    public String getPersonSecondName() {
        return personSecondName;
    }

    public void setPersonSecondName(String personSecondName) {
        this.personSecondName = personSecondName;
    }

    @Override
    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

}
