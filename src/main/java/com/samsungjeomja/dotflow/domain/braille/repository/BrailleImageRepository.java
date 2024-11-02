package com.samsungjeomja.dotflow.domain.braille.repository;

import com.samsungjeomja.dotflow.domain.braille.BrailleImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrailleImageRepository extends MongoRepository<BrailleImage, String> {
}
