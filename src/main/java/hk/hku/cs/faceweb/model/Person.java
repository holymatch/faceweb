package hk.hku.cs.faceweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @ApiModelProperty(notes = "The database generated person ID")
    @JsonProperty("Id")
    private Long id;
    @ApiModelProperty(notes = "The name of the person")
    @JsonProperty("Name")
    private String name;
    @Column(name="detail", columnDefinition="CLOB")
    @Lob
    @ApiModelProperty(notes = "The detail information of person")
    @JsonProperty("Detail")
    private String detail;
    @Lob
    @ApiModelProperty(notes = "The face image using based64 encode format")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "identify")
    @JsonProperty("Face")
    private Face face;

    protected Person() {}

    public Person(String name, String detail, Face face) {
        this.name = name;
        this.detail = detail;
        this.face = face;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", face=" + face +
                '}';
    }
}
