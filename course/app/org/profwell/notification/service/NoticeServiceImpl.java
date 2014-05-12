package org.profwell.notification.service;

import java.util.List;

import org.profwell.generic.service.GenericServiceImpl;
import org.profwell.notification.auxiliary.NoticeFilter;
import org.profwell.notification.dao.NoticeDAO;
import org.profwell.notification.model.Notice;
import org.profwell.security.model.User;

public class NoticeServiceImpl extends GenericServiceImpl<NoticeDAO, Notice>
        implements NoticeService {


    @Override
    public void addNewNotice(String text, User reciever, User sender) {
        Notice notice = new Notice();
        notice.setText(text);
        notice.setSender(sender);
        notice.setReceiver(reciever);
        this.dao.save(notice);
    }

    @Override
    public List<Notice> loadMessages(User user){
        return this.dao.loadMessages(user);
    }

    @Override
    public List<Notice> listNotice(NoticeFilter filter) {
        return this.dao.listNotice(filter);
    }

    @Override
    public void noticeRead(Long noticeId) {
        Notice notice = this.dao.get(noticeId);
        notice.setWasRead(true);
        this.dao.save(notice);
    }
}
