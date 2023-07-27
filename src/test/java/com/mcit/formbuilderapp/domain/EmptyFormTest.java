package com.mcit.formbuilderapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.formbuilderapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EmptyFormTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmptyForm.class);
        EmptyForm emptyForm1 = new EmptyForm();
        emptyForm1.setId(1L);
        EmptyForm emptyForm2 = new EmptyForm();
        emptyForm2.setId(emptyForm1.getId());
        assertThat(emptyForm1).isEqualTo(emptyForm2);
        emptyForm2.setId(2L);
        assertThat(emptyForm1).isNotEqualTo(emptyForm2);
        emptyForm1.setId(null);
        assertThat(emptyForm1).isNotEqualTo(emptyForm2);
    }
}
