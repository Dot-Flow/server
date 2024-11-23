package com.samsungjeomja.dotflow.braille.repository;

import com.samsungjeomja.dotflow.braille.BrailleImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrailleImageRepository extends MongoRepository<BrailleImage, String> {
}
