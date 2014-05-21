package edu.stevens.cs548.clinic.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;



public class ProviderDAO implements IProviderDAO {

	@PersistenceContext
	private EntityManager em;
	private TreatmentDAO treatmentDAO;
	
	
	@Override
	public Provider getProviderByProviderId(long prid) throws ProviderExn {
		TypedQuery<Provider> query = 
				em.createNamedQuery("SerchProviderByProviderId", Provider.class)
				.setParameter("prid", prid);
		List<Provider> providers = query.getResultList();
		if(providers.size()>1)
			throw new ProviderExn("Duplicate patient records:"+ prid);
		else if(providers.size()<0)
			throw new ProviderExn("Patient not found: patient id ="+prid);
		else{
			Provider p = providers.get(0);
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
		
	}

	@Override
	public Provider getProviderByDBId(long id) throws ProviderExn {
		Provider p = em.find(Provider.class, id);
		if(p==null){
			throw new ProviderExn("Patient not found: primary key ="+ id);
		}else{
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
		
	}

	@Override
	public List<Provider> getProviderByName(String name) {
		TypedQuery<Provider> query = 
				em.createNamedQuery("SerchProviderByName", Provider.class)
				.setParameter("name", name);
		List<Provider> providers = query.getResultList();
		for (Provider p : providers){
			p.setTreatmentDAO(this.treatmentDAO);
		}
		return query.getResultList();
		
	}

	@Override
	public List<Provider> getProviderBySpecialization(String specialization) {
		TypedQuery<Provider> query = 
				em.createNamedQuery("SerchProviderBySpecialization", Provider.class)
				.setParameter("specialization", specialization);
		List<Provider> providers = query.getResultList();
		for (Provider p : providers){
			p.setTreatmentDAO(this.treatmentDAO);
		}
		return query.getResultList();
		
	}

	@Override
	public void addProvider(Provider provider) throws ProviderExn {
		long prid = provider.getProviderId();
		TypedQuery<Provider> query = 
				em.createNamedQuery("SerchProviderByProviderId", Provider.class)
				.setParameter("prid", prid);
		List<Provider> providers = query.getResultList();
		if(providers.size()<1){
			em.persist(provider);
			provider.setTreatmentDAO(new TreatmentDAO(this.em));
		} else{
			Provider provider2 = providers.get(0);
			throw new ProviderExn("Insertion:Provider with provider id("+prid+") already exists.\n** Name:"+provider2.getName());
		}

	}

	@Override
	public void deleteProvider(Provider provider) throws ProviderExn {
		em.remove(provider);

	}
	public ProviderDAO(EntityManager em){
		this.em = em;
		this.treatmentDAO=new TreatmentDAO(em);
	}

}
