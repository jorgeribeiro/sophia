package br.gov.ma.tce.sophia.ejb.beans.usuario;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UsuarioFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public Usuario include(Usuario usuario) {
		manager.persist(usuario);
		return usuario;
	}

	public Usuario update(Usuario usuario) {
		manager.merge(usuario);
		return usuario;
	}

	public void delete(int usuarioId) {
		Usuario usuario = findByPrimaryKey(usuarioId);
		manager.remove(usuario);
	}

	public Usuario findByPrimaryKey(Integer usuarioId) {
		Usuario usuario = manager.find(Usuario.class, usuarioId);
		return usuario;
	}

	@SuppressWarnings("unchecked")
	public Collection<Usuario> findAll() {
		Query q = manager.createQuery("select s from " + Usuario.NAME + " s order by s.pessoa.nome");
		return q.getResultList();
	}

	public Usuario findByCpf(String cpf) {
		Query q = manager.createQuery("select s from " + Usuario.NAME + " s  where s.pessoa.cpf=:arg0")
				.setParameter("arg0", cpf);
		return q.getResultList().isEmpty() ? null : (Usuario) q.getResultList().get(0);
	}
}
