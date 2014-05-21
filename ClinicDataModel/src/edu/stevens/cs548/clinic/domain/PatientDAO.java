package edu.stevens.cs548.clinic.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;




public class PatientDAO implements IPatientDAO {

	@PersistenceContext
	private EntityManager em;
	private TreatmentDAO treatmentDAO;
	@Override
	public Patient getPatientByDBId(long id) throws PatientExn {
		Patient p = em.find(Patient.class, id);
		if(p==null){
			throw new PatientExn("Patient not found: primary key ="+ id);
		}else{
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
		
	}

	@Override
	public Patient getPatientByPatientId(long pid) throws PatientExn {
		TypedQuery<Patient> query = 
				em.createNamedQuery("SerchPatientByPatientId", Patient.class)
				.setParameter("pid", pid);
		List<Patient> patients = query.getResultList();
		if(patients.size()>1)
			throw new PatientExn("Duplicate patient records:"+ pid);
		else if(patients.size()<0)
			throw new PatientExn("Patient not found: patient id ="+pid);
		else{
			Patient p = patients.get(0);
			p.setTreatmentDAO(this.treatmentDAO);
			return p;
		}
	}

	@Override
	public List<Patient> getPatientByNameDob(String name, Date dob) {
		TypedQuery<Patient> query = 
				em.createNamedQuery("SerchPatientByNameDOB", Patient.class)
				.setParameter("name", name)
				.setParameter("dob",dob);
		List<Patient> patients = query.getResultList();
		for (Patient p : patients){
			p.setTreatmentDAO(this.treatmentDAO);
		}
		return query.getResultList();
		
	}

	@Override
	public void addPatient(Patient patient) throws PatientExn {
		long pid = patient.getPatientId();
		TypedQuery<Patient> query = 
				em.createNamedQuery("SerchPatientByPatientId", Patient.class)
				.setParameter("pid", pid);
		List<Patient> patients = query.getResultList();
		if(patients.size()<1){
			em.persist(patient);
			patient.setTreatmentDAO(new TreatmentDAO(this.em));
		} else{
			Patient patient2 = patients.get(0);
			throw new PatientExn("Insertion:Patient with patient id("+pid+") already exists.\n** Name:"+patient2.getName());
		}
		
	}

	@Override
	public void deletePatient(Patient patient) throws PatientExn {
		//em.createQuery("delete from Treatement t where t.patient.id = id")
		//.setParameter("id", patient.getId())
		//.executeUpdate();
		em.remove(patient);

	}
	
	public PatientDAO(EntityManager em){
		this.em = em;
		this.treatmentDAO=new TreatmentDAO(em);
	}
	
}
