package com.jeffrpowell.templenamepool.model.mongo;

import com.jeffrpowell.templenamepool.model.NameSubmission;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

public class TempleNamePoolCodecProvider implements CodecProvider{

	@Override
	public <T> Codec<T> get(Class<T> type, CodecRegistry cr)
	{
		if (type == NameSubmission.class) {
			return (Codec<T>) new NameSubmissionCodec(cr);
		}
		return null;
	}

}
