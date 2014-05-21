package edu.stevens.cs548.clinic.domain;


public interface IProviderFactory {

	public Provider createProvider(long prid, String name, String specialization);
}
