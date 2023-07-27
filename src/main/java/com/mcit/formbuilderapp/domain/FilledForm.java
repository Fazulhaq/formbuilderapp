package com.mcit.formbuilderapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FilledForm.
 */
@Entity
@Table(name = "filled_form")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FilledForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "j_son_text", nullable = false)
    private String jSONText;

    @OneToMany(mappedBy = "filledForm")
    @JsonIgnoreProperties(value = { "filledForm" }, allowSetters = true)
    private Set<Feedbackhistory> feedbackhistories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FilledForm id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getjSONText() {
        return this.jSONText;
    }

    public FilledForm jSONText(String jSONText) {
        this.setjSONText(jSONText);
        return this;
    }

    public void setjSONText(String jSONText) {
        this.jSONText = jSONText;
    }

    public Set<Feedbackhistory> getFeedbackhistories() {
        return this.feedbackhistories;
    }

    public void setFeedbackhistories(Set<Feedbackhistory> feedbackhistories) {
        if (this.feedbackhistories != null) {
            this.feedbackhistories.forEach(i -> i.setFilledForm(null));
        }
        if (feedbackhistories != null) {
            feedbackhistories.forEach(i -> i.setFilledForm(this));
        }
        this.feedbackhistories = feedbackhistories;
    }

    public FilledForm feedbackhistories(Set<Feedbackhistory> feedbackhistories) {
        this.setFeedbackhistories(feedbackhistories);
        return this;
    }

    public FilledForm addFeedbackhistory(Feedbackhistory feedbackhistory) {
        this.feedbackhistories.add(feedbackhistory);
        feedbackhistory.setFilledForm(this);
        return this;
    }

    public FilledForm removeFeedbackhistory(Feedbackhistory feedbackhistory) {
        this.feedbackhistories.remove(feedbackhistory);
        feedbackhistory.setFilledForm(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilledForm)) {
            return false;
        }
        return id != null && id.equals(((FilledForm) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilledForm{" +
            "id=" + getId() +
            ", jSONText='" + getjSONText() + "'" +
            "}";
    }
}
