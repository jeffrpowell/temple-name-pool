package com.jeffrpowell.templenamepool.model.mongo;

import com.jeffrpowell.templenamepool.model.WardMember;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class WardMemberCodec implements Codec<WardMember>{

	@Override
	public void encode(BsonWriter writer, WardMember t, EncoderContext ec)
	{
		writer.writeStartDocument();
		writer.writeString("id", t.getId());
		writer.writeString("name", t.getName());
		writer.writeString("email", t.getEmail());
		writer.writeString("phone", t.getPhone());
		writer.writeEndDocument();
	}

	@Override
	public Class<WardMember> getEncoderClass()
	{
		return WardMember.class;
	}

	@Override
	public WardMember decode(BsonReader reader, DecoderContext dc)
	{
		reader.readStartDocument();
		String id = reader.readString("id");
		String name = reader.readString("name");
		String email = reader.readString("email");
		String phone = reader.readString("phone");
		reader.readEndDocument();
		return new WardMember(id, name, email, phone);
	}

}
