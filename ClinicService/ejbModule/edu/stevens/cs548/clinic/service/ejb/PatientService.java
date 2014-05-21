package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.PatientFactory;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class PatientService
 */
@Stateless(name="PatientServiceBean")
// @Local(IPatientServiceLocal.class)
// @Remote(IPatientServiceRemote.class)
// @LocalBean
public class PatientService implements IPatientServiceLocal,
		IPatientServiceRemote {

	/**
	 * Default constructor.
	 */
	private PatientFactory patientFactory;
	private IPatientDAO patientDAO;

	public PatientService() {

		patientFactory = new PatientFactory();
		
	}

	@PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;
	
	
	@PostConstruct
	private void initialize(){
		patientDAO = new PatientDAO(em);
	}

	/**
	 * @see IpatientService#getPatientsByNameDob(String, Date)
	 */
	public PatientDTO[] getPatientsByNameDob(String name, Date dob) {
		List<Patient> patients = patientDAO.getPatientByNameDob(name, dob);
		PatientDTO[] dto = new PatientDTO[patients.size()];
		for(int i=0;i<dto.length;i++)
		{
			dto[i]=new PatientDTO(patients.get(i));
		}
		return dto;
	}

	/**
	 * @throws PatientServiceExn 
	 * @see IpatientService#deletePatient(String, long)
	 */
	public void deletePatient(String name, long id) throws PatientServiceExn {
		
		try{
			Patient patient = patientDAO.getPatientByDBId(id);
			if(!name.equals(patient.getName())){
				throw new PatientServiceExn("Tried to delete wrong pstient:name="+name+", id:"+id);
			}
			else{
				patientDAO.deletePatient(patient);
			}
		}catch(PatientExn e){
			throw new PatientServiceExn(e.toString());
		}
		
	}

	/**
	 * @see IpatientService#getPatientByDbId(long)
	 */
	
	private PatientDTO patientToDTO(Patient patient){
		return new PatientDTO(patient);
	}
	public PatientDTO getPatientByDbId(long id) throws PatientServiceExn{
		
		try{
			Patient patient = patientDAO.getPatientByDBId(id);
			//PatientDTO patientDTO = patientToDTO(patient);
			return new PatientDTO(patient);
		}catch(PatientExn e){
			throw new PatientServiceExn(e.toString());
		}
		//return null;
	}

	/**
	 * @throws PatientServiceExn 
	 * @see IpatientService#createPatient(String, Date, long)
	 */
	public long createPatient(String name, Date dob, long patientId) throws PatientServiceExn {

		Patient patient = this.patientFactory.createPatient(patientId, name, dob);
		try{
			patientDAO.addPatient(patient);
		}catch(PatientExn e){
			throw new PatientServiceExn(e.toString());
		}
		return patient.getId();//return a key
	}

	/**
	 * @see IpatientService#getPatientByPatId(long)
	 */
	public PatientDTO getPatientByPatId(long pid) throws PatientServiceExn{
		try{
			Patient patient = patientDAO.getPatientByPatientId(pid);
			//PatientDTO patientDTO = patientToDTO(patient);
			return new PatientDTO(patient);
		}catch(PatientExn e){
			throw new PatientServiceExn(e.toString());
		}
	}

	@Override
	public void addDrugTreatment(long id, String diagnosis, String drug,
			float dosage) throws PatientNotFoundExn {
		// TODO Auto-generated method stub
		try{
			Patient patient = patientDAO.getPatientByDBId(id);
			try{
				patient.addDrugTreatment(diagnosis, drug, dosage,10000);
			}catch(ProviderExn e){
				//TODO exception
			}//TODO add provider id
		}catch(PatientExn e){
			throw new PatientNotFoundExn(e.toString());
		}
		
		
	}
	
	static class TreatmentPDOtoDTO implements ITreatmentVisitor{

		private TreatmentDTO dto;
		public TreatmentDTO getDTO(){
			return dto;
		}
		@Override
		public void visitDrugTreatment(long tid, String diagnosis, String drug,
				float dosage, Provider provider) {
			dto = new TreatmentDTO();
			dto.setDiagnosis(diagnosis);
			DrugTreatmentType drugInfo = new DrugTreatmentType();
			drugInfo.setDosage(dosage);
			drugInfo.setName(drug);
			dto.setDrugTreatment(drugInfo);
			
		}

		@Override
		public void visitRadiology(long tid, String diagnosis,
				List<Date> dates, Provider provider) {
			dto = new TreatmentDTO();
			dto.setDiagnosis(diagnosis);
			RadiologyType radiInfo = new RadiologyType();
			radiInfo.setDate(dates);
			dto.setRadiology(radiInfo);
		}

		@Override
		public void visitSurgery(long tid, String diagnosis, Date s_date,
				Provider provider) {
			dto = new TreatmentDTO();
			dto.setDiagnosis(diagnosis);
			SurgeryType surgInfo = new SurgeryType();
			surgInfo.setDate(s_date);
			dto.setSurgery(surgInfo);
			
		}
		
	}

	@Override
	public TreatmentDTO[] getTreatments(long id,long[] tids) throws PatientNotFoundExn,
			TreatmentNotFoundExn, PatientServiceExn {
		try{
			Patient patient = patientDAO.getPatientByDBId(id);
			TreatmentDTO[] treatments = new TreatmentDTO[tids.length];
			for(int i=0;i<tids.length;i++){
				TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
				patient.visitTreatment(tids[i], visitor);
				treatments[i]=visitor.getDTO();
			}
			return treatments;
		}catch(PatientExn e){
			throw new PatientNotFoundExn(e.toString());
		}catch(TreatmentExn e){
			throw new PatientServiceExn(e.toString());
		}
		
	}

	@Override
	public void deleteTreatment(long id, long tid) throws PatientNotFoundExn,
			TreatmentNotFoundExn, PatientServiceExn {
		try{
			Patient patient = patientDAO.getPatientByDBId(id);
			patient.deleteTreatment(tid);
		}catch(PatientExn e){
			throw new PatientNotFoundExn(e.toString());
		}catch(TreatmentExn e){
			throw new PatientServiceExn(e.toString());
		}
		
	}

	@Resource(name="SiteInfo")
	private String siteInformation;
	@Override
	public String siteInfo() {
		
		return siteInformation;
	}

}
