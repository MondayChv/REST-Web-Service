package edu.stevens.cs548.clinic.domain;

public class ProviderFactory implements IProviderFactory {

	@Override
	public Provider createProvider(long prid, String name, String specialization) {
		Provider p=new Provider();
		p.setProviderId(prid);
		p.setName(name);
		p.setSpecialization(specialization);
		return p;
	}

}
