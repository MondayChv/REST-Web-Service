package edu.stevens.cs548.clinic.service.web.rest;

import java.util.List;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.web.rest.data.LinkType;
import edu.stevens.cs548.clinic.service.web.rest.data.PatientType;

@XmlRootElement
public class PatientRepresentation extends PatientType {

	public List<LinkType> getLinksTreatments(){
		return this.getTreatments();
	}
	
	public PatientRepresentation(){
		super();
	}
	
	public static LinkType getPatientLink(long id, UriInfo uriInfo) {
		UriBuilder ub = uriInfo.getBaseUriBuilder();
		ub.path("patient").path("{id}");
		String patientURI = ub.build(Long.toString(id)).toString();

		LinkType link = new LinkType();
		link.setUrl(patientURI);
		link.setRelation(Representation.RELATION_PATIENT);
		link.setMediaType(Representation.MEDIA_TYPE);
		return link;
	}
	
	public PatientRepresentation(PatientDTO dto, UriInfo uriInfo){
		// TODO initialize representation from DTO
		this.id = getPatientLink(dto.id, uriInfo);
		this.patientId = dto.patientId;
		this.name = dto.name;
		this.dob = dto.birthDate;
		
		
		/*
		 * Call getTreatments to initialize empty list.
		 */
		List<LinkType> links = this.getTreatments();
		// TODO add treatment representations	
		
		long[] treatments = dto.treatments;
		
		UriBuilder ub =  uriInfo.getBaseUriBuilder();
		ub.path("treatment");
		
		for(long t : treatments){
			LinkType link = new LinkType();
			UriBuilder ubTreatment = ub.clone().path("{tid}");
			String teatmentURI = ubTreatment.build(Long.toString(t)).toString();
			
			link.setUrl(teatmentURI);
			link.setRelation(Representation.RELATION_TREATMENT);
			link.setMediaType(Representation.MEDIA_TYPE);
			links.add(link);
		}
	}
}
