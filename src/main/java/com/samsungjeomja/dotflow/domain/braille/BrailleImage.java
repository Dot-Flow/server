package com.samsungjeomja.dotflow.domain.braille;


import com.samsungjeomja.dotflow.global.BaseData;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "braille_data")
@Getter
@Builder
public class BrailleImage extends BaseData {
    @Id
    private String id;
    private String imageUrl;
    private List<List<Double>> boxes; // Bounding boxes
    private List<String> labels;

}
