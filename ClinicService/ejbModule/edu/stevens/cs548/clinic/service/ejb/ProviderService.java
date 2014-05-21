package edu.stevens.cs548.clinic.service.ejb;

import java.util.Date;
import java.util.List;

import edu.stevens.cs548.clinic.domain.IPatientDAO;
import edu.stevens.cs548.clinic.domain.IPatientDAO.PatientExn;
import edu.stevens.cs548.clinic.domain.IProviderDAO;
import edu.stevens.cs548.clinic.domain.IProviderDAO.ProviderExn;
import edu.stevens.cs548.clinic.domain.ITreatmentDAO.TreatmentExn;
import edu.stevens.cs548.clinic.domain.ITreatmentVisitor;
import edu.stevens.cs548.clinic.domain.Patient;
import edu.stevens.cs548.clinic.domain.PatientDAO;
import edu.stevens.cs548.clinic.domain.Provider;
import edu.stevens.cs548.clinic.domain.ProviderDAO;
import edu.stevens.cs548.clinic.domain.ProviderFactory;
import edu.stevens.cs548.clinic.domain.Treatment;
import edu.stevens.cs548.clinic.service.dto.DrugTreatmentType;
import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.RadiologyType;
import edu.stevens.cs548.clinic.service.dto.SurgeryType;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Session Bean implementation class ProviderService
 */
@Stateless(name="ProviderServiceBean")

public class ProviderService implements IProviderServiceLocal, IProviderServiceRemote {

	private ProviderFactory providerFactory;
	private IProviderDAO providerDAO;
	
	private IPatientDAO patientDAO;
	
    public ProviderService() {
    	providerFactory = new ProviderFactory();
    }

	/**
     * @see IProviderService#createProvider(long, String, String)
     */
    
    @PersistenceContext(unitName = "ClinicDomain")
	private EntityManager em;
	
	
	@PostConstruct
	private void initialize(){
		providerDAO = new ProviderDAO(em);
	}
    public long createProvider(long prid, String name, String specialization) throws ProviderServiceExn {
    	Provider provider = this.providerFactory.createProvider(prid, name, specialization);
		try {
			providerDAO.addProvider(provider);
			return this.getProviderByProviderId(prid).getId();
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
		
    }

	/**
     * @see IProviderService#siteInfo()
     */
    
    @Resource(name="SiteInfo")
	private String siteInformation;
    @Override
	public String siteInfo() {
		
		return siteInformation;
	}

	/**
     * @see IProviderService#getProvidersByName(String)
     */
    @Override
    public ProviderDTO[] getProvidersByName(String name) {
        
    	List<Provider> providers = providerDAO.getProviderByName(name);
    	ProviderDTO[] providerDtos = new ProviderDTO[providers.size()];
    	for (int i = 0; i < providers.size(); i++) {
    		providerDtos[i]=new ProviderDTO(providers.get(i));
    	}
    	return providerDtos;
    }

	/**
     * @throws ProviderServiceExn 
	 * @see IProviderService#getProviderByNPI(long)
     */
    @Override
    public ProviderDTO getProviderByProviderId(long prid) throws ProviderServiceExn {
    	try {
			Provider provider = providerDAO.getProviderByProviderId(prid);
			return new ProviderDTO(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
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
    
    

	/**
     * @throws ProviderServiceExn 
	 * @see IProviderService#addTreatment(long, long, TreatmentDTO)
     */
   /* @Override
    public void addDrugTreatment(long patientId, long providerId, String diagnosis, String drug,
			float dosage) throws ProviderServiceExn {
        
    	try{
    	Provider provider = providerDAO.getProviderByProviderId(providerId);
    	Patient patient = patientDAO.getPatientByPatientId(patientId);
    	patient.addDrugTreatment(diagnosis, drug, dosage,provider.getProviderId());
    	}catch(ProviderExn e){
    		throw new ProviderServiceExn(e.toString()); 
    	}catch(PatientExn e){
			throw new ProviderServiceExn(e.toString()); 
		}

    	
    }*/

	/**
     * @throws ProviderServiceExn 
	 * @see IProviderService#deleteTreatment(long, long)
     */
    @Override
    public void deleteTreatment(long pDbid, long tid) throws ProviderServiceExn {
    	try {
			Patient patient = patientDAO.getPatientByDBId(pDbid);
			patient.deleteTreatment(tid);
		} catch (PatientExn e) {
			throw new ProviderServiceExn(e.toString());
		}catch(TreatmentExn e){
			throw new ProviderServiceExn(e.toString());
		}
    }

	/**
     * @throws ProviderServiceExn 
	 * @see IProviderService#getTreatments(long)
     */
    @Override
    public TreatmentDTO[] getTreatments(long prid) throws ProviderServiceExn {
    	try {
			Provider provider = providerDAO.getProviderByProviderId(prid);
			List<Long> treatments = provider.getTreatmentIds();
			TreatmentDTO[] treatmentDtos = new TreatmentDTO[treatments.size()];
			TreatmentPDOtoDTO visitor = new TreatmentPDOtoDTO();
			for(int i=0;i<treatments.size();i++){
				provider.visitTreatment(treatments.get(i), visitor);
				treatmentDtos[i] = visitor.getDTO();
			}
			return treatmentDtos;
		} catch (ProviderExn e) {
			throw new ProviderNotFoundExn(e.toString());
		} catch (TreatmentExn e){
			throw new ProviderServiceExn(e.toString());
		}
    }

	/**
     * @throws ProviderServiceExn 
	 * @see IProviderService#getPatientTreatments(long, long)
     */
    
	@Override
    public TreatmentDTO[] getPatientTreatments(long pDbid, long prid) throws ProviderServiceExn {
    	try {
			Provider provider = providerDAO.getProviderByProviderId(prid);
			
			TypedQuery<Treatment> query = em.createQuery("select t.id from Treatment t where t.provider.id = :prdbid and t.patient.id = :pdbid", Treatment.class)
					.setParameter("prdbid", provider.getId())
					.setParameter("pdbid",pDbid);
			List<Treatment> treatments=query.getResultList();
			List<Long> treatmentIds = null;
			for(int i=0;i<treatments.size();i++)
		 	 {
				treatmentIds.add(i, treatments.get(i).getId());
		 	 }
			
			TreatmentDTO[] treatmentDtos = new TreatmentDTO[treatmentIds.size()];
			TreatmentPDOtoDTO ptd = new TreatmentPDOtoDTO();
			for(int i=0;i<treatmentIds.size();i++){
				provider.visitTreatment(treatmentIds.get(i), ptd);//ptd is the visitor
				treatmentDtos[i] = ptd.getDTO();
			}
			return treatmentDtos;
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		
		} catch(TreatmentExn e){
			throw new TreatmentNotFoundExn(e.toString());
		}
    }
	@Override
	public ProviderDTO getProviderByProviderDbId(long id)
			throws ProviderServiceExn {
		try {
			Provider provider = providerDAO.getProviderByDBId(id);
			return new ProviderDTO(provider);
		} catch (ProviderExn e) {
			throw new ProviderServiceExn(e.toString());
		}
	}

}
