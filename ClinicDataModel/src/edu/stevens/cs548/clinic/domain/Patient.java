package edu.stevens.cs548.clinic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import static javax.persistence.CascadeType.REMOVE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;

/**
 * Entity implementation class for Entity: Patient
 * 
 */

@Entity
@NamedQueries({
		@NamedQuery(name = "SerchPatientByNameDOB", 
				query = "select p from Patient p where p.name=:name and p.birthDate =:dob"),
		@NamedQuery(name = "SerchPatientByPatientId", 
		        query = "select p from Patient p where p.patientId=:pid") })
@Table(name = "Patient")
public class Patient implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;
	private long patientId;
	private String name;
	@Temporal(TemporalType.DATE)
	private Date birthDate = new Date();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPatientId() {
		return patientId;
	}

	public void setPatientId(long patientId) {
		this.patientId = patientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	@OrderBy
	@OneToMany(mappedBy = "patient", cascade = REMOVE)
	private List<Treatment> treatments;

	protected List<Treatment> getTreatments(){
		return treatments;
	}
	
	protected void setTreatments(List<Treatment> treatments){
		this.treatments = treatments;
	}
	
	@Transient
	private ITreatmentDAO treatmentDAO;
	public void setTreatmentDAO(ITreatmentDAO tdao){
		this.treatmentDAO=tdao;
	}
	
	
    void addTreatment(Treatment t){
    	this.treatmentDAO.addTreatment(t);
		this.getTreatments().add(t);
		if(t.getPatient()!=this)
		     t.setPatient(this);
	}
	
    @Transient
    public IProviderDAO providerDAO;
    public void addDrugTreatment(String diagnosis, String drug, float dosage,long prid) throws ProviderExn{
    	
    	DrugTreatment treatment = new DrugTreatment();
    	treatment.setDiagnosis(diagnosis);
    	treatment.setDrug(drug);
    	treatment.setDosage(dosage);
    	Provider provider = new Provider();
    	provider = providerDAO.getProviderByDBId(prid);
    	treatment.setProvider(provider);
    	treatment.setPhysician();
    	this.addTreatment(treatment);
    }
    
    
    public void addSurgeryTreatment(String diagnosis, String surgeon, Date s_date,long prid) throws ProviderExn{
    	
    	SurgeryTreatment treatment = new SurgeryTreatment();
    	treatment.setDiagnosis(diagnosis);
    	treatment.setDate(s_date);
    	Provider provider = new Provider();
    	provider = providerDAO.getProviderByDBId(prid);
    	treatment.setProvider(provider);
    	treatment.setSurgeon();
    	this.addTreatment(treatment);
    	
    }
    
    public void addRadiologyTreatment(String diagnosis, String radiologist, List<Date> dates,long prid) throws ProviderExn{
    	
    	RadiologyTreatment treatment = new RadiologyTreatment();
    	treatment.setDiagnosis(diagnosis);
    	treatment.setDates(dates);
    	Provider provider = new Provider();
    	provider = providerDAO.getProviderByDBId(prid);
    	treatment.setProvider(provider);
    	treatment.setRadiologist();
    	this.addTreatment(treatment);
    	
    }
    
    
    
    
    public List<Long> getTreatmentIds(){
    	List<Long> tids = new ArrayList<Long>();
    	for(Treatment t:this.getTreatments()){
    		tids.add(t.getId());
    	}
    	return tids;
    }
    
    public void visitTreatment(long tid,ITreatmentVisitor visitor)throws TreatmentExn{
    
        Treatment t = treatmentDAO.getTreatmentByDbId(tid);
       /* if(t.getPatient().equals(this)){
        	throw new TreatmentExn("Inappriate treatment access: patient ="+id+",treatment="+tid);
        }*/
        t.visit(visitor);
    }
    
    public void deleteTreatment(long tid) throws TreatmentExn{

        Treatment t = treatmentDAO.getTreatmentByDbId(tid);
       /* if(t.getPatient().equals(this)){
        	throw new TreatmentExn("Inappriate treatment access: patient ="+id+",treatment="+tid);
        }*/
        treatmentDAO.deleteTreatment(t);
    }
    
    
    public void visitTreatments(ITreatmentVisitor visitor){
    	for(Treatment t:this.getTreatments()){
    		t.visit(visitor);
    	}
    }
    
    
    
	public Patient() {
		super();
		treatments = new ArrayList<Treatment>();
	}

}
