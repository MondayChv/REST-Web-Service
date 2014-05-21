package edu.stevens.cs548.clinic.service.web.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.TreatmentNotFoundExn;

@WebService(targetNamespace = "http://www.example.org/clinic/wsdl/provider")
public interface IProviderWebService {

	@WebMethod(operationName = "create")
	public void createProvider(long prid, String name, String specialization)
			throws ProviderServiceExn;

	@WebMethod
	public ProviderDTO[] getProvidersByName(String name)
			throws ProviderServiceExn;

	@WebMethod
	public ProviderDTO getProviderByProviderId(long prid)
			throws ProviderServiceExn;

	/*@WebMethod
	public void addDrugTreatment(long patientId, long providerId,
			String diagnosis, String drug, float dosage)
			throws ProviderServiceExn;
*/
	@WebMethod
	public void deleteTreatment_prov(long pDbid, long tid)
			throws ProviderServiceExn, ProviderNotFoundExn,
			TreatmentNotFoundExn;
	
	@WebMethod
	public TreatmentDTO[] getPatientTreatments(long pDbid, long prid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn;

	@WebMethod
	public TreatmentDTO[] getTreatments_prov(long prid) throws ProviderServiceExn,
			ProviderNotFoundExn, TreatmentNotFoundExn;

	@WebMethod
	public String siteInfo();
}
