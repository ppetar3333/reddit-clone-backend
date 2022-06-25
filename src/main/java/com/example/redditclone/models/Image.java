package com.example.redditclone.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "image_table")
@Getter
@Setter
@ToString
public class Image {


    public Image() {
        super();
    }

    public Image(String name, String type, byte[] picByte) {
        this.name = name;
        this.type = type;
        this.picByte = picByte;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "picByte", length = 3062955)
    private byte[] picByte;
}
