package org.profwell.vacancy.model;

public interface HookupData {

    HookupStatus getStatus();

    boolean isPassedTestTaskStatus();

    boolean isArchived();

    boolean isCanBeArchived();
}
