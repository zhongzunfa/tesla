package io.github.tesla.authz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.tesla.authz.domain.UserOnline;


@Service
public class SessionService {

  @Autowired
  private SessionDAO sessionDAO;

  public List<UserOnline> list() {
    List<UserOnline> list = new ArrayList<>();
    Collection<Session> sessions = sessionDAO.getActiveSessions();
    for (Session session : sessions) {
      UserOnline userOnline = new UserOnline();
      if (session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
        continue;
      } else {
        SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) session
            .getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        String userName = principalCollection.getRealmNames().iterator().next();
        userOnline.setUsername(userName);
      }
      userOnline.setId((String) session.getId());
      userOnline.setHost(session.getHost());
      userOnline.setStartTimestamp(session.getStartTimestamp());
      userOnline.setLastAccessTime(session.getLastAccessTime());
      userOnline.setTimeout(session.getTimeout());
      list.add(userOnline);
    }
    return list;
  }


  public Collection<Session> sessionList() {
    return sessionDAO.getActiveSessions();
  }


  public boolean forceLogout(String sessionId) {
    Session session = sessionDAO.readSession(sessionId);
    session.setTimeout(0);
    return true;
  }
}
