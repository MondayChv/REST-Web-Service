package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;

import javax.ejb.EJB;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceRemote;
import edu.stevens.cs548.clinic.service.ejb.IpatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IpatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IpatientService.TreatmentNotFoundExn;

@WebService(endpointInterface="edu.stevens.cs548.clinic.service.web.soap.IPatientWebService",
		serviceName="PatientWeb",portName="PatientWebPort",
		targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap/patient")
public class PatientWebService 
implements IPatientWebService
{

	@EJB(beanName="PatientServiceBean")
	IPatientServiceRemote service;
	
	@Override
	public long createPatient(String name, Date dob, long patientId)
			throws PatientServiceExn {
		
		return service.createPatient(name, dob, patientId);
	}

	@Override
	public PatientDTO getPatientByDbId(long id) throws PatientServiceExn {
		
		return service.getPatientByDbId(id);
	}

	@Override
	public PatientDTO getPatientByPatId(long pid) throws PatientServiceExn {
		
		return service.getPatientByPatId(pid);
	}
	
	@Override
	public PatientDTO[] getPatientsByNameDob(String name, Date dob) {
		
		return service.getPatientsByNameDob(name, dob);
	}

	@Override
	public void deletePatient(String name, long id) throws PatientServiceExn {
		this.service.deletePatient(name, id);
		
	}

	@Override
	public void addDrugTreatment(long id, String diagnosis, String drug,
			float dosage) throws PatientNotFoundExn {
		this.service.addDrugTreatment(id, diagnosis, drug, dosage);
		
	}

	@Override
	public TreatmentDTO[] getTreatments(long id, long[] tids)
			throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		
		return service.getTreatments(id, tids);
	}

	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn,
			TreatmentNotFoundExn, PatientServiceExn {
		this.service.deleteTreatment(id, tid);
		
	}

	@Override
	public String siteInfo() {
		return service.siteInfo();
	}

}
