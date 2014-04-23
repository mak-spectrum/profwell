package org.profwell.generic.model;


public interface Identifiable {

    long DEFAULT_UNINITIALIZED_ID_VALUE = 0;

    long getId();

    boolean isNew();

}
