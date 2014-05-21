package edu.stevens.cs548.clinic.service.web.soap;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;
import edu.stevens.cs548.clinic.service.ejb.IpatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IpatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IpatientService.TreatmentNotFoundExn;


@WebService(targetNamespace="http://www.example.org/clinic/wsdl/patient")
/*
 * Endpoint interface for the patient web service
 * */
public interface IPatientWebService {
	/*public class PatientServiceExn extends Exception{
		private static final long serialVersionUID = 1L;
		public PatientServiceExn(String m){
			super(m);
		}
	}
	public class PatientNotFoundExn extends PatientServiceExn{
		private static final long serialVersionUID = 1L;
		public PatientNotFoundExn(String m){
			super(m);
		}
	}
	public class TreatmentNotFoundExn extends PatientServiceExn{
		private static final long serialVersionUID = 1L;
		public TreatmentNotFoundExn(String m){
			super(m);
		}
	}*/
	@WebMethod(operationName="create")
	public long createPatient(String name,Date dob, long patientId) throws PatientServiceExn;
	
	@WebMethod
	public PatientDTO getPatientByDbId(long id) throws PatientServiceExn;
	
	@WebMethod
	public PatientDTO getPatientByPatId(long pid) throws PatientServiceExn;
	
	@WebMethod
	public PatientDTO[] getPatientsByNameDob(String name, Date dob);
	
	@WebMethod
	public void deletePatient(String name, long id) throws PatientServiceExn;
	
	@WebMethod
	public void addDrugTreatment(long id,String diagnosis, String drug, float dosage)throws PatientNotFoundExn;
	
	@WebMethod
	public TreatmentDTO[] getTreatments(long id,long[]tids) throws PatientNotFoundExn,TreatmentNotFoundExn,PatientServiceExn;
	
	@WebMethod
	public void deleteTreatment(long id,long tid)throws PatientNotFoundExn,TreatmentNotFoundExn,PatientServiceExn;

	@WebMethod
	public String siteInfo();

}
