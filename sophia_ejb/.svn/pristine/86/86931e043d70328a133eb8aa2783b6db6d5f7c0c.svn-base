package br.gov.ma.tce.sophia.ejb.beans.emaillog;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EmailLogFacadeBean {
	
	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;
	
	public EmailLog include(EmailLog emailLog) {
		manager.persist(emailLog);
		return emailLog;
	}
	
	public EmailLog update(EmailLog emailLog) {
		manager.merge(emailLog);
		return emailLog;
	}
	
	public void delete(int emailLogId) {
		EmailLog emailLog = findByPrimaryKey(emailLogId);
		manager.remove(emailLog);
	}
	
	public EmailLog findByPrimaryKey(Integer emailLogId) {
		EmailLog emailLog = manager.find(EmailLog.class, emailLogId);
		return emailLog;
	}

}
