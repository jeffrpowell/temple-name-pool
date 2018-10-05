package com.jeffrpowell.templenamepool.ws;

import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.NameRequest;
import com.jeffrpowell.templenamepool.model.TempleName;
import com.jeffrpowell.templenamepool.model.WardMember;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

@Path("name")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NameResource {
    
    private final NamePoolDao namePoolDao;

    @Inject
    public NameResource(NamePoolDao namePoolDao) {
        this.namePoolDao = namePoolDao;
    }
    
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addNamesToPool(FormDataMultiPart multiPart) {
        FormDataBodyPart numSubmissionsPart = multiPart.getField("numSubmissions");
        Integer numSubmissions = numSubmissionsPart.getEntityAs(Integer.class);
        FormDataBodyPart wardMemberPart = multiPart.getField("wardMember");
        String wardMemberJson = wardMemberPart.getEntityAs(String.class);
        return Response.ok().build();
        /*
        List<FormDataBodyPart> bodyParts = multiPart.getFields("files");

		StringBuffer fileDetails = new StringBuffer("");

		/* Save multiple files * /
		for (int i = 0; i < bodyParts.size(); i++) {
			BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyParts.get(i).getEntity();
			String fileName = bodyParts.get(i).getContentDisposition().getFileName();
			saveFile(bodyPartEntity.getInputStream(), fileName);
			fileDetails.append(" File saved at /Volumes/Drive2/temp/file/" + fileName);
		}

		/* Save File 2 * /

		BodyPartEntity bodyPartEntity = ((BodyPartEntity) multiPart.getField("file2").getEntity());
		String file2Name = multiPart.getField("file2").getFormDataContentDisposition().getFileName();
		saveFile(bodyPartEntity.getInputStream(), file2Name);
		fileDetails.append(" File saved at /Volumes/Drive2/temp/file/" + file2Name);

		fileDetails.append(" Tag Details : " + multiPart.getField("tags").getValue());
		System.out.println(fileDetails);

		return Response.ok(fileDetails.toString()).build();
	}

	private void saveFile(InputStream file, String name) {
		try {
			/* Change directory path * /
			java.nio.file.Path path = FileSystems.getDefault().getPath("/Volumes/Drive2/temp/file/" + name);
			/* Save InputStream as file * /
			Files.copy(file, path);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
        */
    }
    
    @POST
	@Path("checkout")
    public List<TempleName> checkoutNames(NameRequest nameRequest) {
        return namePoolDao.checkoutNames(nameRequest);
    }
    
    @DELETE
    public Response markNamesAsCompleted(List<CompletedTempleOrdinances> completedOrdinances) {
        namePoolDao.markNamesAsCompleted(completedOrdinances);
        return Response.ok().build();
    }
}
