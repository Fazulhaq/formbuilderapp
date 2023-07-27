package com.mcit.formbuilderapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.formbuilderapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilledFormTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilledForm.class);
        FilledForm filledForm1 = new FilledForm();
        filledForm1.setId(1L);
        FilledForm filledForm2 = new FilledForm();
        filledForm2.setId(filledForm1.getId());
        assertThat(filledForm1).isEqualTo(filledForm2);
        filledForm2.setId(2L);
        assertThat(filledForm1).isNotEqualTo(filledForm2);
        filledForm1.setId(null);
        assertThat(filledForm1).isNotEqualTo(filledForm2);
    }
}
