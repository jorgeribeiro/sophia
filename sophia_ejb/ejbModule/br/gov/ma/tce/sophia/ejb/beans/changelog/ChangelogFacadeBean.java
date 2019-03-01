package br.gov.ma.tce.sophia.ejb.beans.changelog;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class ChangelogFacadeBean {
	
	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;
	
	public Changelog include(Changelog changelog) {
		manager.persist(changelog);
		return changelog;
	}

	public Changelog update(Changelog changelog) {
		manager.merge(changelog);
		return changelog;
	}

	public void delete(int changelogId) {
		Changelog changelog = findByPrimaryKey(changelogId);
		manager.remove(changelog);
	}

	public Changelog findByPrimaryKey(Integer changelogId) {
		Changelog changelog = manager.find(Changelog.class, changelogId);
		return changelog;
	}

}
