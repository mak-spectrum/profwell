package org.profwell.ui.menu;

public class MenuItem {

    private boolean active;

    private String name;

    private String url;

    public MenuItem(String menuItemName, String menuItemUrl) {
        this.name = menuItemName;
        this.url = menuItemUrl;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
