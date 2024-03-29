package tight.commas.domain.weather.entity;

import jakarta.persistence.*;
import lombok.Getter;
import tight.commas.domain.BaseTimeEntity;

@Entity
@Getter
public class Weather extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "weather_id")
    private Long id;

    private double humidity;    //습도
    private String weather;     //날씨
    private double pop;         //강수 확률
    private double temp;        //온도
    private double pm10_value;  //미세먼지 농도
    private double pm25_value;  //초미세먼지 농도
}
