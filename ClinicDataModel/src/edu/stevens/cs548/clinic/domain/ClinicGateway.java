package edu.stevens.cs548.clinic.domain;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ClinicGateway implements IClinicGateway {

	@Override
	public IPatientFactory getPatientFactory() {
		
		return new PatientFactory();
	}

	private EntityManagerFactory emf;
	@Override
	public IPatientDAO getPatientDAO() {
		EntityManager em=emf.createEntityManager();
		return new PatientDAO(em);
	}
	
	
    public IProviderFactory getProviderFactory() {
		
		return new ProviderFactory();
	}

	private EntityManagerFactory emff;
	@Override
	public IProviderDAO getProviderDAO() {
		EntityManager em=emff.createEntityManager();
		return new ProviderDAO(em);
	}
	
	public ClinicGateway(){
		emf = Persistence.createEntityManagerFactory("ClinicDomain");
	}

}
 