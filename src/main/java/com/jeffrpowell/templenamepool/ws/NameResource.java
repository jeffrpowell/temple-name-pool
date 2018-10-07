package com.jeffrpowell.templenamepool.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.NameRequest;
import com.jeffrpowell.templenamepool.model.NameSubmission;
import com.jeffrpowell.templenamepool.model.Ordinance;
import com.jeffrpowell.templenamepool.model.TempleName;
import com.jeffrpowell.templenamepool.model.WardMember;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
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
	
	private static ObjectMapper getObjectMapper(Providers providers) {
		ContextResolver<ObjectMapper> cr = providers
                .getContextResolver(ObjectMapper.class, MediaType.WILDCARD_TYPE);
        return cr.getContext(Void.class);
	}
	
	private static <T> T extractMultiPartField(FormDataMultiPart multiPart, ObjectMapper objectMapper, String fieldName, Class<T> clazz) throws IOException {
		FormDataBodyPart bodyPart = multiPart.getField(fieldName);
		String json = bodyPart.getEntityAs(String.class);
		return objectMapper.readValue(json, clazz);
	}
    
    @POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response addNamesToPool(FormDataMultiPart multiPart, @Context Providers providers) throws IOException {
        ObjectMapper objectMapper = getObjectMapper(providers);
		Integer numSubmissions = extractMultiPartField(multiPart, objectMapper, "numSubmissions", Integer.class);
		WardMember wardMember = extractMultiPartField(multiPart, objectMapper, "wardMember", WardMember.class);
		List<NameSubmission> submissions = new ArrayList<>();
		for (int i = 0; i < numSubmissions; i++)
		{
			String familySearchId = extractMultiPartField(multiPart, objectMapper, "familySearchId"+i, String.class);
			FormDataBodyPart pdfPart = multiPart.getField("pdf"+i);
			List<Object> ordinanceStringList = extractMultiPartField(multiPart, objectMapper, "ordinances"+i, List.class);
			Set<Ordinance> ordinances = ordinanceStringList.stream().map(obj -> (String)obj).map(Ordinance::valueOf).collect(Collectors.toCollection(() -> EnumSet.noneOf(Ordinance.class)));
			submissions.add(new NameSubmission(familySearchId, wardMember, pdfPart.getEntityAs(byte[].class), ordinances));
		}
		namePoolDao.addNames(submissions);
        return Response.ok().build();
    }
    
    @POST
	@Path("checkout")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response checkoutNames(NameRequest nameRequest) throws IOException {
		StreamingOutput outStream = out -> {
			List<TempleName> checkedOutNames = namePoolDao.checkoutNames(nameRequest);
			try (ZipOutputStream zipStream = new ZipOutputStream(new BufferedOutputStream(out)))
			{
				for (TempleName checkedOutName : checkedOutNames)
				{
					try
					{
						zipStream.putNextEntry(new ZipEntry(checkedOutName.getFamilySearchId()+".pdf"));
						zipStream.write(checkedOutName.getPdf());
						zipStream.closeEntry();
					} catch (IOException ex) {}
				}
			}
			finally {
				if(out != null) {
					out.flush();
					out.close();
				}
			}
		};
		return Response.ok(outStream).header("Content-Disposition", "attachment; filename=\""+nameRequest.getFileName()+".zip\"").build();
    }
    
    @DELETE
    public Response markNamesAsCompleted(List<CompletedTempleOrdinances> completedOrdinances) {
        namePoolDao.markNamesAsCompleted(completedOrdinances);
        return Response.ok().build();
    }
}
