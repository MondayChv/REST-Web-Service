package edu.stevens.cs548.clinic.service.web.soap;

import javax.ejb.EJB;
import javax.jws.WebService;

import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceRemote;

@WebService(endpointInterface="edu.stevens.cs548.clinic.service.web.soap.IProviderWebService",
serviceName="ProviderWeb",portName="ProviderWebPort",
targetNamespace="http://cs548.stevens.edu/clinic/service/web/soap/provider")
public class ProviderWebService implements IProviderWebService{

	@EJB(beanName="ProviderServiceBean")
	IProviderServiceRemote service;
	
	@Override
	public void createProvider(long prid, String name, String specialization)
			throws ProviderServiceExn {
		service.createProvider(prid, name, specialization);
		
	}

	@Override
	public ProviderDTO[] getProvidersByName(String name)
			throws ProviderServiceExn {
		return service.getProvidersByName(name);
	}

	@Override
	public ProviderDTO getProviderByProviderId(long prid)
			throws ProviderServiceExn {
		
		return service.getProviderByProviderId(prid);
	}
/*
	@Override
	public void addDrugTreatment(long patientId, long providerId,
			String diagnosis, String drug, float dosage)
			throws ProviderServiceExn {
		service.addDrugTreatment(patientId, providerId, diagnosis, drug, dosage);
		
	}*/

	@Override
	public void deleteTreatment_prov(long pDbid, long tid)
			throws ProviderServiceExn, ProviderNotFoundExn,
			TreatmentNotFoundExn {
		service.deleteTreatment(pDbid, tid);
		
		
	}

	@Override
	public TreatmentDTO[] getPatientTreatments(long pDbid, long prid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn {
		
		return service.getPatientTreatments(pDbid, prid);
	}

	@Override
	public TreatmentDTO[] getTreatments_prov(long prid) throws ProviderServiceExn,
			ProviderNotFoundExn, TreatmentNotFoundExn {
		return service.getTreatments(prid);
	}

	@Override
	public String siteInfo() {
		
		return service.siteInfo();
	}

}
