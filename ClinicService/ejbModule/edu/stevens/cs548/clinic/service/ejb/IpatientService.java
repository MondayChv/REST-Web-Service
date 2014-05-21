package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;

import java.util.Date;

public interface IpatientService {

	public class PatientServiceExn extends Exception{
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
	}
	public String siteInfo();
	public long createPatient(String name,Date dob, long patientId) throws PatientServiceExn;
	public PatientDTO getPatientByDbId(long id) throws PatientServiceExn;
	public PatientDTO getPatientByPatId(long pid) throws PatientServiceExn;
	public PatientDTO[] getPatientsByNameDob(String name, Date dob);
	public void deletePatient(String name, long id) throws PatientServiceExn;

	public void addDrugTreatment(long id,String diagnosis, String drug, float dosage)throws PatientNotFoundExn;

	public TreatmentDTO[] getTreatments(long id,long[]tids) throws PatientNotFoundExn,TreatmentNotFoundExn,PatientServiceExn;

	public void deleteTreatment(long id,long tid)throws PatientNotFoundExn,TreatmentNotFoundExn,PatientServiceExn;
}
