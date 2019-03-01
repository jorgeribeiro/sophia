package br.gov.ma.tce.sophia.ejb.beans.pessoa;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import br.gov.ma.tce.sophia.ejb.util.Filtro;

@Stateless
public class PessoaFacadeBean {

	@PersistenceContext(unitName = "sigesco_escex")
	public EntityManager manager;

	public Pessoa include(Pessoa pessoa) {
		removeNaoNumeros(pessoa);
		manager.persist(pessoa);
		return pessoa;
	}

	public Pessoa update(Pessoa pessoa) {
		removeNaoNumeros(pessoa);
		manager.merge(pessoa);
		return pessoa;
	}

	public void delete(int pessoaId) {
		Pessoa pessoa = findByPrimaryKey(pessoaId);
		manager.remove(pessoa);
	}

	public Pessoa findByPrimaryKey(Integer pessoaId) {
		Pessoa pessoa = manager.find(Pessoa.class, pessoaId);
		return pessoa;
	}

	@SuppressWarnings("unchecked")
	public Collection<Pessoa> findAll() {
		Query q = manager.createQuery("select s from " + Pessoa.NAME + " s order by s.nome");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Pessoa> filtrarByNome(String filtro) {
		Query q = manager.createQuery("select s from " + Pessoa.NAME + " s where upper(s.nome) like '%"
				+ filtro.toUpperCase() + "%'  order by s.nome");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Pessoa> findByTipoPessoa(String tipo) {
		Query q = manager.createQuery("select s from " + Pessoa.NAME + " s where tipoPessoa=:arg0 order by s.nome")
				.setParameter("arg0", tipo);
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Pessoa> findServidoresAndTerceirizados() {
		Query q = manager.createQuery(
				"select s from " + Pessoa.NAME + " s where tipoPessoa = '2' or tipoPessoa = '4' order by s.nome");
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Pessoa> findServidoresAndTerceirizadosByFiltro(Filtro filtroPessoa) {
		StringBuilder query = new StringBuilder(
				"select distinct s from " + Pessoa.NAME + " s where tipoPessoa = '2' or tipoPessoa = '4' ");
		if (filtroPessoa.getFiltro1() != null && !filtroPessoa.getFiltro1().equals("")) {
			query.append(" and s.nome like '%" + filtroPessoa.getFiltro1().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		if (filtroPessoa.getFiltro2() != null && !filtroPessoa.getFiltro2().equals("")) {
			query.append(" and s.email like '%" + filtroPessoa.getFiltro2().toLowerCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" order by s.nome");

		Query q = manager.createQuery(query.toString());

		return q.getResultList();
	}

	public Pessoa findByCpf(String cpf) {
		Query q = manager.createQuery("select s from " + Pessoa.NAME + " s  where s.cpf=:arg0 order by s.nome")
				.setParameter("arg0", cpf);
		return q.getResultList().isEmpty() ? null : (Pessoa) q.getResultList().get(0);
	}

	public Pessoa findByEmail(String email) {
		Query q = manager.createQuery("select s from " + Pessoa.NAME + " s where s.email=:arg0").setParameter("arg0",
				email);
		return q.getResultList().isEmpty() ? null : (Pessoa) q.getResultList().get(0);
	}

	public Pessoa findByCpfAndEmail(String cpf, String email) {
		Query q = manager.createQuery("select s from " + Pessoa.NAME + " s where s.cpf=:arg0 and s.email=:arg1")
				.setParameter("arg0", cpf).setParameter("arg1", email);
		return q.getResultList().isEmpty() ? null : (Pessoa) q.getResultList().get(0);
	}

	public Pessoa findByMatricula(String matricula) {
		Query q = manager.createQuery("select s from " + Pessoa.NAME + " s where s.servidorMatricula=:arg0")
				.setParameter("arg0", matricula);
		return q.getResultList().isEmpty() ? null : (Pessoa) q.getResultList().get(0);
	}

	/**
	 * Método para evitar inclusão de matrículas repetidas
	 */
	public Pessoa findByPrimaryKeyAndMatricula(Integer pessoaId, String matricula) {
		Query q = manager
				.createQuery(
						"select s from " + Pessoa.NAME + " s where s.pessoaId<>:arg0 and s.servidorMatricula=:arg1")
				.setParameter("arg0", pessoaId).setParameter("arg1", matricula);
		return q.getResultList().isEmpty() ? null : (Pessoa) q.getResultList().get(0);
	}

	/**
	 * Método para evitar inclusão de CPFs repetidos
	 */
	public Pessoa findByPrimaryKeyAndCpf(Integer pessoaId, String cpf) {
		Query q = manager.createQuery("select s from " + Pessoa.NAME + " s where s.pessoaId<>:arg0 and s.cpf=:arg1")
				.setParameter("arg0", pessoaId).setParameter("arg1", cpf);
		return q.getResultList().isEmpty() ? null : (Pessoa) q.getResultList().get(0);
	}

	/**
	 * Método para enviar inclusão de emails repetidos
	 */
	public Pessoa findByPrimaryKeyAndEmail(Integer pessoaId, String email) {
		Query q = manager.createQuery("select s from " + Pessoa.NAME + " s where s.pessoaId<>:arg0 and s.email=:arg1")
				.setParameter("arg0", pessoaId).setParameter("arg1", email);
		return q.getResultList().isEmpty() ? null : (Pessoa) q.getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	public Collection<Pessoa> findPessoasByFiltro(Filtro filtroPessoa) {
		StringBuilder query = new StringBuilder(
				"select distinct s from " + Pessoa.NAME + " s where s.pessoaId is not null ");
		if (filtroPessoa.getFiltro1() != null && !filtroPessoa.getFiltro1().equals("")) {
			query.append(" and s.nome like '%" + filtroPessoa.getFiltro1().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		if (filtroPessoa.getFiltro2() != null && !filtroPessoa.getFiltro2().equals("")) {
			query.append(" and s.email like '%" + filtroPessoa.getFiltro2().toLowerCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" order by s.nome");

		Query q = manager.createQuery(query.toString());
		return q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Pessoa> findPessoasByTipoPessoaAndFiltro(String tipo, Filtro filtroPessoa) {
		StringBuilder query = new StringBuilder(
				"select distinct s from " + Pessoa.NAME + " s where s.tipoPessoa=:arg0");
		if (filtroPessoa.getFiltro1() != null && !filtroPessoa.getFiltro1().equals("")) {
			query.append(" and s.nome like '%" + filtroPessoa.getFiltro1().toUpperCase().replaceAll("\'", "") + "%' ");
		}
		if (filtroPessoa.getFiltro2() != null && !filtroPessoa.getFiltro2().equals("")) {
			query.append(" and s.email like '%" + filtroPessoa.getFiltro2().toLowerCase().replaceAll("\'", "") + "%' ");
		}
		query.append(" order by s.nome");

		Query q = manager.createQuery(query.toString()).setParameter("arg0", tipo);
		return q.getResultList();
	}

	// Remove símbolos inseridos na mask do ZK
	private void removeNaoNumeros(Pessoa pessoa) {
		pessoa.setCpf(pessoa.getCpf().replaceAll("\\D+", ""));
		pessoa.setCep(pessoa.getCep().replaceAll("\\D+", ""));
		if (pessoa.getTelFixo() != null) {
			pessoa.setTelFixo(pessoa.getTelFixo().replaceAll("\\D+", ""));
		}

		if (pessoa.getTelCelular() != null) {
			pessoa.setTelCelular(pessoa.getTelCelular().replaceAll("\\D+", ""));
		}

		if (pessoa.getTelComercial() != null) {
			pessoa.setTelComercial(pessoa.getTelComercial().replaceAll("\\D+", ""));

		}
	}
}
