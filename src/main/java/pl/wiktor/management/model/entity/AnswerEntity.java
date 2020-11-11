package pl.wiktor.management.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "answers")
public class AnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "answer_id")
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    public AnswerEntity(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "AnswerEntity{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
