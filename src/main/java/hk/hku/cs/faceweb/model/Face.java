package hk.hku.cs.faceweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class Face {
    @ApiModelProperty(notes = "The face image using based64 encode format")
    @Column(name="face", columnDefinition="CLOB")
    @Lob
    @JsonProperty("FaceData")
    private String faceData;
    @ApiModelProperty(notes = "The identify id return from face engine after the face engine recognize the face.")
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @JsonProperty("Identify")
    private Long identify;
    @Transient
    @ApiModelProperty(notes = "The score return by face engine, if the score is < 0.3 this pretty sure is the same person in the database")
    @JsonProperty("Score")
    private Float score;

    protected Face() { }

    public Face(String faceData, Long identify) {
        this.faceData = faceData;
        this.identify = identify;
    }

    public String getFaceData() {
        return faceData;
    }

    public void setFaceData(String faceData) {
        this.faceData = faceData;
    }

    public Long getIdentify() {
        return identify;
    }

    public void setIdentify(Long identify) {
        this.identify = identify;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Face{" +
                "faceData='" + (faceData == null ? null: (faceData.length() > 20 ? faceData.substring(0, 20) : faceData) ) + '\'' +
                ", identify=" + identify +
                ", score=" + score +
                '}';
    }
}
