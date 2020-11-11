package pl.wiktor.management.model.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@ToString
@Entity
@Table(name = "questions")
public class QuestionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    private Long id;

    private String content;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AnswerEntity> answers;
}
