package edu.stevens.cs548.clinic.service.ejb;

import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;

public interface IProviderService {
	public class ProviderServiceExn extends Exception {
		private static final long serialVersionUID = 1L;

		public ProviderServiceExn(String m) {
			super(m);
		}
	}

	public class ProviderNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;

		public ProviderNotFoundExn(String m) {
			super(m);
		}
	}

	public class TreatmentNotFoundExn extends ProviderServiceExn {
		private static final long serialVersionUID = 1L;

		public TreatmentNotFoundExn(String m) {
			super(m);
		}
	}
	
	//23-MAR-14
	public ProviderDTO getProviderByProviderDbId(long id)
			throws ProviderServiceExn;

	// 1
	public long createProvider(long prid, String name, String specialization)
			throws ProviderServiceExn;

	// 2
	public ProviderDTO[] getProvidersByName(String name)
			throws ProviderServiceExn;

	// 3
	public ProviderDTO getProviderByProviderId(long prid)
			throws ProviderServiceExn;

	// 4
	/*public void addDrugTreatment(long patientId, long providerId,
			String diagnosis, String drug, float dosage)
			throws ProviderServiceExn;
*/
	// public void deleteProvider (String name, long id) throws
	// ProviderServiceExn;

	// 5
	public void deleteTreatment(long pDbid, long tid)
			throws ProviderServiceExn, ProviderNotFoundExn,
			TreatmentNotFoundExn;

	// 6
	public TreatmentDTO[] getPatientTreatments(long pDbid, long prid)
			throws ProviderNotFoundExn, TreatmentNotFoundExn,
			ProviderServiceExn;

	// 7
	public TreatmentDTO[] getTreatments(long prid) throws ProviderServiceExn,
			ProviderNotFoundExn, TreatmentNotFoundExn;

	public String siteInfo();
}
