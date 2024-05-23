package tight.commas.domain.park.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tight.commas.domain.Address;
import tight.commas.domain.BaseTimeEntity;
import tight.commas.domain.chat.entity.ChatRoom;
import tight.commas.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Park extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "park_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    private String parkName;

    @Embedded
    private Address address;

    private String imageUrl;

    private int phoneNumber;

    @Column(length = 1000)
    private String plant; //주요 식물

    @Column(length = 1000)
    private String mainEquip; //주요 시설

    @Column(length = 1000)
    private String outLine; //개요

    public void saveParkInfo(String name, String content, Address address, String mainEquip, String mainPlant, String imageUrl) {
        this.parkName = name;
        this.outLine = content;
        this.address = address;
        this.mainEquip = mainEquip;
        this.plant = mainPlant;
        this.imageUrl = imageUrl;
    }

    public void saveNaturalTourismInfo(String name, Address address) {
        this.parkName = name;
        this.address = address;
    }
}
