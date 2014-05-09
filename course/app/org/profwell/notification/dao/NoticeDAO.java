package org.profwell.notification.dao;

import java.util.List;

import org.profwell.generic.dao.GenericDAO;
import org.profwell.notification.model.Notice;
import org.profwell.security.model.User;

public interface NoticeDAO extends GenericDAO<Notice> {

    public List<Notice> loadMessages(User user);

}