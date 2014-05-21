package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import static javax.persistence.CascadeType.REMOVE;

/**
 * Entity implementation class for Entity: Provider
 * 
 */
@Entity
@NamedQueries({

		@NamedQuery(name = "SerchProviderByName", query = "select p from Provider p where p.name=:name"),
		@NamedQuery(name = "SerchProviderBySpecialization", query = "select p from Provider p where p.specialization=:specialization"),
		@NamedQuery(name = "SerchProviderByProviderId", query = "select p from Provider p where p.providerId=:prid") })
public class Provider implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	private long providerId;
	private String name;
	private String specialization;

	@OneToMany(mappedBy = "provider", cascade = REMOVE)
	private List<Treatment> treatments;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getProviderId() {
		return providerId;
	}

	public void setProviderId(long providerId) {
		this.providerId = providerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public List<Treatment> getTreatments() {

		return treatments;
	}

	public void setTreatments(List<Treatment> treatments) {
		this.treatments = treatments;
	}

	@Transient
	private ITreatmentDAO treatmentDAO;

	public void setTreatmentDAO(ITreatmentDAO tdao) {
		this.treatmentDAO = tdao;
	}

	public List<Long> getTreatmentIds() {
		List<Long> tids = new ArrayList<Long>();
		for (Treatment t : this.getTreatments()) {
			tids.add(t.getId());
		}
		return tids;
	}

	public void visitTreatment(long tid, ITreatmentVisitor visitor)
			throws TreatmentExn {

		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if (t.getProvider().getProviderId()!=this.providerId) {
			throw new TreatmentExn("Inappriate treatment access: provider ="
					+ id + ",treatment=" + tid);
		}
		t.visit(visitor);
	}

	public IPatientDAO patientDAO;

	public long addDrugTreatment(String diagnosis, String drug, float dosage,
			Patient patient) {

		DrugTreatment treatment = new DrugTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDrug(drug);
		treatment.setDosage(dosage);
		treatment.setPatient(patient);
		treatment.setProvider(this);
		treatment.setPhysician();
		patient.addTreatment(treatment);
		return treatment.getId();
	}

	public long addSurgeryTreatment(String diagnosis, Date s_date,
			Patient patient) {

		SurgeryTreatment treatment = new SurgeryTreatment();

		treatment.setDiagnosis(diagnosis);
		treatment.setDate(s_date);
		treatment.setPatient(patient);
		treatment.setProvider(this);
		treatment.setSurgeon();
		patient.addTreatment(treatment);
		return treatment.getId();
	}

	public long addRadiologyTreatment(String diagnosis, List<Date> dates,
			Patient patient) {

		RadiologyTreatment treatment = new RadiologyTreatment();
		treatment.setDiagnosis(diagnosis);
		treatment.setDates(dates);
		treatment.setPatient(patient);
		treatment.setProvider(this);
		treatment.setRadiologist();
		patient.addTreatment(treatment);
		return treatment.getId();
	}

	
	public void visitPatientTreatment(long tid, Patient patient,
			ITreatmentVisitor visitor) throws TreatmentExn {
		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		if (!t.getProvider().equals(this)) { 
			  throw new TreatmentExn("Inappriate treatment access: provider =" 
					  + id + ",treatment=" + tid); 
			 } 
		if (t.getPatient().equals(patient)) {
			t.visit(visitor);
		}
	}

	public void deleteTreatment(long tid) throws TreatmentExn {

		Treatment t = treatmentDAO.getTreatmentByDbId(tid);
		
		  if (!t.getProvider().equals(this)) { throw new
		  TreatmentExn("Inappriate treatment access: provider =" + id +
		 ",treatment=" + tid); }
		 
		treatmentDAO.deleteTreatment(t);
	}

	public void visitTreatments(ITreatmentVisitor visitor) {
		for (Treatment t : this.getTreatments()) {
			t.visit(visitor);
		}
	}

	public Provider() {
		super();
		treatments = new ArrayList<Treatment>();
	}

}
