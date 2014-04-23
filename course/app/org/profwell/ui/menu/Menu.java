package org.profwell.ui.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private List<MenuItem> items;

    public Menu(MenuItem ... inputItems) {
        this.items = new ArrayList<>();

        for (MenuItem mi : inputItems) {
            this.items.add(mi);
        }
    }

    public List<MenuItem> getItems() {
        return items;
    }

}
