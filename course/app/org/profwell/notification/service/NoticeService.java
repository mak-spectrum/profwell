package org.profwell.notification.service;

import java.util.List;

import org.profwell.generic.service.GenericService;
import org.profwell.notification.model.Notice;
import org.profwell.security.model.User;

public interface NoticeService extends GenericService<Notice> {

    void addNewNotice(String text, User reciever, User sender);

    List<Notice> loadMessages(User user);

}
