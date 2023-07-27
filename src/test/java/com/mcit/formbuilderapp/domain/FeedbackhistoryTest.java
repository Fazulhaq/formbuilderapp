package com.mcit.formbuilderapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mcit.formbuilderapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FeedbackhistoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Feedbackhistory.class);
        Feedbackhistory feedbackhistory1 = new Feedbackhistory();
        feedbackhistory1.setId(1L);
        Feedbackhistory feedbackhistory2 = new Feedbackhistory();
        feedbackhistory2.setId(feedbackhistory1.getId());
        assertThat(feedbackhistory1).isEqualTo(feedbackhistory2);
        feedbackhistory2.setId(2L);
        assertThat(feedbackhistory1).isNotEqualTo(feedbackhistory2);
        feedbackhistory1.setId(null);
        assertThat(feedbackhistory1).isNotEqualTo(feedbackhistory2);
    }
}
