package com.mcit.formbuilderapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;

/**
 * A Feedbackhistory.
 */
@Entity
@Table(name = "feedbackhistory")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Feedbackhistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "feedback_text")
    private String feedbackText;

    @Column(name = "feedback_date")
    private LocalDate feedbackDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "feedbackhistories" }, allowSetters = true)
    private FilledForm filledForm;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Feedbackhistory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedbackText() {
        return this.feedbackText;
    }

    public Feedbackhistory feedbackText(String feedbackText) {
        this.setFeedbackText(feedbackText);
        return this;
    }

    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public LocalDate getFeedbackDate() {
        return this.feedbackDate;
    }

    public Feedbackhistory feedbackDate(LocalDate feedbackDate) {
        this.setFeedbackDate(feedbackDate);
        return this;
    }

    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public FilledForm getFilledForm() {
        return this.filledForm;
    }

    public void setFilledForm(FilledForm filledForm) {
        this.filledForm = filledForm;
    }

    public Feedbackhistory filledForm(FilledForm filledForm) {
        this.setFilledForm(filledForm);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feedbackhistory)) {
            return false;
        }
        return id != null && id.equals(((Feedbackhistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Feedbackhistory{" +
            "id=" + getId() +
            ", feedbackText='" + getFeedbackText() + "'" +
            ", feedbackDate='" + getFeedbackDate() + "'" +
            "}";
    }
}
