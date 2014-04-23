package org.profwell.ui.menu;

import java.util.ArrayList;
import java.util.List;

public class MenuConfiguration {

    private List<Menu> menus = new ArrayList<>();

    public MenuConfiguration(String activeItemName) {

        this.menus.add(
                new Menu(
                        new MenuItem("Dashboard", "/"),
                        new MenuItem("Contact Book", "/personList"),
                        new MenuItem("Vacancies", "/vacancyArchiveList")
                        ));

        this.menus.add(
                new Menu(
                        new MenuItem("Administration", "/administration"),
                        new MenuItem("Dictionaries", "/dictionaries"),
                        new MenuItem("Collaboration", "/collaboration")
                        ));

        this.menus.add(
                new Menu(
                        new MenuItem("FAQ", "#"),
                        new MenuItem("Contact us", "#"),
                        new MenuItem("Logout", "/logout")
                        ));

        for (Menu m : this.menus) {
            for (MenuItem mi : m.getItems()) {
                if (activeItemName.equals(mi.getName())) {
                    mi.setActive(true);
                }
            }
        }

    }

    public List<Menu> getMenus() {
        return this.menus;
    }

}
