package com.jeffrpowell.templenamepool.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffrpowell.templenamepool.dao.NamePoolDao;
import com.jeffrpowell.templenamepool.model.CompletedTempleOrdinances;
import com.jeffrpowell.templenamepool.model.NameRequest;
import com.jeffrpowell.templenamepool.model.NameSubmission;
import com.jeffrpowell.templenamepool.model.Ordinance;
import com.jeffrpowell.templenamepool.model.TempleName;
import com.jeffrpowell.templenamepool.model.WardMember;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Providers;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;

@Path("name")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class NameResource {
    
	private static final Map<String, String> ZIP_FS_ENV = new HashMap<>();
	static {
        ZIP_FS_ENV.put("create", "true");
	}
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
		if (clazz == String.class) {
			return (T) json;
		}
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
			try (InputStream pdfStream = pdfPart.getEntityAs(InputStream.class))
			{
				submissions.add(new NameSubmission(familySearchId, wardMember, IOUtils.toByteArray(pdfStream), ordinances));
			}
		}
		namePoolDao.addNames(submissions);
        return Response.ok().build();
    }
    
	private java.nio.file.Path createTempZipFile(String prefix) throws IOException {
		java.nio.file.Path zipPath = Files.createTempFile(prefix, ".zip");
		try (ZipOutputStream tempZipStream = new ZipOutputStream(new FileOutputStream(zipPath.toFile())))
		{
			tempZipStream.flush();
		}
		return zipPath;
	}
	
    @POST
	@Path("checkout")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response checkoutNames(NameRequest nameRequest) throws IOException {
		List<TempleName> checkedOutNames = namePoolDao.checkoutNames(nameRequest);
		java.nio.file.Path zipPath = createTempZipFile(nameRequest.getFileName());
		try (FileSystem zipfs = FileSystems.newFileSystem(zipPath, null))
		{
			for (TempleName checkedOutName : checkedOutNames)
			{
				try
				{
					java.nio.file.Path pdfPath = zipfs.getPath(checkedOutName.getFamilySearchId()+".pdf");
					try (InputStream pdfStream = new ByteArrayInputStream(checkedOutName.getPdf()))
					{
						Files.copy(pdfStream, pdfPath, StandardCopyOption.REPLACE_EXISTING);
					}
				} catch (IOException ex) {}
			}
		}
		try (InputStream zipStream = new FileInputStream(zipPath.toFile())) {
			return Response.ok(IOUtils.toByteArray(zipStream)).header("Content-Disposition", "attachment; filename=\""+nameRequest.getFileName()+".zip\"").build();
		}
		finally {
			zipPath.toFile().delete();
		}
    }
    
    @DELETE
    public Response markNamesAsCompleted(List<CompletedTempleOrdinances> completedOrdinances) {
        namePoolDao.markNamesAsCompleted(completedOrdinances);
        return Response.ok().build();
    }
}
