package org.sime.seven.managefiles.resource;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.ContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.sime.seven.managefiles.model.FileModel;
import org.sime.seven.managefiles.model.FilesListModel;
import org.sime.seven.managefiles.service.FilesService;

@Path("files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FilesResource {

	// @Context
	// private ServletContext context;
	// final private String separator = System.getProperty("file.separator");

	private FilesService service = new FilesService();

	public FilesResource() {

	}

	@GET
	@Path("test")
	@Produces(MediaType.APPLICATION_JSON)
	public String test() {
		
		return "testing 1,2,3  :  " + service.getDirPath();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getListOfFiles(@Context UriInfo uriInfo) {
		FilesService service = new FilesService();
		FilesListModel list = service.getFilesList(uriInfo);

		return Response.status(Status.OK).entity(list).build();
	}

	@GET
	@Path("download/{fileName}")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response downloadFile(@PathParam("fileName") String fileName) {
		File file = service.downloadFile(fileName);
		ResponseBuilder response = null;
		if (file.exists()) {
			response = Response.ok((Object) file,
					MediaType.APPLICATION_OCTET_STREAM);
			response.header("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
		}else{
			response = Response.noContent();
		}
		return response.build();
	}

	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadFile(FormDataMultiPart formData) {

		FormDataBodyPart filePart = formData.getField("file");
		ContentDisposition fileHeader = filePart.getContentDisposition();
		String fileName = fileHeader.getFileName();
		InputStream is = filePart.getValueAs(InputStream.class);

		// uploadDir = context.getRealPath(separator) + "upload" + separator;

		String reply = "File confirmed. File name is : " + fileName;
		boolean ok = service.uploadFile(is, fileName);
		if (ok) {
			return Response.status(Status.OK).entity(reply).build();
		} else {
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity("saving failed").build();
		}

	}

}
