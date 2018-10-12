package com.jeffrpowell.templenamepool.model.mongo;

import com.jeffrpowell.templenamepool.model.NameSubmission;
import com.jeffrpowell.templenamepool.model.Ordinance;
import com.jeffrpowell.templenamepool.model.WardMember;
import java.util.HashSet;
import java.util.Set;
import org.bson.BsonBinary;
import org.bson.BsonBinarySubType;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class NameSubmissionCodec implements Codec<NameSubmission>{

	private final CodecRegistry codecRegistry;

	public NameSubmissionCodec(CodecRegistry codecRegistry)
	{
		this.codecRegistry = codecRegistry;
	}
	
	@Override
	public void encode(BsonWriter writer, NameSubmission t, EncoderContext ec)
	{
		writer.writeStartDocument();
		writer.writeString("_id", t.getFamilySearchId());
		writer.writeBinaryData("pdf", new BsonBinary(BsonBinarySubType.BINARY, t.getPdf()));
		writer.writeStartArray("ordinances");
		t.getOrdinances().stream().map(Ordinance::name).forEach(writer::writeString);
		writer.writeEndArray();
		codecRegistry.get(WardMember.class).encode(writer, t.getSupplier(), ec.getChildContext());
		writer.writeEndDocument();
	}

	@Override
	public Class<NameSubmission> getEncoderClass()
	{
		return NameSubmission.class;
	}

	@Override
	public NameSubmission decode(BsonReader reader, DecoderContext dc)
	{
		reader.readStartDocument();
		String familySearchId = reader.readString("_id");
		byte[] pdf = reader.readBinaryData("pdf").getData();
		reader.readName(); //ordinances
		reader.readStartArray();
		Set<Ordinance> ordinances = new HashSet<>();
		while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
			ordinances.add(Ordinance.valueOf(reader.readString()));
		}
		reader.readEndArray();
		WardMember supplier = codecRegistry.get(WardMember.class).decode(reader, dc);
		return new NameSubmission(familySearchId, supplier, pdf, ordinances);
	}

}
