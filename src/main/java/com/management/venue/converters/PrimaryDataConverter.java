package com.management.venue.converters;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.management.venue.entites.Primary;
import com.management.venue.pojo.PrimaryData;
import com.management.venue.utility.EncryptionService;

public abstract class PrimaryDataConverter {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Autowired
	protected EncryptionService encryptionService;

	protected <S extends Primary, D extends PrimaryData> D convertPrimaryFields(S source, D target) {
		if (source.getPk() != null) {
			target.setPk(encryptionService.encode(source.getPk().toString()));
		}
		if (source.getCreationTime() != null) {
			target.setCreationTime(source.getCreationTime().format(formatter));
		}
		if (source.getModifiedTime() != null) {
			target.setModifiedTime(source.getModifiedTime().format(formatter));
		}
		return target;
	}

}
