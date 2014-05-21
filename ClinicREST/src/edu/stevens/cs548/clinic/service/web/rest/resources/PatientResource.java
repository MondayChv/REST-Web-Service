package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;
import java.util.Date;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.jws.WebMethod;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.DatatypeConverter;

import edu.stevens.cs548.clinic.service.dto.PatientDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;
import edu.stevens.cs548.clinic.service.ejb.IPatientServiceRemote;
import edu.stevens.cs548.clinic.service.ejb.IpatientService.PatientNotFoundExn;
import edu.stevens.cs548.clinic.service.ejb.IpatientService.PatientServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IpatientService.TreatmentNotFoundExn;
import edu.stevens.cs548.clinic.service.web.rest.PatientRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;



@Singleton
@LocalBean
// @Startup
@Path("/patient")
public class PatientResource {
	final static Logger logger = Logger.getLogger(PatientResource.class
			.getCanonicalName());

	@Context
	UriInfo context;

	/**
	 * Default constructor.
	 */
	public PatientResource() {
		// TODO Auto-generated constructor stub
	}

	// TODO
	@EJB(beanName = "PatientServiceBean")
	private IPatientServiceRemote patientService;

	// TODO
	@WebMethod
	public String getSiteInfo() {
		return patientService.siteInfo();
	}
	
	@GET
    @Produces("text/html")
    public String getHtml(){
		 return "<html><body><h1>Hello World!!</h1>The service is online!!</body></html>";
	}

	/**
	 * Retrieves representation of an instance of PatientResource
	 * 
	 * @return an instance of String
	 */
	@GET
	@Path("{id}")
	@Produces("application/xml")
	public PatientRepresentation getPatient(@PathParam("id") String id) {
		try {
			long key = Long.parseLong(id);
			PatientDTO patientDTO = patientService.getPatientByDbId(key);
			// TODO return patient representation;
			PatientRepresentation patientRep = new PatientRepresentation(
					patientDTO, context);
			return patientRep;
		} catch (PatientServiceExn e) {
			throw new WebApplicationException();
		}
	}

	@GET
	@Path("patientId")
	@Produces("application/xml")
	public PatientRepresentation getPatientByPatientId(
			@QueryParam("id") String patientId) {
		try {
			long pid = Long.parseLong(patientId);
			PatientDTO patientDTO = patientService.getPatientByPatId(pid);
			// TODO return patient representation;
			PatientRepresentation patientRep = new PatientRepresentation(
					patientDTO, context);
			return patientRep;
		} catch (PatientServiceExn e) {
			throw new WebApplicationException();
		}

	}

	@GET
	@Path("patientNameDob")
	@Produces("application/xml")
	public PatientRepresentation[] getPatientByNameDob(
			@QueryParam("name") String name, @QueryParam("dob") String dob) {
		// TODO lookup patient based on name and DOB;
		Date birthDate = DatatypeConverter.parseDate(dob).getTime();
		PatientDTO[] patientDTO = patientService.getPatientsByNameDob(name,
				birthDate);
		PatientRepresentation[] patientReps = new PatientRepresentation[patientDTO.length];
		for (int i = 0; i < patientDTO.length; i++) {
			patientReps[i] = new PatientRepresentation(patientDTO[i], context);
		}
		return patientReps;

	}

	@GET
	@Path("{id}/treatments")
	@Produces("application/xml")
	public TreatmentRepresentation[] getTreatments(@PathParam("id") String id) throws PatientNotFoundExn, TreatmentNotFoundExn, PatientServiceExn {
		try{
    		long key = Long.parseLong(id);
    		PatientDTO patientDTO = patientService.getPatientByDbId(key);
    		
    		long tids[] = patientDTO.treatments;
    		TreatmentDTO[] treatmentDTO = patientService.getTreatments(key, tids);
    		TreatmentRepresentation[] treatmentReps = new TreatmentRepresentation[treatmentDTO.length];
    		for(int i = 0;i<treatmentDTO.length;i++){
    			treatmentReps[i]=new TreatmentRepresentation(treatmentDTO[i],context);
    		}
    		return treatmentReps;
    	
    	}catch(PatientServiceExn e){
        	throw new WebApplicationException();
        }

	}

	@DELETE
	@Path("{id}")
	@Consumes("application/xml")
	public Response deletePatient(@QueryParam("name") String name,
			@PathParam("id") String id) {
		// TODO add patient record;
		try {
			long key = Long.parseLong(id);
			patientService.deletePatient(name, key);
			UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
			URI url = ub.build(Long.toString(key));
			return Response.created(url).build();
		} catch (PatientServiceExn e) {
			throw new WebApplicationException();
		}
	}

	@POST
	@Consumes("application/xml")
	public Response addPatient(PatientRepresentation patientRep) {
		// TODO add patient record;
		try {
			long id = patientService.createPatient(patientRep.getName(),
					patientRep.getDob(), patientRep.getPatientId()
					);

			UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
			URI url = ub.build(Long.toString(id));
			return Response.created(url).build();
		} catch (PatientServiceExn e) {
			throw new WebApplicationException();
		}
	}
}