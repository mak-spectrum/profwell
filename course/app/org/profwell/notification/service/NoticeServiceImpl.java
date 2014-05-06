package org.profwell.notification.service;

import java.util.List;

import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.notification.dao.NoticeDAO;
import org.profwell.notification.model.Notice;
import org.profwell.security.model.User;

public class NoticeServiceImpl extends GenericServiceImpl<NoticeDAO, Notice>
        implements NoticeService {


    public void addNewNotice(String text, User reciever, User sender) {
        Notice notice = new Notice();
        notice.setText(text);
        notice.setSender(sender);
        notice.setReceiver(reciever);
        dao.save(notice);
    }

    public List<Notice> loadMessages(User user){
        return this.dao.loadMessages(user);
    }

}
