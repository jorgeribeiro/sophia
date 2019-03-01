package br.gov.ma.tce.sophia.ejb.beans.colaborador;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.util.Filtro;

@Stateless
public class ColaboradorFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public Colaborador include(Colaborador colaborador) {
		manager.persist(colaborador);
		return colaborador;
	}

	public Colaborador update(Colaborador colaborador) {
		manager.merge(colaborador);
		return colaborador;
	}

	public void delete(int colaboradorId) {
		Colaborador colaborador = findByPrimaryKey(colaboradorId);
		manager.remove(colaborador);
	}

	public Colaborador findByPrimaryKey(Integer colaboradorId) {
		Colaborador colaborador = manager.find(Colaborador.class, colaboradorId);
		return colaborador;
	}

	public Collection<Colaborador> findAll() {
		Query q = manager.createQuery("select s from " + Colaborador.NAME + " s order by s.pessoa.nome");
		Collection<Colaborador> colaboradores = new ArrayList<Colaborador>();
		for (Object o : q.getResultList()) {
			colaboradores.add((Colaborador) o);
		}
		return colaboradores;
	}

	public Collection<Colaborador> findColaboradoresByCadastroConfirmado() {
		Query q = manager.createQuery(
				"select s from " + Colaborador.NAME + " s where s.statusCadastro='2' order by s.pessoa.nome");
		Collection<Colaborador> colaboradores = new ArrayList<Colaborador>();
		for (Object o : q.getResultList()) {
			colaboradores.add((Colaborador) o);
		}
		return colaboradores;
	}

	@SuppressWarnings("unchecked")
	public Collection<Colaborador> filtrarByNome(String filtro) {
		Query q = manager.createQuery("select s from " + Colaborador.NAME + " s where upper(s.pessoa.nome) like '%"
				+ filtro.toUpperCase() + "%'  order by s.pessoa.nome");
		return q.getResultList();
	}

	public Colaborador findByCpf(String cpf) {
		Query q = manager.createQuery("select a from " + Colaborador.NAME + " a where a.cpf=:arg0").setParameter("arg0",
				cpf);
		return q.getResultList().isEmpty() ? null : (Colaborador) q.getResultList().get(0);
	}

	/**
	 * Método para evitar inclusão de CPFs repetidos
	 * 
	 * @param colaboradorId
	 * @param cpf
	 * @return
	 */
	public Colaborador findByPrimaryKeyAndCpf(Integer colaboradorId, String cpf) {
		Query q = manager
				.createQuery("select s from " + Colaborador.NAME + " s where s.colaboradorId<>:arg0 and s.cpf=:arg1")
				.setParameter("arg0", colaboradorId).setParameter("arg1", cpf);
		return q.getResultList().isEmpty() ? null : (Colaborador) q.getResultList().get(0);
	}

	public Colaborador findByEmail(String email) {
		Query q = manager.createQuery("select s from " + Colaborador.NAME + " s where s.pessoa.email=:arg0")
				.setParameter("arg0", email);
		return q.getResultList().isEmpty() ? null : (Colaborador) q.getResultList().get(0);
	}

	/**
	 * Método para enviar inclusão de emails repetidos
	 * 
	 * @param colaboradorId
	 * @param email
	 * @return
	 */
	public Colaborador findByPrimaryKeyAndEmail(Integer colaboradorId, String email) {
		Query q = manager
				.createQuery("select s from " + Colaborador.NAME
						+ " s where s.colaboradorId<>:arg0 and s.pessoa.email=:arg1")
				.setParameter("arg0", colaboradorId).setParameter("arg1", email);
		return q.getResultList().isEmpty() ? null : (Colaborador) q.getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	public Collection<Colaborador> findColaboradoresByFiltro(Filtro filtroColaborador) {
		StringBuilder query = new StringBuilder(
				"select s from " + Colaborador.NAME + " s where s.colaboradorId is not null ");
		if (filtroColaborador.getFiltro1() != null && !filtroColaborador.getFiltro1().equals("")) {
			query.append(" and s.pessoa.nome like '%"
					+ filtroColaborador.getFiltro1().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		if (filtroColaborador.getFiltro2() != null && !filtroColaborador.getFiltro2().equals("")) {
			query.append(" and s.pessoa.email like '%"
					+ filtroColaborador.getFiltro2().toLowerCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" order by s.pessoa.nome");

		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}

}
