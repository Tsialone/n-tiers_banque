package com.example.controllers;

import com.example.dto.DeviseDto;
import com.example.remotes.ChangeServiceRemote;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/devises")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ChangeRestController {

    @EJB(lookup = "java:global/change-ejb/ChangeService!com.example.remotes.ChangeServiceRemote")
    private ChangeServiceRemote changeService;

    @GET
    public List<DeviseDto> getAll() {
        return changeService.getAllDevises();
        // return changeService.getDistinct();

    }

    @GET
    @Path("/{id}")
    public DeviseDto getById(@PathParam("id") Long id) {
        return changeService.getDevise(id);
    }

    @POST
    public DeviseDto add(DeviseDto dto) {
        return changeService.addDevise(dto);
    }

    @PUT
    @Path("/{id}")
    public DeviseDto update(@PathParam("id") Long id, DeviseDto dto) {
        return changeService.updateDevise(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        boolean ok = changeService.deleteDevise(id);
        return ok ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    // @GET
    // @Path("/search")
    // public DeviseDto getByDateAndDevise(@QueryParam("date") String date,
    // @QueryParam("devise") String devise) {
    // return changeService.getByDateBtw(LocalDate.parse(date), devise);
    // }
}
