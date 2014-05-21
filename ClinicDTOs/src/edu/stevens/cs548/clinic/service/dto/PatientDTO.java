package edu.stevens.cs548.clinic.service.dto;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.domain.Patient;


@XmlRootElement(name = "patient-dto",namespace="http://cs548.stevens.edu/clinic/service/web/data/patient")
public class PatientDTO {
	
	
	public long id;
    @XmlElement(name = "patient-id")
    public long patientId;
    @XmlElement(required = true)
    public String name;
   
    @XmlElement(required=true)
    //protected Date dob;
    public Date birthDate;
    
    public long[] treatments;


    public PatientDTO(){
    	
    }
    public PatientDTO(Patient patient){
    	this.id=patient.getId();
    	this.patientId=patient.getPatientId();
    	this.name=patient.getName();
    	this.birthDate=patient.getBirthDate();
    	//this.treatments = new long[patient.getTreatments().size()];
    	//for (int i=0;i<treatments.length;i++){
    	//	this.treatments[i]=patient.getTreatments().get(i).getId(); 
    	//}
    	List<Long> tids = patient.getTreatmentIds();
    	this.treatments=new long[tids.size()];
    	for (int i=0;i<treatments.length;i++){
    		this.treatments[i]=tids.get(i);
    	}
    }

}