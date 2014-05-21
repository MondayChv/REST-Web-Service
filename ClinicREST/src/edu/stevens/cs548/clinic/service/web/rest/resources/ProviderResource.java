package edu.stevens.cs548.clinic.service.web.rest.resources;

import java.net.URI;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ws.rs.Consumes;
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

import edu.stevens.cs548.clinic.service.dto.ProviderDTO;
import edu.stevens.cs548.clinic.service.dto.TreatmentDTO;
import edu.stevens.cs548.clinic.service.ejb.IProviderService.ProviderServiceExn;
import edu.stevens.cs548.clinic.service.ejb.IProviderServiceRemote;
import edu.stevens.cs548.clinic.service.web.rest.ProviderRepresentation;
import edu.stevens.cs548.clinic.service.web.rest.TreatmentRepresentation;

@Singleton
@LocalBean
// @Startup
@Path("/provider")
public class ProviderResource {
	@Context
	private UriInfo context;

	/**
	 * Default constructor.
	 */
	public ProviderResource() {
		// TODO Auto-generated constructor stub
	}

	@EJB(beanName = "ProviderServiceBean")
	private IProviderServiceRemote providerService;

	/**
	 * Retrieves representation of an instance of ProviderResource
	 * 
	 * @return an instance of String
	 */
	@GET
	@Path("NPI")
	@Produces("application/xml")
	public ProviderRepresentation getProvider(@QueryParam("id") String providerId) {
		try {
			long key = Long.parseLong(providerId);
			ProviderDTO providerDTO = providerService.getProviderByProviderId(key);

			ProviderRepresentation ProviderRep = new ProviderRepresentation(
					providerDTO, context);
			return ProviderRep;
		} catch (ProviderServiceExn e) {
			throw new WebApplicationException();
		}
	}

	@GET
	@Path("{id}/treatments")
	@Produces("application/xml")
	public TreatmentRepresentation[] getTreatments(@PathParam("id") String id) {
		try{
			long key = Long.parseLong(id);
			ProviderDTO providerDTO = providerService.getProviderByProviderDbId(key);
		long prid=providerDTO.getProviderId();
		TreatmentDTO[] treatmentDTO = providerService.getTreatments(prid);
		TreatmentRepresentation[] treatmentReps = new TreatmentRepresentation[treatmentDTO.length];
		for(int i = 0;i<treatmentDTO.length;i++){
			treatmentReps[i]=new TreatmentRepresentation(treatmentDTO[i],context);
		}
		return treatmentReps;
		}catch(ProviderServiceExn e){
			throw new WebApplicationException();
		}

	}

	/**
	 * PUT method for updating or creating an instance of ProviderResource
	 * 
	 * @param content
	 *            representation for the resource
	 * @return an HTTP response with content of the updated or created resource.
	 */
	@POST
	@Consumes("application/xml")
	public Response addProvider(ProviderRepresentation providerRep) {
		long id = 0;
		try {
			id = providerService.createProvider(providerRep.getProviderId(),
					providerRep.getName(), providerRep.getSpecialization());
		} catch (ProviderServiceExn e) {
			e.printStackTrace();
		}
		UriBuilder ub = context.getAbsolutePathBuilder().path("{id}");
		URI url = ub.build(Long.toString(id));
		return Response.created(url).build();
	}

}